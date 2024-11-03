package io.graversen.replicate.llama3;

import io.graversen.replicate.common.TextConversation;
import io.graversen.replicate.common.TextMessage;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@UtilityClass
public class Llama3Tokenizer {
    private static final String BEGIN_OF_TEXT = "<|begin_of_text|>";
    private static final String START_HEADER_ID = "<|start_header_id|>";
    private static final String END_HEADER_ID = "<|end_header_id|>";
    private static final String END_OF_TEXT_ID = "<|eot_id|>";

    public static final Integer DEFAULT_CONTEXT_WINDOW_SIZE = 8000;
    public static final Integer APPROXIMATE_CHARACTERS_PER_TOKEN = 4;
    public static final String ROLE_USER = "user";
    public static final String ROLE_ASSISTANT = "assistant";
    public static final String ROLE_SYSTEM = "system";

    public static String beginOfText(@NonNull String text) {
        return String.format("%s%s", BEGIN_OF_TEXT, text);
    }

    public static String endOfText(@NonNull String text) {
        return String.format("%s%s", text, END_OF_TEXT_ID);
    }

    public static String header(@NonNull String text) {
        return String.format("%s%s%s", START_HEADER_ID, text, END_HEADER_ID);
    }

    public static String userHeader() {
        return Llama3Tokenizer.header(ROLE_USER);
    }

    public static String assistantHeader() {
        return Llama3Tokenizer.header(ROLE_ASSISTANT);
    }

    public static String systemHeader() {
        return Llama3Tokenizer.header(ROLE_SYSTEM);
    }

    public static String tokenizeMessage(@NonNull TextMessage message) {
        final var messageBuilder = new StringBuilder();
        addMessageToTextCompletion(messageBuilder).accept(message);
        return messageBuilder.toString();
    }

    public static Llama3TextCompletion generateTextCompletion(@NonNull TextConversation conversation) {
        final var textCompletionBuilder = createBeginOfText(conversation.getSystemMessage());
        conversation.getMessages().forEach(addMessageToTextCompletion(textCompletionBuilder));
        final var textCompletion = textCompletionBuilder.toString();
        return new Llama3TextCompletion(textCompletion);
    }

    public static TextConversation parseTextCompletion(@NonNull String tokenizedConversation) {
        String systemMessage = null;
        final var messages = new ArrayList<TextMessage>();

        final String[] tokens = tokenizedConversation.split(Pattern.quote(END_OF_TEXT_ID));

        for (String token : tokens) {
            token = token.trim();

            if (token.isEmpty()) {
                continue;
            }

            if (token.startsWith(BEGIN_OF_TEXT)) {
                String systemToken = token.substring(BEGIN_OF_TEXT.length()).trim();
                if (systemToken.startsWith(START_HEADER_ID + ROLE_SYSTEM)) {
                    systemMessage = systemToken.substring(systemToken.indexOf(END_HEADER_ID) + END_HEADER_ID.length()).trim();
                }
            } else if (token.startsWith(START_HEADER_ID)) {
                String role = token.substring(START_HEADER_ID.length(), token.indexOf(END_HEADER_ID)).trim();
                String messageContent = token.substring(token.indexOf(END_HEADER_ID) + END_HEADER_ID.length()).trim();

                if (!role.isEmpty() && !messageContent.isEmpty()) {
                    messages.add(new TextMessage(role, messageContent));
                }
            }
        }

        if (systemMessage == null) {
            throw new IllegalArgumentException("System message not found in the tokenized conversation.");
        }

        return new TextConversation(systemMessage, messages);
    }

    public static Integer approximateConversationContextSize(@NonNull TextConversation conversation, @Nullable Integer tokenSize) {
        final var conversationTextCompletion = generateTextCompletion(conversation);
        return getTokens(conversationTextCompletion.getText(), tokenSize);
    }

    public static TextConversation fitToContextWindow(@NonNull TextConversation conversation, @Nullable Integer contextWindowSize) {
        contextWindowSize = Objects.requireNonNullElse(contextWindowSize, DEFAULT_CONTEXT_WINDOW_SIZE);

        final var systemMessage = createBeginOfText(conversation.getSystemMessage());
        final var systemMessageTokens = getTokens(systemMessage.toString(), null);
        int remainingTokens = contextWindowSize - systemMessageTokens;

        final var messages = conversation.getMessages();
        final var fittedMessages = new LinkedList<TextMessage>();

        for (int i = messages.size() - 1; i >= 0; i--) {
            final var message = messages.get(i);
            final var tokenizedMessage = tokenizeMessage(message);
            final var messageTokens = getTokens(tokenizedMessage, null);

            if (remainingTokens - messageTokens >= 0) {
                fittedMessages.addFirst(message);
                remainingTokens -= messageTokens;
            } else {
                break;
            }
        }

        return new TextConversation(conversation.getSystemMessage(), fittedMessages);
    }

    Consumer<TextMessage> addMessageToTextCompletion(@NonNull StringBuilder textCompletionBuilder) {
        return textMessage -> textCompletionBuilder
                .append(header(textMessage.getRole()))
                .append(textMessage.getText())
                .append(END_OF_TEXT_ID);
    }

    Integer getTokens(@NonNull String string, @Nullable Integer tokenSize) {
        tokenSize = Objects.requireNonNullElse(tokenSize, APPROXIMATE_CHARACTERS_PER_TOKEN);
        return string.length() / tokenSize;
    }

    StringBuilder createBeginOfText(@NonNull String systemMessage) {
        return new StringBuilder().append(BEGIN_OF_TEXT)
                .append(systemHeader())
                .append(systemMessage);
    }
}

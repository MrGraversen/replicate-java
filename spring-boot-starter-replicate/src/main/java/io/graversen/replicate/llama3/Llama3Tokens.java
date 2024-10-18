package io.graversen.replicate.llama3;

import io.graversen.replicate.common.TextConversation;
import io.graversen.replicate.common.TextMessage;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.function.Consumer;

@UtilityClass
public class Llama3Tokens {
    private static final String BEGIN_OF_TEXT = "<|begin_of_text|>";
    private static final String START_HEADER_ID = "<|start_header_id|>";
    private static final String END_HEADER_ID = "<|end_header_id|>";
    private static final String END_OF_TEXT_ID = "<|eot_id|>";

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
        return Llama3Tokens.header(ROLE_USER);
    }

    public static String assistantHeader() {
        return Llama3Tokens.header(ROLE_ASSISTANT);
    }

    public static String systemHeader() {
        return Llama3Tokens.header(ROLE_SYSTEM);
    }

    public static Llama3TextCompletion generateTextCompletion(@NonNull TextConversation conversation) {
        final var textCompletionBuilder = new StringBuilder();
        textCompletionBuilder
                .append(BEGIN_OF_TEXT)
                .append(systemHeader())
                .append(conversation.getSystemMessage());

        conversation.getMessages().forEach(addMessageToTextCompletion(textCompletionBuilder));
        final var textCompletion = textCompletionBuilder.toString();
        return new Llama3TextCompletion(textCompletion);
    }

    Consumer<TextMessage> addMessageToTextCompletion(@NonNull StringBuilder textCompletionBuilder) {
        return textMessage -> textCompletionBuilder
                .append(header(textMessage.getRole()))
                .append(textMessage.getText())
                .append(END_OF_TEXT_ID);
    }
}

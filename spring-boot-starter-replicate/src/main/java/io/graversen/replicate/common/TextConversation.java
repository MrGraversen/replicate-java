package io.graversen.replicate.common;

import lombok.NonNull;
import lombok.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Value
public class TextConversation {
    @NonNull String systemMessage;
    @NonNull List<TextMessage> messages;

    public static TextConversation of(@NonNull String systemMessage) {
        return new TextConversation(
                systemMessage,
                List.of()
        );
    }

    public static TextConversation of(@NonNull String systemMessage, @NonNull String userMessage) {
        return new TextConversation(
                systemMessage,
                List.of(TextMessage.user(userMessage))
        );
    }

    public TextConversation append(@NonNull TextMessage message) {
        final var mutableMessages = new ArrayList<>(getMessages());
        mutableMessages.add(message);

        return new TextConversation(
                getSystemMessage(),
                List.copyOf(mutableMessages)
        );
    }

    public List<TextMessage> getLastMessages(@NonNull Integer conversationSize) {
        return messages.stream()
                .skip(Math.max(0, messages.size() - conversationSize))
                .toList();
    }

    public List<TextMessage> getFirstMessages(@NonNull Integer conversationSize) {
        return messages.stream()
                .limit(conversationSize)
                .toList();
    }

    public Optional<TextMessage> getLastMessage() {
        return messages.isEmpty() ? Optional.empty() : Optional.of(messages.getLast());
    }
}

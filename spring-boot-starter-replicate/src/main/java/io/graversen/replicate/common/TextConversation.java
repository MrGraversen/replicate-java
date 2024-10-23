package io.graversen.replicate.common;

import lombok.NonNull;
import lombok.Value;

import java.util.List;

@Value
public class TextConversation {
    @NonNull String systemMessage;
    @NonNull List<TextMessage> messages;

    public static TextConversation of(@NonNull String systemMessage, @NonNull String userMessage) {
        return new TextConversation(
                systemMessage,
                List.of(TextMessage.user(userMessage))
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
}

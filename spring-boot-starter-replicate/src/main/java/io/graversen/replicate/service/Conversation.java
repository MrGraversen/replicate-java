package io.graversen.replicate.service;

import io.graversen.replicate.common.ReplicateModel;
import io.graversen.replicate.common.TextConversation;
import io.graversen.replicate.common.TextMessage;
import io.graversen.replicate.common.TextPredictionRoles;
import lombok.*;

import java.util.UUID;

@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class Conversation {
    @NonNull String id;
    @NonNull String userId;
    @NonNull ReplicateModel model;
    @NonNull TextConversation conversation;

    public static Conversation createDefault(@NonNull String systemMessage, @NonNull ReplicateModel model) {
        return new Conversation(
                createId(),
                "default",
                model,
                TextConversation.of(systemMessage)
        );
    }

    public Conversation appendMessage(@NonNull TextMessage message) {
        return new Conversation(
                getId(),
                getUserId(),
                getModel(),
                getConversation().append(message)
        );
    }

    public Conversation update(@NonNull TextConversation conversation) {
        return new Conversation(
                getId(),
                getUserId(),
                getModel(),
                conversation
        );
    }

    @ToString.Include
    public ConversationStates getState() {
        if (conversation.getMessages().isEmpty()) {
            return ConversationStates.IDLE;
        }

        final var isLastMessageFromUser = conversation.getLastMessage()
                .map(message -> message.getRole().equals(TextPredictionRoles.USER.asString()))
                .orElse(false);

        return isLastMessageFromUser ? ConversationStates.WAITING_FOR_ASSISTANT : ConversationStates.WAITING_FOR_USER;
    }

    private static String createId() {
        return String.format("c_%s", UUID.randomUUID());
    }
}

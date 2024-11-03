package io.graversen.replicate.service;

import io.graversen.replicate.common.ReplicateModel;
import io.graversen.replicate.common.TextConversation;
import io.graversen.replicate.common.TextMessage;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

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

    private static String createId() {
        return String.format("c_%s", UUID.randomUUID().toString());
    }
}

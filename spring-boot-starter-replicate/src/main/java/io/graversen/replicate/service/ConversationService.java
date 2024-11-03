package io.graversen.replicate.service;

import io.graversen.replicate.common.TextConversation;
import io.graversen.replicate.common.TextMessage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class ConversationService {
    private final ConcurrentMap<String, Conversation> conversations = new ConcurrentHashMap<>();

    public Conversation create(@NonNull CreateConversation createConversation) {
        final var conversation = Conversation.createDefault(createConversation.getSystemMessage(), createConversation.getReplicateModel());
        conversations.put(conversation.getId(), conversation);
        return conversation;
    }

    public Optional<Conversation> appendMessage(@NonNull String id, @NonNull TextMessage message) {
        final var conversationOrNull = conversations.computeIfPresent(id, (key, conversation) -> conversation.appendMessage(message));
        return Optional.ofNullable(conversationOrNull);
    }

    public Optional<Conversation> update(@NonNull String id, @NonNull TextConversation conversation) {
        final var conversationOrNull = conversations.computeIfPresent(id, (key, value) -> value.update(conversation));
        return Optional.ofNullable(conversationOrNull);
    }

    public Optional<Conversation> getById(@NonNull String id) {
        return Optional.ofNullable(conversations.get(id));
    }

    public List<Conversation> getByUser(@NonNull String userId) {
        return conversations.values().stream()
                .filter(conversation -> conversation.getUserId().equals(userId))
                .toList();
    }

    public List<Conversation> getAll() {
        return List.copyOf(conversations.values());
    }
}

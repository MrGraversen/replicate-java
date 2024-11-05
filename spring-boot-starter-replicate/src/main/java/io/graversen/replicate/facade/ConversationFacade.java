package io.graversen.replicate.facade;

import io.graversen.replicate.common.TextConversation;
import io.graversen.replicate.common.TextMessage;
import io.graversen.replicate.llama3.Llama3Tokenizer;
import io.graversen.replicate.service.Conversation;
import io.graversen.replicate.service.ConversationService;
import io.graversen.replicate.service.CreateConversation;
import io.graversen.replicate.service.CreateTextPrediction;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class ConversationFacade {
    private final ConcurrentMap<String, ConversationOptions> conversationOptions = new ConcurrentHashMap<>();

    private final @NonNull ReplicateFacade replicateFacade;
    private final @NonNull ConversationService conversationService;

    public Conversation create(@NonNull CreateConversation createConversation, @NonNull ConversationOptions options) {
        final var conversation = conversationService.create(createConversation);
        conversationOptions.put(conversation.getId(), options);
        return conversation;
    }

    public Function<Conversation, CompletableFuture<Conversation>> chat(@NonNull TextMessage message) {
        return conversation -> chat(conversation.getId(), message);
    }

    public CompletableFuture<Conversation> chat(@NonNull String id, @NonNull TextMessage message) {
        var conversation = conversationService.appendMessage(id, message);

        if (conversation.isEmpty()) {
            return CompletableFuture.failedFuture(new IllegalArgumentException("Could not find Conversation: " + id));
        }

        final var textConversation = conversation.get().getConversation();
        final var model = conversation.get().getModel();
        final var options = conversationOptions.get(conversation.get().getId());

        final var createTextPrediction = new CreateTextPrediction(
                textConversation,
                options.getTemperature(),
                null,
                null,
                null,
                null,
                null
        );

        final var pendingPrediction = replicateFacade.createPrediction(model, createTextPrediction);
        return pendingPrediction
                .thenApply(parseTextConversation())
                .thenApply(updateConversation(conversation.get().getId()));
    }

    Function<TextConversation, Conversation> updateConversation(@NonNull String id) {
        return conversation -> conversationService.update(id, conversation)
                .orElseThrow(() -> new IllegalArgumentException("Could not find Conversation: " + id));
    }

    Function<PredictionResponseAndModel, TextConversation> parseTextConversation() {
        return predictionResponseAndModel -> {
            // For now, assume Llama3 is used and apply its tokenization parsing
            // In the future, rely on a strategy pattern implementation selector for multi-model-family support

            final var textInput = predictionResponseAndModel.getPredictionResponse().getInputKey("prompt")
                    .orElseThrow(() -> new IllegalStateException("No text input found in prediction"));

            var conversation = Llama3Tokenizer.parseTextCompletion(textInput);

            final var textOutput = predictionResponseAndModel.getPredictionResponse().getTextOutput()
                    .orElseThrow(() -> new IllegalStateException("No text output found in prediction"));

            return conversation.append(TextMessage.assistant(textOutput));
        };
    }
}

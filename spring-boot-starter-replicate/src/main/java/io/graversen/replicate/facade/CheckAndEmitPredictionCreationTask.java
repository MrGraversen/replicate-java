package io.graversen.replicate.facade;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class CheckAndEmitPredictionCreationTask implements Function<Optional<PredictionResponseAndModel>, PredictionResponseAndModel> {
    private final @NonNull ApplicationEventPublisher eventPublisher;

    @Override
    @SneakyThrows
    public PredictionResponseAndModel apply(@Nullable Optional<PredictionResponseAndModel> predictionResponse) {
        final var response = predictionResponse.orElseThrow(predictionFailedError());
        final var event = mapPredictionCreatedEvent().apply(response);
        eventPublisher.publishEvent(event);
        return response;
    }

    Function<PredictionResponseAndModel, PredictionCreatedEvent> mapPredictionCreatedEvent() {
        return predictionResponse -> new PredictionCreatedEvent(
                predictionResponse.getPredictionResponse().getId(),
                predictionResponse.getModel(),
                predictionResponse.getPredictionResponse()
        );
    }

    private Supplier<Exception> predictionFailedError() {
        return () -> new CompletionException(new RuntimeException("Could not initiate prediction"));
    }
}

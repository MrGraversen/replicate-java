package io.graversen.replicate.facade;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class EmitPredictionResponseTask implements BiConsumer<PredictionResponseAndModel, Throwable> {
    private final @NonNull ApplicationEventPublisher eventPublisher;

    @Override
    public void accept(@Nullable PredictionResponseAndModel predictionResponse, @Nullable Throwable throwable) {
        if (throwable == null) {
            final var event = mapPredictionUpdatedEvent().apply(predictionResponse);
            eventPublisher.publishEvent(event);
        } else {
            final var event = mapPredictionFailedEvent(throwable).apply(predictionResponse);
            eventPublisher.publishEvent(event);
        }
    }

    Function<PredictionResponseAndModel, PredictionUpdatedEvent> mapPredictionUpdatedEvent() {
        return predictionResponse -> {
            final var status = PredictionStatus.fromString(predictionResponse.getPredictionResponse().getStatus());
            return new PredictionUpdatedEvent(
                    predictionResponse.getPredictionResponse().getId(),
                    predictionResponse.getModel(),
                    status,
                    predictionResponse.getPredictionResponse()
            );
        };
    }

    Function<PredictionResponseAndModel, PredictionFailedEvent> mapPredictionFailedEvent(@NonNull Throwable throwable) {
        return predictionResponse -> new PredictionFailedEvent(
                predictionResponse != null ? predictionResponse.getPredictionResponse().getId() : null,
                predictionResponse != null ? predictionResponse.getModel() : null,
                throwable
        );
    }
}

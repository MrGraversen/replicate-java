package io.graversen.replicate.facade;

import io.graversen.replicate.client.feign.FeignUtils;
import io.graversen.replicate.client.replicate.Replicate;
import io.graversen.replicate.service.PredictionResponse;
import io.graversen.replicate.util.ReplicateUtils;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class PollPredictionStatusTask implements Function<PredictionResponseAndModel, PredictionResponseAndModel> {
    private final @NonNull PredictionRetryPolicy retryPolicy;
    private final @NonNull Replicate replicate;
    private final @NonNull FeignUtils feignUtils;

    @Override
    @SneakyThrows
    public PredictionResponseAndModel apply(@Nullable PredictionResponseAndModel predictionResponse) {
        if (predictionResponse == null) {
            return null;
        }

        final var predictionId = predictionResponse.getPredictionResponse().getId();
        log.debug("[Prediction: {}]: Prediction retrieved for processing", predictionId);

        for (int attempt = 1; attempt <= retryPolicy.getMaxAttempts(); attempt++) {
            log.debug("[Prediction: {}]: Attempt {} of {} to retrieve prediction status", predictionId, attempt, retryPolicy.getMaxAttempts());
            final var getPredictionResponse = replicate.getPrediction(predictionId);
            final LinkedHashMap<String, Object> convertedResponse = feignUtils.convert(getPredictionResponse, LinkedHashMap.class);
            final var mappedResponse = ReplicateUtils.mapPredictionResponse(convertedResponse);

            if (mappedResponse.isPresent() && PredictionStatus.SUCCEEDED.asString().equals(mappedResponse.get().getStatus())) {
                log.info("[Prediction: {}]: Prediction succeeded on attempt {}/{}", predictionId, attempt, retryPolicy.getMaxAttempts());
                return new PredictionResponseAndModel(mappedResponse.get(), predictionResponse.getModel());
            }

            if (mappedResponse.isPresent() && PredictionStatus.FAILED.asString().equals(mappedResponse.get().getStatus())) {
                log.error("[Prediction: {}]: Prediction failed on attempt {}/{}", predictionId, attempt, retryPolicy.getMaxAttempts());
                throw new IllegalStateException(String.format("Prediction '%s' failed: %s", predictionId, mappedResponse.get().getStatus()));
            }

            try {
                log.debug("[Prediction: {}]: Prediction not yet succeeded (status: {}), retrying after {} ms", predictionId, mappedResponse.map(PredictionResponse::getStatus).orElse("unknown"), retryPolicy.getDelay().toMillis());
                Thread.sleep(retryPolicy.getDelay().toMillis());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Thread was interrupted during retry delay", e);
            }
        }

        log.error("[Prediction: {}]: Max retry attempts reached without success", predictionId);
        throw new IllegalStateException(String.format("Max retry attempts reached for prediction '%s' without success", predictionId));
    }
}

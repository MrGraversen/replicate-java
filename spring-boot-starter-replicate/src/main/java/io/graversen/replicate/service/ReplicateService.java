package io.graversen.replicate.service;

import io.graversen.replicate.client.feign.FeignUtils;
import io.graversen.replicate.client.replicate.Replicate;
import io.graversen.replicate.common.PredictionMapper;
import io.graversen.replicate.common.PredictionTypes;
import io.graversen.replicate.common.ReplicateModel;
import io.graversen.replicate.util.ReplicateUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplicateService {
    private final @NonNull Replicate replicate;
    private final @NonNull FeignUtils feignUtils;
    private final @NonNull List<PredictionMapper> predictionMappers;

    public Optional<PredictionResponse> createPrediction(@NonNull ReplicateModel model, @NonNull Object createPrediction) {
        return doCreatePrediction(model, createPrediction);
    }

    @SneakyThrows
    public Optional<PredictionResponse> createPrediction(@NonNull ReplicateModel model, @NonNull CreateTextPrediction createPrediction) {
        final var predictionMapper = predictionMappers.stream()
                .filter(supportsTextPredictions())
                .filter(supportsModel(model))
                .findFirst()
                .orElseThrow(unsupportedModelError(model));

        final var mappedPrediction = predictionMapper.apply(model, createPrediction);
        return doCreatePrediction(model, mappedPrediction);
    }

    Predicate<PredictionMapper> supportsTextPredictions() {
        return predictionMapper -> predictionMapper.supportsType(PredictionTypes.TEXT);
    }

    Predicate<PredictionMapper> supportsModel(@NonNull ReplicateModel model) {
        return predictionMapper -> predictionMapper.supportsModel(model);
    }

    private Optional<PredictionResponse> doCreatePrediction(@NonNull ReplicateModel model, @NonNull Object createPrediction) {
        final var replicateResponse = replicate.createPrediction(model.getOwner(), model.getName(), createPrediction);

        if (replicateResponse.status() >= 200 && replicateResponse.status() < 300) {
            final LinkedHashMap<String, Object> convertedResponse = feignUtils.convert(replicateResponse, LinkedHashMap.class);
            return ReplicateUtils.mapPredictionResponse(convertedResponse);
        } else {
            final LinkedHashMap<String, Object> convertedResponse = feignUtils.convert(replicateResponse, LinkedHashMap.class);
            final var errorMessage = String.format(
                    "Replicate API error: %s (HTTP Status %s). Model: %s",
                    convertedResponse.get("detail"),
                    convertedResponse.get("status"),
                    model
            );
            log.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }

    private Supplier<Throwable> unsupportedModelError(@NonNull ReplicateModel model) {
        return () -> new IllegalArgumentException("Unsupported model: " + model);
    }
}

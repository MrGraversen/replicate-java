package io.graversen.replicate.facade;

import io.graversen.replicate.common.ReplicateModel;
import io.graversen.replicate.service.CreateImagePrediction;
import io.graversen.replicate.service.CreateTextPrediction;
import io.graversen.replicate.service.ReplicateService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReplicateFacade {
    private final @NonNull ReplicateService replicateService;
    private final @NonNull ExecutorService executorService;
    private final @NonNull CheckAndEmitPredictionCreationTask checkAndEmitPredictionCreationTask;
    private final @NonNull PollPredictionStatusTask pollPredictionStatusTask;
    private final @NonNull EmitPredictionResponseTask emitPredictionResponseTask;

    public CompletableFuture<PredictionResponseAndModel> createPrediction(
            @NonNull ReplicateModel model,
            @NonNull CreateTextPrediction createPrediction
    ) {
        return CompletableFuture
                .supplyAsync(doCreatePrediction(model, createPrediction), executorService)
                .thenApplyAsync(checkAndEmitPredictionCreationTask, executorService)
                .thenApplyAsync(pollPredictionStatusTask, executorService)
                .whenCompleteAsync(emitPredictionResponseTask, executorService);
    }

    public CompletableFuture<PredictionResponseAndModel> createPrediction(
            @NonNull ReplicateModel model,
            @NonNull CreateImagePrediction createPrediction
    ) {
        return CompletableFuture
                .supplyAsync(doCreatePrediction(model, createPrediction), executorService)
                .thenApplyAsync(checkAndEmitPredictionCreationTask, executorService)
                .thenApplyAsync(pollPredictionStatusTask, executorService)
                .whenCompleteAsync(emitPredictionResponseTask, executorService);
    }

    Supplier<Optional<PredictionResponseAndModel>> doCreatePrediction(
            @NonNull ReplicateModel model,
            @NonNull CreateTextPrediction createPrediction
    ) {
        return () -> replicateService.createPrediction(model, createPrediction)
                .map(predictionResponse -> new PredictionResponseAndModel(predictionResponse, model));
    }

    Supplier<Optional<PredictionResponseAndModel>> doCreatePrediction(
            @NonNull ReplicateModel model,
            @NonNull CreateImagePrediction createPrediction
    ) {
        return () -> replicateService.createPrediction(model, createPrediction)
                .map(predictionResponse -> new PredictionResponseAndModel(predictionResponse, model));
    }
}

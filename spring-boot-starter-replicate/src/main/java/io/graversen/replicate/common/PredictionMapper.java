package io.graversen.replicate.common;

import lombok.NonNull;

import java.util.function.BiFunction;

public interface PredictionMapper<T, R> extends BiFunction<ReplicateModel, T, R> {
    @Override
    R apply(@NonNull ReplicateModel model, @NonNull T createPrediction);

    boolean supportsType(@NonNull PredictionTypes type);

    boolean supportsModel(@NonNull ReplicateModel model);
}

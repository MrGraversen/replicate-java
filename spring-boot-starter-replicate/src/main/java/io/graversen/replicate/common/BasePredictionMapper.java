package io.graversen.replicate.common;

import lombok.NonNull;

import java.util.Objects;
import java.util.Set;

public abstract class BasePredictionMapper<T, R> implements PredictionMapper<T, R> {
    protected abstract Set<ReplicateModel> supportedModels();

    @Override
    public boolean supportsModel(@NonNull ReplicateModel model) {
        return Objects.requireNonNullElseGet(supportedModels(), Set::of).stream().anyMatch(model::equals);
    }
}

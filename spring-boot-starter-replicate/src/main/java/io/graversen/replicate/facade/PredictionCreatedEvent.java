package io.graversen.replicate.facade;

import io.graversen.replicate.common.ReplicateModel;
import io.graversen.replicate.service.PredictionResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PredictionCreatedEvent {
    @NonNull String id;
    @NonNull ReplicateModel model;
    @NonNull PredictionResponse prediction;
}

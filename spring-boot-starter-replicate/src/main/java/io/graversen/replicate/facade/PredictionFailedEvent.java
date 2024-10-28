package io.graversen.replicate.facade;

import io.graversen.replicate.common.ReplicateModel;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PredictionFailedEvent {
    String id;
    ReplicateModel model;
    @NonNull Throwable error;
}

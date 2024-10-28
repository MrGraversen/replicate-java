package io.graversen.replicate.facade;

import lombok.NonNull;

import java.util.Arrays;

public enum PredictionStatus {
    PROCESSING,
    SUCCEEDED,
    FAILED,
    UNKNOWN;

    public String asString() {
        return name().toLowerCase();
    }

    public static PredictionStatus fromString(@NonNull String status) {
        return Arrays.stream(values())
                .filter(predictionStatus -> predictionStatus.name().equalsIgnoreCase(status))
                .findFirst()
                .orElse(UNKNOWN);
    }
}

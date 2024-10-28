package io.graversen.replicate.facade;

import lombok.Getter;
import lombok.NonNull;

import java.time.Duration;

@Getter
public class PredictionRetryPolicy {
    private final @NonNull Integer maxAttempts;
    private final @NonNull Duration delay;

    public PredictionRetryPolicy(@NonNull Integer maxAttempts, @NonNull Duration delay) {
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("maxAttempts must be greater than 0");
        }
        if (delay.isNegative()) {
            throw new IllegalArgumentException("delay must be non-negative");
        }
        this.maxAttempts = maxAttempts;
        this.delay = delay;
    }

    public static PredictionRetryPolicy defaultPolicy() {
        return new PredictionRetryPolicy(50, Duration.ofMillis(200));
    }
}

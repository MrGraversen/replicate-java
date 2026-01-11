package io.graversen.replicate.util;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplicateUrl {
    private final @NonNull String url;

    public static ReplicateUrl fromPrediction(@NonNull String id) {
        return new ReplicateUrl("https://replicate.com/p/%s".formatted(id));
    }

    @Override
    public String toString() {
        return url;
    }
}

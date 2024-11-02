package io.graversen.replicate.service;

import io.graversen.replicate.common.TextToImagePrompt;
import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateImagePrediction {
    private final @NonNull TextToImagePrompt prompt;
    private final @Nullable Integer outputs;
    private final @Nullable Integer inferenceSteps;
    private final @Nullable Integer seed;

}

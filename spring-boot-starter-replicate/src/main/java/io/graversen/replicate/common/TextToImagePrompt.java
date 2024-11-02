package io.graversen.replicate.common;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.Value;

@Value
public class TextToImagePrompt {
    @NonNull String prompt;
    @Nullable String negativePrompt;
    @NonNull TextToImageAspectRatio aspectRatio;
    @Nullable Double promptStrength;
}

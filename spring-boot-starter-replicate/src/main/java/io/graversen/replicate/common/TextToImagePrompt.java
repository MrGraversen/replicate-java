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

    public static TextToImagePrompt portrait(@NonNull String prompt) {
        return new TextToImagePrompt(
                prompt,
                null,
                TextToImageAspectRatio.portrait(),
                null
        );
    }

    public static TextToImagePrompt landscape(@NonNull String prompt) {
        return new TextToImagePrompt(
                prompt,
                null,
                TextToImageAspectRatio.landscape(),
                null
        );
    }
}

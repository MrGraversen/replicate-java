package io.graversen.replicate.common;

import lombok.NonNull;
import lombok.Value;

@Value
public class TextToImageAspectRatio {
    @NonNull Integer height;
    @NonNull AspectRatios aspectRatio;

    public Integer getWidth() {
        return (int) Math.round(height * ((double) aspectRatio.getWidthRatio() / aspectRatio.getHeightRatio()));
    }

    public String getAspectRatioAsString() {
        return aspectRatio.getAspectRatio();
    }
}

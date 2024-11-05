package io.graversen.replicate.common;

import lombok.NonNull;
import lombok.Value;

@Value
public class TextToImageAspectRatio {
    @NonNull Integer height;
    @NonNull AspectRatios aspectRatio;

    public static TextToImageAspectRatio square() {
        return new TextToImageAspectRatio(1080, AspectRatios.RATIO_1_BY_1);
    }

    public static TextToImageAspectRatio portrait() {
        return new TextToImageAspectRatio(1350, AspectRatios.RATIO_4_BY_5);
    }

    public static TextToImageAspectRatio landscape() {
        return new TextToImageAspectRatio(1350, AspectRatios.RATIO_5_BY_4);
    }

    public static TextToImageAspectRatio wide() {
        return new TextToImageAspectRatio(1350, AspectRatios.RATIO_16_BY_9);
    }

    public static TextToImageAspectRatio ultraWide() {
        return new TextToImageAspectRatio(1350, AspectRatios.RATIO_21_BY_9);
    }

    public static TextToImageAspectRatio reel() {
        return new TextToImageAspectRatio(1920, AspectRatios.RATIO_9_BY_16);
    }

    public Integer getWidth() {
        return (int) Math.round(height * ((double) aspectRatio.getWidthRatio() / aspectRatio.getHeightRatio()));
    }

    public String getAspectRatioAsString() {
        return aspectRatio.getAspectRatio();
    }
}

package io.graversen.replicate.service;

import io.graversen.replicate.common.AspectRatios;
import io.graversen.replicate.common.ModerationLevels;
import io.graversen.replicate.common.OutputFormats;
import io.graversen.replicate.common.QualityModes;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Builder
public class CreateImagePrediction2 {
    private final String prompt;
    private final String negativePrompt;
    private final AspectRatios aspectRatio;
    private final Set<String> inputImages;
    private final OutputFormats outputFormat;
    private final ModerationLevels moderationLevel;
    private final QualityModes quality;
}

package io.graversen.replicate.flux;

import io.graversen.replicate.common.*;
import io.graversen.replicate.models.*;
import io.graversen.replicate.service.CreateImagePrediction2;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class Flux2PredictionMapper extends BasePredictionMapper<CreateImagePrediction2, Object> {

    @Override
    protected Set<ReplicateModel> supportedModels() {
        return Set.of(
                FluxModels.FLUX_2_DEV,
                FluxModels.FLUX_2_FLEX,
                FluxModels.FLUX_2_PRO,
                FluxModels.FLUX_2_MAX
        );
    }

    @Override
    public Object apply(@NonNull ReplicateModel model, @NonNull CreateImagePrediction2 createPrediction) {
        if (model.equals(FluxModels.FLUX_2_DEV)) {
            return new BlackForestLabsFlux2DevPredictionrequest(
                    null,
                    mapFluxDevInput().apply(createPrediction),
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } else if (model.equals(FluxModels.FLUX_2_FLEX)) {
            return new BlackForestLabsFlux2FlexPredictionrequest(
                    null,
                    mapFluxFlexInput().apply(createPrediction),
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } else if (model.equals(FluxModels.FLUX_2_PRO)) {
            return new BlackForestLabsFlux2ProPredictionrequest(
                    null,
                    mapFluxProInput().apply(createPrediction),
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } else if (model.equals(FluxModels.FLUX_2_MAX)) {
            return new BlackForestLabsFlux2MaxPredictionrequest(
                    null,
                    mapFluxMaxInput().apply(createPrediction),
                    null,
                    null,
                    null,
                    null,
                    null
            );
        } else {
            throw new IllegalArgumentException("Unsupported Replicate Model: " + model);
        }

    }

    @Override
    public boolean supportsType(@NonNull PredictionTypes type) {
        return PredictionTypes.IMAGE.equals(type);
    }

    Function<CreateImagePrediction2, BlackForestLabsFlux2DevInput> mapFluxDevInput() {
        return createImagePrediction -> new BlackForestLabsFlux2DevInput(
                null,
                null,
                null,
                createImagePrediction.getPrompt(),
                mapGoFast(createImagePrediction),
                mapAspectRatio(createImagePrediction),
                mapInputImages(createImagePrediction),
                mapOutputFormat(createImagePrediction),
                100,
                mapDisableSafetyChecker(createImagePrediction)
        );
    }

    Function<CreateImagePrediction2, BlackForestLabsFlux2FlexInput> mapFluxFlexInput() {
        return createImagePrediction -> new BlackForestLabsFlux2FlexInput(
                null,
                null,
                null,
                null,
                createImagePrediction.getPrompt(),
                null,
                null,
                mapAspectRatio(createImagePrediction),
                mapInputImages(createImagePrediction),
                mapOutputFormat(createImagePrediction),
                100,
                mapSafetyTolerance(createImagePrediction),
                true
        );
    }

    Function<CreateImagePrediction2, BlackForestLabsFlux2MaxInput> mapFluxMaxInput() {
        return createImagePrediction -> new BlackForestLabsFlux2MaxInput(
                null,
                null,
                null,
                createImagePrediction.getPrompt(),
                null,
                mapAspectRatio(createImagePrediction),
                mapInputImages(createImagePrediction),
                mapOutputFormat(createImagePrediction),
                100,
                mapSafetyTolerance(createImagePrediction)
        );
    }

    Function<CreateImagePrediction2, BlackForestLabsFlux2ProInput> mapFluxProInput() {
        return createImagePrediction -> new BlackForestLabsFlux2ProInput(
                null,
                null,
                null,
                createImagePrediction.getPrompt(),
                "1 MP",
                mapAspectRatio(createImagePrediction),
                mapInputImages(createImagePrediction),
                mapOutputFormat(createImagePrediction),
                100,
                mapSafetyTolerance(createImagePrediction)
        );
    }

    private Boolean mapDisableSafetyChecker(@NonNull CreateImagePrediction2 createImagePrediction) {
        if (createImagePrediction.getModerationLevel() != null) {
            return createImagePrediction.getModerationLevel() == ModerationLevels.HIGH
                    ? Boolean.FALSE
                    : Boolean.TRUE;
        } else {
            return Boolean.TRUE;
        }
    }

    private Integer mapSafetyTolerance(@NonNull CreateImagePrediction2 createImagePrediction) {
        if (createImagePrediction.getModerationLevel() != null) {
            return createImagePrediction.getModerationLevel() == ModerationLevels.HIGH ? 2 : 5;
        } else {
            return 5;
        }
    }

    private String mapOutputFormat(@NonNull CreateImagePrediction2 createImagePrediction) {
        return Objects.requireNonNullElse(createImagePrediction.getOutputFormat(), OutputFormats.PNG).toString().toLowerCase();
    }

    private List<URI> mapInputImages(@NonNull CreateImagePrediction2 createImagePrediction) {
        return Objects.requireNonNullElseGet(createImagePrediction.getInputImages(), Set::<String>of).stream()
                .map(URI::create)
                .toList();
    }

    private String mapAspectRatio(@NonNull CreateImagePrediction2 createImagePrediction) {
        return Objects.requireNonNullElse(createImagePrediction.getAspectRatio(), AspectRatios.defaultValue()).getAspectRatio();
    }

    private Boolean mapGoFast(@NonNull CreateImagePrediction2 createImagePrediction) {
        if (createImagePrediction.getQuality() != null) {
            return createImagePrediction.getQuality() == QualityModes.HIGHER_PERFORMANCE
                    ? Boolean.TRUE
                    : Boolean.FALSE;
        } else {
            return Boolean.TRUE;
        }
    }
}

package io.graversen.replicate.flux;

import io.graversen.replicate.common.BasePredictionMapper;
import io.graversen.replicate.common.PredictionTypes;
import io.graversen.replicate.common.ReplicateModel;
import io.graversen.replicate.models.*;
import io.graversen.replicate.service.CreateImagePrediction;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;

@Component
public class FluxPredictionMapper extends BasePredictionMapper<CreateImagePrediction, Object> {
    @Override
    protected Set<ReplicateModel> supportedModels() {
        return Set.of(
                FluxModels.FLUX_DEV,
                FluxModels.FLUX_SCHNELL,
                FluxModels.FLUX_PRO
        );
    }

    @Override
    public Object apply(@NonNull ReplicateModel model, @NonNull CreateImagePrediction createPrediction) {
        if (model.equals(FluxModels.FLUX_DEV)) {
            return new BlackForestLabsFluxDevPredictionrequest(
                    null,
                    mapFluxDevInput().apply(createPrediction),
                    null,
                    null,
                    null,
                    null
            );
        } else if (model.equals(FluxModels.FLUX_SCHNELL)) {
            return new BlackForestLabsFluxSchnellPredictionrequest(
                    null,
                    mapFluxSchnellInput().apply(createPrediction),
                    null,
                    null,
                    null,
                    null
            );
        } else if (model.equals(FluxModels.FLUX_PRO)) {
            return new BlackForestLabsFluxProPredictionrequest(
                    null,
                    mapFluxProInput().apply(createPrediction),
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

    Function<CreateImagePrediction, BlackForestLabsFluxDevInput> mapFluxDevInput() {
        return createImagePrediction -> new BlackForestLabsFluxDevInput(
                createImagePrediction.getSeed(),
                null,
                createImagePrediction.getPrompt().getPrompt(),
                false,
                createImagePrediction.getPrompt().getPromptStrength(),
                null,
                createImagePrediction.getOutputs(),
                createImagePrediction.getPrompt().getAspectRatio().getAspectRatioAsString(),
                "png",
                100,
                null,
                createImagePrediction.getInferenceSteps(),
                true
        );
    }

    Function<CreateImagePrediction, BlackForestLabsFluxSchnellInput> mapFluxSchnellInput() {
        return createImagePrediction -> new BlackForestLabsFluxSchnellInput(
                createImagePrediction.getSeed(),
                createImagePrediction.getPrompt().getPrompt(),
                false,
                null,
                createImagePrediction.getOutputs(),
                createImagePrediction.getPrompt().getAspectRatio().getAspectRatioAsString(),
                "png",
                100,
                true
        );
    }

    Function<CreateImagePrediction, BlackForestLabsFluxProInput> mapFluxProInput() {
        return createImagePrediction -> new BlackForestLabsFluxProInput(
                createImagePrediction.getSeed(),
                createImagePrediction.getInferenceSteps(),
                createImagePrediction.getPrompt().getPrompt(),
                createImagePrediction.getPrompt().getPromptStrength(),
                null,
                createImagePrediction.getPrompt().getAspectRatio().getAspectRatioAsString(),
                5
        );
    }
}

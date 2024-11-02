package io.graversen.replicate.flux;

import io.graversen.replicate.common.ReplicateModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FluxModels {
    public static final ReplicateModel FLUX_SCHNELL = new ReplicateModel("black-forest-labs", "flux-schnell");
    public static final ReplicateModel FLUX_DEV = new ReplicateModel("black-forest-labs", "flux-dev");
    public static final ReplicateModel FLUX_PRO = new ReplicateModel("black-forest-labs", "flux-pro");
}

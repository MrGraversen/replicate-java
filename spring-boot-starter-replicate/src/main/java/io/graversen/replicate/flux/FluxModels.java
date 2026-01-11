package io.graversen.replicate.flux;

import io.graversen.replicate.common.ReplicateModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class FluxModels {
    public static final ReplicateModel FLUX_2_DEV = new ReplicateModel("black-forest-labs", "flux-2-dev");
    public static final ReplicateModel FLUX_2_FLEX = new ReplicateModel("black-forest-labs", "flux-2-flex");
    public static final ReplicateModel FLUX_2_PRO = new ReplicateModel("black-forest-labs", "flux-2-pro");
    public static final ReplicateModel FLUX_2_MAX = new ReplicateModel("black-forest-labs", "flux-2-max");
}

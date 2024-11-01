package io.graversen.replicate.llama3;

import io.graversen.replicate.common.ReplicateModel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Llama3Models {
    public static final ReplicateModel LLAMA_3_1_405B_INSTRUCT = new ReplicateModel("meta", "meta-llama-3.1-405b-instruct");
    public static final ReplicateModel LLAMA_3_70B_INSTRUCT = new ReplicateModel("meta", "meta-llama-3-70b-instruct");
    public static final ReplicateModel LLAMA_3_8B_INSTRUCT = new ReplicateModel("meta", "meta-llama-3-8b-instruct");
}

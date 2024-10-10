package io.graversen.replicate.client;

import feign.Param;
import feign.RequestLine;
import io.graversen.replicate.client.prediction.CreatePredictionDTO;
import io.graversen.replicate.client.prediction.CreatePredictionResponseDTO;

public interface Replicate {
    @RequestLine("POST /models/{model_owner}/{model_name}/predictions")
    CreatePredictionResponseDTO createPrediction(@Param("model_owner") String modelOwner, @Param("model_name") String modelName, CreatePredictionDTO createPrediction);

    default String apiVersion() {
        return "1.0.0-a1";
    }
}

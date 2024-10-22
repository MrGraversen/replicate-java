package io.graversen.replicate.client.replicate;

import feign.Param;
import feign.RequestLine;
import feign.Response;

public interface Replicate {
    @RequestLine("GET /models/{model_owner}/{model_name}")
    GetModelResponseDTO getModel(
            @Param("model_owner") String modelOwner,
            @Param("model_name") String modelName
    );

    @RequestLine("GET /models/{model_owner}/{model_name}/versions")
    GetModelVersionsResponseDTO getModelVersions(
            @Param("model_owner") String modelOwner,
            @Param("model_name") String modelName
    );

    @RequestLine("POST /models/{model_owner}/{model_name}/predictions")
    Response createPrediction(
            @Param("model_owner") String modelOwner,
            @Param("model_name") String modelName,
            Object createPredictionRequest
    );

    @RequestLine("GET /predictions/{prediction_id}")
    Response getPrediction(
            @Param("prediction_id") String predictionId
    );

    default String apiVersion() {
        return "1.0.0-a1";
    }
}

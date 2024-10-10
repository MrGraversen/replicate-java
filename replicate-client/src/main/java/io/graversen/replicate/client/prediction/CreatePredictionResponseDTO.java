package io.graversen.replicate.client.prediction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CreatePredictionResponseDTO {
    @JsonProperty("id")
    @NonNull String id;
    @JsonProperty("error")
    String error;
    @JsonProperty("status")
    String status;
    @JsonProperty("urls")
    CreatePredictionUrlsDTO urls;
}

package io.graversen.replicate.client.prediction;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class CreatePredictionUrlsDTO {
    @JsonProperty("cancel")
    @NonNull String cancelUrl;
    @JsonProperty("get")
    @NonNull String getUrl;
}

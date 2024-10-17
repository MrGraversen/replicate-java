package io.graversen.replicate.client.replicate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class GetModelResponseDTO {
    @JsonProperty("name")
    String name;
    @JsonProperty("description")
    String description;
    @JsonProperty("run_count")
    Integer runCount;
}

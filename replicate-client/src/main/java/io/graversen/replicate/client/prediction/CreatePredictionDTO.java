package io.graversen.replicate.client.prediction;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.graversen.replicate.client.ReplicateInputDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreatePredictionDTO {
    @JsonProperty("input")
    private final @NonNull ReplicateInputDTO input;
    @JsonProperty("webhook")
    private final String webhookUrl;
    @JsonProperty("webhook_events_filter")
    private final String[] webhookEventsFilter;
}

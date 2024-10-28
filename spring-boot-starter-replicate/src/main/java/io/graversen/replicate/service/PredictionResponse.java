package io.graversen.replicate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.Value;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Value
@RequiredArgsConstructor
public class PredictionResponse {
    @NonNull String id;
    String version;
    OffsetDateTime createdAt;
    OffsetDateTime startedAt;
    OffsetDateTime completedAt;
    String error;
    String status;
    Object input;
    Object output;
    PredictionUrls urls;

    @ToString.Include
    public Optional<Duration> getQueueLatency() {
        if (createdAt == null || startedAt == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(Duration.between(createdAt, startedAt));
    }

    @ToString.Include
    public Optional<Duration> getProcessingLatency() {
        if (startedAt == null || completedAt == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(Duration.between(startedAt, completedAt));
    }

    public <T> Optional<T> getOutput(@NonNull Function<Object, T> outputMapper) {
        try {
            if (output != null) {
                return Optional.of(outputMapper.apply(output));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public <T> Optional<T> getOutput(@NonNull Class<T> outputType) {
        try {
            return Optional.ofNullable(outputType.cast(output));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<String> getTextOutput() {
        try {
            return getOutput(output -> (ArrayList<String>) output).map(composeTextResponse());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private Function<List<String>, String> composeTextResponse() {
        return strings -> strings.stream().filter(string -> !string.isBlank()).collect(Collectors.joining());
    }
}

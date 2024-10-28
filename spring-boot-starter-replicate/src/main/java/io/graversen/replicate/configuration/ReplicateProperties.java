package io.graversen.replicate.configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "replicate")
public class ReplicateProperties {
    private final @NonNull String token;
    private final String apiUrl;
    private final Duration predictionPollDelay;
    private final Integer predictionPollAttempts;
}

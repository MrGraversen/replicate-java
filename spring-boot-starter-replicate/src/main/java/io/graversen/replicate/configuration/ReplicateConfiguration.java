package io.graversen.replicate.configuration;

import io.graversen.replicate.client.configuration.ReplicateClientProperties;
import io.graversen.replicate.client.configuration.ReplicateClients;
import io.graversen.replicate.client.feign.FeignUtils;
import io.graversen.replicate.client.replicate.Replicate;
import io.graversen.replicate.facade.PredictionRetryPolicy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

@Configuration
@EnableConfigurationProperties(ReplicateProperties.class)
@Import({Llama3Configuration.class, FluxConfiguration.class})
@ComponentScan({"io.graversen.replicate.service", "io.graversen.replicate.facade"})
public class ReplicateConfiguration {
    @Bean
    public Replicate replicate(ReplicateProperties properties) {
        final var replicateClientProperties = mapReplicateClientProperties().apply(properties);
        return ReplicateClients.v1(replicateClientProperties);
    }

    @Bean
    public FeignUtils feignUtils() {
        return new FeignUtils(ReplicateClients.objectMapper());
    }

    @Bean
    @ConditionalOnMissingBean
    public ExecutorService defaultReplicateExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public PredictionRetryPolicy predictionRetryPolicy(ReplicateProperties properties) {
        final var defaultRetryPolicy = PredictionRetryPolicy.defaultPolicy();
        final var pollDelay = Objects.requireNonNullElse(properties.getPredictionPollDelay(), defaultRetryPolicy.getDelay());
        final var pollAttempts = Objects.requireNonNullElse(properties.getPredictionPollAttempts(), defaultRetryPolicy.getMaxAttempts());
        return new PredictionRetryPolicy(pollAttempts, pollDelay);
    }

    Function<ReplicateProperties, ReplicateClientProperties> mapReplicateClientProperties() {
        return replicateProperties -> new ReplicateClientProperties(
                replicateProperties.getToken(),
                replicateProperties.getApiUrl()
        );
    }
}

package io.graversen.replicate.client.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.Feign;
import feign.Logger;
import feign.http2client.Http2Client;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.graversen.replicate.client.feign.ApplicationJsonInterceptor;
import io.graversen.replicate.client.feign.AuthorizationInterceptor;
import io.graversen.replicate.client.feign.Slf4jDebugLogger;
import io.graversen.replicate.client.replicate.Replicate;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Objects;
import java.util.function.UnaryOperator;

@UtilityClass
public class ReplicateClients {
    public static Replicate v1(@NonNull String token) {
        return ReplicateClients.v1(ReplicateClientProperties.withToken(token));
    }

    public static Replicate v1(@NonNull ReplicateClientProperties properties) {
        final UnaryOperator<Feign.Builder> feignCustomizer = feign -> feign
                .encoder(new JacksonEncoder(objectMapper()))
                .decoder(new JacksonDecoder())
                .logLevel(Logger.Level.FULL)
                .logger(new Slf4jDebugLogger());

        return ReplicateClients.v1(properties, feignCustomizer);
    }

    public static Replicate v1(@NonNull ReplicateClientProperties properties, @NonNull UnaryOperator<Feign.Builder> feignCustomizer) {
        final var feignBuilder = feignCustomizer.apply(feignBuilder())
                .requestInterceptor(new AuthorizationInterceptor(properties.getToken()))
                .requestInterceptor(new ApplicationJsonInterceptor());

        final var apiUrl = Objects.requireNonNullElse(properties.getApiUrl(), ReplicateDefaults.REPLICATE_V1_API_URL);

        return feignBuilder.target(Replicate.class, apiUrl);
    }

    public static ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .registerModule(new JavaTimeModule());
    }

    private static Feign.Builder feignBuilder() {
        return Feign.builder()
                .client(new Http2Client());
    }
}

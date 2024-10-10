package io.graversen.replicate.client;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.Feign;
import feign.Logger;
import feign.http2client.Http2Client;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.graversen.replicate.client.utils.ApplicationJsonInterceptor;
import io.graversen.replicate.client.utils.AuthorizationInterceptor;
import io.graversen.replicate.client.utils.Slf4jDebugLogger;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.function.UnaryOperator;

@UtilityClass
public class ReplicateClients {
    public static Replicate v1(@NonNull String replicateToken) {
        final UnaryOperator<Feign.Builder> feignCustomizer = feign -> feign
                .encoder(new JacksonEncoder(objectMapper()))
                .decoder(new JacksonDecoder())
                .logLevel(Logger.Level.BASIC)
                .logger(new Slf4jDebugLogger());

        return ReplicateClients.v1(replicateToken, feignCustomizer);
    }

    public static Replicate v1(@NonNull String replicateToken, @NonNull UnaryOperator<Feign.Builder> feignCustomizer) {
        final var feignBuilder = feignCustomizer.apply(feignBuilder())
                .requestInterceptor(new AuthorizationInterceptor(replicateToken))
                .requestInterceptor(new ApplicationJsonInterceptor());

        return feignBuilder.target(Replicate.class, "https://api.replicate.com/v1");
    }

    private static Feign.Builder feignBuilder() {
        return Feign.builder()
                .client(new Http2Client());
    }

    private ObjectMapper objectMapper() {
        return new ObjectMapper()
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }
}

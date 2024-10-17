package io.graversen.replicate.client.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class FeignUtils {
    private final @NonNull ObjectMapper objectMapper;

    @SneakyThrows
    public <T> T convert(@NonNull Response response, @NonNull Class<T> convertedClass) {
        return objectMapper.readValue(response.body().asInputStream(), convertedClass);
    }
}

package io.graversen.replicate.util;

import io.graversen.replicate.service.PredictionResponse;
import io.graversen.replicate.service.PredictionUrls;
import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Optional;

@Slf4j
@UtilityClass
public class ReplicateUtils {
    private static final String ATTRIBUTE_ID = "id";
    private static final String ATTRIBUTE_VERSION = "version";
    private static final String ATTRIBUTE_CREATED_AT = "created_at";
    private static final String ATTRIBUTE_STARTED_AT = "started_at";
    private static final String ATTRIBUTE_COMPLETED_AT = "completed_at";
    private static final String ATTRIBUTE_ERROR = "error";
    private static final String ATTRIBUTE_STATUS = "status";
    private static final String ATTRIBUTE_OUTPUT = "output";
    private static final String ATTRIBUTE_URLS = "urls";
    private static final String ATTRIBUTE_CANCEL_URL = "cancel";
    private static final String ATTRIBUTE_GET_URL = "get";

    public static Optional<PredictionResponse> mapPredictionResponse(@NonNull LinkedHashMap<String, Object> responseMap) {
        try {
            final var id = (String) responseMap.get(ATTRIBUTE_ID);
            final var version = (String) responseMap.get(ATTRIBUTE_VERSION);
            final var createdAt = parseOffsetDateTime((String) responseMap.get(ATTRIBUTE_CREATED_AT));
            final var startedAt = parseOffsetDateTime((String) responseMap.get(ATTRIBUTE_STARTED_AT));
            final var completedAt = parseOffsetDateTime((String) responseMap.get(ATTRIBUTE_COMPLETED_AT));
            final var error = (String) responseMap.get(ATTRIBUTE_ERROR);
            final var status = (String) responseMap.get(ATTRIBUTE_STATUS);
            final var output = responseMap.get(ATTRIBUTE_OUTPUT);

            final var urlsMap = (LinkedHashMap<String, Object>) responseMap.get(ATTRIBUTE_URLS);
            final var cancelUrl = (String) urlsMap.get(ATTRIBUTE_CANCEL_URL);
            final var getUrl = (String) urlsMap.get(ATTRIBUTE_GET_URL);

            PredictionUrls urls = null;
            if (cancelUrl != null && getUrl != null) {
                urls = new PredictionUrls(cancelUrl, getUrl);
            }

            final var predictionResponse = new PredictionResponse(id, version, createdAt, startedAt, completedAt, error, status, output, urls);
            return Optional.of(predictionResponse);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    @SneakyThrows
    private static String getStringField(@NonNull Class<?> responseObjectClass, @NonNull Object instance, @NonNull String methodName) {
        final var getter = responseObjectClass.getMethod(methodName);
        return getter.invoke(instance) != null ? (String) getter.invoke(instance) : null;
    }

    @SneakyThrows
    private static OffsetDateTime getOffsetDateTimeField(@NonNull Class<?> responseObjectClass, @NonNull Object instance, @NonNull String methodName) {
        final var getter = responseObjectClass.getMethod(methodName);
        return getter.invoke(instance) != null ? parseOffsetDateTime((String) getter.invoke(instance)) : null;
    }

    private static OffsetDateTime parseOffsetDateTime(@Nullable String isoDateTime) {
        return isoDateTime != null ? OffsetDateTime.parse(isoDateTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
    }
}

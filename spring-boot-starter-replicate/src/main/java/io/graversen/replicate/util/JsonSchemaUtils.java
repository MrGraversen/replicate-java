package io.graversen.replicate.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
@UtilityClass
public class JsonSchemaUtils {
    private static final String SET_ADDITIONAL_PROPERTY_METHOD = "setAdditionalProperty";

    public static void setAdditionalProperties(@NonNull Object object, @NonNull Map<String, Object> keysAndValues) {
        keysAndValues.forEach((key, value) -> setAdditionalProperties(object, key, value));
    }

    public static void setAdditionalProperties(@NonNull Object object, @NonNull String key, @NonNull Object value) {
        try {
            final var method = object.getClass().getMethod(SET_ADDITIONAL_PROPERTY_METHOD, String.class, Object.class);
            method.invoke(object, key, value);
        } catch (NoSuchMethodException e) {
            log.error("Could not invoke {}#{} because there is no such method", object.getClass().getSimpleName(), SET_ADDITIONAL_PROPERTY_METHOD);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

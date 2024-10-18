package io.graversen.replicate.common;

import lombok.NonNull;
import lombok.Value;

@Value
public class TextMessage {
    @NonNull String role;
    @NonNull String text;

    public static TextMessage user(@NonNull String text) {
        return new TextMessage(TextPredictionRoles.USER.asString(), text);
    }

    public static TextMessage assistant(@NonNull String text) {
        return new TextMessage(TextPredictionRoles.ASSISTANT.asString(), text);
    }
}

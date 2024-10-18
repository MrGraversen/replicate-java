package io.graversen.replicate.common;

public enum TextPredictionRoles {
    USER,
    ASSISTANT;

    public String asString() {
        return name().toLowerCase();
    }
}

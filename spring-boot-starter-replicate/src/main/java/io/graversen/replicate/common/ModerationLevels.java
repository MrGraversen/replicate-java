package io.graversen.replicate.common;

public enum ModerationLevels {
    HIGH,
    LOW;

    public static ModerationLevels defaultValue() {
        return HIGH;
    }
}

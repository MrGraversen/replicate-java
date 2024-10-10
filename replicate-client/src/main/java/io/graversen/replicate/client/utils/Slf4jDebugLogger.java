package io.graversen.replicate.client.utils;

import feign.Logger;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Slf4jDebugLogger extends Logger {
    @Override
    protected void log(String configKey, String format, Object... args) {
        log.debug("[{}]: {}", configKey, String.format(format, args));
    }
}

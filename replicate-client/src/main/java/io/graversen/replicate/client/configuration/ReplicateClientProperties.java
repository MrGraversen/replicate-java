package io.graversen.replicate.client.configuration;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReplicateClientProperties {
    private final @NonNull String token;
    private final String apiUrl;

    public static ReplicateClientProperties withToken(@NonNull String token) {
        return new ReplicateClientProperties(token, null);
    }
}

package io.graversen.replicate.client.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AuthorizationInterceptor implements RequestInterceptor {
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final @NonNull String replicateToken;

    @Override
    public void apply(RequestTemplate template) {
        template.header(AUTHORIZATION_HEADER, BEARER_PREFIX + replicateToken);
    }
}

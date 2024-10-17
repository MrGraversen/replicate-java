package io.graversen.replicate.client.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class ApplicationJsonInterceptor implements RequestInterceptor {
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String CONTENT_TYPE_VALUE = "application/json";

    @Override
    public void apply(RequestTemplate template) {
        template.header(CONTENT_TYPE_HEADER, CONTENT_TYPE_VALUE);
    }
}

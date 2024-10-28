package io.graversen.replicate.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class PredictionUrls {
    @NonNull String cancelUrl;
    @NonNull String getUrl;
}

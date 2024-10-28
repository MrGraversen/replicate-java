package io.graversen.replicate.facade;

import io.graversen.replicate.common.ReplicateModel;
import io.graversen.replicate.service.PredictionResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Value
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class PredictionResponseAndModel {
    @NonNull PredictionResponse predictionResponse;
    @NonNull ReplicateModel model;
}

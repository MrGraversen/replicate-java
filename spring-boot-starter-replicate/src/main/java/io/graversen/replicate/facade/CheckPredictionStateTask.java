package io.graversen.replicate.facade;

import jakarta.annotation.Nullable;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class CheckPredictionStateTask implements Function<Optional<PredictionResponseAndModel>, PredictionResponseAndModel> {
    @Override
    @SneakyThrows
    public PredictionResponseAndModel apply(@Nullable Optional<PredictionResponseAndModel> predictionResponse) {
        return predictionResponse.orElseThrow(predictionFailedError());
    }

    private Supplier<Exception> predictionFailedError() {
        return () -> new CompletionException(new RuntimeException("Could not initiate prediction"));
    }
}

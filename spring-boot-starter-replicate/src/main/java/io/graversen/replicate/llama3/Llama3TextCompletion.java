package io.graversen.replicate.llama3;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Llama3TextCompletion {
    private final @NonNull String text;
}

package io.graversen.replicate.facade;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ConversationOptions {
    private final @NonNull Double temperature;
}

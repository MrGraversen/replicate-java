package io.graversen.replicate.service;

import io.graversen.replicate.common.ReplicateModel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateConversation {
    private final @NonNull String systemMessage;
    private final @NonNull ReplicateModel replicateModel;
}

package io.graversen.replicate.service;

import io.graversen.replicate.common.TextConversation;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CreateTextPrediction {
    private final @NonNull TextConversation conversation;
    private final Double temperature;
    private final Integer maxTokens;
    private final Integer minTokens;
    private final String promptTemplate;
    private final Double topP;
    private final Integer topK;

    public static CreateTextPrediction fromOneMessage(@NonNull String systemPrompt, @NonNull String userMessage) {
        return CreateTextPrediction.fromConversation(TextConversation.of(systemPrompt, userMessage));
    }

    public static CreateTextPrediction fromConversation(@NonNull TextConversation conversation) {
        return new CreateTextPrediction(conversation, null, null, null, null, null, null);
    }
}

package io.graversen.replicate.llama3;

import io.graversen.replicate.common.BasePredictionMapper;
import io.graversen.replicate.common.PredictionTypes;
import io.graversen.replicate.common.ReplicateModel;
import io.graversen.replicate.models.*;
import io.graversen.replicate.service.CreateTextPrediction;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;

@Component
public class Llama3PredictionMapper extends BasePredictionMapper<CreateTextPrediction, Object> {
    @Override
    protected Set<ReplicateModel> supportedModels() {
        return Set.of(
                Llama3Models.LLAMA_3_1_405B_INSTRUCT,
                Llama3Models.LLAMA_3_70B_INSTRUCT,
                Llama3Models.LLAMA_3_8B_INSTRUCT
        );
    }

    @Override
    public Object apply(@NonNull ReplicateModel model, @NonNull CreateTextPrediction createPrediction) {
        if (model.equals(Llama3Models.LLAMA_3_70B_INSTRUCT)) {
            return new MetaMetaLlama370BInstructPredictionrequest(
                    null,
                    mapLlama370bInput().apply(createPrediction),
                    null,
                    null,
                    null,
                    null
            );
        } else if (model.equals(Llama3Models.LLAMA_3_8B_INSTRUCT)) {
            return new MetaMetaLlama38BInstructPredictionrequest(
                    null,
                    mapLlama38bInput().apply(createPrediction),
                    null,
                    null,
                    null,
                    null
            );
        } else if (model.equals(Llama3Models.LLAMA_3_1_405B_INSTRUCT)) {
            return new MetaMetaLlama31405BInstructPredictionrequest(
                    null,
                    mapLlama31405bInput().apply(createPrediction),
                    null,
                    null,
                    null,
                    null
            );
        } else {
            throw new IllegalArgumentException("Unsupported Replicate Model: " + model);
        }
    }

    @Override
    public boolean supportsType(@NonNull PredictionTypes type) {
        return PredictionTypes.TEXT.equals(type);
    }

    Function<CreateTextPrediction, MetaMetaLlama31405BInstructInput> mapLlama31405bInput() {
        return createTextPrediction -> {
            final var conversation = createTextPrediction.getConversation();
            final var truncatedConversation = Llama3Tokenizer.fitToContextWindow(conversation, Llama3Tokenizer.DEFAULT_CONTEXT_WINDOW_SIZE);
            final var textCompletion = Llama3Tokenizer.generateTextCompletion(truncatedConversation);

            return new MetaMetaLlama31405BInstructInput(
                    createTextPrediction.getTopK(),
                    createTextPrediction.getTopP(),
                    textCompletion.getText(),
                    createTextPrediction.getMaxTokens(),
                    createTextPrediction.getMinTokens(),
                    createTextPrediction.getTemperature(),
                    truncatedConversation.getSystemMessage(),
                    null,
                    null,
                    null
            );
        };
    }

    Function<CreateTextPrediction, MetaMetaLlama370BInstructInput> mapLlama370bInput() {
        return createTextPrediction -> {
            final var conversation = createTextPrediction.getConversation();
            final var truncatedConversation = Llama3Tokenizer.fitToContextWindow(conversation, Llama3Tokenizer.DEFAULT_CONTEXT_WINDOW_SIZE);
            final var textCompletion = Llama3Tokenizer.generateTextCompletion(truncatedConversation);

            return new MetaMetaLlama370BInstructInput(
                    createTextPrediction.getTopK(),
                    createTextPrediction.getTopP(),
                    textCompletion.getText(),
                    createTextPrediction.getMaxTokens(),
                    createTextPrediction.getMinTokens(),
                    createTextPrediction.getTemperature(),
                    createTextPrediction.getPromptTemplate(),
                    null,
                    null
            );
        };
    }

    Function<CreateTextPrediction, MetaMetaLlama38BInstructInput> mapLlama38bInput() {
        return createTextPrediction -> {
            final var conversation = createTextPrediction.getConversation();
            final var truncatedConversation = Llama3Tokenizer.fitToContextWindow(conversation, Llama3Tokenizer.DEFAULT_CONTEXT_WINDOW_SIZE);
            final var textCompletion = Llama3Tokenizer.generateTextCompletion(truncatedConversation);

            return new MetaMetaLlama38BInstructInput(
                    createTextPrediction.getTopK(),
                    createTextPrediction.getTopP(),
                    textCompletion.getText(),
                    createTextPrediction.getMaxTokens(),
                    createTextPrediction.getMinTokens(),
                    createTextPrediction.getTemperature(),
                    createTextPrediction.getPromptTemplate(),
                    null,
                    null
            );
        };
    }
}
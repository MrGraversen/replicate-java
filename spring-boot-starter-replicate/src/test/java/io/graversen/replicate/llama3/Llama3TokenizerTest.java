package io.graversen.replicate.llama3;

import io.graversen.replicate.common.TextConversation;
import io.graversen.replicate.common.TextMessage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class Llama3TokenizerTest {
    private final String systemMessage = "You are a helpful assistant";
    private final List<TextMessage> messages = List.of(
            new TextMessage("user", "Hello"),
            new TextMessage("assistant", "Hi there!"),
            new TextMessage("user", "How are you?"),
            new TextMessage("assistant", "I'm fine, thank you! How can I assist you today?"),
            new TextMessage("user", "Tell me a joke."),
            new TextMessage("assistant", "Why don't scientists trust atoms? Because they make up everything!")
    );

    @Test
    public void fitToContextWindow_defaultWindow() {
        final var conversation = new TextConversation(systemMessage, messages);
        final var fittedConversation = Llama3Tokenizer.fitToContextWindow(conversation, null);

        assertNotNull(fittedConversation);
        assertTrue(fittedConversation.getMessages().size() <= messages.size());
        assertEquals(systemMessage, fittedConversation.getSystemMessage());
        assertEquals(messages.get(0), fittedConversation.getMessages().get(0));
        assertEquals(messages.get(5), fittedConversation.getMessages().get(5));
        fittedConversation.getMessages().forEach(message ->
                assertTrue(messages.contains(message), "Fitted conversation should only contain messages from the original conversation")
        );
    }

    @Test
    public void fitToContextWindow_windowTooSmall() {
        final var conversation = new TextConversation(systemMessage, messages);
        final var fittedConversation = Llama3Tokenizer.fitToContextWindow(conversation, 128);

        assertNotNull(fittedConversation);
        assertTrue(fittedConversation.getMessages().size() < messages.size());
        assertNotEquals(messages.get(0), fittedConversation.getMessages().get(0));
        assertEquals(messages.get(2), fittedConversation.getMessages().get(1));
    }
}
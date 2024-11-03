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
    void parseTextCompletion_exampleConversation() {
        final var tokenizedConversation =
                "<|begin_of_text|><|start_header_id|>system<|end_header_id|>\n" +
                "You are a helpful assistant.<|eot_id|><|start_header_id|>user<|end_header_id|>\n" +
                "Hello. How are you?<|eot_id|><|start_header_id|>assistant<|end_header_id|>\n" +
                "Hello! I'm doing great, thanks for asking! I'm here to help you with anything you need, so please feel free to ask me any questions or share what's on your mind. How about you? How's your day going so far?<|eot_id|><|start_header_id|>user<|end_header_id|>\n" +
                "It is going good thanks<|eot_id|><|start_header_id|>assistant<|end_header_id|>\n" +
                "That's wonderful to hear! I'm glad to know that your day is going well. If you don't mind me asking, what's been the highlight of your day so far? Is there anything exciting or interesting that's happened? I'm all ears and happy to listen!<|eot_id|><|start_header_id|>user<|end_header_id|>\n" +
                "Nah fam<|eot_id|>";

        final var conversation = Llama3Tokenizer.parseTextCompletion(tokenizedConversation);

        assertEquals("You are a helpful assistant.", conversation.getSystemMessage());
        assertEquals(5, conversation.getMessages().size());

        assertEquals("user", conversation.getMessages().get(0).getRole());
        assertEquals("Hello. How are you?", conversation.getMessages().get(0).getText());

        assertEquals("assistant", conversation.getMessages().get(1).getRole());
        assertEquals("Hello! I'm doing great, thanks for asking! I'm here to help you with anything you need, so please feel free to ask me any questions or share what's on your mind. How about you? How's your day going so far?", conversation.getMessages().get(1).getText());

        assertEquals("user", conversation.getMessages().get(2).getRole());
        assertEquals("It is going good thanks", conversation.getMessages().get(2).getText());

        assertEquals("assistant", conversation.getMessages().get(3).getRole());
        assertEquals("That's wonderful to hear! I'm glad to know that your day is going well. If you don't mind me asking, what's been the highlight of your day so far? Is there anything exciting or interesting that's happened? I'm all ears and happy to listen!", conversation.getMessages().get(3).getText());

        assertEquals("user", conversation.getMessages().get(4).getRole());
        assertEquals("Nah fam", conversation.getMessages().get(4).getText());
    }

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
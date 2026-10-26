package com.psai.pdf_spring_ai;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PdfSpringAiApplicationTests {

	@Autowired(required = false)
    private ChatClient chatClient;

    @Test
    void contextLoads() {
        assertNotNull(chatClient, "OpenAI client should be loaded");
    }

}
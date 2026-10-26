package com.psai.pdf_spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AiProcessingService {

    private final ChatClient chatClient;

    public AiProcessingService(@Qualifier("defaultChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String summarizeText(String text) {
        String prompt = """
            Please summarize the following text in 3-5 bullet points.
            Focus on key ideas and main concepts.
            
            Text: %s
            """.formatted(text);

        return chatClient.prompt(prompt).call().content();
    }

    public String answerQuestion(String text, String question) {
        String prompt = """
            Based on the following text, answer this question: %s
            
            Text: %s
            
            Provide a concise answer with supporting details from the text.
            """.formatted(question, text);

        return chatClient.prompt(prompt).call().content();
    }
}

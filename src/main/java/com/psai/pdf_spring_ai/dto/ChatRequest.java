package com.psai.pdf_spring_ai.dto;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private String question;
    private String filename;
}
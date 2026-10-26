package com.psai.pdf_spring_ai.controller;

import com.psai.pdf_spring_ai.dto.ChatRequest;
import com.psai.pdf_spring_ai.dto.ChatResponse;
import com.psai.pdf_spring_ai.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:4200/") // Adjust as needed for your CORS policy
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChatResponse> chat(
            @RequestParam(value = "message", required = false) String message,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        if (file != null && !file.isEmpty()) {
            ChatResponse response = chatService.processMessageWithFile(message, file);
            return ResponseEntity.ok(response);
        } else {
            ChatRequest request = new ChatRequest();
            request.setMessage(message);
            ChatResponse response = chatService.processMessage(request);
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws IOException {
        Resource resource = chatService.loadFileAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
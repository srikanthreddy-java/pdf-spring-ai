package com.psai.pdf_spring_ai.service;

import com.psai.pdf_spring_ai.config.FileStorageProperties;
import com.psai.pdf_spring_ai.dto.ChatRequest;
import com.psai.pdf_spring_ai.dto.ChatResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class ChatService {

    private final Path fileStorageLocation;
    private final Tika tika = new Tika();

    @Autowired
    private AiProcessingService aiProcessingService;

    @Qualifier("defaultChatClient")
    @Autowired
    private ChatClient chatClient;

    @Autowired
    private PdfReaderService pdfreaderService;

    @Value("${file.allowed-types}")
    private String allowedTypes;

    public ChatService(FileStorageProperties fileStorageProperties) throws IOException {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
        log.info("File storage location initialized at {}", this.fileStorageLocation);
    }

    public ChatResponse processMessage(ChatRequest request) {
        log.info("Processing message: {}", request.getMessage());
        String responseContent = chatClient.prompt(request.getMessage()).call().content();
        log.info("Generated response: {}", responseContent);
        return new ChatResponse(responseContent);
    }

    public String storeFile(MultipartFile file) throws IOException {
        log.info("Storing file: {}", file.getOriginalFilename());
        // Validate file type
        String fileType = tika.detect(file.getBytes());
        log.info("Detected file type: {}", fileType);
        if (!allowedTypes.contains(fileType)) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        // Generate unique filename
        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path targetLocation = this.fileStorageLocation.resolve(filename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        log.info("File stored successfully at {}", targetLocation);
        return filename;
    }

    public ChatResponse processMessageWithFile(String message, MultipartFile file) throws IOException {
        log.info("Processing message with file: {}", file.getOriginalFilename());
        // Extract text from PDF
        String pdfText = pdfreaderService.extractTextFromPdf(file);
        log.info("Extracted text from PDF: {}", pdfText);
        // Store the file for future reference
        String filename = storeFile(file);
        log.info("File stored with filename: {}", filename);
        String responseContent;
        if(message == null || message.trim().isEmpty()) {
            // Summarize the PDF text
            responseContent = aiProcessingService.summarizeText(pdfText);
            log.info("Generated summary: {}", responseContent);
        } else {
            // Answer the question based on the PDF text
            responseContent = aiProcessingService.answerQuestion(pdfText, message);
            log.info("Generated answer: {}", responseContent);
        }
        return new ChatResponse(responseContent);
    }

    public Resource loadFileAsResource(String filename) throws MalformedURLException {
        Path filePath = this.fileStorageLocation.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("File not found " + filename);
        }
    }
}

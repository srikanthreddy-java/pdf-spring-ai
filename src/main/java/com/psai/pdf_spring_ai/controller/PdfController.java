package com.psai.pdf_spring_ai.controller;

import com.psai.pdf_spring_ai.service.PdfReaderService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200") // Allow all origins for CORS
@RestController
@RequestMapping("/api/chat")
@Slf4j
public class PdfController {

    private final PdfReaderService pdfReaderService;

    public PdfController(PdfReaderService pdfReaderService) {
        this.pdfReaderService = pdfReaderService;
    }
    
    @GetMapping("/test")
    public String sample() {
    	return "test done";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handlePdfUpload(@RequestParam("file") MultipartFile file)
            throws IOException {
        String text = pdfReaderService.extractTextFromPdf(file.getInputStream());
        log.info("Extracted text from PDF: {}", text);
        return ResponseEntity.ok().body(Map.of(
                "filename", "test.pdf",
                "message", "File uploaded successfully"
        ));
    }

    @PostMapping("/ask")
    public String askQuestion(@RequestParam("file") MultipartFile file,
                              @RequestParam String question) throws IOException {
        String text = pdfReaderService.extractTextFromPdf(file.getInputStream());
        log.info("Extracted text with ask from PDF: {}", text);
        return "Question received: " + question + ". Processing your request...";
    }
}

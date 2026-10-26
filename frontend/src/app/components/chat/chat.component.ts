import { Component, OnInit } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { Message } from '../../models/message.model';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.css'],
})
export class ChatComponent implements OnInit {
  messages: Message[] = [];
  newMessage: string = '';
  selectedFile: File | null = null;
  uploadedFiles: string[] = [];

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    // Initial bot message
    this.addBotMessage('Hello! How can I assist you today?');
  }

  sendMessage(): void {
    if (!this.newMessage.trim() && !this.selectedFile) return;

    const userMessage: Message = {
      id: uuidv4(),
      content: this.newMessage || `Sent file: ${this.selectedFile?.name}`,
      sender: 'user',
      timestamp: new Date(),
      file: this.selectedFile
        ? {
            name: this.selectedFile.name,
            type: this.selectedFile.type,
            size: this.selectedFile.size,
          }
        : undefined,
    };

    this.messages.push(userMessage);

    this.apiService
      .sendMessageWithFile(this.newMessage, this.selectedFile)
      .subscribe({
        next: (response) => {
          console.log('Response from server:', response);
          this.addBotMessage(response.content);
          this.clearInput();
        },
        error: (error) => {
          this.addBotMessage(
            'Sorry, I encountered an error processing your request. ' +
              (error.error?.message || 'Please try again.')
          );
          console.error('Error:', error);
          this.clearInput();
        },
      });
  }

  addBotMessage(content: string): void {
    const botMessage: Message = {
      id: uuidv4(),
      content,
      sender: 'bot',
      timestamp: new Date(),
    };
    this.messages.push(botMessage);
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  removeFile(): void {
    this.selectedFile = null;
  }

  clearInput(): void {
    this.newMessage = '';
    this.selectedFile = null;
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Message } from '../models/message.model';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  private apiUrl = 'http://localhost:8080/api/chat';

  constructor(private http: HttpClient) {}

  sendMessageWithFile(message: string, file: File | null): Observable<Message> {
    const formData = new FormData();
    if (message) {
      formData.append('message', message);
    }
    if (file) {
      formData.append('file', file);
    }
    return this.http.post<Message>(this.apiUrl, formData);
  }

  downloadFile(filename: string): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/files/${filename}`, {
      responseType: 'blob',
    });
  }
}

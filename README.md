# PDF Spring AI

This project is a Spring Boot application that lets users upload PDF files, extract the text, and ask AI-powered questions or generate summaries using OpenAI through Spring AI.

It also includes an Angular frontend for interacting with the backend.

## Features

- Upload PDF files
- Extract text from PDF documents
- Ask questions based on uploaded PDF content
- Generate summaries from PDF text
- REST API endpoints for chat and file download
- Swagger UI for API testing

## Tech Stack

- Java 21
- Spring Boot 4.1.2
- Spring AI 2.0.2
- OpenAI integration
- Angular frontend
- Maven

## Prerequisites

Before running the project, install:

- JDK 21 or newer
- Maven
- Node.js 18+ and npm
- An OpenAI API key

## Project Structure

```text
pdf-spring-ai/
├── src/main/java/           # Spring Boot backend
├── src/main/resources/       # application.yml and configs
├── src/test/java/           # tests
├── frontend/                # Angular UI
├── pom.xml                  # Maven project config
├── mvnw                     # Maven wrapper
├── mvnw.cmd                 # Maven wrapper for Windows
├── README.md                # project documentation
└── uploads/                 # uploaded PDF files
```

## Configuration

Set your OpenAI API key as an environment variable.

### Windows PowerShell

```powershell
$env:OPENAI_API_KEY="sk-your-openai-key"
```

### macOS/Linux

```bash
export OPENAI_API_KEY="sk-your-openai-key"
```

Update the Spring configuration in `src/main/resources/application.yml`:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        model: gpt-4o-mini
        temperature: 0.7
```

Important:

- Use a real OpenAI key
- Do not use an Azure or custom gateway URL unless you are explicitly configured for that provider
- For plain OpenAI, the default OpenAI endpoint is used automatically

## Run the Backend

From the project root:

### Windows

```powershell
./mvnw.cmd spring-boot:run
```

### macOS/Linux

```bash
./mvnw spring-boot:run
```

The backend starts on:

```text
http://localhost:8080
```

## Run the Frontend

Open a new terminal and go to the frontend folder:

```bash
cd frontend
npm install
npm start
```

The Angular app typically runs on:

```text
http://localhost:4200
```

## API Endpoints

### Chat without file

```http
POST /api/chat
Content-Type: application/x-www-form-urlencoded
```

Form data:

- `message` : the question or prompt

Example:

```bash
curl -X POST "http://localhost:8080/api/chat" \
  -F "message=Can you summarize today ind vs NZ cricket match?"
```

### Chat with PDF file

```http
POST /api/chat
Content-Type: multipart/form-data
```

Form data:

- `message` : optional question
- `file` : uploaded PDF file

Example:

```bash
curl -X POST "http://localhost:8080/api/chat" \
  -F "message=Summarize this document" \
  -F "file=@/path/to/sample.pdf"
```

### Download uploaded file

```http
GET /api/chat/files/{filename}
```

## Swagger UI

Once the backend is running, open:

```text
http://localhost:8080/swagger-ui.html
```

## Common Issues

### 404: Unknown from OpenAI

This usually means one of the following:

- the API key is not a valid OpenAI key
- the key belongs to a different provider or gateway
- the requested model is not available for the account
- the app is calling the wrong base URL

For plain OpenAI usage, the correct setup is:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY}
      chat:
        model: gpt-4o-mini
```

### Frontend not connecting to backend

Check that the backend is running on port 8080 and the frontend is using the correct API path.

## Build the Project

### Build backend

```bash
./mvnw clean package
```

### Build frontend

```bash
cd frontend
npm install
npm run build
```

## Notes

- Uploaded files are stored under `uploads/`
- Supported upload type is PDF
- The backend logs request and response details for debugging

## License

This project is for educational/demo purposes unless otherwise specified.


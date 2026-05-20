# Auto Resume Sender

A simple Java utility for automated resume distribution via Gmail SMTP.

The application reads email addresses from a text file and automatically sends personalized emails with attached resumes.

## Features

- Automatic email sending via Gmail SMTP
- Language detection by domain
    - `.ru` / `.by` → Russian cover letter
    - all other domains → English cover letter
- Sends both Russian and English PDF resumes
- Reads recipient emails from a plain text file
- Built with Java + Gradle
- Simple console logging

---

## Tech Stack

- Java 25
- Gradle
- Jakarta Mail API

---

## Project Structure

```text
project/
│
├── emails.txt
├── resume_ru.pdf
├── resume_en.pdf
├── build.gradle
├── settings.gradle
└── src/main/java/org/example/ResumeSender.java
```

---

## Setup

### 1. Clone repository

```bash
git clone <your_repo_url>
```

---

### 2. Configure Gmail SMTP

Enable:
- 2-Step Verification
- App Passwords

Generate Gmail App Password and paste it into:

```java
private static final String PASSWORD = "YOUR_APP_PASSWORD";
```

---

### 3. Add files

Create:

```text
emails.txt
resume_ru.pdf
resume_en.pdf
```

Example `emails.txt`:

```text
hr@company.ru
jobs@startup.by
career@example.com
```

---

## Run

```bash
gradle run
```

or run `ResumeSender.java` directly from IntelliJ IDEA.

---

## Notes

- The application sends:
    - Russian email text for `.ru` and `.by` domains
    - English email text for all other domains
- Both resumes are always attached
- Small delay between emails helps avoid Gmail rate limits

---

## Disclaimer

This project was created for educational and personal job-search automation purposes.
package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class ResumeSender {

    private static final String RU_RESUME = "resume_ru.pdf";
    private static final String EN_RESUME = "resume_en.pdf";

    static void main(String[] args) throws Exception {

        // Load config

        Properties config = new Properties();

        config.load(new FileInputStream("config.properties"));

        String email = config.getProperty("email");

        String password = config.getProperty("password");

        String subjectRu = config.getProperty("subject.ru");

        String subjectEn = config.getProperty("subject.en");

        // Load email bodies

        String bodyRu = Files.readString(Paths.get("body_ru.txt"));

        String bodyEn = Files.readString(Paths.get("body_en.txt"));

        // Load recipients

        List<String> emails = Files.readAllLines(Paths.get("emails.txt"));

        // SMTP

        Properties props = new Properties();

        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(
                props,
                new Authenticator() {

                    @Override
                    protected PasswordAuthentication
                    getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                email,
                                password
                        );
                    }
                }
        );

        int success = 0;
        int failed = 0;

        // Send emails

        for (String recipient : emails) {

            recipient = recipient.trim();

            if (recipient.isEmpty()) {
                continue;
            }

            boolean isRussianDomain =
                    recipient.endsWith(".ru")
                            || recipient.endsWith(".by");

            String subject = isRussianDomain ? subjectRu : subjectEn;

            String body = isRussianDomain ? bodyRu : bodyEn;

            try {

                MimeMessage message = new MimeMessage(session);

                message.setFrom(new InternetAddress(email));

                message.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(recipient)
                );

                message.setSubject(
                        subject,
                        "UTF-8"
                );

                // Text
                MimeBodyPart textPart = new MimeBodyPart();

                textPart.setText(
                        body,
                        "UTF-8"
                );

                Multipart multipart = new MimeMultipart();

                multipart.addBodyPart(textPart);

                // RU resume
                MimeBodyPart ruResume = new MimeBodyPart();

                ruResume.attachFile(new File(RU_RESUME));

                multipart.addBodyPart(ruResume);

                // EN resume
                MimeBodyPart enResume = new MimeBodyPart();

                enResume.attachFile(new File(EN_RESUME));

                multipart.addBodyPart(enResume);

                message.setContent(multipart);

                Transport.send(message);

                success++;

                System.out.println("[OK] " + recipient);

                Thread.sleep(3000);

            } catch (Exception e) {

                failed++;

                System.out.println(
                        "[ERROR] "
                                + recipient
                                + " -> "
                                + e.getMessage()
                );
            }
        }

        System.out.println();
        System.out.println("===== DONE =====");
        System.out.println("SUCCESS: " + success);
        System.out.println("FAILED: " + failed);
    }
}
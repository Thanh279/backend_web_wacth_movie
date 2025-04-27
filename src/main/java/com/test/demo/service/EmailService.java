package com.test.demo.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private static final String FROM_EMAIL = "drawin278@gmail.com";
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(JavaMailSender javaMailSender, SpringTemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    public void sendSimpleEmail(String to) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(FROM_EMAIL);
        msg.setTo(to);
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World from Spring Boot Email");
        try {
            this.javaMailSender.send(msg);
            logger.info("Simple email sent from {} to {}", FROM_EMAIL, to);
        } catch (MailException e) {
            logger.error("Error sending simple email to {}: {}", to, e.getMessage(), e);
            throw new RuntimeException("Failed to send simple email to " + to, e);
        }
    }

    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setFrom(FROM_EMAIL);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            this.javaMailSender.send(mimeMessage);
            logger.info("Email sent from {} to {} with subject: {}", FROM_EMAIL, to, subject);
        } catch (MailException | MessagingException e) {
            logger.error("Error sending email from {} to {}: {}", FROM_EMAIL, to, e.getMessage(), e);
            throw new RuntimeException("Failed to send email to " + to, e);
        }
    }

    public void sendEmailFromTemplateSync(String to, String subject, String templateName, String username) {
        try {
            Context context = new Context();
            context.setVariable("name", username);
            String content = this.templateEngine.process(templateName, context);
            logger.info("Template content for {}: {}", to, content);
            this.sendEmailSync(to, subject, content, false, true);
        } catch (Exception e) {
            logger.error("Error processing template or sending email from {} to {}: {}", FROM_EMAIL, to, e.getMessage(), e);
            throw new RuntimeException("Failed to process template or send email to " + to, e);
        }
    }
}
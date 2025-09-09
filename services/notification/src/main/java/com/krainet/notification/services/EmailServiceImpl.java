package com.krainet.notification.services;

import com.krainet.notification.core.interfaces.services.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    private String getHtml(String message){
        String template = "notification.html";
        Map<String, Object> variables = new HashMap<>();
        variables.put("message", message);
        Context context = new Context();
        context.setVariables(variables);
        return templateEngine.process(template, context);
    }

    @Async
    @SneakyThrows
    public void send(String to, String subject, String text) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper =
                new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        try {
            mimeMessageHelper.setText(getHtml(text), true);
            mimeMessageHelper.setTo(Arrays.stream(to.split(";")).map(str->str.trim()).toArray(String[]::new));
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setFrom("notification@krainet.com");

            mailSender.send(mimeMessage);
            log.info("Email sent to " + to);

        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }
}

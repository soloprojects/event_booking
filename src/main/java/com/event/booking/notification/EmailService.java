package com.event.booking.notification;

import com.event.booking.entity.Event;
import com.event.booking.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Async("taskExecutor")
    public void send(List<User> users, Event event) {
        for (User user : users) {
            try {
                sendEmailToUser(user, event);

            } catch (MessagingException e) {
                logger.error("Failed to send email to email: {}, message : {}", user.getEmail(), e.getMessage());
            } catch (Exception e) {
                logger.error("An unexpected error occurred during email sending: {}", e.getMessage());
            }
        }
    }

    private void sendEmailToUser(User user, Event event) throws MessagingException {
        String templateName = "mail/notification-email";
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED, StandardCharsets.UTF_8.name());

        Map<String, Object> properties = new HashMap<>();
        properties.put("eventName", event.getName());
        properties.put("eventDate", event.getDate());

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("solomon.social@gmail.com");
        helper.setTo(user.getEmail());
        helper.setSubject("Welcome to our Event platform");

        String template = templateEngine.process(templateName, context);
        helper.setText(template, true);

        //mailSender.send(mimeMessage);
    }

}

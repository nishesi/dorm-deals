package ru.itis.master.party.dormdeals.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender mailSender;
    private final Configuration configuration;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String subject, String templateName, Map<String, String> data) {

        String text = processTemplate(templateName, data);

        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setSubject(subject);
            messageHelper.setText(text, true);
            messageHelper.setTo(to);
            messageHelper.setFrom(from);
        };

        mailSender.send(mimeMessagePreparator);
    }

    private String processTemplate(String templateName, Map<String, String> data)  {
        try {
            Template template = configuration.getTemplate(templateName);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(out);
            template.process(data, writer);
            writer.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(out.toByteArray())));
            return reader.lines().collect(Collectors.joining());
        } catch (TemplateException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}

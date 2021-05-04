package br.com.chart.enterative.service;

import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;

import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

/**
 *
 * @author William Leite
 */
@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendSimpleMail(final Map<String, Object> context, final String subject, final String from, final String to,
            final Locale locale, final String template) throws MessagingException {
        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariables(context);

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject(subject);
        message.setFrom(from);
        message.setTo(to);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process(template, ctx);
        message.setText(htmlContent, true);
        this.mailSender.send(mimeMessage);
    }
}

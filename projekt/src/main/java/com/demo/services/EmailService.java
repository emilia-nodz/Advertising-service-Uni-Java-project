package com.demo.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.jms.MessageListener;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class EmailService {
    // Aby przetestować działanie użyłam Mailtrap, musicie poniżej wstawić username i password z tego co znajdziecie w Sandbox
    private final String username = "7befe8541766a1";
    private final String password = "4550d54ecb09c7";
    private Logger logger = Logger.getLogger(MessageListener.class.getName());

    public void sendEmail(String to, String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
        props.put("mail.smtp.port", "2525");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            msg.setSubject(subject);
            msg.setText(content);
            Transport.send(msg);
            logger.log(Level.INFO,"Pomyślnie wsyłano wiadomość");
        } catch (MessagingException e) {
            logger.log(Level.SEVERE,"Błąd podczas wysyłania wiadomości: ", e);
        }
    }
}

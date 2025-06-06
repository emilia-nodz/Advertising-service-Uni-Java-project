package com.demo.services;

import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.MessageDrivenContext;
import jakarta.inject.Inject;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:app/jms/queue1"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
})
public class MessageReceiver implements MessageListener {

    @Inject
    private EmailService emailService;

    @Resource
    private MessageDrivenContext mdc;

    private Logger logger = Logger.getLogger(MessageReceiver.class.getName());

    @Override
    public void onMessage(Message message) {
        try {
            String text = message.getBody(String.class);
            String[] parts = text.split("\\|\\|");

            if (parts.length == 3) {
                emailService.sendEmail(parts[0], parts[1], parts[2]);
            } else {
                logger.warning("Nieprawidłowy format wiadomości: " + text);
            }
        } catch (JMSException e) {
            logger.log(Level.SEVERE, "Błąd odbierania wiadomości:", e);
            mdc.setRollbackOnly();
        }
    }
}

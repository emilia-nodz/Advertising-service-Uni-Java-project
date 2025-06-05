package com.demo.services;

import jakarta.annotation.Resource;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.MessageDrivenContext;
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

    private Logger logger = Logger.getLogger(MessageListener.class.getName());
    @Resource
    private MessageDrivenContext mdc;

    @Override
    public void onMessage(Message message) {
        try {
            logger.info("Otrzymana wiadomość: " + message.getBody(String.class));
        } catch (JMSException e) {
            logger.log(Level.SEVERE,"Błąd podczas odbierania wiadomości: ",e);
            mdc.setRollbackOnly();
        }
    }
}

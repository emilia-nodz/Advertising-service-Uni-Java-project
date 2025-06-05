package com.demo.services;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@Stateless
public class MessageSender {

    @Resource(lookup = "java:app/jms/cf1")
    private ConnectionFactory connectionFactory;

    @Resource(lookup="java:app/jms/queue1")
    private Queue queue;

    public void send(String messageText) {
        try (JMSContext context = connectionFactory.createContext()) {
            context.createProducer().send(queue,messageText);
        }
    }
}

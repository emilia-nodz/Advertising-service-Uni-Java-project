package com.demo.controllers;

import com.demo.services.MessageSender;
import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@Named("messageController")
@ViewScoped
public class MessageController implements Serializable {
    @EJB
    private MessageSender messageSender;

    private String message;

    public MessageController() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void onSend() {
        messageSender.send(message);
    }
}

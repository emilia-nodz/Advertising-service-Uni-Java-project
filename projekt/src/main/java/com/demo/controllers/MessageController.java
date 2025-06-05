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

    private String to; // Osoba, do której ma trafić wiadomość
    private String subject; // Temat
    private String body; // Treść wiadomości/maila

    public MessageController() { }

    // Gettery i settery:
    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void onSend() {
        messageSender.send(to, subject, body);
    }
}

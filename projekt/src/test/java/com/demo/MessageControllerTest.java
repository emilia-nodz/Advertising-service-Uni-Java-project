package com.demo;

import com.demo.controllers.MessageController;
import com.demo.services.MessageSender;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MessageControllerTest {
    private static final Logger logger = LogManager.getLogger(MessageControllerTest.class);
    private MessageController messageController;
    private MessageSender mockSender;

    @BeforeEach
    void setUp() { // Tworzenie potrzebnych mocków
        mockSender = Mockito.mock(MessageSender.class);
        messageController = new MessageController() {
            {
                this.messageSender = mockSender;
            }
        };
    }

    @Test // Test sprawdzający, czy wysyłanie maila działa poprawnie
    void test_sending_mail() {
        String to = "test@test.pl";
        String subject = "Twoje ogłoszenie zostało zaakceptowane";
        String body = "Twoje ogłoszenie zostało zaakceptowane przez moderatora";

        messageController.setTo(to);
        messageController.setSubject(subject);
        messageController.setBody(body);
        messageController.onSend();
        verify(mockSender, times(1)).send(to, subject, body);
        logger.info("Wiadomość testowa została poprawnie wysłana");
    }
}

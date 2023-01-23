package ipsen2.groep8.werkplekkenreserveringsappbackend.thread;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;

import ipsen2.groep8.werkplekkenreserveringsappbackend.service.EmailService;

public class MailThread extends Thread {
    
    private String to;
    private String subject;
    private String body;

    private EmailService emailService;

    public MailThread(String to, String subject, String body, EmailService emailService) {
        this.to = to;
        this.subject = subject;
        this.body = body;
        this.emailService = emailService;
    }
    
    public void run() {
        try {
            this.emailService.sendMessage(this.to, this.subject, this.body);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

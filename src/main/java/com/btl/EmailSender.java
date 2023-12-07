package com.btl;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {

    public static void sendEmail(String recipient, String subject, String text) throws MessagingException {
        String username = "dicedictionary@gmail.com";
        String password = "kjjddqpstasinclv";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

        Session session = Session.getInstance(props,auth);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient,false));
            message.setSubject(subject);
            message.setText(text);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        Transport.send(message);
    }
}
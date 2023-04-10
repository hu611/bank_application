package com.base.util;

import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtils{
    public static void _send_email(String to, String msg, String subject) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        ObjectMapper objectMapper = new ObjectMapper();

        File file = new File("/Users/austin/Desktop/projects/Bank_Application/Email_info.json");
        EmailDto emailDto = objectMapper.readValue(file,EmailDto.class);
        String from= emailDto.getGmail();
        String password = emailDto.getPassword();
        //get Session
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from,password);
                    }
                });
        MimeMessage message = new MimeMessage(session);
        message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));
        message.setSubject(subject);
        message.setText(msg);
        //send message
        Transport.send(message);
        System.out.println("message sent successfully");

    }
}

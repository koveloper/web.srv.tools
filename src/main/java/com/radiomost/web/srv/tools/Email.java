/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.radiomost.web.srv.tools;

import com.sun.mail.smtp.SMTPTransport;
import java.util.Date;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author kgn
 */
public class Email {

    private String SMTP_SERVER = "";
    private String USERNAME = "";
    private String PASSWORD = "";

    private Session session = null;

    public Email(String SMTP_SERVER, String USERNAME, String PASSWORD) {
        this.SMTP_SERVER = SMTP_SERVER;
        this.USERNAME = USERNAME;
        this.PASSWORD = PASSWORD;
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "465"); // default port 25
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        session = Session.getInstance(prop, null);
    }

    public String sendText(String from, String to, String title, String text) throws Exception {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(from));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse("", false));
        msg.setSubject(title);
        msg.setText(text);
        msg.setSentDate(new Date());
        SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
        t.connect(SMTP_SERVER, USERNAME, PASSWORD);
        t.sendMessage(msg, msg.getAllRecipients());
        String answer = t.getLastServerResponse();
        t.close();
        return answer;
    }

    public String sendHTML(String from, String to, String title, String htmlTemplate) throws Exception {
        Message msg = new MimeMessage(session);

        msg.setFrom(new InternetAddress(from));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
        msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse("", false));
        msg.setSubject(title);
        msg.setContent(htmlTemplate, "text/html; charset=UTF-8");
        msg.setSentDate(new Date());
        SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
        t.connect(SMTP_SERVER, USERNAME, PASSWORD);
        t.sendMessage(msg, msg.getAllRecipients());
        String answer = t.getLastServerResponse();
        t.close();
        return answer;
    }
}

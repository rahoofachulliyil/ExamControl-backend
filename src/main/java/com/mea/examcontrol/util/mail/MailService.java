package com.mea.examcontrol.util.mail;

import javax.mail.MessagingException;

import javax.mail.internet.MimeMessage;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service("mailService")
public class MailService implements MailRepository{
 
    @Autowired
    JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromMail;
    
 
    public void sendEmail(Mail mail) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
 
        try {
 
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
 
            mimeMessageHelper.setSubject(mail.getMailSubject());
            mimeMessageHelper.setFrom(fromMail);
            mimeMessageHelper.setTo(mail.getMailTo());
            
            
            String html = "<!doctype html>\n" +
                "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\"\n" +
                "      xmlns:th=\"http://www.thymeleaf.org\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\"\n" +
                "          content=\"width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0\">\n" +
                "    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\n" +
                "    <title>Email</title>\n" +
                "<style>\n"+
                " h1 {\n"+
                "color:red;\n"+
                "}span {color:blue;}\n"+
                "</style>\n"+
                "</head>\n" +
                "<body>\n" +
                "<div><h1>Mea Exam Cell</h1> </div><div><b>" + mail.getMailSubject() + "</b></div>\n" +
                "\n" +
                "<div>  <b>" + mail.getMailContent() + "</b></div>\n" +
                "</body>\n" +
                "</html>\n";
                mimeMessageHelper.setText(html,true);
            mailSender.send(mimeMessageHelper.getMimeMessage());
 
        } catch (MessagingException e) {
            e.printStackTrace();
        } 
    }
 
}

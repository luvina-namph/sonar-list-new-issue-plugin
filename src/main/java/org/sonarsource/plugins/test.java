package org.sonarsource.plugins;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.sonar.api.utils.log.Loggers;

public class test {

    public static void main(String[] args) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", CommonConstant.SMTP_HOST);

        Session session = Session.getDefaultInstance(properties);

        try {
            // create mail message
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(CommonConstant.MAIL_FROM));

            for (String mailTo : CommonConstant.MAIL_TO) {
                message.addRecipients(Message.RecipientType.TO, mailTo);
            }

            message.setSubject( "test");

            message.setText("test");

            // Send mail
            Transport.send(message);
            Loggers.get(MailSender.class).info("    " + "send mail success.");
        }catch (MessagingException e) {
            Loggers.get(MailSender.class).info(e.getMessage());
        }

    }

}

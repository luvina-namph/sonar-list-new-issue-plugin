package org.sonarsource.plugins;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.sonar.api.utils.log.Loggers;

public class  MailSender {

    public boolean sendMail(IssueReportDto issueReportDto) {
        
        String reportContent = buildReportContent(issueReportDto);
        Loggers.get(MailSender.class).info(reportContent);
        
        // Add properties of smtp mail server
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

            message.setSubject( "[SonarQube] Analysis result for" +
                                " [" + issueReportDto.getProjectName() + "]" +
                                " At rev " + issueReportDto.getVersion() +
                                " ");

            message.setText(reportContent);

            // Send mail
            Transport.send(message);
            Loggers.get(MailSender.class).info("    " + "send mail success.");
        }catch (MessagingException e) {
            Loggers.get(MailSender.class).info(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * build String report content for send mail after sonarQube analysis 
     * 
     * @param issueReportDto 
     * @return report content
     */
    private String buildReportContent(IssueReportDto issueReportDto) {
        StringBuilder reportContent = new StringBuilder("SonarQube report");
        reportContent.append("\n    " + "The project name is : " + issueReportDto.getProjectName());
        reportContent.append("\n    " + "Revision is         : " + issueReportDto.getVersion());
        reportContent.append("\n    " + "Quality gate is     : " + issueReportDto.getQualityGateStatus());
        int rowCount = 0;
        for (String kee : issueReportDto.getIssueKees()) {
            if (rowCount == 0) {
                reportContent.append("\n    " + "The bug are: ");
            }
            reportContent.append("\n    " + CommonConstant.SONAR_QUBE_SERVER + "issues/search#issues=" + kee);
            ++rowCount;
        }
        reportContent.append("\n    " + "Total bug = " + rowCount);
        
        return reportContent.toString();
    }
}

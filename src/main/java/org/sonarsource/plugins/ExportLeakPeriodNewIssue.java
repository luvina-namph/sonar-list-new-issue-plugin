package org.sonarsource.plugins;

import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class ExportLeakPeriodNewIssue implements PostProjectAnalysisTask {
    private static final Logger LOGGER = Loggers.get(ExportLeakPeriodNewIssue.class);

    @Override
    public void finished(ProjectAnalysis analysis) {

        String projectUuid = analysis.getProject().getUuid();
        String projectName = analysis.getProject().getName();
        
        IssueDao issueDao = new IssueDao();
        IssueReportDto issueReportDto = issueDao.getLeakPeriodNewIssue(projectName, projectUuid);

        issueReportDto.setProjectName(projectName);

        QualityGate gate = analysis.getQualityGate();
        if (gate != null) {
            issueReportDto.setQualityGateStatus(gate.getStatus().toString());
        }

        // send report mail
        MailSender mailSender = new MailSender();
        if (mailSender.sendMail(issueReportDto)) {
            LOGGER.info("    Mail report has been sent");
        }
    }
}

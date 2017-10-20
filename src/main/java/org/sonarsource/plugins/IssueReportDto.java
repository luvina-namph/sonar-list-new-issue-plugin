package org.sonarsource.plugins;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class IssueReportDto implements Serializable{
    private static final long serialVersionUID = 7578975835435939797L;

    private String projectName;
    private String qualityGateStatus;
    private String version;
    private List<String> issueKees;

    public IssueReportDto() {
        issueKees = Collections.emptyList();
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public List<String> getIssueKees() {
        return issueKees;
    }
    public void setIssueKees(List<String> issueKees) {
        this.issueKees = issueKees;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getQualityGateStatus() {
        return qualityGateStatus;
    }
    public void setQualityGateStatus(String qualityGateStatus) {
        this.qualityGateStatus = qualityGateStatus;
    }
}

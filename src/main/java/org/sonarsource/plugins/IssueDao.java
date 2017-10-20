package org.sonarsource.plugins;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class IssueDao {
    private static final Logger LOGGER = Loggers.get(IssueDao.class);

    /**
     * Connect db and get Kee (unique column) of new issue form Leak Period
     * 
     * @param projectName name of project 
     * @param projectUuid unique id of project
     * @return IssueReportDto
     */
    public IssueReportDto getLeakPeriodNewIssue (String projectName, String projectUuid) {
        LOGGER.info("\nproject is : " + projectName + ", " + projectUuid);
        IssueReportDto issueReportDto = new IssueReportDto();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            LOGGER.info(e.getMessage());
        }

        try (
            Connection conn = DriverManager.getConnection(
                    CommonConstant.SONAR_QUBE_JDBC_URL,
                    CommonConstant.SONAR_QUBE_JDBC_USERNAME,
                    CommonConstant.SONAR_QUBE_JDBC_PASS);
            Statement connStatement = conn.createStatement()
        ) {
            String sqlGetSnapshotPeriodTime =
                    " SELECT snapshots.period1_date, " +
                    "   snapshots.build_date, " +
                    "   snapshots.version " +
                    " FROM snapshots " +
                    " INNER JOIN projects " +
                    "   ON projects.id = snapshots.project_id " +
                    " WHERE projects.project_uuid = \"" + projectUuid + "\"" +
                    "   AND projects.name = \"" + projectName + "\" " +
                    "   AND snapshots.islast = 1; ";
            ResultSet snapshotPeriodTimeSet = connStatement.executeQuery(sqlGetSnapshotPeriodTime);
            LOGGER.info(sqlGetSnapshotPeriodTime);

            snapshotPeriodTimeSet.next();
            
            issueReportDto.setVersion(snapshotPeriodTimeSet.getString("version"));

            String sqlGetPeriodIssueKee =
                    " SELECT issues.kee " +
                    " FROM issues " +
                    " WHERE issues.project_uuid = \"" + projectUuid + "\"" +
                    "   AND issues.issue_creation_date > " + snapshotPeriodTimeSet.getLong("period1_date") +
                    "   AND issues.issue_creation_date < " + snapshotPeriodTimeSet.getLong("build_date");
            ResultSet issueSet = connStatement.executeQuery(sqlGetPeriodIssueKee);
            LOGGER.info(sqlGetPeriodIssueKee);

            List<String> issueKees = new ArrayList<>();
            while (issueSet.next()) {
                issueKees.add(issueSet.getString("kee"));
            }

            issueReportDto.setIssueKees(issueKees);

            // close result set
            snapshotPeriodTimeSet.close();
            issueSet.close();
        } catch (SQLException ex) {
            LOGGER.info(ex.getMessage());
        }
        return issueReportDto;
    }
}

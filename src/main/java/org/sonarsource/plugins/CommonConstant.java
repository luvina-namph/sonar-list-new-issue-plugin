package org.sonarsource.plugins;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommonConstant {
    private CommonConstant() {
        throw new IllegalStateException("Common Constant");
      }
    public static final String SMTP_HOST = "mail.luvina.net";
    public static final String MAIL_FROM = "noreply@luvina.net";
    public static final List<String> MAIL_TO = 
            Collections.unmodifiableList(
                    Arrays.asList("phamhoainam@luvina.net"));
    public static final String SONAR_QUBE_SERVER = "http://192.168.0.235:9000/sonar/";
    public static final String SONAR_QUBE_JDBC_URL = "jdbc:mysql://192.168.0.235:3306/sonar";
    public static final String SONAR_QUBE_JDBC_USERNAME = "sonar";
    public static final String SONAR_QUBE_JDBC_PASS = "nam0687";
}

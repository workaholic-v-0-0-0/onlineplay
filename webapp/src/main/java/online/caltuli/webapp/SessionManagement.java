package online.caltuli.webapp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import online.caltuli.business.UserManager;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.UserConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Timestamp;

public class SessionManagement {

    private static final Logger logger = LogManager.getLogger(SessionManagement.class);

    // if no session is defined in the HttpServletRequest instance parameter :
    // fetch information related to connection, record them into connections tables
    // so that the related primary key value is automatically generated ;
    // fetch them from connections table and record them into a userConnection instance
    public static HttpSession initialiseSessionIfNot(HttpServletRequest request, HttpServletResponse response)
            throws BusinessException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                ||
                // To handle the case where Tomcat has been restarted, by reconstructing a new session
                // without persisting its attributes
                session.getAttribute("userConnection") == null) {
            session = request.getSession(true);
            UserConnection userConnection = null;
            String ipAddress = request.getRemoteAddr();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            try {
                userConnection = UserManager.logUserConnection(ipAddress, timestamp);
                logger.info("userConnection.getId() = " + userConnection.getId());
                session.setAttribute("userConnection", userConnection);
            } catch (BusinessException e) {
                logger.info("can't register information related to the user connection in database");
                throw e;
            }
        }
        return session;
    }

    public static boolean isAuthenticated(HttpSession session) {
        UserConnection userConnection = (UserConnection) session.getAttribute("userConnection");
        return userConnection != null && userConnection.getUserId() != -1;
    }
}

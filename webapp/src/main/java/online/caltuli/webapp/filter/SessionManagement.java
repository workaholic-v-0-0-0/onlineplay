package online.caltuli.webapp.filter;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.inject.Inject;
import online.caltuli.business.UserManager;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.UserConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.Timestamp;

@ApplicationScoped
public class SessionManagement {

    private static final Logger logger = LogManager.getLogger(SessionManagement.class);

    @Inject
    private UserManager userManager;

    // if no session is defined in the HttpServletRequest instance parameter :
    // fetch information related to connection, record them into connections tables
    // so that the related primary key value is automatically generated ;
    // fetch them from connections table and record them into a userConnection instance
    public HttpSession initialiseSessionIfNot(HttpServletRequest request, HttpServletResponse response)
            throws BusinessException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null
                ||
                // To handle the case where Tomcat has been restarted, by reconstructing a new session
                // without persisting its attributes
                session.getAttribute("userConnection") == null) {
            session = request.getSession(true);
            UserConnection userConnection = null;

            // Due to Nginx acting as a reverse proxy in front of Tomcat, request.getRemoteAddr()
            // // returns the localhost address. To obtain the actual client IP address, we use
            // the X-Real-IP and X-Forwarded-For headers set by Nginx
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            String ipAddress = httpRequest.getHeader("X-Real-IP");
            if (ipAddress == null) {
                String forwarded = httpRequest.getHeader("X-Forwarded-For");
                if (forwarded != null) {
                    // Takes the first IP address from the X-Forwarded-For header in case the request
                    // passed through multiple proxies.
                    ipAddress = forwarded.split(",")[0];
                }
            }
            if (ipAddress == null) {
                ipAddress = request.getRemoteAddr();
            }

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            try {
                userConnection = userManager.logUserConnection(ipAddress, timestamp);
                logger.info("userConnection.getId() = " + userConnection.getId());
                session.setAttribute("userConnection", userConnection);
            } catch (BusinessException e) {
                logger.info("can't register information related to the user connection in database");
                throw e;
            }
        }
        return session;
    }

    public boolean isAuthenticated(HttpSession session) {
        UserConnection userConnection = (UserConnection) session.getAttribute("userConnection");
        return userConnection != null && userConnection.getUserId() != -1;
    }
}

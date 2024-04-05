package online.caltuli.webapp.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import online.caltuli.model.UserConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// check if IP is not forbidden by fetching this information
// in session.userConnection.isAllowed ;
// if it's forbidden, redirect to error page with suitable message
public class IPCheckFilter implements Filter {

    private final Logger logger = LogManager.getLogger(IPCheckFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (!path.equals("/error")) {

            HttpServletResponse httpResponse = (HttpServletResponse) response;
            HttpSession session = httpRequest.getSession(false);

            if (session != null) {
                UserConnection userConnection = (UserConnection) session.getAttribute("userConnection");
                Boolean isAllowed = null;
                if (userConnection != null && ((isAllowed = userConnection.getIsAllowed()) == null || !isAllowed)) {
                    httpRequest.getSession().setAttribute("errorMessage", "Access Denied: Your IP address has been restricted due to security policies. Please contact support if you believe this is an error.");
                    httpResponse.sendRedirect("error");
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }
}

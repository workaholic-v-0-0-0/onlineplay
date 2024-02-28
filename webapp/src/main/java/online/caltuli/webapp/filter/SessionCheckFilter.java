package online.caltuli.webapp.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.webapp.SessionManagement;
import online.caltuli.webapp.servlet.Home;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

// if no session define it and initialize its 'userConnection' attribute
public class SessionCheckFilter implements Filter {

    private Logger logger = LogManager.getLogger(Home.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        if (!path.equals("/error")) {

            HttpServletResponse httpResponse = (HttpServletResponse) response;

            try {
                SessionManagement.initialiseSessionIfNot((HttpServletRequest) request, httpResponse);
            } catch (BusinessException e) {
                // httpRequest.getSession().setAttribute("errorMessage", e.getMessage()); // to debug
                httpRequest.getSession(true).setAttribute("errorMessage", "We apologize for the inconvenience. Our database server is currently unavailable. We are actively working to restore access. Please try again later.");
                httpResponse.sendRedirect("error");
                return;
            }
        }
        chain.doFilter(request, response);
    }
}

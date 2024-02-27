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
        SessionManagement.initialiseSessionIfNot((HttpServletRequest) request, (HttpServletResponse) response);
        chain.doFilter(request, response);
    }
}

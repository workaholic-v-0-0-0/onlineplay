package online.caltuli.webapp.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import online.caltuli.business.UserManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// Implements a filter to redirect HTTP requests to HTTPS, enhancing security.
public class HttpsRedirectFilter implements Filter {

    private final Logger logger = LogManager.getLogger(HttpsRedirectFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        // logger.info("HERE");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String upgradeHeader = request.getHeader("Upgrade");

        if (upgradeHeader != null && "websocket".equalsIgnoreCase(upgradeHeader)) {
            chain.doFilter(req, res);
        }
        else if (!request.isSecure()) {
            String url = "https://" + request.getServerName() + request.getRequestURI();
            if (request.getQueryString() != null) {
                url += "?" + request.getQueryString();
            }
            response.sendRedirect(url);
        } else {
            chain.doFilter(req, res);
        }
    }
}

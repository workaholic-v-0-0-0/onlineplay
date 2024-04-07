package online.caltuli.webapp.servlet.gui;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Error extends HttpServlet {

    private final Logger logger = LogManager.getLogger(Error.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String errorMessage = null;
        if (session != null) {
            errorMessage = (String) session.getAttribute("errorMessage");
            session.removeAttribute("errorMessage");
            session.invalidate();
            logger.info("Error message retrieved and session invalidated.");
        } else {
            logger.info("No existing session found for the request.");
        }
        if (errorMessage == null || errorMessage.isEmpty()) {
            errorMessage = "undefined cause";
            logger.info("No error message provided, defaulting to 'Undefined cause'.");
        } else {
            logger.info("Forwarding to error page with message: " + errorMessage);
        }
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/WEB-INF/error.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}

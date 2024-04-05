package online.caltuli.webapp.servlet;

import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import online.caltuli.business.UserManager;
import online.caltuli.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class Logout extends HttpServlet {
    private static final long serialVersionUID = 1L;
    @Inject
    private UserManager userManager;
    private Logger logger = LogManager.getLogger(Home.class); // to debug

    public Logout() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        userManager.disconnectUser(((User) session.getAttribute("user")).getId());
        request.getSession().invalidate();
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : "home");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
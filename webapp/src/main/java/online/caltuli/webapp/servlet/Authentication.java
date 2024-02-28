package online.caltuli.webapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import online.caltuli.business.exception.BusinessException;
import online.caltuli.business.UserManager;
import online.caltuli.model.UserConnection;
import online.caltuli.model.User;
import online.caltuli.model.exceptions.user.UserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// to debug
import java.io.PrintWriter;

public class Authentication extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = LogManager.getLogger(Authentication.class);

    public Authentication() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/WEB-INF/authentication.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		User user = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		try {
			user = UserManager.authenticateUser(username, password);
			session.setAttribute("user", user);
			if (user == null) {
				request.setAttribute("authenticationFailed", true);
			} else {
				request.setAttribute("hasJustBeenAuthenticated", user.getUsername());
				((UserConnection) session.getAttribute("userConnection")).setUserId(user.getId());
				try {
					UserManager.updateUserConnection(
							((UserConnection) session.getAttribute("userConnection"))
					);
				} catch (BusinessException e) {
					logger.info("Information related to user "
							+ user.getId()
							+ " a.k.a. "
							+ user.getUsername()
							+ " can't be updated with the suitable user id.\n"
							+ "It will stay anonymous");
				}
			}
		} catch (BusinessException e) {
			request.setAttribute("authenticationProblemEncountred", e.getMessage());
		} catch (UserException e) {
			request.setAttribute("authenticationProblemEncountred", "Your account has been invalidated.");
		}

		//request.getRequestDispatcher("/home").forward(request, response);
		this.getServletContext().getRequestDispatcher("/WEB-INF/authentication.jsp").forward(request, response);
	}
}

package online.caltuli.webapp.servlet.gui;

import online.caltuli.business.exception.BusinessException;
import online.caltuli.business.UserManager;

import online.caltuli.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.inject.Inject;

import online.caltuli.model.exceptions.user.*;

import java.io.IOException;

public class Registration extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private UserManager userManager;

    public Registration() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/WEB-INF/registration.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String message = request.getParameter("message");

		User user = null;
		try {
			user = userManager.registerUser(username, password, email, message);
			if (user == null) {
				request.setAttribute("registrationProblemEncountred", "username " + username + " already used");
			} else {
				request.setAttribute("hasJustBeenRegistred", username);
			}
		} catch (BusinessException e) {
			request.setAttribute("registrationProblemEncountred", e.getMessage());
		} catch (UserException e) {
			request.setAttribute("registrationProblemEncountred", e.getMessage());
		}

		this.getServletContext().getRequestDispatcher("/WEB-INF/registration.jsp").forward(request, response);
	}

}

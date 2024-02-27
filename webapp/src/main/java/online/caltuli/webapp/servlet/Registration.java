package online.caltuli.webapp.servlet;

import online.caltuli.business.exception.BusinessException;
import online.caltuli.business.UserManager;

import online.caltuli.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.caltuli.model.exceptions.user.*;

import java.io.IOException;

// \begin{todebug}
import java.io.PrintWriter;

public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Registration() {
        // TODO Auto-generated constructor stub
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
			user = UserManager.registerUser(username, password, email, message);
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

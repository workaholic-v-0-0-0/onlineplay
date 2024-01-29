package online.caltuli.webapp.servlet;

import online.caltuli.business.BusinessException;
import online.caltuli.business.UserManager;
import online.caltuli.model.UserConnection;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

// \begin{todebug}
import java.io.PrintWriter;

public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Register() {
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// redirect to home if no session or if it has no "userConnection" attribute
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("userConnection") == null) {
			request.getRequestDispatcher("/home").forward(request, response);
		} else {
			this.getServletContext().getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int userId = -1;
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		String message = request.getParameter("message");

		PrintWriter out = response.getWriter();
		try {
			userId = UserManager.registerUser(username, password, email, message);
		} catch (BusinessException e) {
			out.println(e.getMessage());
		}
		if (userId == -1) {
			request.setAttribute("registrationProblemEncountred", "USERNAMEALREADYUSED");
		} else {
			request.setAttribute("registrationProblemEncountred", "NONE");
		}
		this.getServletContext().getRequestDispatcher("/WEB-INF/register.jsp").forward(request, response);
	}

}

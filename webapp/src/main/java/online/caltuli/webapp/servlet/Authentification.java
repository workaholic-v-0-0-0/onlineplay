package online.caltuli.webapp.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import online.caltuli.business.BusinessException;
import online.caltuli.business.UserManager;
import online.caltuli.consumer.UserConnectionsDao;
import online.caltuli.model.UserConnection;
import online.caltuli.model.User;

// to debug
import java.io.PrintWriter;

public class Authentification extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Authentification() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.getServletContext().getRequestDispatcher("/WEB-INF/authentification.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = null;
		User user = null;
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		// to debug
		PrintWriter out = response.getWriter();
		out.println("ok?");
		try {
			user = UserManager.authenticateUser(username, password);
		} catch (BusinessException e) {
			out.println(e.getMessage());
		}
		if (user == null) {
			out.println("failed.");
		} else {
			out.println("Authentication succeeded.");
		}


		// PROBLÈME ICI, ON DIRAIT QUE LE CHAMPS EST SUPPRIMÉ DE LA TABLE
		// LORS DE LA TENTATIVE DE MISE À JOUR DE LA TABLE connections AVEC
		// L'IDENTIFIANT DE L'UTILISATEUR
		if (user == null) {
			out.println("failed.");
		} else {
			session = request.getSession(false);
			out.println(session.getAttribute("userConnection"));
			out.println(((UserConnection) session.getAttribute("userConnection")).getUserId());
			((UserConnection) session.getAttribute("userConnection")).setUserId(user.getId());
			out.println(((UserConnection) session.getAttribute("userConnection")).getUserId());

			out.println("begin dumpUserConnectionInResponse");
			dumpUserConnectionInResponse(
					response,
					(UserConnection) session.getAttribute("userConnection")
			);
			out.println("begin dumpUserConnectionInResponse");

			try {
				UserManager.updateUserConnection(
						((UserConnection) session.getAttribute("userConnection"))
				);
			} catch (BusinessException e) {
				out.println(e.getMessage());
			}
		}
		request.getRequestDispatcher("/home").forward(request, response);
	}

	// to debug
	private void dumpUserConnectionInResponse(
			HttpServletResponse response,
			UserConnection userConnection) {
		try {
			PrintWriter out = response.getWriter();
			out.println("userConnection.getId() -> " + userConnection.getId());
			out.println("userConnection.getIpAddress() -> " + userConnection.getIpAddress());
			out.println("userConnection.getTimestamp() -> " + userConnection.getTimestamp());
			out.println("userConnection.getUserId() -> " + userConnection.getUserId());
		} catch (Exception e) {
			try {
				PrintWriter out = response.getWriter();
				out.println(e.getMessage());
			} catch (Exception e2) {

			}
		}
	}

}

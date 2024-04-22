package online.caltuli.webapp.servlet.gui;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import online.caltuli.business.PlayerManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.User;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;

import org.apache.logging.log4j.Logger;

public class Home extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Inject
	private PlayerManager playerManager;

	private Logger logger = LogManager.getLogger(Home.class); // to debug
       
    public Home() {
		super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// pour des raisons de sécurité, il faut convertir les éléments des listes
		// en Player et en GameSummary afin de ne pas exposer les hashes des
		// utilisateurs
		request.setAttribute(
				"authenticatedUsers",
				playerManager.
						usersToPlayer(
								playerManager.getAuthenticatedUsers()
						)
		);
		request.setAttribute(
				"waitingToPlayUsers",
				playerManager
						.usersToPlayer(
								playerManager.getWaitingToPlayUser()
						)
		);
		request.setAttribute(
				"games",
				playerManager.
						gamesToGameSummaries(
								playerManager.getGames()
						)
		);
		this.getServletContext().getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// C'est qui ?
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");

		if (user == null) { // C'est personne
			// il faut lui expliquer qu'il doit s'authentifier
		}

		// Qu'est-ce qu'il veut ?
		String action = request.getParameter("action");
		if ("new_game".equals(action)) { // il veut faire une partie
			try {
				playerManager.makeUserProposeGame(user);
			} catch (BusinessException e) {
				logger.info("User " + user.getId() + " had not been able to propose a game.");
			}
		}

		// pour repasser les paramètres pour afficher tout de suite les trois
		// listes
		doGet(request, response);
	}

}

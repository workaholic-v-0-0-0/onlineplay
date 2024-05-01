package online.caltuli.webapp.servlet.gui;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import online.caltuli.business.ConstantGridParser;
import online.caltuli.business.PlayerManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.Coordinates;
import online.caltuli.model.User;
import online.caltuli.model.UserConnection;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Enumeration;

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


		for (int line = 0 ; line < 6 ; line++) {
			for (int column = 0 ; column < 7 ; column++) {
				int length = ConstantGridParser.arrayOfCoordinatesRowsStartingFromBottomWithCoordinates[line][column].length;
				logger.info("length: " + length);
				if (length == 0) {
					logger.info("param[" + line + "," + column + "] = empty");
				}
				for (int l = 0 ; l < length ; l++) {
					Coordinates[] r = ConstantGridParser.arrayOfCoordinatesRowsStartingFromBottomWithCoordinates[line][column][l];
					logger.info("param[" + line + "," + column + "][" + l + "]=["
							+ "(" + r[0].getX() + "," + r[0].getY() + "), "
							+ "(" + r[1].getX() + "," + r[1].getY() + "), "
							+ "(" + r[2].getX() + "," + r[2].getY() + "), "
							+ "(" + r[3].getX() + "," + r[3].getY() + "), ")
							;
				}
			}
		}

		logger.info("This is a GET request.");

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

		// monitoring des paramètres POST
		Enumeration<String> parameterNames = request.getParameterNames();
		StringBuilder paramsLog = new StringBuilder("Received POST parameters: ");
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String[] paramValues = request.getParameterValues(paramName);  // Utilisez getParameterValues pour gérer les paramètres multiples
			paramsLog.append(paramName).append("=");

			if (paramValues.length > 1) {
				paramsLog.append(Arrays.toString(paramValues));
			} else {
				paramsLog.append(paramValues[0]);
			}
			if (parameterNames.hasMoreElements()) {
				paramsLog.append(", ");
			}
		}
		logger.info(paramsLog.toString());
		// fin monitoring

		// C'est qui ?
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");

		if (user == null) { // C'est personne
			// il faut lui expliquer qu'il doit s'authentifier
		}

		// Qu'est-ce qu'il veut ?
		String action = request.getParameter("action");

		// il veut proposer une partie et attendre un adversaire
		if ("new_game".equals(action)) {
			try {
				playerManager.makeUserProposeGame(user);
			} catch (BusinessException e) {
				logger.info("User " + user.getId() + " had not been able to propose a game.");
			}
		}

		// il veut jouer contre un utilisateur qui s'est déjà proposé
		if ("play_with".equals(action)) {
			// on récupère id du joueur contre qui il veut jouer dans userId
			String userIdString = request.getParameter("user_id");
			int userId = 0;
			if (userIdString == null || userIdString.isEmpty()) {
				// ???
			}
			try {
				userId = Integer.parseInt(userIdString);
			} catch (NumberFormatException e) {
				logger.info("blabla");
			}

			// début to debug
			logger.info("This is a POST request.");
			if (user == null) {
				logger.info(
						"user is null and userConnection.id: " +
						((UserConnection) session.getAttribute("userConnection")).getId());
			} else {
				logger.info(
						"user is not null and userConnection.id: " +
								((UserConnection) session.getAttribute("userConnection")).getId());
			}
			logger.info("id " + userId + " wants to play against id " + user.getId());
			// fin to debug


			try {
				playerManager.makeUserPlayWithUser(
						userId,
						user
				);
			} catch (BusinessException e) {
				logger.info("???");
			}

		}



		// pour repasser les paramètres pour afficher tout de suite les trois
		// listes
		doGet(request, response);
	}

}

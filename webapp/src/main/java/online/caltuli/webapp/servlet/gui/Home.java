package online.caltuli.webapp.servlet.gui;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import online.caltuli.business.ConstantGridParser;
import online.caltuli.business.GameManager;
import online.caltuli.business.PlayerManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.*;
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


		/* begin debug
		for (int line = 0 ; line < 6 ; line++) {
			for (int column = 0 ; column < 7 ; column++) {
				int length = ConstantGridParser.arrayOfCoordinatesRowsStartingFromBottomWithCoordinates[line][column].length;
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

		 */

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

		logger.info("deGet is called");

		// C'est qui ?
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");

		// si authentifié
		if (user != null) {
			logger.info("This is user " + user.getId());
			// est-il en train de jouer une partie ou d'attendre un adversaire ?
			GameManager gameManager = (GameManager) session.getAttribute("gameManager");
			if (gameManager == null) {
				// il n'est impliqué dans aucune partie
				logger.info("User "
						+ user.getId()
						+ " is neither waiting for an opponent nor playing a game."
				);
			}

			if (gameManager != null) {
				// il est impliqué dans une partie
				Game game = gameManager.getGame();
				Player firstPlayer = game.getFirstPlayer();
				Player secondPlayer = game.getSecondPlayer();
				logger.info("User "
						+ user.getId()
						+ " is related to game "
						+ gameManager.getGame().getId()
						+ " via gameManager "
						+ gameManager
				);

				if (game.getGameState() == GameState.WAIT_OPPONENT) {
					// il attend un adversaire
					logger.info("User "
							+ user.getId()
							+ " is currently waiting an other user to play "
					);
				} else {
					// il est en train de jouer contre un autre adversaire
					logger.info("User "
							+ user.getId()
							+ " is currently playing against user "
							+ (
									(secondPlayer.getId() == user.getId()) ?
											firstPlayer.getId()
											:
											secondPlayer.getId()
							)
					);

					GameState playerWon, waitPlayerMove, otherPlayerWon, waitOtherPlayerMove;
					if (firstPlayer.getId() == user.getId()) {
						playerWon = GameState.FIRST_PLAYER_WON;
						waitPlayerMove = GameState.WAIT_FIRST_PLAYER_MOVE;
						otherPlayerWon = GameState.SECOND_PLAYER_WON;
						waitOtherPlayerMove = GameState.WAIT_SECOND_PLAYER_MOVE;
					} else {
						playerWon = GameState.SECOND_PLAYER_WON;
						waitPlayerMove = GameState.WAIT_SECOND_PLAYER_MOVE;
						otherPlayerWon = GameState.FIRST_PLAYER_WON;
						waitOtherPlayerMove = GameState.WAIT_FIRST_PLAYER_MOVE;
					}

					GameState gameState = game.getGameState();
					if (gameState == playerWon) {
						// Code à exécuter si le joueur a gagné
					} else if (gameState == waitPlayerMove) {
						// Code à exécuter si on attend que le joueur joue
					} else if (gameState == otherPlayerWon) {
						// Code à exécuter si l'autre joueur a gagné
					} else if (gameState == waitOtherPlayerMove) {
						// Code à exécuter si on attend que l'autre joueur joue
					} else if (gameState == GameState.DRAW) {
						// Code à exécuter en cas de match nul
					} else {
						// Gérer les autres états, tels que WAIT_OPPONENT et TO_BE_CANCELLED, si nécessaire
					}
				}
			}
		}

		this.getServletContext().getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// monitoring des paramètres POST
		/*
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
		 */
		// fin monitoring

		logger.info("doPost is called");

		// C'est qui ?
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");

		if (user == null) { // C'est personne
			// il faut lui expliquer qu'il doit s'authentifier
			logger.info("It's nobody");
		}

		// Qu'est-ce qu'il veut ?
		String action = request.getParameter("action");
		GameManager gameManager = null;

		// il veut proposer une partie et attendre un adversaire
		if ("new_game".equals(action)) {
			try {
				gameManager = playerManager.makeUserProposeGame(user);
				session.setAttribute("gameManager", gameManager);
			} catch (BusinessException e) {
				logger.info("User " + user.getId() + " had not been able to propose a game.");
			}
			logger.info("GameManager fetched by user "
					+ user.getId()
					+ " is "
					+ gameManager
					+ ". Its Game instance attribute is game with id "
					+ gameManager.getGame().getId()
			);
		}

		// il veut jouer contre un utilisateur qui s'est déjà proposé
		if ("play_with".equals(action)) {
			// on récupère id du joueur contre qui il veut jouer dans userId
			String userIdString = request.getParameter("user_id");
			int userId = 0;
			if (userIdString == null || userIdString.isEmpty()) {
				// ???
				logger.info("blabla");
			}
			try {
				userId = Integer.parseInt(userIdString);
			} catch (NumberFormatException e) {
				// ???
				logger.info("blabla");
			}

			// début to debug
			/*
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
			 */
			// fin to debug

			try {
				gameManager = playerManager
						.makeUserPlayWithUser(
								userId,
								user
						);
				session.setAttribute("gameManager", gameManager);
			} catch (BusinessException e) {
				logger.info("???");
			}

			logger.info("GameManager fetched by user "
					+ user.getId()
					+ " is "
					+ gameManager
					+ ". Its Game instance attribute is game with id "
					+ gameManager.getGame().getId()
			);

		}


		// pour repasser les paramètres pour afficher tout de suite les trois
		// listes
		// et pour ...???
		doGet(request, response);
	}

}

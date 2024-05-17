package online.caltuli.webapp.servlet.gui;

import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
import online.caltuli.business.ConstantGridParser;
import online.caltuli.business.GameManager;
import online.caltuli.business.PlayerManager;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.*;

import java.io.IOException;
import java.util.HashSet;

import com.fasterxml.jackson.databind.ObjectMapper;

import online.caltuli.webapp.util.JsonUtil;
import online.caltuli.webapp.websocket.GameWebSocket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Home extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@Inject
	private PlayerManager playerManager;
	private Logger logger = LogManager.getLogger(Home.class);
    public Home() {
		super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Set attributes to display public information related to user activities

		// Convert and set the list of authenticated users for display
		request.setAttribute(
				"authenticatedUsers",
				playerManager.
						usersToPlayer(
								playerManager.getAuthenticatedUsers()
						)
		);
		// Convert and set the list of users who are waiting to play for display
		request.setAttribute(
				"waitingToPlayUsers",
				playerManager
						.usersToPlayer(
								playerManager.getWaitingToPlayUser()
						)
		);
		// Convert and set the list of ongoing games into summaries for display
		request.setAttribute(
				"games",
				playerManager.
						gamesToGameSummaries(
								playerManager.getGames()
						)
		);


		// Retrieve the current user and session information
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");
		Player player = (Player) user;

		// Check if the user is authenticated and their game status
		if (user != null) {
			// Retrieve any existing GameManager instance that corresponds
			// to the game the user may be involved in.
			GameManager gameManager =
					(GameManager) session.getAttribute("gameManager");
			if (gameManager == null) {
				// he is not involved in a game
			}

			if (gameManager != null) {

				// User is currently involved in a game
				Game game = gameManager.getGame();

				// Serialize the game object to JSON, manually escape it to ensure
				// safety, and set game details as attributes on the request
				ObjectMapper objectMapper = new ObjectMapper();
				String rawJson = objectMapper.writeValueAsString(game);
				String safeJson = rawJson.replace("\"", "\\\""); // Basic manual escaping
				request.setAttribute("game", safeJson);
				request.setAttribute("gameId", game.getId());
				request.setAttribute("playerId", player.getId());

				// via the preceding parameters, the div with id "root" of the
				// generated page will serve as the mounting point
				// for the React application. This setup initializes global
				// JavaScript variables using server-side values, which are then
				// utilized by the React application to manage game state and
				// WebSocket connections

			}
		}
		this.getServletContext().getRequestDispatcher("/WEB-INF/home.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Retrieve the current session and user
		HttpSession session = request.getSession(false);
		User user = (User) session.getAttribute("user");
		Player player = (Player) user;

		if (user == null) { // the user is not authenticated
			// Inform that the user needs to authenticate before proceeding
			logger.info("Attempt to access without authentication.");
			return;  // Optionally redirect to login or send an error response
		}

		// Determine the user's intended action from the POST request parameters.
		String action = request.getParameter("action");

		GameManager gameManager = null;

		// Handle user actions based on the specified action parameter.
		if ("new_game".equals(action)) {
			// The user wants to propose a new game
			try {
				gameManager = playerManager.makeUserProposeGame(user);
				session.setAttribute("gameManager", gameManager);
			} catch (BusinessException e) {
				logger.info("User " + user.getId() + " had not been able to propose a game.");
			}
		}

		if ("play_with".equals(action)) {
			// The user wants to play against another user who has already proposed
			// a game
			String userIdString = request.getParameter("user_id");
			int userId = 0;
			if (userIdString == null || userIdString.isEmpty()) {
				logger.info("No user ID provided for 'play_with' action.");
				return;  // Optionally handle this error more explicitly
			}
			try {
				userId = Integer.parseInt(userIdString);
				gameManager =
						playerManager
							.makeUserPlayWithUser(
									userId,
									user
							);
				session.setAttribute("gameManager", gameManager);
			} catch (NumberFormatException e) {
				logger.info("Invalid user ID format received: " + userIdString);
			} catch (BusinessException e) {
				logger.info(
						"Unable to set up game with user "
								+ userId
								+ ": "
								+ e.getMessage()
				);
			}

			// Notify all relevant users about the updates via WebSocket
			// communication.
			JsonObject newSecondPlayerUpdateJsonObject = null;
			String newSecondPlayerUpdateJson = JsonUtil.convertToJson(player);
			newSecondPlayerUpdateJsonObject =
					Json.createObjectBuilder()
						.add("update", "secondPlayer")
						.add("newValue", newSecondPlayerUpdateJson)
						.build();
			JsonObject newGameStateUpdateJsonObject = null;
			String newGameStateUpdateJson =
					JsonUtil.convertToJson(
							GameState.WAIT_FIRST_PLAYER_MOVE
					);
			newGameStateUpdateJsonObject =
					Json.createObjectBuilder()
						.add("update", "gameState")
						.add("newValue", newGameStateUpdateJson)
						.build();
			HashSet<Session> webSocketSessions =
					GameWebSocket.getSessionsRelatedToGameId(
							gameManager.getGame().getId()
					);
			for (Session webSocketSession : webSocketSessions) {
				if (webSocketSession != null && webSocketSession.isOpen()) {
					try {
						webSocketSession
								.getBasicRemote()
								.sendText(
										newSecondPlayerUpdateJsonObject.toString()
								);
						webSocketSession
								.getBasicRemote()
								.sendText(
										newGameStateUpdateJsonObject.toString()
								);
					} catch (Exception e) {
						logger.info(e.getMessage());
					}
				}
			}

		}

		// Reuse the doGet method to refresh and display the three lists immediately,
		// and ensure all request attributes are properly set for rendering in the
		// user interface.

		doGet(request, response);

		// The "root" div in the generated page acts as the operational center
		// for the React application. This is where React mounts and manages
		// the UI components, including the dynamic game board.
		// The setup uses global JavaScript variables initialized with server-side
		// values to manage the state of the game and handle WebSocket connections,
		// enabling a responsive and interactive gaming experience.
		// Users can visualize the game state in real-time and interact with the game
		// through this interface.
	}
}

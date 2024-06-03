/**
 * FILE: Home.java
 * PURPOSE: Servlet which serves home.jsp, the main landing page for the Onlineplay web
 * application.
 * FUNCTIONNALITIES AFFORDED VIA THE SERVED JSP PAGE home.jsp:
 *   1) Displays information about currently authenticated users, users awaiting opponents
 *      for games, and ongoing games. These lists are updated in real-time through GET
 *      requests to the UserActivities servlet, which fetches updated data every 5 seconds.
 *   2) Provides links to other pages of the web application for user account creation,
 *      authentication, and logging out.
 *   3) Offers external links: a GitHub link to the project's source code and a link to
 *      the project's documentation site.
 *   4) Supports actions via POST requests: for authenticated users to join games proposed
 *      by others or to propose new games.
 *   5) Enables interactive gameplay through WebSockets and the React library: users can
 *      view and play Power 4 games interactively if they are authenticated and involved
 *      in a game.
 * AUTHOR: Sylvain Labopin
 * SERVLET ATTRIBUTES USED:
 *   - authenticatedUsers: List of authenticated users formatted for the client.
 *   - waitingToPlayUsers: List of users waiting for game matchups.
 *   - games: Summary information of ongoing games.
 *   - game, gameId, playerId: Game details serialized to JSON and IDs used to initialize
 *     React components for the user interface.
 * POST REQUEST PARAMETERS:
 *   - "action": Determines the type of operation to execute ('new_game' or 'play_with').
 *   - "user_id": Specifies the ID of the user to join in a game (used with 'play_with').
* ICI
 * WebSocket interactions:
 *   - Uses GameWebSocket to communicate state changes to the game and user status through
 *     WebSocket sessions tied to each game's ID.
 *   - Manages a map of game IDs to WebSocket sessions, enabling updates to specific games
 *     and players.
 *
 * Session variables:
 *   - userConnection: Maintains the user's connection state and identity.
 *   - user: Stores authenticated user data during a session.
 *
 * Request handling:
 *   - doGet: Prepares attributes necessary for rendering the homepage UI in React.
 *   - doPost: Processes user inputs from form submissions, updates game states and
 *     communicates these changes via WebSocket to the client.
 *
 * Dependencies:
 *   - PlayerManager: Manages user and game data transactions.
 *   - Logger: Logs activities and errors for monitoring.
 *   - HttpSession, HttpServletRequest, HttpServletResponse: Manages sessions and
 *     handles HTTP transactions.
 *
 * Author: Sylvain Labopin
 */

package online.caltuli.webapp.servlet.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;
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
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import online.caltuli.webapp.util.CustomColorsGridSerializer;
import online.caltuli.webapp.util.CustomCoordinatesSerializer;
import online.caltuli.webapp.util.CustomGameSerializer;
import online.caltuli.webapp.util.JsonUtil;
import online.caltuli.webapp.websocket.GameWebSocket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Home extends HttpServlet {

	private static final long serialVersionUID = 1L;
	@Inject
	private PlayerManager playerManager;
	private Logger logger = LogManager.getLogger(Home.class);

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

			//todebug
			logger.info("doGet");
			logger.info("user.getId:" + user.getId());
			logger.info("GameWebSocket.sessions:" + GameWebSocket.sessions);

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
				String safeJson = rawJson.replace("\"", "\\\"").replace("'", "\\'");; // Basic manual escaping
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

		//todebug
		logger.info("doPost");
		logger.info("user.getId:" + user.getId());
		logger.info("GameWebSocket.sessions:" + GameWebSocket.sessions);

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
						logger.info("notify a relevant user");
						logger.info("webSocketSession:" + webSocketSession);
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

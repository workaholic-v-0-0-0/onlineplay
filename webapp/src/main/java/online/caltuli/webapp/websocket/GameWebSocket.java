package online.caltuli.webapp.websocket;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import online.caltuli.business.GameManager;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.*;

import online.caltuli.webapp.util.JsonUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ServerEndpoint("/game/{gameId}")
public class GameWebSocket {
    //private static final Map<Integer, HashSet<Session>> sessions = new ConcurrentHashMap<>();
    public static final Map<Integer, HashSet<Session>> sessions = new ConcurrentHashMap<>();
    private CurrentModel currentModel;
    private GameManager gameManager;
    private Game game;

    private Logger logger = LogManager.getLogger(GameWebSocket.class); // to debug

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") String gameId) {
        this.currentModel = CDI.current().select(CurrentModel.class).get();
        logger.info("on open");
        if (this.currentModel == null) {
            logger.info("this.currentModel is null");
        } else {
            logger.info("this.currentModel is not null");
        }

        logger.info("gameId is " + gameId);



        Integer gameKey = null;

        logger.info("HERE 0");
        try {
            Thread.sleep(1000);  // Pause for 1000 milliseconds (1 second)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Thread was interrupted", e);
        }
        try {

            gameKey = Integer.parseInt(gameId);
            logger.info("HERE -1");
            this.game = this.currentModel.getGames().get(gameKey);
            logger.info("HERE -2");
        } catch (NumberFormatException e) {
            logger.info("Invalid gameId format: " + gameId);
        }

        logger.info("HERE 1");
        logger.info("gameKey: " + gameKey);
        gameManager = (GameManager) currentModel.getGameManagers().get(game);

        logger.info("HERE 2");

        if (sessions.get(gameKey) == null) {
            sessions.put(gameKey, new HashSet<>());
        }
        sessions.get(gameKey).add(session);

        logger.info("sessions: " + sessions);

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        logger.info("Message received: " + message);
        logger.info("sessions: " + sessions);
        //JsonObject json = Json.createReader(new StringReader(message)).readObject();

        JsonObject jsonMessage;
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            jsonMessage = reader.readObject();
        } catch (Exception e) {
            logger.error("Failed to parse the message as JSON", e);
            return;  // Exit if the parsing fails
        }

        String updateType = jsonMessage.getString("update", null);
        if (updateType == null) {
            logger.error("Update type is missing in the message");
            return;  // Exit if the update type is missing
        }

        switch (updateType) {
            case "colorsGrid":
                int playedColumn = 0;
                JsonValue columnValue = jsonMessage.get("column");
                if (columnValue != null && columnValue.getValueType() == JsonValue.ValueType.NUMBER) {
                    playedColumn = jsonMessage.getInt("column");
                } else if (columnValue != null && columnValue.getValueType() == JsonValue.ValueType.STRING) {
                    try {
                        playedColumn = Integer.parseInt(jsonMessage.getString("column"));
                    } catch (NumberFormatException e) {
                        logger.error("Column value is not a number: " + jsonMessage.getString("column"), e);
                        return;
                    }
                }
                Coordinates coordinatesPlayed = null;
                try {
                    coordinatesPlayed = gameManager.playMove(playedColumn);
                } catch (BusinessException e) {
                    return;
                }

                // inform all related clients

                // new colored cell
                int playerId = 0;
                JsonValue playerIdValue = jsonMessage.get("playerId");
                if (playerIdValue != null && playerIdValue.getValueType() == JsonValue.ValueType.NUMBER) {
                    playerId = jsonMessage.getInt("playerId");
                } else if (playerIdValue != null && playerIdValue.getValueType() == JsonValue.ValueType.STRING) {
                    try {
                        playerId = Integer.parseInt(jsonMessage.getString("playerId"));
                    } catch (NumberFormatException e) {
                        logger.error("playerId value is not a number: " + jsonMessage.getString("playerId"), e);
                        return;
                    }
                }
                String color = (playerId == game.getFirstPlayer().getId()) ? "red" : "green";
                JsonObject newColorsGridUpdateJsonObject =
                        Json.createObjectBuilder()
                                .add("update", "colorsGrid")
                                .add("x", coordinatesPlayed.getX())
                                .add("y", coordinatesPlayed.getY())
                                .add("color", color)
                                .build();

                // new gameState
                JsonObject newGameStateUpdateJsonObject = null;
                String newGameStateUpdateJson =
                        JsonUtil.convertToJson(
                                game.getGameState()
                        );
                newGameStateUpdateJsonObject =
                        Json.createObjectBuilder()
                                .add("update", "gameState")
                                .add("newValue", newGameStateUpdateJson)
                                .build();

                // to debug by diplaying EvolutiveGridParser on front-end
                /*
                JsonObject newGameManagerUpdateJsonObject = null;
                String newGameManagerUpdateJson =
                        JsonUtil.convertToJson(
                                gameManager
                        );
                newGameManagerUpdateJsonObject =
                        Json.createObjectBuilder()
                                .add("update", "gameManager")
                                .add("newValue", newGameManagerUpdateJson)
                                .build();

                 */

                for (Session webSocketSession : GameWebSocket.getSessionsRelatedToGameId(game.getId())) {
                    if (webSocketSession != null && webSocketSession.isOpen()) {
                        try {
                            webSocketSession
                                    .getBasicRemote()
                                    .sendText(
                                            newColorsGridUpdateJsonObject.toString()
                                    );
                            webSocketSession
                                    .getBasicRemote()
                                    .sendText(
                                            newGameStateUpdateJsonObject.toString()
                                    );

                            // to debug by diplaying EvolutiveGridParser on front-end
                            /*
                            webSocketSession
                                .getBasicRemote()
                                .sendText(
                                        newGameManagerUpdateJsonObject.toString()
                                );

                             */
                        } catch (Exception e) {
                            logger.info(e.getMessage());
                        }
                    }
                }

                break;
            default:
                logger.warn("Received an unsupported update type: " + updateType);
                break;
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessions.remove(session);
        logger.info("Session closed, id: " + session.getId() + ", Close Reason: " + closeReason.getReasonPhrase() + ", Code: " + closeReason.getCloseCode());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.info("Error on session " + session.getId() + ": " + throwable.getMessage());
    }

    public static HashSet<Session> getSessionsRelatedToGameId(Integer gameId) {
        return sessions.get(gameId);
    }



}

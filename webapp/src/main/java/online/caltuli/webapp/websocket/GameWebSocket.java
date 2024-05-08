package online.caltuli.webapp.websocket;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.Session;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;

import java.io.IOException;
import java.io.StringReader;

import online.caltuli.business.GameManager;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.CurrentModel;
import online.caltuli.model.Game;

import online.caltuli.webapp.servlet.gui.Home;

import online.caltuli.webapp.util.JsonUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ServerEndpoint("/game/{gameId}")
public class GameWebSocket {
    //@Inject
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

        try {
            Integer gameKey = Integer.parseInt(gameId);
            this.game = this.currentModel.getGames().get(gameKey);

            if (this.game == null) {
                logger.info("this.game is null");
            } else {
                logger.info("this.game is not null: " + this.game);
            }
        } catch (NumberFormatException e) {
            logger.info("Invalid gameId format: " + gameId);
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        // Traiter les messages reçus, par exemple des mouvements des joueurs
        logger.info("Message received: " + message);
        // Parsez le message JSON
        JsonObject json = Json.createReader(new StringReader(message)).readObject();
        if ("move".equals(json.getString("type"))) {
            int column = json.getInt("column");
            // Appliquer le mouvement dans la logique du jeu, mettre à jour l'état du jeu
            try {
                gameManager.playMove(column);
                // Renvoyer le nouvel état du jeu à tous les clients
                String gameStateJson = JsonUtil.convertToJson(gameManager.getGame());
                for (Session sess : session.getOpenSessions()) {
                    if (sess.isOpen()) {
                        sess.getBasicRemote().sendText(gameStateJson);
                    }
                }
            } catch (BusinessException e) {
                logger.error("Failed to play move: " + e.getMessage(), e);
                // Handle the error appropriately, maybe send an error message back to the client
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        logger.info("Session closed, id: " + session.getId());
    }

    @OnError
    public void onError(Throwable throwable) {
        logger.info("Error: " + throwable.getMessage());
    }
}

package online.caltuli.batch.userInteractionSimulation.clients;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import online.caltuli.batch.userInteractionSimulation.virtualUsers.ColorsGridUpdateDescription;
import online.caltuli.batch.userInteractionSimulation.virtualUsers.GameEvent;
import online.caltuli.batch.userInteractionSimulation.virtualUsers.GameStateUpdateDescription;
import online.caltuli.batch.userInteractionSimulation.virtualUsers.interfaces.GameObserver;

import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import online.caltuli.batch.userInteractionSimulation.virtualUsers.interfaces.UpdateDescription;

import online.caltuli.business.ai.Column;
import online.caltuli.model.GameState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameWebSocketClient {
    private CompletableFuture<WebSocket> futureWebSocket = new CompletableFuture<>();
    private WebSocket webSocket = null;
    private HttpClient httpClient;
    private List<GameObserver> observers = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(GameWebSocketClient.class);

    public GameWebSocketClient(HttpClient httpClient, String uri) {
        this.httpClient = httpClient;
        WebSocket.Listener listener = new WebSocketClientListener();
        logger.info("Attempting to build and connect WebSocket to URI: {}", uri);
        httpClient.newWebSocketBuilder()
                .buildAsync(URI.create(uri), listener)
                .thenAccept(this::handleAccept)
                .exceptionally(this::handleException);
    }

    private void handleAccept(WebSocket ws) {
        this.webSocket = ws;
        logger.info("WebSocket Client Connected successfully");
        futureWebSocket.complete(ws);  // Signal that the WebSocket is ready to be used
    }

    private Void handleException(Throwable ex) {
        logger.error("Failed to establish WebSocket connection: {}", ex.getMessage(), ex);
        futureWebSocket.completeExceptionally(ex);
        return null;
    }

    private class WebSocketClientListener implements WebSocket.Listener {
        @Override
        public void onOpen(WebSocket webSocket) {
            logger.info("WebSocket session opened");
            webSocket.request(1); // Request the next message
        }

        @Override
        public CompletionStage<Void> onText(WebSocket webSocket, CharSequence data, boolean last) {
            String receivedText = data.toString();
            logger.info("Message received: {}", receivedText);
            JsonObject jsonMessage;

            try (JsonReader reader = Json.createReader(new StringReader(receivedText))) {
                jsonMessage = reader.readObject();
            } catch (Exception e) {
                logger.error("Failed to parse the message as JSON", e);
                //webSocket.request(1); // Request the next message
                return CompletableFuture.completedFuture(null);  // Properly handle completion
            }

            String whatToBeUpdated = jsonMessage.getString("update", null);
            if (whatToBeUpdated == null) {
                logger.error("Update type is missing in the message");
                //webSocket.request(1); // Request the next message
                return CompletableFuture.completedFuture(null);
            }

            UpdateDescription updateDescription = null;
            switch (whatToBeUpdated) {
                case "colorsGrid":
                    int columnIndex = jsonMessage.getInt("y", -1);
                    if (columnIndex == -1) {
                        logger.error("Column index is missing or invalid in the message");
                        //webSocket.request(1); // Request the next message
                        return CompletableFuture.completedFuture(null);
                    }
                    Column column =
                        Column.fromIndex(columnIndex);
                    updateDescription = new ColorsGridUpdateDescription(column);
                    break;
                case "gameState":
                    String newState = jsonMessage.getString("newValue", null);
                    if (newState == null) {
                        logger.error("New game state value is missing in the message");
                        //webSocket.request(1); // Request the next message
                        return CompletableFuture.completedFuture(null);
                    }
                    newState = newState.replace("\"", "");  // Enlève les guillemets autour de l'état
                    updateDescription = new GameStateUpdateDescription(GameState.valueOf(newState));
                    break;
                case "secondPlayer":
                    // Example placeholder if additional handling is needed
                    break;
                default:
                    logger.warn("Received an unsupported update type: " + whatToBeUpdated);
                    //webSocket.request(1); // Request the next message
                    return CompletableFuture.completedFuture(null);
            }

            if (updateDescription != null) {
                GameEvent gameEvent = new GameEvent(whatToBeUpdated, updateDescription);
                notifyObservers(gameEvent);
            }

            webSocket.request(1); // Request the next message
            return CompletableFuture.completedFuture(null);
        }


        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            logger.error("WebSocket connection encountered an error: {}", error.getMessage(), error);
        }

        @Override
        public CompletionStage<Void> onClose(WebSocket webSocket, int statusCode, String reason) {
            logger.info("WebSocket closed with statusCode: {}, reason: {}", statusCode, reason);
            return CompletableFuture.completedFuture(null);
        }
    }

    public void sendMessage(String message) {
        if (webSocket != null && futureWebSocket.isDone()) {
            logger.info("Sending message: {}", message);
            webSocket.sendText(message, true);
        } else {
            logger.warn("Attempted to send message, but WebSocket is not open or ready");
        }
    }

    public void close() {
        if (webSocket != null) {
            logger.info("Closing WebSocket");
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Closing");
        } else {
            logger.warn("Attempted to close WebSocket, but it is not currently open");
        }
    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(GameEvent gameEvent) {
        for (GameObserver observer : observers) {
            observer.update(gameEvent);
        }
    }

    public CompletableFuture<WebSocket> getFutureWebSocket() {
        return futureWebSocket;
    }

    public void setFutureWebSocket(CompletableFuture<WebSocket> futureWebSocket) {
        this.futureWebSocket = futureWebSocket;
    }

    public WebSocket getWebSocket() {
        return webSocket;
    }

    public void setWebSocket(WebSocket webSocket) {
        this.webSocket = webSocket;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}

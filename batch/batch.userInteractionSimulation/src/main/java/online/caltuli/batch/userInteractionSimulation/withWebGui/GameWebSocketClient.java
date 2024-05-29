package online.caltuli.batch.userInteractionSimulation.withWebGui;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GameWebSocketClient {
    private CompletableFuture<WebSocket> futureWebSocket = new CompletableFuture<>();
    private WebSocket webSocket = null;
    private HttpClient httpClient;
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
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            String receivedText = data.toString();
            logger.info("Message received: {}", receivedText);
            webSocket.request(1); // Request the next message
            return null;
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

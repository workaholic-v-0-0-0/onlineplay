package online.caltuli.batch.userInteractionSimulation.config.network;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum NetworkConfig {
    LOCAL_HTTPS("https", "localhost", "8443", "/webapp"),
    DROPLET_HTTPS("https", "caltuli.online", "443", "/webapp"),
    LOCAL_WSS("wss", "localhost", "8443", "/webapp/game"),
    DROPLET_WSS("wss", "caltuli.online", "443", "/webapp/game"),
    DROPLET_HTTPS_VERSION_SYLVAIN("https", "caltuli.online", "443", "/webapp_version_sylvain"),
    DROPLET_WSS_VERSION_SYLVAIN("wss", "caltuli.online", "443", "/webapp_version_sylvain/game");

    private final String protocol;
    private final String host;
    private final String port;
    private final String basePath;
    protected static final Logger logger = LogManager.getLogger(NetworkConfig.class);

    NetworkConfig(String protocol, String host, String port, String basePath) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.basePath = basePath;
    }

    public String buildHttpUrl() {
        return protocol + "://" + host + ":" + port + basePath;
    }

    public String buildWsUrl(int gameId) {
        return protocol + "://" + host + ":" + port + basePath + "/" + gameId;
    }
}
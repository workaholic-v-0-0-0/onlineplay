package online.caltuli.batch.userInteractionSimulation.clients;

import online.caltuli.batch.userInteractionSimulation.config.network.ClientConfig;
import online.caltuli.batch.userInteractionSimulation.config.network.NetworkConfig;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import online.caltuli.batch.userInteractionSimulation.virtualUsers.UserActionManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpClientSSLContextV2 {
    private NetworkConfig httpsNetworkConfig;
    private ClientConfig clientConfig;
    private final HttpClient httpClient;
    private String url;
    protected static final Logger logger = LogManager.getLogger(HttpClientSSLContextV2.class);

    public HttpClientSSLContextV2(
            NetworkConfig httpsNetworkConfig,
            ClientConfig clientConfig)
            throws
                NoSuchAlgorithmException,
                KeyManagementException,
                KeyStoreException {
        this.url = httpsNetworkConfig.buildHttpUrl();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManager[] trustManagers;
        if (clientConfig.isTrustAllCertificate()) {
            trustManagers = new TrustManager[] {
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] chain, String authType) { }
                        public void checkServerTrusted(X509Certificate[] chain, String authType) { }
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                    }
            };
        } else {
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((java.security.KeyStore) null);
            trustManagers = tmf.getTrustManagers();
        }
        sslContext.init(null, trustManagers, new java.security.SecureRandom());

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        this.httpClient = HttpClient.newBuilder()
                .sslContext(sslContext)
                .cookieHandler(cookieManager)
                .build();
    }

    public void cleanupCookies() {
        CookieManager cookieManager = (CookieManager) httpClient.cookieHandler().orElse(null);
        if (cookieManager != null) {
            cookieManager.getCookieStore().removeAll();
            logger.info("All cookies have been cleared.");
        }
    }

    public String sendGetRequest(String urlSuffix) throws Exception {
        logger.info("sendGetRequest");
        logger.info("urlSuffix:"+urlSuffix);
        logger.info("this.url + \"/\" + urlSuffix:"+this.url + "/" + urlSuffix);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(this.url + "/" + urlSuffix))
            .GET()
            .build();
        HttpResponse<String> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public String sendPostRequest(String urlSuffix, String postParams) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(this.url + "/" + urlSuffix))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(postParams))
            .build();
        HttpResponse<String> response =
            httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
}

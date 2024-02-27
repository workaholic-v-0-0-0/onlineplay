package online.caltuli.consumer.api.abuseipdb;

import online.caltuli.consumer.api.abuseipdb.exception.ConfigurationLoadException;

import org.apache.http.client.fluent.Request;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;
import java.io.InputStream;
import java.util.function.Supplier;

public class AbuseIpDbClient {
    private final static String PROPERTIES_FILE = "config.abuse-ip-db.properties";
    private static Properties properties = new Properties();
    private static volatile AbuseIpDbClient instance;
    private String baseUrl;
    private String apiKey;
    private Supplier<InputStream> inputStreamSupplier; // make tests easier

    private AbuseIpDbClient() throws ConfigurationLoadException {
        this.inputStreamSupplier = () ->
                AbuseIpDbClient.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
        loadProperties(this.inputStreamSupplier);
        this.baseUrl = properties.getProperty("abuseipdb.api.url");
        this.apiKey = properties.getProperty("abuseipdb.api.key");
    }

    private static void loadProperties(Supplier<InputStream> inputStreamSupplier) throws ConfigurationLoadException {
        Logger logger = LogManager.getLogger(AbuseIpDbClient.class);
        try (InputStream inputStream = inputStreamSupplier.get()) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                logger.info("Property file '" + PROPERTIES_FILE + "' not found in the classpath");
                throw new ConfigurationLoadException("Property file '" + PROPERTIES_FILE + "' not found in the classpath");
            }
        } catch (IOException e) {
            logger.info("Error loading properties file");
            throw new ConfigurationLoadException("Error loading properties file");
        }

        // Afficher les propriétés chargées
        /*
        System.out.println("Properties loaded:");
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            System.out.println(key + "=" + value);
        }
        */
    }

    public static AbuseIpDbClient getInstance() throws ConfigurationLoadException {
        if (instance == null) {
            instance = new AbuseIpDbClient();
        }
        return instance;
    }

    /*
    send HTTP request :
        GET /api/v2/check?ipAddress=[ip parameter value]&maxAgeInDays=90 HTTP/1.1
        Host: api.abuseipdb.com
        Key: [Authentication token from AbuseIpDb company]
        Accept: application/json
    return related HTTP response in JSON format
    */
    protected String fetchIpDetailsAsJson(String ip) throws IOException {
        String response =
                Request.Get(
                        baseUrl
                                + "?ipAddress="
                                + ip
                                + "&maxAgeInDays=90"
                        )
                .addHeader("Key", apiKey)
                .addHeader("Accept", "application/json")
                .execute().returnContent().asString();
        return response;
    }
}

/*
{"data":{"ipAddress":"82.65.137.201","isPublic":true,"ipVersion":4,"isWhitelisted":null,"abuseConfidenceScore":0,"countryCode":"FR","usageType":"Fixed Line ISP","isp":"Free SAS","domain":"free.fr","hostnames":["82-65-137-201.subs.proxad.net"],"isTor":false,"totalReports":0,"numDistinctUsers":0,"lastReportedAt":null}}
 */
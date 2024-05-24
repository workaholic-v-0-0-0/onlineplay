package online.caltuli.consumer.api.abuseipdb;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import online.caltuli.consumer.api.abuseipdb.exception.AbuseIpDbException;
import online.caltuli.consumer.api.abuseipdb.exception.ConfigurationLoadException;
import online.caltuli.consumer.api.abuseipdb.exception.JsonDoesNotDescribeIpDetailsException;

import java.util.Map;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// check if an IP is allowed to use the web application using AbuseIpDbClient class
public class IpValidator {

    /*
    While constructor-based Dependency Injection could have simplified testing,
    the decision was made against its use in favor of understanding testing
    strategies for Singleton patterns and static classes. Consequently,
    this class lacks a constructor like:
    public IpValidator(IAbuseIpDbClient abuseIpDbClient) {
        this.abuseIpDbClient = abuseIpDbClient;
    }
    to explore alternative testing methodologies in IpValidatorTest.
    */

    public static boolean isAllowedIp(String ip) throws AbuseIpDbException {
        Map<String, Object> ipDetails = null;
        boolean isAllowedIp = false;
        Object isWhitelistedObj = null;
        boolean isWhitelisted = true;
        Logger logger = LogManager.getLogger(IpValidator.class);
        try {
            ipDetails = getIpDetailsFromJson(ip);
            isWhitelistedObj = ipDetails.get("isWhitelisted");
            isWhitelisted = isWhitelistedObj instanceof Boolean && (Boolean) isWhitelistedObj;
            isAllowedIp = (ip.startsWith("192.168"))
                    || (ip.equals("127.0.0.1"))
                    ||
                    (
                            ((((int) ipDetails.get("abuseConfidenceScore")) == 0)
                            && (isWhitelistedObj == null || isWhitelisted)
                            && (("FR".equals(ipDetails.get("countryCode").toString()))))
                            ||
                            // to allow functionnal tests from batch.userInteractionSimulation
                            (ip.equals("64.23.187.50"))
            ///* // to debug (to check the access is denied)
                    //*/
            );
        } catch (JsonParseException e) {
            logger.info("While analysing " + ip + " - Error parsing JSON response: " + e.getMessage());
            throw new AbuseIpDbException("Error parsing JSON response: " + e.getMessage());
        } catch (JsonProcessingException e) {
            logger.info("While analysing " + ip + " - Error processing JSON response: " + e.getMessage());
            throw new AbuseIpDbException("Error processing JSON response: " + e.getMessage());
        } catch (IOException e) {
            logger.info("While analysing " + ip + " - Network error while fetching IP details as Json string: " + e.getMessage());
            throw new AbuseIpDbException("Network error while fetching IP details as Json string: " + e.getMessage());
        } catch (JsonDoesNotDescribeIpDetailsException e) {
            logger.info("While analysing " + ip + " :" + e.getMessage());
            throw new AbuseIpDbException(e.getMessage());
        }
        if (!isAllowedIp) {
            logger.info("Forbidden IP "
                    + ip
                    + " ("
                    + "abuseConfidenceScore: "
                    + ipDetails.get("abuseConfidenceScore")
                    + ", "
                    + "isWhitelisted: "
                    + ipDetails.get("isWhitelisted")
                    + ", "
                    + "countryCode: "
                    + ipDetails.get("countryCode")
                    + ")"
            );
        }
        return isAllowedIp;
    }

    protected static Map<String, Object> getIpDetailsFromJson(String ip)
            throws IOException, JsonDoesNotDescribeIpDetailsException, ConfigurationLoadException {
        Map<String, Object> responseMap = jsonToMap(
                AbuseIpDbClient.getInstance().fetchIpDetailsAsJson(ip));
        Object ipDetailsObj = responseMap.get("data");
        if (!(ipDetailsObj instanceof Map<?, ?>)) {
            throw new JsonDoesNotDescribeIpDetailsException(
                    "\"data\" is missing or its value can't be interpreted as map"
            );
        }
        return (Map<String, Object>) ipDetailsObj;
    }

    protected static Map<String, Object> jsonToMap(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<Map<String, Object>>() {});
    }
}

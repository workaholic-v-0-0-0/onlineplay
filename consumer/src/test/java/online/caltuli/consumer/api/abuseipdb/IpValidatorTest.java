package online.caltuli.consumer.api.abuseipdb;

import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import online.caltuli.consumer.api.abuseipdb.exception.AbuseIpDbException;
import online.caltuli.consumer.api.abuseipdb.exception.JsonDoesNotDescribeIpDetailsException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("IpValidator")
@DisplayName("IpValidator class tests")
public class IpValidatorTest {

    private static final Logger logger = LogManager.getLogger(IpValidator.class);

    @Nested
    @Tag("jsonToMapMethodTests")
    @DisplayName("jsonToMap method tests")
    public class jsonToMapTest {

        @Test
        @DisplayName("jsonToMap should throw JsonProcessingException when JSON is invalid")
        void jsonToMap_ShouldThrowJsonProcessingException_WhenJsonIsInvalid() {
            // Arrange
            String invalidJson = "{invalidKey:\"value\"}";
            // Act & Assert
            assertThatThrownBy(() -> IpValidator.jsonToMap(invalidJson))
                    .withFailMessage("Json string is invalid")
                    .isInstanceOf(JsonProcessingException.class);
            // better not to use .hasMessageContaining because error message can change
        }

        @Test
        @DisplayName("jsonToMap should correctly convert a valid JSON string to a Map")
        void jsonToMap_ShouldCorrectlyConvert_WhenGivenSimpleValidJson() {
            // Arrange
            String validJson = "{\"key\":\"value\"}";
            // Act & Assert
            assertThatCode(() -> {
                Map<String, Object> result = IpValidator.jsonToMap(validJson);
                assertThat(result)
                        .isNotNull()
                        .hasSize(1)
                        .containsEntry("key", "value");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("jsonToMap should handle JSON with various data types")
        void jsonToMap_ShouldCorrectlyConvert_WhenGivenComplexValidJson() {
            // Arrange
            String jsonWithVariousTypes = "{\"number\": 42, \"boolean\": true, \"nested\": {\"key\": \"value\"}, \"array\": [1, 2, 3]}";
            // Act and Assert
            assertThatCode(() -> {
                Map<String, Object> result = assertDoesNotThrow(() -> IpValidator.jsonToMap(jsonWithVariousTypes));
                assertThat((Map<String, Object>) result.get("nested"))
                        .containsEntry("key", "value");
                assertThat((List<Integer>) result.get("array"))
                        .containsExactly(1, 2, 3);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @Tag("getIpDetailsFromJsonMethodTests")
    @DisplayName("getIpDetailsFromJson method tests")
    @ExtendWith(MockitoExtension.class)
    public class getIpDetailsFromJsonTest {

        private MockedStatic<AbuseIpDbClient> mockedStatic;
        private AbuseIpDbClient mockAbuseIpDbClient;

        @BeforeEach
        public void setUp() throws IOException {
            mockAbuseIpDbClient = mock(AbuseIpDbClient.class);
            mockedStatic = Mockito.mockStatic(AbuseIpDbClient.class);
            mockedStatic.when(AbuseIpDbClient::getInstance).thenReturn(mockAbuseIpDbClient);
        }

        @AfterEach
        public void tearDown() {
            mockedStatic.close();
        }

        @Test
        @DisplayName("Should parse valid JSON response correctly")
        public void
        getIpDetailsFromJson_shouldReturnIpDetailsAsMap_WithoutThrowingException_whenGivenValidJsonResponse()
                throws Exception { // manage fetchIpDetailsAsJson throwing
            // Arrange
            String validJsonResponse = "{\"data\": {\"ipAddress\": \"8.8.8.8\", \"abuseConfidenceScore\": 0, \"isWhitelisted\": null, \"countryCode\": \"FR\"}}";
            when(mockAbuseIpDbClient.fetchIpDetailsAsJson(anyString())).thenReturn(validJsonResponse);
            // Act & Assert
            assertThatCode(() -> {
                Map<String, Object> ipDetails = IpValidator.getIpDetailsFromJson("8.8.8.8");
                assertThat(ipDetails)
                        .containsEntry("ipAddress", "8.8.8.8")
                        .containsEntry("abuseConfidenceScore", 0)
                        .containsEntry("isWhitelisted", null)
                        .containsEntry("countryCode", "FR");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("getIpDetailsFromJson should throw JsonDoesNotDescribeIpDetailsException when JSON response is the empty string")
        public void getIpDetailsFromJson_ShouldThrowMismatchedInputException_WhenJsonResponseIsTheEmptyString()
                throws Exception {
            // Arrange
            String invalidJsonResponse = "";
            when(mockAbuseIpDbClient.fetchIpDetailsAsJson(anyString())).thenReturn(invalidJsonResponse);
            // Act & Assert
            assertThatThrownBy(() -> IpValidator.getIpDetailsFromJson("8.8.8.8"))
                    .isInstanceOf(MismatchedInputException.class);
        }

        @Test
        @DisplayName("getIpDetailsFromJson should throw JsonDoesNotDescribeIpDetailsException when JSON response does not contain 'data' key")
        public void getIpDetailsFromJson_ShouldThrowException_WhenJsonResponseDoesNotContainDataKey()
                throws Exception {
            // Arrange
            String invalidJsonResponse = "{\"message\": \"No data found\"}";
            when(mockAbuseIpDbClient.fetchIpDetailsAsJson(anyString())).thenReturn(invalidJsonResponse);
            // Act & Assert
            assertThatThrownBy(() -> IpValidator.getIpDetailsFromJson("8.8.8.8"))
                    .isInstanceOf(JsonDoesNotDescribeIpDetailsException.class)
                    .hasMessageContaining("\"data\" is missing or its value can't be interpreted as map");
        }

        @Test
        @DisplayName("getIpDetailsFromJson should throw JsonDoesNotDescribeIpDetailsException when JSON response contains 'data' key but with no map value")
        public void getIpDetailsFromJson_ShouldThrowException_WhenJsonResponseContainsDataKeyWithNoMapValue()
                throws Exception {
            // Arrange
            String invalidJsonResponse = "{\"data\": \"A string but not a map value\"}";
            when(mockAbuseIpDbClient.fetchIpDetailsAsJson(anyString())).thenReturn(invalidJsonResponse);
            // Act & Assert
            assertThatThrownBy(() -> IpValidator.getIpDetailsFromJson("8.8.8.8"))
                    .isInstanceOf(JsonDoesNotDescribeIpDetailsException.class)
                    .hasMessageContaining("\"data\" is missing or its value can't be interpreted as map");
        }

        @Test
        @DisplayName("getIpDetailsFromJson should throw JsonParseException when response is not valid JSON")
        public void getIpDetailsFromJson_ShouldThrowJsonParseException_WhenResponseIsNotValidJson()
                throws Exception { // manage fetchIpDetailsAsJson throwing
            // Arrange
            String notJsonResponse = "Not a JSON response";
            when(mockAbuseIpDbClient.fetchIpDetailsAsJson(anyString())).thenReturn(notJsonResponse);
            // Act & Assert
            assertThatThrownBy(() -> IpValidator.getIpDetailsFromJson("8.8.8.8"))
                    .isInstanceOf(JsonParseException.class);
        }
    }

    @Nested
    @Tag("isValidIpTests")
    @DisplayName("isAllowedIp method tests")
    @ExtendWith(MockitoExtension.class)
    public class isValidIpTest {

        private MockedStatic<IpValidator> mockedStatic;

        @BeforeEach
        public void setUp() throws IOException {
            mockedStatic = Mockito.mockStatic(IpValidator.class);
            mockedStatic.when(() -> IpValidator.isAllowedIp(anyString())).thenCallRealMethod();
        }

        @AfterEach
        public void tearDown() {
            mockedStatic.close();
        }

        @Test
        @DisplayName("isAllowedIp should return True when given a valid IP")
        public void isAllowedIp_ShouldReturnTrue_WhenGivenAValidIp() {
            // Arrange
            String allowedIp = "8.8.8.8";
            Map<String, Object> ipDetails = null;
            ipDetails = new HashMap<>();
            ipDetails.put("abuseConfidenceScore", 0);
            ipDetails.put("isWhitelisted", null);
            ipDetails.put("countryCode", "FR");
            mockedStatic.when(() -> IpValidator.getIpDetailsFromJson(anyString())).thenReturn(ipDetails);
            // Act & Assert
            assertThatCode(() -> {
                boolean result = result = IpValidator.isAllowedIp(allowedIp);
                assertThat(result)
                        .withFailMessage("isAllowedIp should return True when given a valid IP but it doesn't")
                        .isTrue();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("isAllowedIp should return False when given an IP with abuseConfidenceScore greater or equal 1")
        public void isAllowedIp_ShouldReturnFalse_WhenGivenAnIpWithAbuseConfidenceScoreGreaterOrEqualOne () {
            // Arrange
            String forbiddenIp = "198.235.24.225";
            Map<String, Object> ipDetails = null;
            ipDetails = new HashMap<>();
            ipDetails.put("abuseConfidenceScore", 1);
            ipDetails.put("isWhitelisted", null);
            ipDetails.put("countryCode", "FR");
            mockedStatic.when(() -> IpValidator.getIpDetailsFromJson(anyString())).thenReturn(ipDetails);
            // Act & Assert
            assertThatCode(() -> {
                boolean result = result = IpValidator.isAllowedIp(forbiddenIp);
                assertThat(result)
                        .withFailMessage("isAllowedIp should return False when given an IP with abuseConfidenceScore greater or equal 1 but it doesn't")
                        .isFalse();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("isAllowedIp should return False when given an IP from a forbidden country")
        public void isAllowedIp_ShouldReturnFalse_WhenGivenAnIpfromAForbiddenCountry () {
            // Arrange
            String forbiddenIp = "198.235.24.225";
            Map<String, Object> ipDetails = null;
            ipDetails = new HashMap<>();
            ipDetails.put("abuseConfidenceScore", 0);
            ipDetails.put("isWhitelisted", null);
            ipDetails.put("countryCode", "KP");
            mockedStatic.when(() -> IpValidator.getIpDetailsFromJson(anyString())).thenReturn(ipDetails);
            // Act & Assert
            assertThatCode(() -> {
                boolean result = result = IpValidator.isAllowedIp(forbiddenIp);
                assertThat(result)
                        .withFailMessage("isAllowedIp should return False when given an IP from a forbidden country but it doesn't")
                        .isFalse();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("isAllowedIp should return False when given a blacklisted IP")
        public void isAllowedIp_ShouldReturnFalse_WhenGivenABlacklistedIp() {
            // Arrange
            String forbiddenIp = "198.235.24.225";
            Map<String, Object> ipDetails = null;
            ipDetails = new HashMap<>();
            ipDetails.put("abuseConfidenceScore", 0);
            ipDetails.put("isWhitelisted", false);
            ipDetails.put("countryCode", "FR");
            mockedStatic.when(() -> IpValidator.getIpDetailsFromJson(anyString())).thenReturn(ipDetails);
            // Act & Assert
            assertThatCode(() -> {
                boolean result = result = IpValidator.isAllowedIp(forbiddenIp);
                assertThat(result)
                        .withFailMessage("isAllowedIp should return False when given a blacklisted IP")
                        .isFalse();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("isAllowedIp should throw an AbuseIpDbException when getIpDetailsFromJson throw a JsonParseException")
        public void isAllowedIpisValidIp_ShouldThrowAnAbuseIpDbException_WhenGetIpDetailsFromJsonThrowAJsonParseException () {
            // Arrange
            String ip = "8.8.8.8";
            mockedStatic.when(() -> IpValidator.getIpDetailsFromJson(anyString())).thenThrow(new JsonParseException("Test error message", JsonLocation.NA));
            // Act & Assert
            assertThatThrownBy(() -> IpValidator.isAllowedIp(ip))
                    .isInstanceOf(AbuseIpDbException.class)
                    .hasMessageContaining("Error parsing JSON response: " + "Test error message");
        }

        @Test
        @DisplayName("isAllowedIp should throw an AbuseIpDbException when getIpDetailsFromJson throw a JsonMappingException")
        public void isAllowedIp_ShouldThrowAnAbuseIpDbException_WhenGetIpDetailsFromJsonThrowAJsonMappingException () {
            // Arrange
            String ip = "8.8.8.8";
            mockedStatic.when(() -> IpValidator.getIpDetailsFromJson(anyString())).thenThrow(new JsonMappingException((JsonParser) null, "Test error message"));
            // Act & Assert
            assertThatThrownBy(() -> IpValidator.isAllowedIp(ip))
                    .isInstanceOf(AbuseIpDbException.class)
                    .hasMessageContaining("Error processing JSON response: " + "Test error message");
        }

        @Test
        @DisplayName("isAllowedIp should throw an AbuseIpDbException when getIpDetailsFromJson throw a IOException")
        public void isAllowedIp_ShouldThrowAnAbuseIpDbException_WhenGetIpDetailsFromJsonThrowAIOException () {
            // Arrange
            String ip = "8.8.8.8";
            mockedStatic.when(() -> IpValidator.getIpDetailsFromJson(anyString())).thenThrow(new IOException("Test error message"));
            // Act & Assert
            assertThatThrownBy(() -> IpValidator.isAllowedIp(ip))
                    .isInstanceOf(AbuseIpDbException.class)
                    .hasMessageContaining("Network error while fetching IP details as Json string: " + "Test error message");
        }

        @Test
        @DisplayName("isAllowedIp should throw an AbuseIpDbException when getIpDetailsFromJson throw a JsonDoesNotDescribeIpDetailsException")
        public void isAllowedIp_ShouldThrowAnAbuseIpDbException_WhenGetIpDetailsFromJsonThrowAJsonDoesNotDescribeIpDetailsException () {
            // Arrange
            String ip = "8.8.8.8";
            mockedStatic.when(() -> IpValidator.getIpDetailsFromJson(anyString())).thenThrow(new JsonDoesNotDescribeIpDetailsException("Test error message"));
            // Act & Assert
            assertThatThrownBy(() -> IpValidator.isAllowedIp(ip))
                    .isInstanceOf(AbuseIpDbException.class)
                    .hasMessageContaining("Test error message");
        }
    }
}
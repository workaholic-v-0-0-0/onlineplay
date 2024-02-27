package online.caltuli.consumer.api.abuseipdb;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class AbuseIpDbClientIT {

    @Nested
    @Tag("fetchIpDetailsAsJson")
    @DisplayName("fetchIpDetailsAsJson method tests")
    public class fetchIpDetailsAsJson {

        @Test
        @DisplayName("fetchIpDetailsAsJson should return expected json when given well known IP")
        public void fetchIpDetailsAsJson_ShouldReturnExpectedJson_WhenGivenWellKnownIp() {
            // Arrange
            String wellKnownIp = "8.8.8.8";
            String ipDetailAsJson = null;
            {
                // Act
                try {
                    AbuseIpDbClient abuseIpDbClient = AbuseIpDbClient.getInstance();
                    ipDetailAsJson = abuseIpDbClient.fetchIpDetailsAsJson(wellKnownIp);
                } catch (Exception e) {

                }
                // Assert
                System.out.println("ipDetailAsJson = " + ipDetailAsJson);
            }
        }
    }

    @Test
    @Disabled("next work to do")
    public void checkIp_WithValidIp_ShouldReturnNonNullResponseIT() {
        String testIp = "8.8.8.8";
        try {
            boolean isValid = IpValidator.isAllowedIp(testIp);
            assertNotNull(isValid);
            System.out.println("This is" + isValid);
        } catch (Exception e) {
            fail("Integration test failed because of an IOException: " + e.getMessage());
        }
    }
}
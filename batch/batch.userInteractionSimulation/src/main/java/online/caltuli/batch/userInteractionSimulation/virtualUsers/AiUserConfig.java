package online.caltuli.batch.userInteractionSimulation.virtualUsers;

public class AiUserConfig {
    private String urlPrefix;
    private String username;
    private boolean validateSSL;

    public AiUserConfig(String urlPrefix, String username, boolean validateSSL) {
        this.urlPrefix = urlPrefix;
        this.username = username;
        this.validateSSL = validateSSL;
    }

    // Getters pour accéder aux propriétés
    public String getUrlPrefix() {
        return urlPrefix;
    }

    public String getUsername() {
        return username;
    }

    public boolean isValidateSSL() {
        return validateSSL;
    }
}

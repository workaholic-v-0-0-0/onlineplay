package online.caltuli.batch.userInteractionSimulation.config.virtualUsers;

public enum VirtualUserInformationConfig {
    AI_USER(
        "virtual-ai-user",
        "123",
        "Hello, I'm clever!",
        "ai.virtual@example.com"),
    RANDOM_USER(
        "virtual-random-user",
        "123",
        "Hi there, I'm stupid!",
        "random.virtual@example.com");

    private final String username;
    private final String password;
    private final String message;
    private final String email;

    VirtualUserInformationConfig(
            String username,
            String password,
            String message,
            String email) {
        this.username = username;
        this.password = password;
        this.message = message;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }

    public String getEmail() {
        return email;
    }
}


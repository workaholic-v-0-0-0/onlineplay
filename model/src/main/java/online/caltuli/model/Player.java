package online.caltuli.model;

import online.caltuli.model.exceptions.user.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player {
    private int id;
    private String username;
    private String message;

    private static final Logger playerLogger = LogManager.getLogger(Player.class);

    public Player() {

    }

    public Player(int id, String username, String message) throws UserException {

        List<String> validationErrors = new ArrayList<>();

        this.setId(-1);  // -1 means id is not yet defined ; done while registration in users table

        try {
            setUsername(username);
        } catch (InvalidUsernameException e) {
            validationErrors.add("invalid username");
        }
        try {
            this.setMessage(message);
        } catch (InvalidMessageException e) {
            validationErrors.add("invalid message");
        }

        if (!validationErrors.isEmpty()) {
            String errorMessage = String.join(", ", validationErrors);
            playerLogger.info("User constructor encountered validation errors: " + errorMessage);
            throw new UserException(errorMessage);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws InvalidUsernameException {
        String regex = "^[A-Za-z0-9._-]{3,20}$";
        if (!username.matches(regex)) {
            playerLogger.info("Invalid username");
            throw new InvalidUsernameException("Invalid username: does not meet the criteria.");
        }
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) throws InvalidMessageException {
        String regex = "^[A-Za-z0-9 .,!?\\-']{0,254}$";

        if (message != null && !message.matches(regex)) {
            playerLogger.info("Invalid message format");
            throw new InvalidMessageException("Message contains invalid characters or exceeds the maximum length of 254 characters.");
        }

        this.message = message;
    }
}

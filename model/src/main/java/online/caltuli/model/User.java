package online.caltuli.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import online.caltuli.model.exceptions.user.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class User extends Player {
    //private int id;
    //private String username;

    @JsonIgnore
    private String passwordHash;

    @JsonIgnore
    private String email;
    //private String message;

    private static final Logger userLogger = LogManager.getLogger(User.class);

    public User() {

    }

    public User(String username, String passwordHash, String email, String message) throws UserException {

        List<String> validationErrors = new ArrayList<>();

        this.setId(-1);  // -1 means id is not yet defined ; done while registration in users table

        try {
            setUsername(username);
        } catch (InvalidUsernameException e) {
            validationErrors.add("invalid username");
        }
        try {
            setPasswordHash(passwordHash);
        } catch (InvalidPasswordHashException e) {
            validationErrors.add("invalid password hash");
        }
        try {
            this.setEmail(email);
        } catch (InvalidMailException e) {
            validationErrors.add("invalid email");
        }
        try {
            this.setMessage(message);
        } catch (InvalidMessageException e) {
            validationErrors.add("invalid message");
        }

        if (!validationErrors.isEmpty()) {
            String errorMessage = String.join(", ", validationErrors);
            userLogger.info("User constructor encountered validation errors: " + errorMessage);
            throw new UserException(errorMessage);
        }
    }

    /*
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
            userLogger.info("Invalid username");
            throw new InvalidUsernameException("Invalid username: does not meet the criteria.");
        }
        this.username = username;
    }
     */

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) throws InvalidPasswordHashException {
        String regex = "^\\$2[ayb]\\$\\d{2}\\$[./A-Za-z0-9]{53}$";
        if (!passwordHash.matches(regex)) {
            userLogger.info("Invalid password hash");
            throw new InvalidPasswordHashException("Invalid password hash: does not meet the BCrypt format.");
        }
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws InvalidMailException {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!email.matches(regex)) {
            userLogger.info("Invalid email format");
            throw new InvalidMailException("Invalid email format: does not meet the criteria.");
        }
        this.email = email;
    }

    /*
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) throws InvalidMessageException {
        String regex = "^[A-Za-z0-9 .,!?\\-']{0,254}$";

        if (message != null && !message.matches(regex)) {
            userLogger.info("Invalid message format");
            throw new InvalidMessageException("Message contains invalid characters or exceeds the maximum length of 254 characters.");
        }

        this.message = message;
    }

     */
}

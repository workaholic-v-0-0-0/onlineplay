package online.caltuli.model.exceptions.user;

public class InvalidPasswordHashException extends UserException {
    public InvalidPasswordHashException(String message) {
        super(message);
    }
}

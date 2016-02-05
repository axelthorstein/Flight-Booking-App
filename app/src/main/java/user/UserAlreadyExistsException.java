package user;

/**
 * A class that raises an exception if a User is already in the Database.
 */
public class UserAlreadyExistsException extends Exception {

    /**
     * Raises an exception if a User is already in the Database.
     */
    public UserAlreadyExistsException() {
        super();
    }

    /**
     * Raises an exception if a User is already in the Database with a message.
     * @param message is the message to be outputted.
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
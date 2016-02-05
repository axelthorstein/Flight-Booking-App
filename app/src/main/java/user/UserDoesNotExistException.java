package user;

/**
 * A class that raises an exception if a User is not in the Database.
 */
public class UserDoesNotExistException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Raises an exception if a User is not in the Database, with a message.
     * @param message is the message to be outputted.
     */
    public UserDoesNotExistException(String message) {
        super(message);
    }
}
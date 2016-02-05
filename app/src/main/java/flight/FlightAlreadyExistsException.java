package flight;

/**
 * A class that raises an exception if a Flight is already in the Database.
 */
public class FlightAlreadyExistsException extends Exception {

    private static final long serialVersionUID = -8698868685465728192L;

    /**
     * Raises an exception if a Flight is already in the Database.
     */
    public FlightAlreadyExistsException() {
        super();
    }

}
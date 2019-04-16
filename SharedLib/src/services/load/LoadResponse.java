package services.load;

/**
 * Class to represent server response to load service
 */
public class LoadResponse {
    /**
     * Success or error message for Fill Service
     */
    private final String message;

    public String getMessage() {
        return message;
    }

    /**
     * Create Fill Response with success message
     *
     * @param numP number of persons created (int)
     * @param numE number of events created (int)
     */
    public LoadResponse(int numU, int numP, int numE) {
        message = "Successfully added " + numU +
                " users, " + numP +
                " persons, and " + numE + " events to the database.";
        System.out.println(message);
    }

    /**
     * Create Fill Response with error message
     *
     * @param error non empty string
     */
    public LoadResponse(String error) {
        message = error;
        System.out.println(error);
    }
}

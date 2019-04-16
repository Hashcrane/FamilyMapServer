package services.fill;

/**
 * class for sending responses from Fill Service
 */
public class FillResponse {

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
    public FillResponse(int numP, int numE) {
        message = "Successfully added " + numP +
                " persons and " + numE + " events to the database.";
        System.out.println(message);
    }

    /**
     * Create Fill Response with error message
     *
     * @param error non empty string
     */
    public FillResponse(String error) {
        message = error;
        System.out.println(error);
    }
}

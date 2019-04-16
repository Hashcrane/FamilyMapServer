package services.clear;

/**
 * Represents server response for /clear service
 */
public class ClearResponse {


    /**
     * String containing response message;
     */
    private final String message;

    /**
     * Constructs a response with message
     * @param message non empty String response
     */
    public ClearResponse(String message) {
        this.message = message;
        System.out.println(message);
    }

    public String getMessage() {
        return message;
    }

}

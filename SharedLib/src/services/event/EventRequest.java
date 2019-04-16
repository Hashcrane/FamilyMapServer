package services.event;

public class EventRequest {

    /**
     * non null token to validate request
     */
    private final String token;

    /**
     * person ID to request person object
     */
    private String eventID;

    /**
     * Creates EventRequest that requests a specific event
     * @param token Authorization token to validate request
     * @param eventID Event id to be requested
     */
    public EventRequest(String token, String eventID) {
        this.token = token;
        this.eventID = eventID;
    }

    /**
     * Creates Event Request object that requests all events related to persons related to a user related to an AuthToken
     * @param token Authorization token to validate request
     */
    public EventRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public String getEventID() {
        return eventID;
    }
}



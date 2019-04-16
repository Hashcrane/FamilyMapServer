package services.person;


public class PersonRequest {
    /**
     * non null token to validate request
     */
     private final String token;

    /**
     * person ID to request person object
     */
    private String personID;

    /**
     * Create PersonRequest object for a single person
     * @param token AuthToken for validating request
     * @param personID id of person being retrieved from database
     */
    public PersonRequest(String token, String personID) {
        this.token = token;
        this.personID = personID;
    }

    /**
     * Creates Person Request object for a list of all persons related to a user
     * @param token AuthToken for validating request
     */
    public PersonRequest(String token) {
        this.token = token;
    }

    String getToken() {
        return token;
    }

    String getPersonID() {
        return personID;
    }
}

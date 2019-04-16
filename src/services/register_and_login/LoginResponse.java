package services.register_and_login;


/**
 * represents server response to login requests
 */

public class LoginResponse {

    /**
     * token to verify User actions, can be null
     */
    private final String authToken;

    /**
     * Non empty string username
     */
    private final String userName;
    /**
     * non empty string containing the user's person id
     */
    private final String personID;
    /**
     * Error message
     */
    private final String message;

    public String getMessage() {
        return message;
    }

    public String getUsername() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public String getToken() {
        return authToken;
    }

    /**
     * Create a Response with populated values
     *
     * @param a holds AuthToken object
     * @param p holds person id
     */
    public LoginResponse(String a, String user, String p) {
        authToken = a;
        personID = p;
        userName = user;
        message = null;
    }

    /**
     * Create invalid LoginResponse object
     *
     * @param error String containing error message
     */
    public LoginResponse(String error) {
        authToken = null;
        userName = null;
        personID = null;
        message = error;
        System.out.println(error);
    }
}

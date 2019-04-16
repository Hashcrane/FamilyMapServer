package services.register_and_login;


/**
 * Represents server response to register_and_login requests
 */
public class RegisterResponse {
    /**
     * AuthToken for validating server actions
     */
    private final String authToken;
    /**
     * non empty string
     */
    private final String userName;
    /**
     * non empty string
     */
    private final String personID;

    /**
     * Error message
     */
    private final String message;

    public String getMessage(){
        return message;
    }

   /* public void setMessage(String error) {
        message = error;
    }*/

    public String getToken() {
        return authToken;
    }

    public String getUsername() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    /**
     * creates response with valid fields
     *
     * @param t AuthToken for server actions
     * @param u non empty string, username
     * @param p non empty string, person ID
     */
    public RegisterResponse(String t, String u, String p) {
        authToken = t;
        userName = u;
        personID = p;
        message = null;
    }

    public RegisterResponse(LoginResponse loginResponse){
        authToken = loginResponse.getToken();
        userName = loginResponse.getUsername();
        personID = loginResponse.getPersonID();
        message = null;
    }
    /**
     * creates response with null fields
     *
     * @param error String containing error message
     */
    public RegisterResponse(String error) {
        authToken = null;
        userName = null;
        personID = null;
        message = error;
        System.out.println(error);
    }
}

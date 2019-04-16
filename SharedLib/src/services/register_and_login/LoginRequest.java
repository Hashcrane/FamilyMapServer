package services.register_and_login;

/**
 * Handles login Requests, packs and sends data to LoginService
 */
public class LoginRequest {
    /**
     * Non empty string
     */
    private final String userName;
    /**
     * Non empty string
     */
    private final String password;

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    /**
     * Create a Login Request object
     *
     * @param u username, non empty string
     * @param p password, non empty string
     */
    public LoginRequest(String u, String p) {
        userName = u;
        password = p;
    }
}

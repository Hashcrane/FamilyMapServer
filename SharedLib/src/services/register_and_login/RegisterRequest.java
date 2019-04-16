package services.register_and_login;

import models.User;

public class RegisterRequest {




    private String userName;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;

    /**
     * Create request object with fields to create a new user
     * @param username String (non null)
     * @param password String (non null)
     * @param email String (non null)
     * @param firstName String (non null)
     * @param lastName String (non null)
     * @param gender String (non null)
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, String gender) {
        this.userName = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     * Create a Register request from a user object
     * @param user user to register
     */
    public RegisterRequest(User user) {
        userName = user.getUsername();
        password = user.getPassword();
        email = user.getEmail();
        firstName = user.getF_name();
        lastName = user.getL_name();
        gender = user.getGender();
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    /**
     * Validates object before use
     * @return true if good to go, false if not
     */
    public boolean Validate() {
        if (userName == null) return false;
        if (password == null) return false;
        if (email == null) return false;
        if (firstName == null) return false;
        if (lastName == null) return false;
        if (gender == null) return false;
        return true;

    }
}

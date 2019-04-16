package services.register_and_login;

import dataAccess.AuthTokenDAO;
import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.UserDAO;
import models.AuthToken;
import models.User;


/**
 * Class that handles loging requests by verifying User existence
 * Verifies the Login and displays a message
 */
public class LoginService {
    /**
     * Verifies a login attempt and displays appropriate message
     *
     * @param lreq Login Request object containing username and password
     * @return Generates AuthToken on success, Null on failure
     */
    public LoginResponse PublicLogin(LoginRequest lreq) {

        //UserDAO to check password
        Database db = new Database();
        boolean valid = false;
        User user;
        try {
            UserDAO userDAO = new UserDAO(db.openConnection());
            user = userDAO.GetUserByName(lreq.getUsername());
            db.closeConnection(true);
            if (user != null) {
                if (user.getPassword().equals(lreq.getPassword())) {
                    valid = true;
                }
            }
        } catch (DataAccessException e) {
            return new LoginResponse("Internal server error");
        }
        if (valid) {
            System.out.println("Logging in user: " + lreq.getUsername());
            AuthToken authToken = new AuthToken(user.getID());
            try {
                AuthTokenDAO authTokenDAO = new AuthTokenDAO(db.openConnection());
                authTokenDAO.Insert(authToken);
                db.closeConnection(true);
            } catch (DataAccessException e) {
                return new LoginResponse("Internal server error");
            }
            System.out.println(user.getUsername() + " Logged in");
            return new LoginResponse(authToken.getKey(), user.getUsername(), user.getID());
        }
        return new LoginResponse("Request property missing or has invalid value");
    }

    /**
     * FamilyMapServer internal login, doesn't check password, used when RegisterService logs a user in and adds user
     * to database
     *
     * @param user user object containing username and password
     * @return Generates AuthToken on success, Null on failure
     */
    LoginResponse PrivateLogin(User user) {
        AuthToken authToken = new AuthToken(user.getID());
        Database db = new Database();
        try {
            System.out.println("Logging in user: " + user.getUsername());
            /*UserDAO userDAO = new UserDAO(db.openConnection());
            if (userDAO.Insert(user)) {
                db.closeConnection(true);
            } else {
                db.closeConnection(false);
            }*/
            AuthTokenDAO authTokenDAO = new AuthTokenDAO(db.openConnection());
            if (authTokenDAO.Insert(authToken)) {
                db.closeConnection(true);
                System.out.println(user.getUsername() + " Logged in");
                return new LoginResponse(authToken.getKey(), user.getUsername(), user.getID());
            } else {
                db.closeConnection(false);
                return new LoginResponse("PrivateLogin: unable to complete login");
            }
        } catch (DataAccessException e) {
            return new LoginResponse("PrivateLogin: unable to complete login");
        }
    }
}

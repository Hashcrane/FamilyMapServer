package services.register_and_login;

import dataAccess.DataAccessException;
import dataAccess.Database;
import dataAccess.UserDAO;
import models.Person;
import models.User;
import services.fill.FillResponse;
import services.fill.FillService;

/**
 * Class handles Register User requests
 */
public class RegisterService {

    /**
     * Registers a new User, adds them to database, generates 4 generations of person and event data
     *
     * @param request request to Register User
     * @return Register response: success or failure
     */
    public RegisterResponse RegisterUser(RegisterRequest request) {
        if (request.Validate()) {
            //check if Username is already taken
            try {
                Database db = new Database();
                UserDAO userDAO = new UserDAO(db.openConnection());
                User user = userDAO.GetUserByName(request.getUsername());
                db.closeConnection(true);
                if (user != null) return new RegisterResponse("Username already taken by another user");

            } catch (DataAccessException e) {
                return new RegisterResponse("Internal server error");
            }

            //Create USer
            Person newPerson = new Person(request);
            User newUser = new User(newPerson, request.getPassword(), request.getEmail());

            try {
                Database db = new Database();
                UserDAO userDAO = new UserDAO(db.openConnection());
                if (userDAO.Insert(newUser)) {
                    db.closeConnection(true);
                } else {
                    db.closeConnection(false);
                    return new RegisterResponse("Request property missing or has invalid value");
                }
            } catch (DataAccessException e) {
                return new RegisterResponse("Internal server error");
            }

            FillService fillService = new FillService();

            FillResponse fillResponse = fillService.Fill(request.getUsername(), 4);
            if (fillResponse.getMessage().equals("Internal server error")) {
                return new RegisterResponse("Internal server error");
            }


            LoginService loginService = new LoginService();
            return new RegisterResponse(loginService.PrivateLogin(newUser));
        }
        return new RegisterResponse("Request property missing or has invalid value");
    }


}

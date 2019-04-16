package services.person;

import dataAccess.*;
import models.Person;
import models.User;

import java.util.ArrayList;

/**
 * Class handles /Person server requests
 */
public class PersonService {
    /**
     * internal personID populated when Request is validated
     */
    private String personID;

    private String descendant;

    /**
     * Get all persons related to a user
     *
     * @param perReq contains a user object to request persons related
     * @return PersonResponse containing a list of persons
     */
    public PersonResponse GetFamily(PersonRequest perReq) {
        if (validate(perReq, 1)) {
            System.out.println("Key Validated");
            Database db = new Database();
            try {
                System.out.println("Retrieving Persons");
                PersonDAO personDAO = new PersonDAO(db.openConnection());
                ArrayList<Person> persons = personDAO.GetPersonsRelatedTo(null, descendant,
                        personID, 2);
                db.closeConnection(true);
                return new PersonResponse(persons);
            } catch (DataAccessException e) {
                return new PersonResponse("Internal server error");
            }
        } else {
            return new PersonResponse("Invalid auth token");
        }
    }

    /**
     * Get single person from database
     *
     * @param perReq contains personID of person object
     * @return PersonResponse containing a single person object
     */
    public PersonResponse GetPerson(PersonRequest perReq) {

        if (validate(perReq, 2)) {
            System.out.println("Key Validated");
            Database db = new Database();

            try {
                System.out.println("Retrieving Person");
                PersonDAO personDAO = new PersonDAO(db.openConnection());
                Person person = personDAO.GetPerson(perReq.getPersonID(), 1);
                if (person == null || perReq.getPersonID() == null) {
                    db.closeConnection(false);
                    return new PersonResponse("Invalid personID parameter");
                }
                //descendant = person.getDescendant();
                db.closeConnection(true);

            } catch (DataAccessException e) {
                return new PersonResponse("Internal server error");
            }

            try {
                System.out.println("Retrieving Person");
                PersonDAO personDAO = new PersonDAO(db.openConnection());
                Person person = personDAO.GetPerson(perReq.getPersonID(), 1);
                if (person == null || !person.getDescendant().equals(descendant)) {
                    db.closeConnection(false);
                    return new PersonResponse("Requested person does not belong to this user");
                }
                db.closeConnection(true);
                return new PersonResponse(person);
            } catch (DataAccessException e) {
                return new PersonResponse("Internal server error");
            }
        } else {
            return new PersonResponse("Invalid auth token");
        }
    }

    /**
     * Validates fields in person request
     *
     * @param perReq Person request object to be validated
     * @param i      option number. 1 for the GetFamily request, 2 for the GetPerson request
     * @return true if request is valid, false if otherwise
     */
    private boolean validate(PersonRequest perReq, int i) {
        /**
         * user ID for completing requests
         */
        String userID;

        if (perReq.getToken() == null) return false;
        if (i == 2) {
            if (perReq.getPersonID() == null) return false;
        }
        Database db = new Database();
        try {
            AuthTokenDAO aDAO = new AuthTokenDAO(db.openConnection());
            userID = aDAO.GetUser(perReq.getToken());
            personID = userID;
            if (userID != null) {
                db.closeConnection(true);
                UserDAO userDAO = new UserDAO(db.openConnection());
                User user = userDAO.GetUserByID(personID);
                if (user != null) {
                    db.closeConnection(true);
                } else {
                    db.closeConnection(false);
                    return false;
                }
                descendant = user.getUsername();
                return true;
            }
            db.closeConnection(false);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
}

package services.fill;

import dataAccess.*;
import models.Person;
import models.User;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Requests newly generated Persons and events for a user
 */
public class FillService {

    private Person p;


    private void GetPerson(String username) {
        Database db = new Database();
        try {
            UserDAO userDAO = new UserDAO(db.openConnection());
            User user = userDAO.GetUserByName(username);
            db.closeConnection(true);
            if (user == null) {
                p = null;
                return;
            }

            p = new Person(user);
        } catch (DataAccessException e) {
            p = new Person(null, null, null, null, null, null, null, null);
        }
    }

    /**
     * Generate numGenerations worth of data for a specific user
     *
     * @param username       username of User object to generate information for
     * @param numGenerations non negative integer greater than 0
     * @return true if successful, false if failed
     */
    public FillResponse Fill(String username, int numGenerations) {
        //validate username
        if (numGenerations < 0) return new FillResponse("Invalid username or generations parameter");
        GetPerson(username);

        if (p == null) {
            return new FillResponse("Invalid username or generations parameter");
        } else {
            if (p.anyNull()) return new FillResponse("Invalid username or generations parameter");
            //check for already produced data
            //if already data, delete all generations for user, persons, events
            if (ClearRelatedData(username, p)) {
                //generate specified numGenerations
                FillGenerator fillGen = new FillGenerator(p);
                fillGen.Generate(numGenerations);
                Database db = new Database();
                try {
                    Connection c = db.openConnection();
                    PersonDAO pDAO = new PersonDAO(c);
                    if (pDAO.InsertMany(fillGen.getAncestors())) {
                        db.closeConnection(true);
                        if (!c.isClosed()) {
                            db.closeConnection(true);
                        }
                        c = db.openConnection();
                        EventDAO eDAO = new EventDAO(c);
                        if (eDAO.InsertMany(fillGen.getEvents())) {
                            db.closeConnection(true);
                            if (!c.isClosed()) {
                                db.closeConnection(true);
                            }

                            return new FillResponse(fillGen.getNumPersons(), fillGen.getNumEvents());
                        } else {
                            db.closeConnection(false);
                        }
                    } else {
                        db.closeConnection(false);
                    }
                } catch (DataAccessException e) {

                    return new FillResponse("Internal server error");
                } catch (SQLException e) {
                    e.printStackTrace();
                    return new FillResponse("Internal server error");
                }
            } else {
                return new FillResponse("Internal server error");
            }
        }
        return new FillResponse("Internal server error");
    }

    /**
     * Clears Persons and Events related to user but not the user's person object
     *
     * @param username username of person to clear the events and persons of
     */
    private boolean ClearRelatedData(String username, Person person) {
        Database db = new Database();
        try {
            Connection c = db.openConnection();
            PersonDAO pDAO = new PersonDAO(c);
            if (pDAO.DeleteRelatedPersons(username)) {
                EventDAO eDAO = new EventDAO(c);
                if (eDAO.DeleteRelatedEvents(username)) {
                    db.closeConnection(true);
                    return true;
                } else {
                    db.closeConnection(false);
                    return false;
                }
            } else {
                db.closeConnection(false);
                return false;
            }

        } catch (DataAccessException e) {
            return false;
        }
    }
}

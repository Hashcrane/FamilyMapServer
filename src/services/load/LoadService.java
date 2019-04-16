package services.load;

import dataAccess.*;
import models.Event;
import models.Person;
import models.User;

import java.sql.Connection;

/**
 * Service Class to load bulk user, event, and person objects into database
 */
public class LoadService {
    public LoadResponse Load(LoadRequest lReq) {
        for (User user : lReq.getUsers()) {
            if (user.anyNull()) return new LoadResponse("Invalid request data (missing values, invalid values, etc.)");
        }
        for (Person person : lReq.getPersons()) {
            if (person.anyNull()) return new LoadResponse("Invalid request data (missing values, invalid values, etc.)");
        }
        for (Event event : lReq.getEvents()) {
            if (event.anyNull()) return new LoadResponse("Invalid request data (missing values, invalid values, etc.)");
        }

        Database db = new Database();
        try {
            db.clearTables();
            Connection c = db.openConnection();
            UserDAO uDAO = new UserDAO(c);
            uDAO.InsertMany(lReq.getUsers());
            PersonDAO pDAO = new PersonDAO(c);
            pDAO.InsertMany(lReq.getPersons());
            EventDAO eDAO = new EventDAO(c);
            eDAO.InsertMany(lReq.getEvents());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            e.printStackTrace();
            try {
                db.closeConnection(false);
            }catch (DataAccessException er){
                er.printStackTrace();
                return new LoadResponse("Internal server error");
            }
            return new LoadResponse("Internal server error");
        }
        return new LoadResponse(lReq.getUsers().size(), lReq.getPersons().size(), lReq.getEvents().size());
    }
}

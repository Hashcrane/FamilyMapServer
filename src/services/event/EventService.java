package services.event;

import dataAccess.*;
import models.Event;
import models.User;

import java.util.ArrayList;

public class EventService {
    private String personID;

    private String descendant;

    /**
     * Returns all events for all family members of the current user
     *
     * @param evReq eventRequest object containing data to complete request
     * @return EventResponse object containing list of events
     */
    public EventResponse GetAllEvents(EventRequest evReq) {
        if (validate(evReq, 1)) {
            System.out.println("Key Validated");
            Database db = new Database();
            try {
                System.out.println("Retrieving Events");
                EventDAO eventDAO = new EventDAO(db.openConnection());
                ArrayList<Event> events = eventDAO.GetEventsRelatedTo(descendant, 1);
                db.closeConnection(true);
                return new EventResponse(events);
            } catch (DataAccessException e) {
                return new EventResponse("Internal server error");
            }
        } else {
            return new EventResponse("Invalid auth token");
        }
    }

    /**
     * Returns the single Event object with the specified id
     *
     * @param evReq eventRequest object containing data to complete request
     * @return EventResponse object containing event
     */
    public EventResponse GetEvent(EventRequest evReq) {
        if (validate(evReq, 2)) {
            Database db = new Database();
            try {
                System.out.println("Retrieving Event");
                EventDAO eventDAO = new EventDAO(db.openConnection());
                Event event = eventDAO.GetEvent(evReq.getEventID(), null);
                db.closeConnection(true);
                if (event == null ||evReq.getEventID() == null) {
                    db.closeConnection(false);
                    return new EventResponse("Invalid eventID parameter");
                }
            } catch (DataAccessException e) {
                return new EventResponse("Internal server error");
            }
            System.out.println("Key Validated");

            try {
                System.out.println("Retrieving Event");
                EventDAO eventDAO = new EventDAO(db.openConnection());
                Event event = eventDAO.GetEvent(evReq.getEventID(), descendant);
                if (event == null) {
                    db.closeConnection(false);
                    return new EventResponse("Requested event does not belong to this user");
                } else {
                    db.closeConnection(true);
                    return new EventResponse(event);
                }
            } catch (DataAccessException e) {
                return new EventResponse("Internal server error");
            }
        } else {
            return new EventResponse("Invalid auth token");
        }
    }

    /**
     * Validates fields in the EventRequest object
     *
     * @param evReq  EventRequest object to be validated
     * @param option option number. 1 represents GetAllEvents requests, 2 represents GetEvent requests
     * @return true for valid fields, false for invalid
     */
    private boolean validate(EventRequest evReq, int option) {
        System.out.println("Validating Authorization key");
        String userID;

        if (evReq.getToken() == null) return false;
        if (option == 2) {
            if (evReq.getEventID() == null) return false;
        }
        Database db = new Database();
        try {
            AuthTokenDAO aDAO = new AuthTokenDAO(db.openConnection());
            userID = aDAO.GetUser(evReq.getToken());
            if (userID != null) {
                db.closeConnection(true);
                personID = userID;
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

package dataAccess;

import models.Event;

import java.sql.*;
import java.util.ArrayList;


/**
 * Class for Accessing EVENTS table in Database
 */
public class EventDAO {
    public EventDAO(Connection conn) {
        this.c = conn;
    }

    /**
     * connection to database
     */
    private final Connection c;
    /**
     * Prepared statement for database operations
     */
    private PreparedStatement stmt = null;
    /**
     * Result Set for pulling items out of database
     */
    private ResultSet rs = null;


    /**
     * Retrieves all Events from for all family members of current user
     *
     * @param descendant String of Username of person
     * @param option     (int) 1 for searching by descendant, 2 for searching by personID
     * @return a list of all EVENTS objects in the database or null if not in database
     * @throws DataAccessException
     */
    public ArrayList<Event> GetEventsRelatedTo(String descendant, int option) throws DataAccessException {
        stmt = null;
        rs = null;

        ArrayList<Event> events = new ArrayList<>();
        try {
            String sql;
            if (option == 1) {
                sql = "SELECT * FROM EVENTS WHERE DESCENDANT='" + descendant + "'";
            } else {
                sql = "SELECT * FROM EVENTS WHERE PERSON_ID='" + descendant + "'";
            }
            stmt = c.prepareStatement(sql);
            rs = stmt.executeQuery();


            while (rs.next()) {
                String eventID = rs.getString("EVENT_ID");
                String descend = rs.getString("DESCENDANT");
                String personID = rs.getString("PERSON_ID");
                String lat = rs.getString("LATITUDE");
                String lon = rs.getString("LONGITUDE");
                String country = rs.getString("COUNTRY");
                String city = rs.getString("CITY");
                String eventType = rs.getString("EVENT_TYPE");
                int year = rs.getInt("YEAR");
                Event e = new Event(eventID, descend, personID, lat, lon, country, city, eventType, year);
                events.add(e);
            }

            if (events.isEmpty()) return events;

            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while getting events");
        }
        return events;
    }

    /**
     * Searches Database for Event matching id
     *
     * @param id ID of EVENT to search for
     * @param des String of the username the event belongs to
     * @return EVENT object or null if not in database
     * @throws DataAccessException
     */
    public Event GetEvent(String id, String des) throws DataAccessException {
        stmt = null;
        rs = null;
        Event ev = null;
        try {
            if (des == null) {
                String sql = "SELECT * FROM EVENTS WHERE EVENT_ID = ?";
                stmt = c.prepareStatement(sql);
                stmt.setString(1, id);
                rs = stmt.executeQuery();
            } else {
                String sql = "SELECT * FROM EVENTS WHERE EVENT_ID = ? AND DESCENDANT = ?";
                stmt = c.prepareStatement(sql);
                stmt.setString(1, id);
                stmt.setString(2, des);
                rs = stmt.executeQuery();
            }

            if (rs.next()) {
                String eventID = rs.getString("EVENT_ID");
                String descendant = rs.getString("DESCENDANT");
                String personID = rs.getString("PERSON_ID");
                String lat = rs.getString("LATITUDE");
                String lon = rs.getString("LONGITUDE");
                String country = rs.getString("COUNTRY");
                String city = rs.getString("CITY");
                String eventType = rs.getString("EVENT_TYPE");
                int year = rs.getInt("YEAR");
                ev = new Event(eventID, descendant, personID, lat, lon, country, city, eventType, year);
            }
            stmt.close();
            rs.close();

            if (ev != null) return ev;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while getting event");
        }
        return null;
    }

    /**
     * Delete Event matching id
     *
     * @param id ID of EVENT to be deleted
     * @return true if successfully deleted EVENT
     * @throws DataAccessException
     */
    public boolean Delete(String id) throws DataAccessException {
        stmt = null;
        boolean success = false;
        try {
            String sql = "DELETE FROM EVENTS WHERE EVENT_ID='" + id + "'";
            stmt = c.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                success = true;
                System.out.println("Deleted " + id + "from EVENTS Database");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting event");
        }
        return success;
    }

    /**
     * Delete all events related to a user
     *
     * @param descendant String representing the username
     * @return true on success, false on failure
     * @throws DataAccessException
     */
    public boolean DeleteRelatedEvents(String descendant) throws DataAccessException {
        stmt = null;
        boolean success = false;
        try {
            String sql = "DELETE FROM EVENTS WHERE DESCENDANT='" + descendant + "'";
            stmt = c.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                success = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting event");
        }
        System.out.println("Deleted Events Related to " + descendant);
        return true;
    }

    /**
     * Deletes all Events from EVENTS table
     *
     * @return true if successful clear of all Events
     * @throws DataAccessException
     */
    public boolean Clear() throws DataAccessException {

        PreparedStatement stmt;

        boolean success = false;
        try {
            String sql = "DELETE FROM EVENTS";
            stmt = c.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                success = true;
                System.out.println("Cleared EVENTS Database");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing database");
        }
        return success;
    }

    /**
     * Insert Event into database
     *
     * @param ev EVENT object to insert in to EVENTS Table
     * @return true if insert is successful, false if not
     * @throws DataAccessException
     */
    public boolean Insert(Event ev) throws DataAccessException {
        stmt = null;
        if (ev.anyNull()) throw new DataAccessException("Invalid Event Object has null values");
        boolean success = false;
        try {
            String sql = "INSERT INTO EVENTS (EVENT_ID, DESCENDANT, PERSON_ID, LATITUDE," +
                    "LONGITUDE, COUNTRY, CITY, EVENT_TYPE, YEAR) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, ev.getEventID());
            stmt.setString(2, ev.getDescendant());
            stmt.setString(3, ev.getPersonID());
            stmt.setString(4, ev.getLatitude());
            stmt.setString(5, ev.getLongitude());
            stmt.setString(6, ev.getCountry());
            stmt.setString(7, ev.getCity());
            stmt.setString(8, ev.getEventType());
            stmt.setString(9, String.valueOf(ev.getYear()));


            if (stmt.executeUpdate() == 1) {
                success = true;
                System.out.println("Inserted " + ev.getEventID() + " into EVENTS Database");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into database");
        }
        return success;
    }

    public boolean InsertMany(ArrayList<Event> events) throws DataAccessException {
        stmt = null;

        for (Event ev : events) {
            assert ev != null;
            if (ev.anyNull()) throw new DataAccessException("Invalid Event Object has null values");
        }

        int count = 0;
        boolean success = false;
        try {
            for (Event ev : events) {
                String sql = "INSERT INTO EVENTS (EVENT_ID, DESCENDANT, PERSON_ID, LATITUDE," +
                        "LONGITUDE, COUNTRY, CITY, EVENT_TYPE, YEAR) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                stmt = c.prepareStatement(sql);
                stmt.setString(1, ev.getEventID());
                stmt.setString(2, ev.getDescendant());
                stmt.setString(3, ev.getPersonID());
                stmt.setString(4, ev.getLatitude());
                stmt.setString(5, ev.getLongitude());
                stmt.setString(6, ev.getCountry());
                stmt.setString(7, ev.getCity());
                stmt.setString(8, ev.getEventType());
                stmt.setString(9, String.valueOf(ev.getYear()));

                if (stmt.executeUpdate() == 1) {
                    ++count;
                }
                stmt.close();
            }
            if (count == events.size()) {
                success = true;
                System.out.println("Inserted " + events.size() + " events into Database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into database");
        }
        return success;
    }

    /*  /**
     * Update Event in database
     *
     * @param ev EVENT object to replace existing object in EVENTS Table that matches ev.getEventID()
     * @return true if update is successful, false if not
     * @throws DataAccessException
     */
   /* public boolean Update(Event ev) throws DataAccessException {
        stmt = null;

        boolean success = false;
        try {
            String sql = "UPDATE EVENTS" +
                    "DESCENDANT = ?," +
                    "PERSON_ID = ?," +
                    "LATITUDE = ?," +
                    "LONGITUDE = ?," +
                    "COUNTRY = ?," +
                    "CITY = ?," +
                    "EVENT_TYPE = ?," +
                    "YEAR = ?" +
                    "WHERE EVENT_ID = ?";
            stmt = c.prepareStatement(sql);
            stmt.setString(9, ev.getEventID());
            stmt.setString(1, ev.getDescendant());
            stmt.setString(2, ev.getPersonID());
            stmt.setString(3, ev.getLatitude().toString());
            stmt.setString(4, ev.getLongitude().toString());
            stmt.setString(5, ev.getCountry());
            stmt.setString(6, ev.getCity());
            stmt.setString(7, ev.getEventType());
            stmt.setString(8, String.valueOf(ev.getYear()));


            if (stmt.executeUpdate() == 1) {
                success = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating database");
        }
        return success;
    }
    */
}

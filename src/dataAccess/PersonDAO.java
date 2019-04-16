package dataAccess;

import java.sql.*;
import java.util.ArrayList;

import models.Person;
import models.User;

/**
 * Class for Accessing PERSONS table in Database
 */

public class PersonDAO {

    public PersonDAO(Connection conn) {
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
     * Retrieve set of all Persons related to User
     *
     * @param option   int with option 1 for using a user object, or 2 using a string as the username
     *                 or 3 for search using personID
     * @param username String of user's username
     * @param user     user to search for related Persons
     * @param personID String for personID
     * @return a TreeSet of Person objects in the PERSONS database or null if not in database
     * @throws DataAccessException
     */
    public ArrayList<Person> GetPersonsRelatedTo(User user, String username, String personID, int option) throws DataAccessException {
        stmt = null;
        rs = null;
        String userID;
        if (option == 1) {
            if (user.anyNull()) throw new DataAccessException("Invalid User Object has null values");
            userID = user.getUsername();
        } else if (option == 2) {
            userID = username;
        } else {
            userID = personID;
        }
        ArrayList<Person> people = new ArrayList<>();

        try {
            String sql;
            if (option == 3) {
                sql = "SELECT * FROM PERSONS WHERE ID='" + userID + "'";
            } else {
                 sql = "SELECT * FROM PERSONS WHERE DESCENDANT='" + userID + "'";
            }
            stmt = c.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {
                String id = rs.getString("ID");
                String descendant = rs.getString("DESCENDANT");
                String f_name = rs.getString("FIRST_NAME");
                String l_name = rs.getString("LAST_NAME");
                String gender = rs.getString("GENDER");
                String father = rs.getString("FATHER");
                String mother = rs.getString("MOTHER");
                String spouse = rs.getString("SPOUSE");
                Person p = new Person(id, descendant, f_name, l_name, gender, father, mother, spouse);
                people.add(p);
            }
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while getting Persons");
        }
        return people;
    }

    /**
     * Searches Database for person matching id
     *
     * @param id     ID of Person to search for Person object
     * @param option int for 1: ID or 2: DESCENDANT
     * @return Person object or null if not in database
     * @throws DataAccessException
     */
    public Person GetPerson(String id, int option) throws DataAccessException {
        stmt = null;
        rs = null;
        Person p = null;
        try {
            String sql;
            if (option == 1) {
                sql = "SELECT * FROM PERSONS WHERE ID = ?";
            } else {
                sql = "SELECT * FROM PERSONS WHERE DESCENDANT = ?";
            }
            stmt = c.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String ID = rs.getString("ID");
                String descendant = rs.getString("DESCENDANT");
                String f_name = rs.getString("FIRST_NAME");
                String l_name = rs.getString("LAST_NAME");
                String gender = rs.getString("GENDER");
                String father = rs.getString("FATHER");
                String mother = rs.getString("MOTHER");
                String spouse = rs.getString("SPOUSE");
                p = new Person(ID, descendant, f_name, l_name, gender, father, mother, spouse);

            }
            stmt.close();
            rs.close();
            if (p != null) return p;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while getting Person");
        }
        return null;
    }

    /**
     * Delete Person matching id
     *
     * @param id ID of Person to be deleted
     * @return true if successfully deleted EVENT
     * @throws DataAccessException
     */
    public boolean Delete(String id) throws DataAccessException {
        stmt = null;
        boolean success = false;
        try {
            String sql = "DELETE FROM PERSONS WHERE ID='" + id + "'";
            stmt = c.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                success = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting Person from Database");
        }
        System.out.println("Deleted person " + id);
        return success;
    }

    /**
     * Deletes all person related to a username, except the user's person object
     *
     * @param descendant string username
     * @return true if successful, false if not
     * @throws DataAccessException
     */
    public boolean DeleteRelatedPersons(String descendant) throws DataAccessException {
        stmt = null;
        boolean success = true;
        try {
            String sql = "DELETE FROM PERSONS WHERE DESCENDANT='" + descendant + "'";
            stmt = c.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                success = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting Person from Database");
        }
        System.out.println("Deleted persons related to " + descendant);
        return true;
    }

    /**
     * Deletes all Persons from PERSONS table
     *
     * @return true if successful clear of all Persons
     * @throws DataAccessException
     */
    public boolean Clear() throws DataAccessException {

        PreparedStatement stmt;

        boolean success = false;
        try {
            String sql = "DELETE FROM PERSONS";
            stmt = c.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                success = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing database");
        }
        System.out.println("Cleared PERSONS Database");
        return success;
    }

    /**
     * Insert Person into database
     *
     * @param p Person object to insert in to PERSONS Table
     * @return true if insert is successful, false if not
     * @throws DataAccessException
     */
    public boolean Insert(Person p) throws DataAccessException {
        stmt = null;
        if (p.anyNull()) return false;

        boolean success = false;
        try {


            String sql = "INSERT INTO PERSONS VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, p.getID());
            stmt.setString(2, p.getDescendant());
            stmt.setString(3, p.getF_name());
            stmt.setString(4, p.getL_name());
            stmt.setString(5, p.getGender());
            stmt.setString(6, p.getFather());
            stmt.setString(7, p.getMother());
            stmt.setString(8, p.getSpouse());


            if (stmt.executeUpdate() == 1) {
                success = true;
                System.out.println("Inserted " + p.getID() + " into PERSONS Database");
            }

            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into database");
        }

        return success;
    }

    /**
     * insert list of persons into database
     *
     * @param persons List of person objects
     * @return true on success, false on failure
     * @throws DataAccessException
     */
    public boolean InsertMany(ArrayList<Person> persons) throws DataAccessException {
        stmt = null;

        for (Person p : persons) {
            assert p != null;
            if (p.anyNull())
                throw new DataAccessException("Invalid Person Object has null values " + p.getID() + " " + p.getDescendant());
        }

        int count = 0;
        boolean success = false;
        try {
            for (Person p : persons) {

                String sql = "INSERT INTO PERSONS VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                stmt = null;
                stmt = c.prepareStatement(sql);
                stmt.setString(1, p.getID());
                stmt.setString(2, p.getDescendant());
                stmt.setString(3, p.getF_name());
                stmt.setString(4, p.getL_name());
                stmt.setString(5, p.getGender());
                stmt.setString(6, p.getFather());
                stmt.setString(7, p.getMother());
                stmt.setString(8, p.getSpouse());

                if (stmt.executeUpdate() == 1) {
                    ++count;
                }
                stmt.close();
            }
            if (count == persons.size()) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into database");
        }
        System.out.println("Loaded " + persons.size() + " persons into Database");
        return success;
    }

    /**
     * Update Person in database
     *
     * @param p Person object to replace existing object in PERSONS Table that matches p.getID()
     * @return true if update is successful, false if not
     * @throws DataAccessException
     */
    public boolean Update(Person p) throws DataAccessException {
        stmt = null;

        boolean success = false;
        try {
            String sql = "UPDATE PERSONS SET DESCENDANT = ?," +
                    "FIRST_NAME = ?, LAST_NAME = ?, GENDER = ?, FATHER = ?," +
                    "MOTHER = ?, SPOUSE = ?" +
                    "WHERE ID = ?";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, p.getDescendant());
            stmt.setString(2, p.getF_name());
            stmt.setString(3, p.getL_name());
            stmt.setString(4, p.getGender());
            stmt.setString(5, p.getFather());
            stmt.setString(8, p.getMother());
            stmt.setString(8, p.getSpouse());
            stmt.setString(8, p.getID());


            if (stmt.executeUpdate() == 1) {
                success = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating database");
        }
        System.out.println("Updated person " + p.getID());
        return success;
    }
}

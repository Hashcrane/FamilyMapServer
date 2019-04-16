package dataAccess;

import java.sql.*;
import java.util.ArrayList;

import models.User;


/**
 * Class for Accessing USERS table in Database
 */
public class UserDAO{
    public UserDAO(Connection conn){
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
     * Searches Database for User matching id
     *
     * @param id ID of User to search for
     * @return User object or null if not in database
     * @throws DataAccessException
     */
    public User GetUserByID(String id) throws DataAccessException {
        assert id != null;
        stmt = null;
        ResultSet rs = null;

        User u = null;
        try {
            String sql = "SELECT * FROM USERS WHERE ID = ?";

            stmt = c.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String email = rs.getString("EMAIL");
                String f_name = rs.getString("FIRST_NAME");
                String l_name = rs.getString("LAST_NAME");
                String gender = rs.getString("GENDER");
                String ID = rs.getString("ID");
                u = new User(username, password, email, f_name, l_name, gender, ID);
            }
            stmt.close();
            rs.close();
            if (u != null) return u;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while getting User");
        }
        stmt = null;
        rs = null;
        return null;
    }

    /**
     * Search for a user by username
     * @param user_name String username
     * @return User object, null if not in database
     * @throws DataAccessException
     */
    public User GetUserByName(String user_name) throws DataAccessException {
        assert user_name != null;
        stmt = null;
        ResultSet rs;

        User u = null;
        try {
            String sql = "SELECT * FROM USERS WHERE USERNAME = ?";

            stmt = c.prepareStatement(sql);
            stmt.setString(1, user_name);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("USERNAME");
                String password = rs.getString("PASSWORD");
                String email = rs.getString("EMAIL");
                String f_name = rs.getString("FIRST_NAME");
                String l_name = rs.getString("LAST_NAME");
                String gender = rs.getString("GENDER");
                String ID = rs.getString("ID");
                u = new User(username, password, email, f_name, l_name, gender, ID);
            }
            stmt.close();
            rs.close();
            if (u != null) return u;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while getting User");
        }
        stmt = null;
        return null;
    }

    /**
     * Delete User matching id
     *
     * @param id ID of User to be deleted
     * @return true if successfully deleted user
     * @throws DataAccessException
     */
    public boolean Delete(String id) throws DataAccessException {
        assert id != null;
        PreparedStatement stmt;

        boolean success = false;
        try {
            String sql = "DELETE FROM USERS WHERE ID='" + id + "'";
            stmt = c.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                success = true;
                System.out.println("Deleted " + id + " from USERS Database");
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while Deleting from Database");
        }
        return success;
    }

    /**
     * Deletes all Users from USERS table
     * @return true if successful clear of all Users
     * @throws DataAccessException
     */
    public boolean Clear() throws DataAccessException {

        PreparedStatement stmt;

        boolean success = false;
        try {
            String sql = "DELETE FROM USERS";
            stmt = c.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                success = true;
                System.out.println("Cleared USERS Database");
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing Database");
        }
        return success;
    }

    /**
     * fill database with list many users at once
     * @param users List of users to insert into database
     * @return true on success, false on fail
     * @throws DataAccessException
     */
    public boolean InsertMany(ArrayList<User> users) throws DataAccessException{
        stmt = null;
        for (User u : users) {
            assert u != null;
            assert (!u.anyNull());
        }
        int count = 0;
        boolean success = false;
        try {
            for (User u : users) {
                String sql = "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, GENDER, ID)" +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
                stmt = null;
                stmt = c.prepareStatement(sql);
                stmt.setString(1, u.getUsername());
                stmt.setString(2, u.getPassword());
                stmt.setString(3, u.getEmail());
                stmt.setString(4, u.getF_name());
                stmt.setString(5, u.getL_name());
                stmt.setString(6, u.getGender());
                stmt.setString(7, u.getID());

                if (stmt.executeUpdate() == 1) {
                    ++count;
                }
                stmt.close();
            }
            if (count == users.size()) {
            success = true;
                System.out.println("Inserted " + users.size() + " users into Database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into Database");
        }
        return success;

    }

    /**
     * Insert User into database
     *
     * @param u User object to insert in to User Table
     * @return true if insert is successful, false if not
     * @throws DataAccessException
     */
    public boolean Insert(User u) throws DataAccessException {
        assert u != null;
        if (u.anyNull()) return false;
        stmt = null;

        boolean success = false;
        try {
            String sql = "INSERT INTO USERS (USERNAME, PASSWORD, EMAIL, FIRST_NAME, LAST_NAME, GENDER, ID)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getF_name());
            stmt.setString(5, u.getL_name());
            stmt.setString(6, u.getGender());
            stmt.setString(7, u.getID());

            if (stmt.executeUpdate() == 1) {
                success = true;
                System.out.println("Inserted " + u.getUsername() + " into Database");
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into Database");
        }
        return success;
    }
/*
    /**
     * Update User in database
     *
     * @param u User object to replace existing user object in User Table that matches u.ID()
     * @return true if update is successful, false if not
     * @throws DataAccessException
     */
   /* public boolean Update(User u) throws DataAccessException {
        PreparedStatement stmt = null;
        boolean success = false;
        try {
            String sql = "UPDATE USERS SET USERNAME = ?, PASSWORD = ?," +
                    "EMAIL = ?, FIRST_NAME = ?, LAST_NAME = ?, GENDER = ?" +
                    "WHERE ID = ?";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getF_name());
            stmt.setString(5, u.getL_name());
            stmt.setString(6, u.getGender().toString());
            stmt.setString(7, u.getID());


            if (stmt.executeUpdate() == 1) {
                success = true;
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating Database");
        }
        return success;
    }*/
}

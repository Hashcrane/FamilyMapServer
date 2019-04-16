package dataAccess;

import models.AuthToken;

import java.sql.*;



/**
 * Handles access of the AuthToken table in database
 */
public class AuthTokenDAO {
    public AuthTokenDAO(Connection conn) {
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
     * Searches Database for Token matching id
     *
     * @param id ID of User to search for AuthToken
     * @return AuthToken object or null if not in database
     * @throws DataAccessException
     */
    public AuthToken GetToken(String id) throws DataAccessException {
        stmt = null;
        rs = null;

        AuthToken t = null;
        try {
            String sql = "SELECT * FROM AUTH_TOKENS WHERE USER_ID = ?";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                String key = rs.getString("KEY");
                String ID = rs.getString("USER_ID");
                t = new AuthToken(key, ID);
            }
            stmt.close();
            rs.close();
            if (t != null) return t;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while retrieving token");
        }
        return null;
    }

    /**
     * Searches Database for user id matching auth key
     *
     * @param key AuthToken key
     * @return AuthToken object or null if not in database
     * @throws DataAccessException
     */
    public String GetUser(String key) throws DataAccessException {
        stmt = null;
        rs = null;


        AuthToken t = null;
        try {
            String sql = "SELECT * FROM AUTH_TOKENS WHERE KEY = ?";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, key);
            rs = stmt.executeQuery();
            String ID = null;
            if (rs.next()) {
                ID = rs.getString("USER_ID");

            }
            stmt.close();
            rs.close();
            if (ID != null) return ID;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while checking token");
        }
        return null;
    }

    /**
     * Delete AuthToken matching id
     *
     * @param id UserID of AuthToken to be deleted
     * @return true if successfully deleted EVENT
     * @throws DataAccessException
     */
    public boolean Delete(String id) throws DataAccessException {
        stmt = null;

        boolean success = false;
        try {
            String sql = "DELETE FROM AUTH_TOKENS WHERE USER_ID ='" + id + "'";
            stmt = c.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                success = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while deleting token");
        }
        return success;
    }

    /**
     * Clears AuthTokens from Database
     *
     * @return true on success, false on failure
     * @throws DataAccessException
     */
    public boolean Clear() throws DataAccessException {

        PreparedStatement stmt;

        boolean success = false;
        try {
            String sql = "DELETE FROM AUTH_TOKENS";
            stmt = c.prepareStatement(sql);

            if (stmt.executeUpdate() == 1) {
                success = true;
            }

            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while clearing Database");
        }
        return success;
    }

    /**
     * Insert AuthToken into database
     *
     * @param t AuthToken object to insert in to AUTH_TOKENS Table
     * @return true if insert is successful, false if not
     * @throws DataAccessException
     */
    public boolean Insert(AuthToken t) throws DataAccessException {
        stmt = null;
        if (t.anyNull()) throw new DataAccessException("Invalid AuthToken Object has null values");
        boolean success = false;
        try {

            String sql = "INSERT INTO AUTH_TOKENS (KEY, USER_ID)" +
                    "VALUES (?, ?)";
            stmt = c.prepareStatement(sql);
            stmt.setString(1, t.getKey());
            stmt.setString(2, t.getUserID());

            if (stmt.executeUpdate() == 1) {
                success = true;
            }
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting token in database");
        }
        return success;
    }

   /* /**
     * Update AuthToken in database
     *
     * @param t AuthToken object to replace existing object in AUTH_TOKENS Table that matches t.getUserID()
     * @return true if update is successful, false if not
     * @throws DataAccessException
     */
   /* public boolean Update(AuthToken t) throws DataAccessException {
        stmt = null;
        boolean success = false;
        try {
            String sql = "UPDATE EVENTS" +
                    "KEY = ?" +
                    "WHERE USER_ID = ?";

            stmt = c.prepareStatement(sql);
            stmt.setString(1, t.getKey());
            stmt.setString(2, t.getUserID());

            if (stmt.executeUpdate() == 1) {
                success = true;
            }
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while updating token");
        }
        return success;
    }*/
}

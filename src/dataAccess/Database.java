package dataAccess;

import java.sql.*;

/**
 * starts or creates databases for Users, Events, Persons, and Authorization Tokens
 */
public class Database {

    public Connection c;

  /*  public Database() throws DataAccessException {
        createTables();
    }*/

    public Connection openConnection() throws DataAccessException {
        c = null;
        try {

            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:familymap.db");
            //System.out.println("Opened database successfully");

            c.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            System.out.println("ERROR! Could not load database driver");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }
        return c;
    }

    public void closeConnection(boolean commit) throws DataAccessException {

        try {
            if (c == null) {
                return;
            }
            if (c.isClosed()) {
                return;
            }
            if (commit) {
                c.commit();
            } else {
                c.rollback();
            }

            c.close();
            c = null;
        } catch (SQLException e) {
            e.printStackTrace();

            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void createTables() throws DataAccessException {

        Statement stmt;

        try {
            openConnection();
            stmt = c.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS `USERS` (\n" +
                    "\t`USERNAME`\tTEXT NOT NULL UNIQUE,\n" +
                    "\t`PASSWORD`\tTEXT NOT NULL,\n" +
                    "\t`EMAIL`\tTEXT NOT NULL,\n" +
                    "\t`FIRST_NAME`\tTEXT NOT NULL,\n" +
                    "\t`LAST_NAME`\tTEXT NOT NULL,\n" +
                    "\t`GENDER`\tTEXT NOT NULL,\n" +
                    "\t`ID`\tTEXT NOT NULL UNIQUE,\n" +
                    "\tFOREIGN KEY (`ID`) REFERENCES PERSONS (`ID`)\n" +
                    ")";

            stmt.executeUpdate(sql);
            stmt.close();
            closeConnection(true);

            openConnection();
            stmt = c.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS `PERSONS` (\n" +
                    "\t`ID`\tTEXT NOT NULL UNIQUE,\n" +
                    "\t`DESCENDANT`\tTEXT NOT NULL,\n" +
                    "\t`FIRST_NAME`\tTEXT NOT NULL,\n" +
                    "\t`LAST_NAME`\tTEXT NOT NULL,\n" +
                    "\t`GENDER`\tTEXT NOT NULL,\n" +
                    "\t`FATHER`\tTEXT,\n" +
                    "\t`MOTHER`\tTEXT,\n" +
                    "\t`SPOUSE`\tTEXT,\n" +
                    "PRIMARY KEY (`ID`)," +
                    "FOREIGN KEY (DESCENDANT) REFERENCES USERS(USERNAME)" +
                    ")";
            stmt.executeUpdate(sql);
            stmt.close();
            closeConnection(true);

            openConnection();
            stmt = c.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS `EVENTS` (\n" +
                    "\t`EVENT_ID`\tTEXT NOT NULL UNIQUE,\n" +
                    "\t`DESCENDANT`\tTEXT NOT NULL,\n" +
                    "\t`PERSON_ID`\tTEXT NOT NULL,\n" +
                    "\t`LATITUDE`\tTEXT NOT NULL,\n" +
                    "\t`LONGITUDE`\tTEXT NOT NULL,\n" +
                    "\t`COUNTRY`\tTEXT NOT NULL,\n" +
                    "\t`CITY`\tTEXT NOT NULL,\n" +
                    "\t`EVENT_TYPE`\tTEXT NOT NULL,\n" +
                    "\t`YEAR`\tINTEGER NOT NULL,\n" +
                    "PRIMARY KEY (EVENT_ID)," +
                    "FOREIGN KEY (DESCENDANT) REFERENCES USERS(USERNAME)," +
                    "FOREIGN KEY (PERSON_ID) REFERENCES PERSONS(ID)" +
                    ")";
            stmt.executeUpdate(sql);
            stmt.close();
            closeConnection(true);

            openConnection();
            stmt = c.createStatement();
            sql = "CREATE TABLE IF NOT EXISTS `AUTH_TOKENS` (\n" +
                    "\t`USER_ID`\tTEXT NOT NULL,\n" +
                    "\t`KEY`\tTEXT NOT NULL UNIQUE,\n" +
                    "\tPRIMARY KEY(`KEY`)\n" +
                    "FOREIGN KEY (USER_ID) REFERENCES USERS(ID)" +
                    ")";
            stmt.executeUpdate(sql);
            stmt.close();
            closeConnection(true);
        } catch (SQLException e) {
            System.out.println("ERROR! Could not connect to database");
            e.printStackTrace();
            throw new DataAccessException("SQL Error encountered while creating tables");
        }
        c = null;
        //System.out.println("Tables created successfully");
    }

    public void clearTables() throws DataAccessException {
        openConnection();

        try {
            Statement stmt = c.createStatement();
            String sql = "DELETE FROM USERS";
            stmt.executeUpdate(sql);
            stmt.close();


            stmt = c.createStatement();
            sql = "DELETE FROM PERSONS";
            stmt.executeUpdate(sql);
            stmt.close();


            stmt = c.createStatement();
            sql = "DELETE FROM EVENTS";
            stmt.executeUpdate(sql);
            stmt.close();


            stmt = c.createStatement();
            sql = "DELETE FROM AUTH_TOKENS";
            stmt.executeUpdate(sql);
            stmt.close();
            closeConnection(true);
        } catch (SQLException e) {
            e.printStackTrace();
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}

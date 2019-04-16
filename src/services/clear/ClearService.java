package services.clear;

import dataAccess.DataAccessException;
import dataAccess.Database;

public class ClearService {


    /**
     * Clears all data from all tables in database
     *
     * @return true on success, false on failure
     */
    public ClearResponse clear() {
        //Delete all data from databases but keep tables
        Database d = new Database();
        try {
            d.clearTables();

            return new ClearResponse("Clear succeeded.");

        } catch (DataAccessException e) {
            return new ClearResponse("Internal server error");
        }
    }
}

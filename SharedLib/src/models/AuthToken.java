package models;

import java.util.Objects;
import java.util.UUID;

/**
 * Generates an Authorization key for a user
 */
public class AuthToken {
    public String getKey() {
        return key;
    }

    /**
     * @param o object to check equality of
     * @return true if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return key.equals(authToken.key) &&
                userID.equals(authToken.userID);
    }

    /**
     * @return hash of AuthToken
     */
    @Override
    public int hashCode() {
        return Objects.hash(key, userID);
    }

    public String getUserID() {
        return userID;
    }

    /**
     * Generated string used to verify data access
     */
    private final String key;
    /**
     * user id to match AuthToken with a user
     */
    private final String userID;

    /**
     * @param ID User ID associated with Authorization token
     *           Generates an Authorization key object with new key
     */
    public AuthToken(String ID) {
        userID = ID;
        key = UUID.randomUUID().toString();
    }
    /**
     * @param KEY Authorization key string (not null)
     * @param ID User ID associated with Authorization token
     *           Creates an Authorization key object
     */
    public AuthToken(String KEY, String ID) {
        userID = ID;
        key = KEY;
    }

    public boolean anyNull() {
        if (userID == null) return true;
        return key == null;
    }
}

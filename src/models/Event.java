package models;

import java.util.Objects;
import java.util.UUID;
//FIXME: IMPLEMENT CLONEABLE FOR QUICKLY UPDATING AN EVENT IN TABLE

/**
 * Object for storing information about events and user associated with it
 */
public class Event {
    /**
     * Unique event id
     */
    private final String eventID;
    /**
     * User (Username) to which this person belongs
     */
    private final String descendant;
    /**
     * ID of person to which this event belongs
     */
    private final String personID;
    /**
     * Latitude of event’s location
     */
    private final String latitude;
    /**
     * Longitude of event’s location
     */
    private final String longitude;
    /**
     * Country in which event occurred
     */
    private final String country;
    /**
     * City in which event occurred
     */
    private final String city;
    /**
     * Type of event (birth, baptism, christening, marriage, death, etc.)
     */
    private final String eventType;
    /**
     * Year in which event occurred
     */
    private final int year;

    /**
     * Create Event from a location object and other identifiers
     *
     * @param location   location object
     * @param descendant username of user
     * @param personID   id of person object tied to event
     * @param eventType  type of event (string)
     * @param year       year of event (int)
     */
    public Event(Location location, String descendant, String personID, String eventType, int year) {
        this.descendant = descendant;
        this.personID = personID;
        this.eventType = eventType;
        this.year = year;
        eventID = UUID.randomUUID().toString();
        this.longitude = location.getLongitude();
        this.latitude = location.getLatitude();
        this.city = location.getCity();
        this.country = location.getCountry();
    }

    public boolean anyNull() {
        if (eventID == null) return true;
        if (descendant == null) return true;
        if (personID == null) return true;
        if (latitude == null) return true;
        if (longitude == null) return true;
        if (country == null) return true;
        return eventType == null;
    }

    /**
     * @param des  User (Username) to which this person belongs
     * @param pID  ID of person to which this event belongs
     * @param lat  Latitude of event’s location
     * @param lon  Longitude of event’s location
     * @param con  Country in which event occurred
     * @param cit  City in which event occurred
     * @param type Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param y    Year in which event occurred
     *             <p>
     *             Create an event object and generate a new Event ID
     */
    public Event(String des, String pID, String lat, String lon,
                 String con, String cit, String type, int y) {
        eventID = UUID.randomUUID().toString();
        descendant = des;
        personID = pID;
        latitude = lat;
        longitude = lon;
        country = con;
        city = cit;
        eventType = type;
        year = y;
    }


    /**
     * @param id   Unique Event ID
     * @param des  User (Username) to which this person belongs
     * @param pID  ID of person to which this event belongs
     * @param lat  Latitude of event’s location
     * @param lon  Longitude of event’s location
     * @param con  Country in which event occurred
     * @param cit  City in which event occurred
     * @param type Type of event (birth, baptism, christening, marriage, death, etc.)
     * @param y    Year in which event occurred
     *             <p>
     *             Create an event object with an already existing Event ID
     */
    public Event(String id, String des, String pID, String lat, String lon,
                 String con, String cit, String type, int y) {
        eventID = id;
        descendant = des;
        personID = pID;
        latitude = lat;
        longitude = lon;
        country = con;
        city = cit;
        eventType = type;
        year = y;
    }

    /**
     * compare object equality
     *
     * @param o object to check equality of
     * @return returns true if objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return year == event.year &&
                eventID.equals(event.eventID) &&
                descendant.equals(event.descendant) &&
                personID.equals(event.personID) &&
                latitude.equals(event.latitude) &&
                longitude.equals(event.longitude) &&
                country.equals(event.country) &&
                city.equals(event.city) &&
                eventType.equals(event.eventType);
    }

    /**
     * @return hash of Event object
     */
    @Override
    public int hashCode() {
        return Objects.hash(eventID, descendant, personID, latitude, longitude, country, city, eventType, year);
    }

    public String getEventID() {
        return eventID;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }
}

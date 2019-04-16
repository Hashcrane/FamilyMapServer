package services.load;

import models.Event;
import models.Person;
import models.User;

import java.util.ArrayList;

/**
 * Class represents a request to load information into database
 */
public class LoadRequest {
    /**
     * Array of Users
     */
    private final ArrayList<User> users;

    /**
     * Array of Persons
     */
    private final ArrayList<Person> persons;

    /**
     * Array of Events
     */
    private final ArrayList<Event> events;

    /**
     * load array of objects into database
     * @param users Array of users
     * @param persons Array of persons
     * @param events Array of events
     */
    public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}

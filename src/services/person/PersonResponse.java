package services.person;

import models.Person;

import java.util.ArrayList;

public class PersonResponse {

    /**
     * success or error message, can be null
     */
    private String message;
    /**
     * Person object retrieved from Database
     */
    private Person person;
    /**
     * Array of persons retrieved from Database
     */
    private ArrayList<Person> persons;

    /**
     * Create response with error message
     * @param message non empty string
     */
    public PersonResponse(String message) {
        this.message = message;
    }

    /**
     * Response for a singular person request
     * @param person Person retrieved from database
     */
    public PersonResponse(Person person) {
        this.person = person;
    }

    /**
     * Response for a list of persons requested
     * @param persons List of persons retrieved from database
     */
    public PersonResponse(ArrayList<Person> persons) {
        this.persons = persons;
    }

    public String getMessage() {
        return message;
    }

    public Person getPerson() {
        return person;
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }
}

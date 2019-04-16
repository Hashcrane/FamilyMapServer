package services.event;

import models.Event;

import java.util.ArrayList;

public class EventResponse {
    /**
     * success or error message, can be null
     */
    private String message;
    /**
     * event object retrieved from Database
     */
    private Event event;
    /**
     * Array of events retrieved from Database
     */
    private ArrayList<Event> events;

    public EventResponse(String message) {
        this.message = message;
        System.out.println(message);
    }

    /**
     * Create Event Response that contains an event object
     * @param event event retrieved from database
     */
    public EventResponse(Event event) {
        this.event = event;
        this.message = null;
    }

    /**
     * Create Event response that contains list of events
     * @param events list of events from database
     */
    public EventResponse(ArrayList<Event> events) {
        this.events = events;
    }

    public String getMessage() {
        return message;
    }

    public Event getEvent() {
        return event;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}

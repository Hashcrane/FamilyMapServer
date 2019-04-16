package Tests;

import dataAccess.*;
import models.Event;
import models.Person;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import services.clear.ClearService;
import services.event.EventRequest;
import services.event.EventResponse;
import services.event.EventService;
import services.fill.FillGenerator;
import services.fill.FillResponse;
import services.fill.FillService;
import services.load.LoadRequest;
import services.load.LoadResponse;
import services.load.LoadService;
import services.person.PersonRequest;
import services.person.PersonResponse;
import services.person.PersonService;
import services.register_and_login.*;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class JunitServiceTests {

    private Database db;

    @Before
    public void setUp() throws Exception {
        try {
            db = new Database();
            db.createTables();
            db.clearTables();

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
        if (db.c != null) db.closeConnection(true);
        db = null;
        db = null;
    }

    @Test
    public void FillandDeleteRelatedEventsTest() throws Exception {
        System.out.println("FillandDeleteRelatedEventsTest");
        db.clearTables();
        Person person = new Person("Username", "FirstName", "LastName", "m", null, null, null);
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));

        assertNull(registerResponse.getMessage()); //null message is successful response
        FillService fillService = new FillService();
        boolean success = true;
        try {
            FillResponse fillResponse = fillService.Fill(person.getDescendant(), 4);
            Connection c = db.openConnection();
            EventDAO eventDAO = new EventDAO(c);
            ArrayList<Event> events = eventDAO.GetEventsRelatedTo(person.getDescendant(), 1);
            assertNotNull(events);
            assertTrue(events.size() > 30); //31 people are created each with multiple events
            assertNotEquals("FillService: Failed, user does not exist", fillResponse.getMessage());
            assertNotEquals("FillService: Failed to generate persons and events", fillResponse.getMessage());
            assertNotEquals("FillService: Failed to clear persons and events of user", fillResponse.getMessage());
            assertNotEquals("FillService: Failed to generate persons and events", fillResponse.getMessage());
            eventDAO.DeleteRelatedEvents(person.getDescendant());
            events = eventDAO.GetEventsRelatedTo(person.getDescendant(), 1);
            assertTrue(events.isEmpty());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void FillandDeleteRelatedEventsTestFail() throws Exception {
        System.out.println("FillandDeleteRelatedEventsTestFail");
        db.clearTables();
        Person person = new Person("Username", "FirstName", "LastName", "m", null, null, null);
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));

        assertNull(registerResponse.getMessage()); //null message is successful response
        FillService fillService = new FillService();
        boolean success = true;
        try {
            EventDAO eventDAO = new EventDAO(db.openConnection());
            fillService.Fill(person.getDescendant(), 1);
            eventDAO.DeleteRelatedEvents("fail");
            ArrayList<Event> events = eventDAO.GetEventsRelatedTo(person.getDescendant(), 1);
            assertFalse(events.isEmpty());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void FillandDeleteRelatedPersonsTest() throws Exception {
        System.out.println("FillandDeleteRelatedPersonsTest");
        db.clearTables();
        Person person = new Person("Username", "FirstName", "LastName", "m", null, null, null);
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));

        assertNull(registerResponse.getMessage()); //null message is successful response
        FillService fillService = new FillService();
        boolean success = true;
        try {
            PersonDAO personDAO = new PersonDAO(db.openConnection());
            fillService.Fill(person.getDescendant(), 1);
            personDAO.DeleteRelatedPersons(person.getDescendant());
            ArrayList<Person> persons = personDAO.GetPersonsRelatedTo(null, person.getDescendant(), null, 2);
            assertEquals(0, persons.size());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void FillandDeleteRelatedPersonsTestFail() throws Exception {
        System.out.println("FillandDeleteRelatedPersonsTestFail");
        db.clearTables();
        Person person = new Person("Username", "FirstName", "LastName", "m", null, null, null);
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));

        assertNull(registerResponse.getMessage()); //null message is successful response
        FillService fillService = new FillService();
        boolean success = true;
        try {
            PersonDAO personDAO = new PersonDAO(db.openConnection());
            FillResponse fillResponse = fillService.Fill(person.getDescendant(), 1);
            //assertEquals("FillService: Failed, invalid number of generations", fillResponse.getMessage());
            personDAO.DeleteRelatedPersons("fail");
            ArrayList<Person> persons = personDAO.GetPersonsRelatedTo(null, person.getDescendant(), null, 2);
            assertFalse(persons.isEmpty());

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test //Will only fail if too many connections are open
    public void FillandClearTest() throws Exception {
        System.out.println("FillandClearTest");
        db.clearTables();
        Person person = new Person("Username", "FirstName", "LastName", "m", null, null, null);
        FillService fillService = new FillService();
        fillService.Fill(person.getDescendant(), 6);
        ClearService clearService = new ClearService();
        clearService.clear();

        boolean success = true;
        try {
            PersonDAO personDAO = new PersonDAO(db.openConnection());
            Person p = personDAO.GetPerson(person.getID(), 1);
            db.closeConnection(true);
            assertNull(p);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void RegisterServiceTest() throws Exception {
        System.out.println("RegisterServiceTest");
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));

        assertNull(registerResponse.getMessage()); //null message is successful response

    }

    @Test
    public void RegisterServiceTestFail() throws Exception {
        System.out.println("RegisterServiceTestFail");
        User user = new User(null, "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));

        assertEquals(registerResponse.getMessage(), "Request property missing or has invalid value"); //null message is successful response

    }

    @Test //Tests having multiple logins for same user
    public void LoginServiceTest() throws Exception {
        System.out.println("LoginServiceTest");
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));

        assertNull(registerResponse.getMessage()); //null message is successful response
        LoginService loginService = new LoginService();
        LoginResponse loginResponse = loginService.PublicLogin(new LoginRequest(user.getUsername(), user.getPassword()));
        assertNull(loginResponse.getMessage());
    }

    @Test //Tests logging in with invalid password
    public void LoginServiceTestFail() throws Exception {
        System.out.println("LoginServiceTestFail");
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));

        assertNull(registerResponse.getMessage()); //null message is successful response
        LoginService loginService = new LoginService();
        LoginResponse loginResponse = loginService.PublicLogin(new LoginRequest(user.getUsername(), "invalid"));
        assertNotNull(loginResponse.getMessage());
    }

    @Test
    public void PersonSingleServiceTest() throws Exception {
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));
        //assertNull(registerResponse.getMessage());
        PersonService personService = new PersonService();
        PersonResponse personResponse = personService.GetPerson(new PersonRequest(registerResponse.getToken(),
                registerResponse.getPersonID()));
        assertNull(personResponse.getMessage());
        assertNotNull((personResponse.getPerson()));
    }

    @Test
    public void PersonSingleServiceTestFail() throws Exception {
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));
        PersonService personService = new PersonService();
        PersonResponse personResponse = personService.GetPerson(new PersonRequest("dumbKey"));
        assertNotNull(personResponse.getMessage());
        assertNull((personResponse.getPerson()));
    }

    @Test
    public void PersonALLServiceTest() throws Exception {
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));
        PersonService personService = new PersonService();
        PersonResponse personResponse = personService.GetFamily(new PersonRequest(registerResponse.getToken()));
        assertNull(personResponse.getMessage());
        assertFalse(personResponse.getPersons().isEmpty());
    }

    @Test
    public void EventSingleServiceTest() throws Exception {
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));
        EventService eventService = new EventService();
        EventResponse eventResponse = eventService.GetAllEvents(new EventRequest(registerResponse.getToken()));

        assertNull(registerResponse.getMessage()); //null message is successful response
        EventRequest eventRequest = new EventRequest(registerResponse.getToken(),
                eventResponse.getEvents().get(0).getEventID());
        eventResponse = eventService.GetEvent(eventRequest);

        assertNull(eventResponse.getMessage());
        assertFalse(eventResponse.getEvent().anyNull());
    }

    @Test
    public void EventSingleServiceTestFail() throws Exception {
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));
        EventService eventService = new EventService();
        EventResponse eventResponse = eventService.GetAllEvents(new EventRequest(registerResponse.getToken()));

        assertNull(registerResponse.getMessage()); //null message is successful response
        EventRequest eventRequest = new EventRequest("dumbKey",
                eventResponse.getEvents().get(0).getEventID());
        eventResponse = eventService.GetEvent(eventRequest);

        assertNotNull(eventResponse.getMessage());
    }

    @Test
    public void EventAllServiceTest() throws Exception {
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));
        EventService eventService = new EventService();
        EventResponse eventResponse = eventService.GetAllEvents(new EventRequest(registerResponse.getToken()));

        assertNull(eventResponse.getMessage());
        assertFalse(eventResponse.getEvents().isEmpty());
    }

    @Test
    public void EventAllServiceTestFail() throws Exception {
        User user = new User("Username", "password", "email", "Eric", "Larson", "m");

        RegisterService registerService = new RegisterService();
        RegisterResponse registerResponse = registerService.RegisterUser(new RegisterRequest(user));
        EventService eventService = new EventService();
        EventResponse eventResponse = eventService.GetAllEvents(new EventRequest("dumbKey"));

        assertNotNull(eventResponse.getMessage());
    }

    @Test
    public void LoadServiceTest() throws Exception {
        Person person = new Person("Username", "FirstName", "LastName", "m",
                null, null, null);
        FillGenerator fillGenerator = new FillGenerator(person);
        fillGenerator.Generate(4);
        User user = new User(person, "password", "email");
        LoadService loadService = new LoadService();
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        LoadResponse loadResponse = loadService.Load(new LoadRequest(users, fillGenerator.getAncestors(),
                fillGenerator.getEvents()));
        assertEquals("Successfully added " + users.size() + " users, " +
                fillGenerator.getAncestors().size() + " persons, and " +
                fillGenerator.getEvents().size() + " events to the database.", loadResponse.getMessage());
    }

    @Test
    public void LoadServiceTestFail() throws Exception {
        Person person = new Person("Username", "FirstName", "LastName", "m",
                null, null, null);
        FillGenerator fillGenerator = new FillGenerator(person);
        fillGenerator.Generate(4);
        User user = new User(person, "password", null);
        LoadService loadService = new LoadService();
        ArrayList<User> users = new ArrayList<>();
        users.add(user);
        LoadResponse loadResponse = loadService.Load(new LoadRequest(users, fillGenerator.getAncestors(),
                fillGenerator.getEvents()));
        assertNotEquals("LoadResponse: Successfully added " + users.size() + " users, " +
                fillGenerator.getAncestors().size() + " persons, and " +
                fillGenerator.getEvents().size() + " events to the database.", loadResponse.getMessage());
    }
}

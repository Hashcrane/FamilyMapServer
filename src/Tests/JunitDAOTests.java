package Tests;

import dataAccess.*;
import models.AuthToken;
import models.Event;
import models.Person;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.util.ArrayList;

import static org.junit.Assert.*;


public class JunitDAOTests {
    private Database db;
    private UserDAO uDAO;
    private PersonDAO pDAO;
    private EventDAO eDAO;
    private AuthTokenDAO aDAO;

    @Before
    public void setUp() throws Exception {
        try {
            db = new Database();
            db.createTables();
            db.clearTables();
            uDAO = null;
            pDAO = null;
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() throws Exception {
       // db.closeConnection(false);
        //db.clearTables();
        //db.closeConnection(false);
        if (db.c != null) db.closeConnection(false);
        db = null;
        eDAO = null;
        aDAO = null;
        uDAO = null;
        pDAO = null;
    }
/*FIXME:FIXME:FIXME:FIXME:FIXME:FIXME:FIXME:FIXME:FIXME:
ALL DAO updates
FIXME:FIXME:FIXME:FIXME:FIXME:FIXME:FIXME:FIXME:FIXME:
 */

    @Test
    public void UserDAOInsertTest() throws Exception {
        System.out.println("UserDAOInsertTest");
        //test insert and query

        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();

        assertNotNull(id);
        //insert
        User result;

        try {
            Connection c = db.openConnection();
            uDAO = new UserDAO(c);
            assertTrue(uDAO.Insert(u));

            result = uDAO.GetUserByID(id);
            //query
            assertEquals(u, result);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            fail();

        }
    }

    @Test
    public void UserDAOInsertFailTest() throws Exception {
        System.out.println("UserDAOInsertFailTest");
        //test insert and query

        db.clearTables();
        User u = new User(null, "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();

        assertNotNull(id);
        //insert
        boolean success = true;

        try {
            Connection c = db.openConnection();
            uDAO = new UserDAO(c);
            uDAO.Insert(u);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void UserDAODeleteTest() throws Exception {
        System.out.println("UserDAODeleteTest");

        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();

        assertNotNull(id);
        //insert
        User result = null;
        boolean success = false;
        try {
            Connection c = db.openConnection();
            uDAO = new UserDAO(c);
            assertTrue(uDAO.Insert(u));

            assertTrue(uDAO.Delete(id));
            User test = uDAO.GetUserByID(id);
            if (test == null) success = true;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void UserDAODeleteFailTest() throws Exception {
        System.out.println("UserDAODeleteFailTest");
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();

        assertNotNull(id);
        //insert
        User result = null;
        boolean success;
        try {
            Connection c = db.openConnection();
            uDAO = new UserDAO(c);
            assertTrue(uDAO.Insert(u));

            success = uDAO.Delete("fail");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void UserDAOClearTest() throws Exception {
        System.out.println("UserDAOClearTest");
        //test insert and query

        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();

        assertNotNull(id);
        //insert
        boolean success = false;
        try {
            Connection c = db.openConnection();
            uDAO = new UserDAO(c);
            assertTrue(uDAO.Insert(u));

            assertTrue(uDAO.Clear());
            User test = uDAO.GetUserByID(id);
            if (test == null) success = true;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void UserDAOQueryFailTest() throws Exception {
        System.out.println("UserDAOQueryFailTest");
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();

        assertNotNull(id);
        //insert
        boolean success = false;
        try {
            Connection c = db.openConnection();
            uDAO = new UserDAO(c);
            assertTrue(uDAO.Insert(u));

            User test = uDAO.GetUserByID("fail");
            if (test == null) success = true;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void UserLoadTest() throws Exception {
        System.out.println("UserLoadTest");
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();
        User u2 = new User("jeff2", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id2 = u2.getID();
        User u3 = new User("jeff3", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id3 = u3.getID();

        ArrayList<User> users = new ArrayList<>();
        users.add(u);
        users.add(u2);
        users.add(u3);

        User r1;
        User r2;
        User r3;
        boolean success;
        try {
            Connection c = db.openConnection();
            uDAO = new UserDAO(c);

            assertTrue(uDAO.InsertMany(users));
            r1 = uDAO.GetUserByID(id);
            r2 = uDAO.GetUserByID(id2);
            r3 = uDAO.GetUserByID(id3);

            assertEquals(r1, u);
            assertEquals(r2, u2);
            assertEquals(r3, u3);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void UserLoadFailTest() throws Exception {
        System.out.println("UserLoadFailTest");
        db.clearTables();
        Person p = new Person("descendant1", "eric", "larson", "m", "1q4352", null, null);
        String id = p.getID();
        Person p2 = new Person("descendant2", "ryan", "larson", "m", "1q4352", null, null);
        String id2 = p2.getID();
        Person p3 = new Person(null, "kyle", "larson", "m", "1q4352", null, null);
        String id3 = p3.getID();

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(p);
        persons.add(p2);
        persons.add(p3);

        Person r1;
        Person r2;
        Person r3;
        boolean success;
        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);

            assertTrue(pDAO.InsertMany(persons));
            r1 = pDAO.GetPerson(id, 1);
            r2 = pDAO.GetPerson(id2, 1);
            r3 = pDAO.GetPerson(id3, 1);

            assertEquals(r1, p);
            assertEquals(r2, p2);
            assertEquals(r3, p3);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void PersonDAOInsertTest() throws Exception {
        //Test Insert and Query
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();
        Person p = new Person(id, "eric", "larson", "m", "1q4352", null, null);
        String pId = p.getID();

        assertNotNull(pId);
        //insert
        Person result = null;

        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);
            //insert
            assertTrue(pDAO.Insert(p));
            result = pDAO.GetPerson(pId, 1);
            assertNull(pDAO.GetPerson("fail", 1));
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertEquals(result, p);
    }

    @Test
    public void PersonDAOInsertFailTest() throws Exception {
        //Test Insert and Query
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();
        Person p = new Person(id, null, null, "m", "1q4352", null, null);
        String pId = p.getID();

        assertNotNull(pId);
        //insert
        boolean success = true;
        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);
            //insert
            pDAO.Insert(p);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void PersonDAODeleteTest() throws Exception {
        //Test Insert and Query
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();
        Person p = new Person(id, "Eric", "Larson", "m", "1q4352", null, null);
        String pId = p.getID();

        assertNotNull(pId);
        //insert
        boolean success = true;
        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);
            //insert
            pDAO.Insert(p);
            assertTrue(pDAO.Delete(pId));
            Person test = pDAO.GetPerson(pId, 1);
            if (test == null) success = true;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void PersonDAODeleteFailTest() throws Exception {
        //Test Insert and Query
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();
        Person p = new Person(id, "Eric", "Larson", "m", "1q4352", null, null);
        String pId = p.getID();

        assertNotNull(pId);
        //insert
        boolean success;
        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);
            //insert
            pDAO.Insert(p);
            success = pDAO.Delete("fail");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void PersonDAOClearTest() throws Exception {
        //Test Insert and Query
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();
        Person p = new Person(id, "eric", "larson", "m", "1q4352", null, null);
        String pId = p.getID();

        assertNotNull(pId);
        //insert
        boolean success = false;
        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);
            //insert
            assertTrue(pDAO.Insert(p));
            assertTrue(pDAO.Clear());
            Person test = pDAO.GetPerson(pId, 1);
            if (test == null) success = true;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertTrue(success);
    }

    @Test
    public void PersonDAOQueryFailTest() throws Exception {
        //Test Insert and Query
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String id = u.getID();
        Person p = new Person(id, "eric", "larson", "m", "1q4352", null, null);
        String pId = p.getID();

        assertNotNull(pId);
        //insert

        boolean success = false;
        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);
            //insert
            assertTrue(pDAO.Insert(p));

            Person test = pDAO.GetPerson("fail", 1);
            if (test == null) success = true;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void PersonLoadTest() throws Exception {
        db.clearTables();
        Person p = new Person("descendant1", "eric", "larson", "m", "1q4352", null, null);
        String id = p.getID();
        Person p2 = new Person("descendant2", "ryan", "larson", "m", "1q4352", null, null);
        String id2 = p2.getID();
        Person p3 = new Person("descendant3", "kyle", "larson", "m", "1q4352", null, null);
        String id3 = p3.getID();

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(p);
        persons.add(p2);
        persons.add(p3);

        Person r1;
        Person r2;
        Person r3;
        boolean success;
        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);

            assertTrue(pDAO.InsertMany(persons));
            r1 = pDAO.GetPerson(id, 1);
            r2 = pDAO.GetPerson(id2, 1);
            r3 = pDAO.GetPerson(id3, 1);

            assertEquals(r1, p);
            assertEquals(r2, p2);
            assertEquals(r3, p3);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void PersonLoadFailTest() throws Exception {
        db.clearTables();
        Person p = new Person("descendant1", "eric", "larson", "m", "1q4352", null, null);
        String id = p.getID();
        Person p2 = new Person("descendant2", "ryan", "larson", "m", "1q4352", null, null);
        String id2 = p2.getID();
        Person p3 = new Person(null, "kyle", "larson", "m", "1q4352", null, null);
        String id3 = p3.getID();

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(p);
        persons.add(p2);
        persons.add(p3);

        Person r1;
        Person r2;
        Person r3;
        boolean success;
        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);

            assertTrue(pDAO.InsertMany(persons));
            r1 = pDAO.GetPerson(id, 1);
            r2 = pDAO.GetPerson(id2, 1);
            r3 = pDAO.GetPerson(id3, 1);

            assertEquals(r1, p);
            assertEquals(r2, p2);
            assertEquals(r3, p3);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void PersonGetRelatedTest() throws Exception {
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String uID = u.getUsername();
        Person p = new Person(uID, "eric", "larson", "m", "1q4352", null, null);
        String id = p.getID();
        Person p2 = new Person(uID, "ryan", "larson", "m", "1q4352", null, null);
        String id2 = p2.getID();
        Person p3 = new Person(uID, "kyle", "larson", "m", "1q4352", null, null);
        String id3 = p3.getID();

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(p);
        persons.add(p2);
        persons.add(p3);

        boolean success;
        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);

            assertTrue(pDAO.InsertMany(persons));
            ArrayList<Person> testArray = pDAO.GetPersonsRelatedTo(u,null, null, 1);

            Person r1 = pDAO.GetPerson(id, 1);
            assertEquals(r1, p);
            assertEquals(testArray.get(0), p);
            assertEquals(testArray.get(1), p2);
            assertEquals(testArray.get(2), p3);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void PersonGetRelatedFailTest() throws Exception {
        db.clearTables();
        User u = new User(null, "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String uID = u.getID();
        Person p = new Person(uID, "eric", "larson", "m", "1q4352", null, null);
        // String id = p.getID();
        Person p2 = new Person(uID, "ryan", "larson", "m", "1q4352", null, null);
        // String id2 = p2.getID();
        Person p3 = new Person(uID, "kyle", "larson", "m", "1q4352", null, null);
        //String id3 = p3.getID();

        ArrayList<Person> persons = new ArrayList<>();
        persons.add(p);
        persons.add(p2);
        persons.add(p3);

        boolean success;
        try {
            Connection c = db.openConnection();
            pDAO = new PersonDAO(c);
            //User test = null;

            assertTrue(pDAO.InsertMany(persons));
            ArrayList<Person> testArray = pDAO.GetPersonsRelatedTo(u, null, null, 1);

            assertEquals(testArray.get(0), p);
            assertEquals(testArray.get(1), p2);
            assertEquals(testArray.get(2), p3);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void EventDAOInsertTest() throws Exception {
        //test insert and query

        db.clearTables();
        Event ev = new Event("descendant", "person", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id = ev.getEventID();

        assertNotNull(id);
        //insert
        Event result;

        try {
            Connection c = db.openConnection();
            eDAO = new EventDAO(c);
            assertTrue(eDAO.Insert(ev));

            result = eDAO.GetEvent(id, null);
            //query
            assertEquals(ev, result);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            fail();

        }
    }

    @Test
    public void EventDAOInsertFailTest() throws Exception {
        //test insert and query

        db.clearTables();
        Event ev = new Event(null, "person", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id = ev.getEventID();

        assertNotNull(id);
        //insert
        boolean success = true;

        try {
            Connection c = db.openConnection();
            eDAO = new EventDAO(c);
            eDAO.Insert(ev);

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void EventDAODeleteTest() throws Exception {
        db.clearTables();
        Event ev = new Event("descendant", "person", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id = ev.getEventID();

        assertNotNull(id);
        //insert
        Event result = null;
        boolean success = false;
        try {
            Connection c = db.openConnection();
            eDAO = new EventDAO(c);
            assertTrue(eDAO.Insert(ev));

            assertTrue(eDAO.Delete(id));
            Event test = eDAO.GetEvent(id, ev.getPersonID());
            if (test == null) success = true;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void EventDAODeleteFailTest() throws Exception {
        db.clearTables();
        Event ev = new Event("descendant", "person", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id = ev.getEventID();

        assertNotNull(id);
        //insert
        User result = null;
        boolean success;
        try {
            Connection c = db.openConnection();
            eDAO = new EventDAO(c);
            assertTrue(eDAO.Insert(ev));

            success = eDAO.Delete("fail");

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void EventDAOClearTest() throws Exception {
        //test insert and query

        db.clearTables();
        Event ev = new Event("descendant", "person", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id = ev.getEventID();

        assertNotNull(id);
        //insert
        boolean success = false;
        try {
            Connection c = db.openConnection();
            eDAO = new EventDAO(c);
            assertTrue(eDAO.Insert(ev));

            assertTrue(eDAO.Clear());
            Event test = eDAO.GetEvent(id, ev.getPersonID());
            if (test == null) success = true;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void EventDAOQueryFailTest() throws Exception {
        db.clearTables();
        Event ev = new Event("descendant", "person", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id = ev.getEventID();

        assertNotNull(id);
        //insert
        boolean success = false;
        try {
            Connection c = db.openConnection();
            eDAO = new EventDAO(c);
            assertTrue(eDAO.Insert(ev));

            Event test = eDAO.GetEvent("fail", ev.getPersonID());
            if (test == null) success = true;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void EventLoadTest() throws Exception {
        db.clearTables();
        Event ev = new Event("descendant", "person", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id = ev.getEventID();
        Event ev2 = new Event("descendant2", "person2", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id2 = ev2.getEventID();
        Event ev3 = new Event("descendant3", "person3", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id3 = ev3.getEventID();
        ArrayList<Event> events = new ArrayList<>();
        events.add(ev);
        events.add(ev2);
        events.add(ev3);

        Event r1;
        Event r2;
        Event r3;
        boolean success;
        try {
            Connection c = db.openConnection();
            eDAO = new EventDAO(c);

            assertTrue(eDAO.InsertMany(events));
            db.closeConnection(true);
            eDAO = new EventDAO(db.openConnection());
            r1 = eDAO.GetEvent(id, null);
            r2 = eDAO.GetEvent(id2, null);
            r3 = eDAO.GetEvent(id3, null);

            assertEquals(r1, ev);
            assertEquals(r2, ev2);
            assertEquals(r3, ev3);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void EventLoadFailTest() throws Exception {
        db.clearTables();
        Event ev = new Event("descendant", "person", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id = ev.getEventID();
        Event ev2 = new Event("descendant2", "person2", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id2 = ev2.getEventID();
        Event ev3 = new Event(null, "person3", "1234", "1234", "country", "city", "gay marriage", 2009);
        ArrayList<Event> events = new ArrayList<>();
        events.add(ev);
        events.add(ev2);
        events.add(ev3);

        Event r1;
        Event r2;

        boolean success;
        try {
            Connection c = db.openConnection();
            eDAO = new EventDAO(c);

            assertTrue(eDAO.InsertMany(events));
            r1 = eDAO.GetEvent(id, ev.getPersonID());
            r2 = eDAO.GetEvent(id2, ev2.getPersonID());


            assertEquals(r1, ev);
            assertEquals(r2, ev2);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void EventGetRelatedTest() throws Exception {
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String uID = u.getID();
        Event ev = new Event(uID, "person", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id = ev.getEventID();
        Event ev2 = new Event(uID, "person2", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id2 = ev2.getEventID();
        Event ev3 = new Event(uID, "person3", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id3 = ev3.getEventID();
        ArrayList<Event> events = new ArrayList<>();
        events.add(ev);
        events.add(ev2);
        events.add(ev3);

        boolean success;
        try {
            Connection c = db.openConnection();
            eDAO = new EventDAO(c);

            assertTrue(eDAO.InsertMany(events));
            ArrayList<Event> testArray = eDAO.GetEventsRelatedTo(uID, 1);

            assertEquals(testArray.get(0), ev);
            assertEquals(testArray.get(1), ev2);
            assertEquals(testArray.get(2), ev3);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void EventGetRelatedFailTest() throws Exception {
        db.clearTables();
        User u = new User(null, "bev", "jbev@gmail.com", "jeff", "bev", "m");
        String uID = u.getID();
        Event ev = new Event(uID, "person", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id = ev.getEventID();
        Event ev2 = new Event(uID, "person2", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id2 = ev2.getEventID();
        Event ev3 = new Event(uID, "person3", "1234", "1234", "country", "city", "gay marriage", 2009);
        String id3 = ev3.getEventID();
        ArrayList<Event> events = new ArrayList<>();
        events.add(ev);
        events.add(ev2);
        events.add(ev3);

        boolean success;
        try {
            Connection c = db.openConnection();
            eDAO = new EventDAO(c);
            //User test = null;

            assertTrue(eDAO.InsertMany(events));
            ArrayList<Event> testArray = eDAO.GetEventsRelatedTo("fail", 1);

            assertTrue(testArray.isEmpty());

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void TokenQueryFailTest() throws Exception {
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        AuthToken t = new AuthToken(u.getID());
        String test = t.getKey();

        boolean success;
        try {
            Connection c = db.openConnection();
            aDAO = new AuthTokenDAO(c);

            aDAO.GetToken("fail");
            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void TokenInsertTest() throws Exception {
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        AuthToken t = new AuthToken(u.getID());

        boolean success;
        try {
            Connection c = db.openConnection();
            aDAO = new AuthTokenDAO(c);

            assertTrue(aDAO.Insert(t));
            AuthToken test = aDAO.GetToken(u.getID());
            assertEquals(t, test);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void TokenInsertFailTest() throws Exception {
        db.clearTables();
        User u = new User(null, "bev", "jbev@gmail.com", "jeff", "bev", "m");
        AuthToken t = new AuthToken(null);

        boolean success;
        try {
            Connection c = db.openConnection();
            aDAO = new AuthTokenDAO(c);

            aDAO.Insert(t);
            //AuthToken test = aDAO.GetToken(u.getID());
            //assertEquals(t, test);

            success = true;
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void TokenDeleteTest() throws Exception {
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        AuthToken t = new AuthToken(u.getID());

        boolean success;
        try {
            Connection c = db.openConnection();
            aDAO = new AuthTokenDAO(c);

            assertTrue(aDAO.Insert(t));
            AuthToken test = aDAO.GetToken(u.getID());
            assertEquals(t, test);
            assertTrue(aDAO.Delete(u.getID()));
            AuthToken result = aDAO.GetToken(u.getID());

            success = true;

            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }

    @Test
    public void TokenDeleteFailTest() throws Exception {
        db.clearTables();
        // User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        // AuthToken t = new AuthToken(u.getID());

        boolean success;
        try {
            Connection c = db.openConnection();
            aDAO = new AuthTokenDAO(c);

            // assertTrue(aDAO.Insert(t));
            //  AuthToken test = aDAO.GetToken(u.getID());
            //  assertEquals(t, test);
            success = aDAO.Delete("fail");
            //AuthToken result = aDAO.GetToken(u.getID());


            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void TokenClearTest() throws Exception {
        db.clearTables();
        User u = new User("jeff", "bev", "jbev@gmail.com", "jeff", "bev", "m");
        AuthToken t = new AuthToken(u.getID());

        boolean success = false;
        try {
            Connection c = db.openConnection();
            aDAO = new AuthTokenDAO(c);

            assertTrue(aDAO.Insert(t));
            AuthToken test = aDAO.GetToken(u.getID());
            assertEquals(t, test);
            assertTrue(aDAO.Clear());
            test = aDAO.GetToken(u.getID());
            //AuthToken result = aDAO.GetToken(u.getID());
            if (test == null) success = true;


            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            success = false;
        }
        assertTrue(success);
    }
}
package services.fill;

import dataAccess.DataAccessException;
import models.Event;
import models.Location;
import models.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates Person and Events for FillService
 */
public class FillGenerator {
    /**
     * person object to build generations for
     */
    private Person primary;
    /**
     * non negative integer
     */
    private int numPersons;
    /**
     * non negative integer
     */
    private int numEvents;
    /**
     * locations for use in random generation
     */
    private ArrayList<Location> locations = new ArrayList<>();
    /**
     * female first names
     */
    private ArrayList<String> fnames = new ArrayList<>();
    /**
     * male first names
     */
    private ArrayList<String> mnames = new ArrayList<>();
    /**
     * surnames
     */
    private ArrayList<String> snames = new ArrayList<>();

    /**
     * Generated persons
     */
    private ArrayList<Person> ancestors = new ArrayList<>();

    /**
     * Generated events
     */
    private ArrayList<Event> events = new ArrayList<>();


    /**
     * stores birthdays
     */
    private ArrayList<Integer> birthYears = new ArrayList<>();
    /**
     * year reference for creating events
     */
    private int baseYear;
    /**
     * total expected number of people created
     */
    private int expectedNumPeople;

    private int currentGeneration;


    /**
     * Average lifespan for both sexes in US
     */
    private final int AVERAGE_LIFESPAN = 79;

    /**
     * Create FillGenerator with person
     *
     * @param primary person to create generations for
     */
    public FillGenerator(Person primary) {
        this.primary = primary;
        currentGeneration = 1;
        ImportLocations();
        ImportFnames();
        ImportMnames();
        ImportSnames();
        baseYear = Calendar.getInstance().get(Calendar.YEAR);
        createBirth(primary, 0);
        createOther(primary, birthYears.get(birthYears.size() - 1), baseYear);
        ancestors.add(primary);
    }

    /**
     * Initializes recursive call to generate persons and events
     *
     * @param numGenerations number of generations to create
     */
    public void Generate(int numGenerations) {
        if (numGenerations < 0) {
            numGenerations = 0;
        }
        if (numGenerations == 0) {
            numEvents = events.size();
            numPersons = ancestors.size();
        } else {
            expectedNumPeople = (int) Math.pow(2, (double) numGenerations + 1) - 2; // - 2 because person 1 is counted already
            Generate(numGenerations, createParents(primary, birthYears.get(0)));
            numEvents = events.size();
            numPersons = ancestors.size();
        }
    }

    /**
     * Recursively generates persons and events until iteration == numGenerations
     *
     * @param numGenerations number of generations to create
     * @param people         list of people to create new parents for
     */
    void Generate(int numGenerations, ArrayList<Person> people) {
        if (ancestors.size() - 1 >= expectedNumPeople) return;
        if (currentGeneration >= numGenerations) return;
        else {
            int momSideBenchYear = birthYears.get(birthYears.size() - 2);
            int dadSideBenchYear = birthYears.get(birthYears.size() - 1);
            ++currentGeneration;
            //create parents for mother
            Generate(numGenerations, createParents(people.get(0), momSideBenchYear));
            //--currentGeneration;
            //create parents for father
            Generate(numGenerations, createParents(people.get(1), dadSideBenchYear));
            --currentGeneration;
        }
    }

    /**
     * Creates parents for a child
     *
     * @param child      (person object)
     * @param childBirth int year of child's birth
     * @return list of person objects representing parents. Will always be size 2
     */
    private ArrayList<Person> createParents(Person child, int childBirth) {
        Person mom = new Person(child.getDescendant(), GetFname(), GetSname(),
                "f", null, null, null);
        Person dad = new Person(child.getDescendant(), GetMname(), child.getL_name(),
                "m", null, null, mom.getID());
        mom.setSpouse(dad.getID());
        child.setFather(dad.getID());
        child.setMother(mom.getID());
        assert primary.getFather() != null;
        assert primary.getMother() != null;

        int momBirth = createBirth(mom, childBirth);
        int dadBirth = createBirth(dad, childBirth);

        //Conditionally create death
        int momDeath = 0;
        int dadDeath = 0;
        if ((baseYear - AVERAGE_LIFESPAN - 25) > momBirth) {
            momDeath = createDeath(mom, momBirth);
        }
        if ((baseYear - AVERAGE_LIFESPAN - 25) > dadBirth) {
            dadDeath = createDeath(dad, dadBirth);
        }
        if (momDeath == 0) {
            Random rand = new Random();
            if (rand.nextInt(50) == 1) {
                momDeath = createDeath(mom, momBirth);
                while (momDeath < childBirth) {
                    events.remove(events.size() - 1);
                    momDeath = createDeath(mom, momBirth);
                }
            }
        }
        if (dadDeath == 0) {
            Random rand = new Random();
            if (rand.nextInt(50) == 1) {
                dadDeath = createDeath(dad, dadBirth);
                while (dadDeath < childBirth) {
                    events.remove(events.size() - 1);
                    dadDeath = createDeath(dad, dadBirth);
                }
            }
        }
        createMarriage(dad, mom, childBirth, momBirth, dadBirth, momDeath, dadDeath);
        Random rand = new Random();
        int numRand1 = rand.nextInt(6) + 1;
        for (int i = 0; i < numRand1; ++i) {
            createOther(mom, momBirth, momDeath);
        }
        numRand1 = rand.nextInt(6) + 1;
        for (int i = 0; i < numRand1; ++i) {
            createOther(dad, dadBirth, dadDeath);
        }


        ArrayList<Person> parents = new ArrayList<>();
        parents.add(mom);
        parents.add(dad);
        ancestors.add(mom);
        ancestors.add(dad);


        return parents;
    }

    /**
     * Creates a birthday for a person and adds to birthYear list
     *
     * @param p          person to create a birthday for
     * @param childBirth year (int) of the person's child's birthday, Can be 0 if the person is the first generation
     * @return int of the birthyear created
     */
    private int createBirth(Person p, int childBirth) {
        Location location = GetRandLocation();
        if (p.equals(primary)) {
            Event event = new Event(location, primary.getDescendant(), primary.getID(), "Birth",
                    GetBirthday(0));
            birthYears.add(event.getYear());
            events.add(event);
            return event.getYear();
        } else {
            Event event = new Event(location, primary.getDescendant(), p.getID(), "Birth", GetBirthday(childBirth));
            birthYears.add(event.getYear());
            events.add(event);
            return event.getYear();
        }
    }

    /**
     * Creates a deathday for a person
     *
     * @param p           person to create a deathday for
     * @param personBirth year (int) of the person's child's birthday
     * @return int of the death year created
     */
    private int createDeath(Person p, int personBirth) {
        Location location = GetRandLocation();
        int dYear = GetDeathDay(personBirth);

        Event event = new Event(location, primary.getDescendant(), p.getID(), "Death", dYear);
        events.add(event);
        return dYear;
    }

    /**
     * Creates a marriage of two people and addes to events list
     *
     * @param m          male person
     * @param f          female person
     * @param childBirth birth of the child for generating logical marriage dates
     */
    private void createMarriage(Person m, Person f, int childBirth, int momBirth, int dadBirth, int momDeath, int dadDeath) {
        Location location = GetRandLocation();
        int avg = (momBirth + dadBirth) / 2;
        int eventYear;
        if (momDeath == 0 || dadDeath == 0) {
            eventYear = ThreadLocalRandom.current().nextInt(avg + 18, baseYear);
        } else {
            if (momDeath < dadDeath) {
                eventYear = ThreadLocalRandom.current().nextInt(momBirth + 18, momDeath - 1);
            } else {
                eventYear = ThreadLocalRandom.current().nextInt(dadBirth + 18, dadDeath - 1);
            }
        }
        Event pop = new Event(location, primary.getDescendant(), m.getID(), "Marriage to " + f.getF_name(), eventYear);
        Event mom = new Event(location, primary.getDescendant(), f.getID(), "Marriage to " + m.getF_name(), eventYear);

        events.add(pop);
        events.add(mom);
    }

    /**
     * Creates a random other event that is not birth, death, or marriage and adds to events list
     *
     * @param p           person to create event for
     * @param personBirth year of person's birth, for lower date range
     * @param personDeath year of person's death, for upper date range. Can be 0 if the person has not died. Then it will use the current year.
     */
    private void createOther(Person p, int personBirth, int personDeath) {
        assert personBirth > 0;
        //assert personDeath > 0;
        Location location = GetRandLocation();
        int eventYear;
        if (personDeath == 0) {
            eventYear = ThreadLocalRandom.current().nextInt(personBirth + 18, baseYear);
        } else {
            eventYear = ThreadLocalRandom.current().nextInt(personBirth + 19, personDeath);
        }
        String[] types = {"Arrested for murder by ax", "Kidney stolen", "Arrested for J-walking", "Parking ticket",
                "Heart Transplant", "First Kiss", "Second arrest for public urination", "Attacked by wild animal",
                "Arrested for petty theft", "Arrested for public swearing", "Arrested for soiling a pool", "Arrested for cow tipping",
                "Heart surgery", "Declared Bankruptcy", "Mistook a dwarf for a child", "Adopted a child", "Adopted a cat",
                "Adopted a dog", "Adopted a bird", "Adopted a parrot", "Purchased a home", "Broke a child\'s toy",
                "Built a useless contraption", "Ate Corn for the first time", "Learned milk was not for them",
                "Purchased land", "Purchased a boat", "Protested at a Protest", "Obsessed over making the perfect sandwich",
                "Publicly humiliated", "Learned to read", "Obsessed over existential philosophy", "Abandoned organized religion",
                "Stopped believing in Santa", "Mental Breakdown", "Bit by stray dog"};
        Random rand = new Random();
        String eventType = types[rand.nextInt(types.length)];
        events.add(new Event(location, primary.getDescendant(), p.getID(), eventType, eventYear));
    }

    /**
     * populate locations from JSON file
     */
    private void ImportLocations() {
        FillGsonDeserializer fillGsonDeserializer = new FillGsonDeserializer();
        try {
            locations = fillGsonDeserializer.GetLocations();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * populate female names from JSON file
     */
    private void ImportFnames() {
        FillGsonDeserializer fillGsonDeserializer = new FillGsonDeserializer();
        try {
            fnames = fillGsonDeserializer.GetFnames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * populate male names from JSON file
     */
    private void ImportMnames() {
        FillGsonDeserializer fillGsonDeserializer = new FillGsonDeserializer();
        try {
            mnames = fillGsonDeserializer.GetMnames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * populate surnames from JSON file
     */
    private void ImportSnames() {
        FillGsonDeserializer fillGsonDeserializer = new FillGsonDeserializer();
        try {
            snames = fillGsonDeserializer.GetSnames();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Gets a random location from the locations index
     *
     * @return location object
     */
    private Location GetRandLocation() {
        Random rand = new Random();
        return locations.get(rand.nextInt(locations.size()));
    }

    /**
     * Get a random female name from the index
     *
     * @return string
     */
    private String GetFname() {
        Random rand = new Random();
        return fnames.get(rand.nextInt(fnames.size()));
    }

    /**
     * Get a random male name from the index
     *
     * @return string
     */
    private String GetMname() {
        Random rand = new Random();
        return mnames.get(rand.nextInt(mnames.size()));
    }

    /**
     * Get a random surname from the index
     *
     * @return string
     */
    private String GetSname() {
        Random rand = new Random();
        return snames.get(rand.nextInt(snames.size()));
    }

    /**
     * Generate a death day as year (int)
     *
     * @param birth birth year (int)
     * @return int death day
     */
    private int GetDeathDay(int birth) {
        return ThreadLocalRandom.current().nextInt(birth + 20, birth + AVERAGE_LIFESPAN + 25);
    }

    /**
     * Generate a birth year that is between 20 and 50 years before a child's birthday
     *
     * @param childBirth child's birth year (int)
     * @return int, parent birth year
     */
    private int GetBirthday(int childBirth) {
        if (childBirth == 0) {
            return ThreadLocalRandom.current().nextInt(baseYear - 50, baseYear - 20);
        }
        return ThreadLocalRandom.current().nextInt(childBirth - 50, childBirth - 22);
    }


    public ArrayList<Person> getAncestors() {
        return ancestors;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    /*public ArrayList<Integer> getBirthYears() {
        return birthYears;
    }*/

    int getNumPersons() {
        return numPersons;
    }

    int getNumEvents() {
        return numEvents;
    }
}

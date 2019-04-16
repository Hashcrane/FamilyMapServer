package models;

import services.register_and_login.RegisterRequest;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;

/**
 * Person object for storing information about a person and the User associated with it
 */
public class Person implements Comparator<Person> {
    /**
     * Unique Person id (non-empty string)
     */
    private final String personID;
    /**
     * User (Username) to which this person belongs
     */
    private final String descendant;
    /**
     * Person’s first name (non-empty string)
     */
    private final String firstName;
    /**
     * Person’s last name (non-empty string)
     */
    private final String lastName;
    /**
     * Person’s gender (string: “f” or “m”)
     */
    private final String gender;
    /**
     * ID of person’s father (possibly null)
     */
    private String father;
    /**
     * ID of person’s mother (possibly null)
     */
    private String mother;
    /**
     * ID of person’s spouse (possibly null)
     */
    private String spouse;

    public boolean anyNull() {
        if (personID == null) return true;
        if (descendant == null) return true;
        if (firstName == null) return true;
        if (lastName == null) return true;
        if (gender == null) return true;
        if (gender.length() != 1) return true;
        if (gender.toLowerCase().charAt(0) != 'm' &&
                gender.toLowerCase().charAt(0) != 'f') {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Create a Person from a registerRequest object (generates new person ID)
     *
     * @param registerRequest Non Null, contains Person fields
     */
    public Person(RegisterRequest registerRequest) {
        descendant = registerRequest.getUsername();
        firstName = registerRequest.getFirstName();
        lastName = registerRequest.getLastName();
        gender = registerRequest.getGender().toLowerCase();
        personID = UUID.randomUUID().toString();
    }

    /**
     * @param des User (Username) to which this person belongs
     * @param f   Person’s first name (non-empty string)
     * @param l   Person’s last name (non-empty string)
     * @param g   Person’s gender (string: “f” or “m”)
     * @param dad ID of person’s father (possibly null)
     * @param mom ID of person’s mother (possibly null)
     * @param s   ID of person’s spouse (possibly null)
     *            <p>
     *            Constructs a Person Object and generates an id
     */

    public Person(String des, String f, String l, String g,
                  String dad, String mom, String s) {
        personID = UUID.randomUUID().toString();
        descendant = des;
        firstName = f;
        lastName = l;
        gender = g;
        father = dad;
        mother = mom;
        spouse = s;
    }

    /**
     * Create Person from User object
     * @param user
     */
    public Person(User user) {
        personID = user.getID();
        descendant = user.getUsername();
        firstName = user.getF_name();
        lastName = user.getL_name();
        gender = user.getGender().toLowerCase();
    }

    /**
     * @param id  Unique id for Person (non-empty string)
     * @param des User (Username) to which this person belongs
     * @param f   Person’s first name (non-empty string)
     * @param l   Person’s last name (non-empty string)
     * @param g   Person’s gender (string: “f” or “m”)
     * @param dad ID of person’s father (possibly null)
     * @param mom ID of person’s mother (possibly null)
     * @param s   ID of person’s spouse (possibly null)
     *            <p>
     *            Constructs a Person Object
     */
    public Person(String id, String des, String f, String l, String g,
                  String dad, String mom, String s) {
        personID = id;
        descendant = des;
        firstName = f;
        lastName = l;
        gender = g.toLowerCase();
        father = dad;
        mother = mom;
        spouse = s;
    }

    /**
     * Compares its two arguments for order.  Returns a negative integer,
     * zero, or a positive integer as the first argument is less than, equal
     * to, or greater than the second.<p>
     * <p>
     * The implementor must ensure that {@code sgn(compare(x, y)) ==
     * -sgn(compare(y, x))} for all {@code x} and {@code y}.  (This
     * implies that {@code compare(x, y)} must throw an exception if and only
     * if {@code compare(y, x)} throws an exception.)<p>
     * <p>
     * The implementor must also ensure that the relation is transitive:
     * {@code ((compare(x, y)>0) && (compare(y, z)>0))} implies
     * {@code compare(x, z)>0}.<p>
     * <p>
     * Finally, the implementor must ensure that {@code compare(x, y)==0}
     * implies that {@code sgn(compare(x, z))==sgn(compare(y, z))} for all
     * {@code z}.<p>
     * <p>
     * It is generally the case, but <i>not</i> strictly required that
     * {@code (compare(x, y)==0) == (x.equals(y))}.  Generally speaking,
     * any comparator that violates this condition should clearly indicate
     * this fact.  The recommended language is "Note: this comparator
     * imposes orderings that are inconsistent with equals."<p>
     * <p>
     * In the foregoing description, the notation
     * {@code sgn(}<i>expression</i>{@code )} designates the mathematical
     * <i>signum</i> function, which is defined to return one of {@code -1},
     * {@code 0}, or {@code 1} according to whether the value of
     * <i>expression</i> is negative, zero, or positive, respectively.
     *
     * @param o1 the first object to be compared.
     * @param o2 the second object to be compared.
     * @return a negative integer, zero, or a positive integer as the
     * first argument is less than, equal to, or greater than the
     * second.
     * @throws NullPointerException if an argument is null and this
     *                              comparator does not permit null arguments
     * @throws ClassCastException   if the arguments' types prevent them from
     *                              being compared by this comparator.
     */
    @Override
    public int compare(Person o1, Person o2) {
        return o1.getL_name().compareTo(o2.getL_name());
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
        Person person = (Person) o;
        return personID.equals(person.personID) &&
                descendant.equals(person.descendant) &&
                firstName.equals(person.firstName) &&
                lastName.equals(person.lastName) &&
                gender.equals(person.gender) &&
                Objects.equals(father, person.father) &&
                Objects.equals(mother, person.mother) &&
                Objects.equals(spouse, person.spouse);
    }

    /**
     * @return hash of Person object
     */
    @Override
    public int hashCode() {
        return Objects.hash(personID, descendant, firstName, lastName, gender, father, mother, spouse);
    }

    public String getID() {
        return personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public String getF_name() {
        return firstName;
    }

    public String getL_name() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getFather() {
        return father;
    }

    public String getMother() {
        return mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }
}

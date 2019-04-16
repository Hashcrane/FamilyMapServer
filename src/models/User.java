package models;

import java.util.Comparator;
import java.util.Objects;
import java.util.UUID;


/**
 * User object for storing information about a User
 */
public class User implements Comparator<User> {
    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
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

    public String getID() {
        return personID;
    }

    public boolean anyNull() {
        if (userName == null) return true;
        if (password == null) return true;
        if (email == null) return true;
        if (firstName == null) return true;
        if (lastName == null) return true;
        if (personID == null) return true;
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
     * Unique user name (non-empty string)
     */
    private final String userName;
    /**
     * User’s password (non-empty string)
     */
    private final String password;
    /**
     * User’s email address (non-empty string)
     */
    private final String email;
    /**
     * User’s first name (non-empty string)
     */
    private final String firstName;
    /**
     * User’s last name (non-empty string)
     */
    private final String lastName;
    /**
     * User’s gender (string: “f” or “m”)
     */
    private final String gender;
    /**
     * User's unique id (non-empty string)
     */
    private final String personID;

    /**
     * @param u Unique user name (non-empty string)
     * @param p User’s password (non-empty string)
     * @param e User’s email address (non-empty string)
     * @param f User’s first name (non-empty string)
     * @param l User’s last name (non-empty string)
     * @param g User’s gender (string: “f” or “m”)
     *          <p>
     *          For Testing Only: Constructs a User object and generates a unique ID for the User
     */

    public User(String u, String p, String e, String f, String l, String g) {
        userName = u;
        password = p;
        email = e;
        firstName = f;
        lastName = l;
        gender = g.toLowerCase();
        personID = UUID.randomUUID().toString();
    }

    /**
     * Create a new User object from a person object
     *
     * @param person   non null person object
     * @param password String non null
     * @param email    String non null
     */
    public User(Person person, String password, String email) {
        userName = person.getDescendant();
        this.password = password;
        this.email = email;
        firstName = person.getF_name();
        lastName = person.getL_name();
        gender = person.getGender().toLowerCase();
        personID = person.getID();
    }

    /**
     * @param u Unique user name (non-empty string)
     * @param p User’s password (non-empty string)
     * @param e User’s email address (non-empty string)
     * @param f User’s first name (non-empty string)
     * @param l User’s last name (non-empty string)
     * @param g User’s gender (string: “f” or “m”)
     *          <p>
     *          Constructs a User object with a current ID for the User
     */
    public User(String u, String p, String e, String f, String l, String g, String id) {
        userName = u;
        password = p;
        email = e;
        firstName = f;
        lastName = l;
        gender = g.toLowerCase();
        personID = id;
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
    public int compare(User o1, User o2) {
        return o1.getUsername().compareTo(o2.getUsername());
    }

    /**
     * @param o object to check equality of
     * @return true if objects are equal
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userName.equals(user.userName) &&
                password.equals(user.password) &&
                email.equals(user.email) &&
                firstName.equals(user.firstName) &&
                lastName.equals(user.lastName) &&
                gender.equals(user.gender) &&
                personID.equals(user.personID);
    }

    /**
     * @return returns hash of User object
     */
    @Override
    public int hashCode() {
        return Objects.hash(userName, password, email, firstName, lastName, gender, personID);
    }
}

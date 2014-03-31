package net.coderodde.multilog.model;

/**
 * This class implements a user type for <tt>multilog</tt>..
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class User {

    /**
     * The ID of this user. Same type as Postgres' BIGSERIAL.
     */
    private long id;

    /**
     * The name of this user.
     */
    private String username;

    /**
     * The user category of this user.
     */
    private UserType userType;

    /**
     * The first name of this user.
     */
    private String firstName;

    /**
     * The last name of this user.
     */
    private String lastName;

    /**
     * The e-mail address of this user.
     */
    private String email;

    /**
     * If set to <code>true</code>, the first and the last names are displayed
     * on profile page.
     */
    private boolean showRealName;

    /**
     * If set to <code>true</code>
     */
    private boolean showEmail;

    /**
     * Returns the ID of this user.
     *
     * @return the ID of this user.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the user name.
     *
     * @return the user name.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the first name of this user.
     *
     * @return the first name of this user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of this user.
     *
     * @return the last name of this user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the email of this user.
     *
     * @return the email of this user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns <code>true</code>, if showing the real name of this user is
     * allowed; <code>false</code> otherwise.
     *
     * @return a boolean value indicating whether showing the real name of this
     * user is allowed.
     */
    public boolean getShowRealName() {
        return showRealName;
    }

    /**
     * Returns <code>true</code>, if showing email of this user is
     * allowed; <code>false</code> otherwise.
     *
     * @return a boolean value indicating whether showing email address of this
     * user is allowed.
     */
    public boolean getShowEmail() {
        return showEmail;
    }

    /**
     * Sets the ID of this user.
     *
     * @param id the ID to set.
     *
     * @return itself for chaining.
     */
    public User setId(final long id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the username of this user.
     *
     * @param username the user name to set.
     *
     * @return itself for chaining.
     */
    public User setUsername(final String username) {
        this.username = username;
        return this;
    }

    /**
     * Sets the first name of this user.
     *
     * @param firstName the first name to set.
     *
     * @return itself for chaining.
     */
    public User setFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Sets the last name of this user.
     *
     * @param lastName the last name to set.
     *
     * @return itself for chaining.
     */
    public User setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Sets the email of this user.
     *
     * @param email the email to set.
     *
     * @return itself for chaining.
     */
    public User setEmail(final String email) {
        this.email = email;
        return this;
    }

    /**
     * Sets the boolean value indicating whether showing the email address of
     * this user is allowed.
     *
     * @param showEmail the boolean flag to set.
     *
     * @return itself for chaining.
     */
    public User setShowEmail(final boolean showEmail) {
        this.showEmail = showEmail;
        return this;
    }

    /**
     * Sets the boolean value indicating whether showing the real name of
     * this user is allowed.
     *
     * @param showRealName the boolean flag to set.
     *
     * @return itself for chaining.
     */
    public User setShowRealName(final boolean showRealName) {
        this.showRealName = showRealName;
        return this;
    }

    /**
     * Syntactic sugar.
     *
     * @return itself.
     */
    public User end() {
        return this;
    }
}

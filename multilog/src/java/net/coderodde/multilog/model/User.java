package net.coderodde.multilog.model;

/**
 * This class implements a user type for <tt>multilog</tt>..
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class User {

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

    public User setUsername(final String username) {
        this.username = username;
        return this;
    }

    public User setFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    public User setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User setEmail(final String email) {
        this.email = email;
        return this;
    }

    public User setShowEmail(final boolean showEmail) {
        this.showEmail = showEmail;
        return this;
    }

    public User setShowRealName(final boolean showRealName) {
        this.showRealName = showRealName;
        return this;
    }

    public User end() {
        return this;
    }
}

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
}

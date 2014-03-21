package net.coderodde.multilog.model;

/**
 * This class implements a user type for <tt>multilog</tt>..
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class User {

    private long id;
    private String username;
    private UserType userType;
    private String password;
    private String salt;
}

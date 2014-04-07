package net.coderodde.multilog.model;

/**
 * This class defines the user categories in <tt>multilog</tt>.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public enum UserType {

    /**
     * The basic user.
     */
    USER,

    /**
     * Moderator.
     */
    MOD,

    /**
     * Administrator.
     */
    ADMIN;

    public static void main(String... args) {

        System.out.println(UserType.valueOf("ADMIN"));
        System.out.println(UserType.valueOf("MOD"));
        System.out.println(UserType.valueOf("USER"));
    }
}

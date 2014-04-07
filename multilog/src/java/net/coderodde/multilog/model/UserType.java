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
    USER("User"),

    /**
     * Moderator.
     */
    MOD("Moderator"),

    /**
     * Administrator.
     */
    ADMIN("Administrator");

    private String text;

    /**
     * Initializes every enumeration constant to the textual names of roles.
     *
     * @param text the textual name of a type.
     */
    private UserType(final String text) {
        this.text = text;
    }

    /**
     * Returns the textual representation of a type.
     *
     * @return the textual representation.
     */
    public String toString() {
        return text;
    }
}

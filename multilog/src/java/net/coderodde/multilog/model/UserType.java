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
    USER("User", "USER"),

    /**
     * Moderator.
     */
    MOD("Moderator", "MOD"),

    /**
     * Administrator.
     */
    ADMIN("Administrator", "ADMIN");

    /**
     * The textual representation of a user type.
     */
    private String text;

    /**
     * The name of a user name as defined in the database.
     */
    private String sqlName;

    /**
     * Initializes every enumeration constant to the textual names of roles.
     *
     * @param text the textual name of a type.
     */
    private UserType(final String text, final String sqlName) {
        this.text = text;
        this.sqlName = sqlName;
    }

    /**
     * Returns the textual representation of a type.
     *
     * @return the textual representation.
     */
    public String toString() {
        return text;
    }

    /**
     * Returns the name of this user type as specified in the DB.
     *
     * @return the SQL name of this type.
     */
    public String getDBName() {
        return sqlName;
    }
}

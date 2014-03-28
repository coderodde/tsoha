package net.coderodde.multilog;

/**
 * This class defines the constants for configuring the system.
 *
 * @author Rodion Efremov
 */
public class Config {

    /**
     * Defines the length of each salt.
     */
    public static final int SALT_LENGTH = 32;

    /**
     * Defines the lookup name for JDBC.
     */
    public static final String DATABASE_LOOKUP_NAME =
            "java:/comp/env/jdbc/mlogDB";

    /**
     * Session related magic data.
     */
    public static final class SESSION_MAGIC {

        /**
         * The key name in a session for user name.
         */
        public static final String USERNAME = "username";

        /**
         * The key name in a session for the password.
         */
        public static final String PASSWORD = "password";
    }

    /**
     * SQL magic constants.
     */
    public static final class SQL_MAGIC {

        /**
         * The template for fetching the user by a name and a password.
         */
        public static final String FETCH_USER_BY_NAME =
                "SELECT * FROM users WHERE usename IS '?';";
    }
}

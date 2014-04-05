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
     * For the sake of better server logs.
     */
    public static final String ERROR_BADGE = "[Error] ";

    /**
     * Session related magic data.
     */
    public static final class SESSION_MAGIC {

        /**
         * The key name in a session for user name. "username".
         */
        public static final String USERNAME = "username";

        /**
         * The key name in a session for the password. "password".
         */
        public static final String PASSWORD = "password";

        /**
         * The key for signed in user. "singed_in".
         */
        public static final String SIGNED_IN_USER_ATTRIBUTE = "signed_in";

        /**
         * The key for IDs passed to different views.
         */
        public static final String ID = "id";
    }

    /**
     * SQL magic constants.
     */
    public static final class SQL_MAGIC {

        /**
         * The template for fetching the user by a name and a password.
         */
        public static final String FETCH_USER_BY_NAME =
                "SELECT * FROM users WHERE username = ?;";

        /**
         * The query to get all available topics.
         */
        public static final String FETCH_ALL_TOPICS =
                "SELECT * FROM topics;";

        /**
         * The template for getting a topic by its ID.
         */
        public static final String FETCH_TOPIC_BY_ID =
                "SELECT * FROM topics WHERE topic_id = ?;";
    }
}

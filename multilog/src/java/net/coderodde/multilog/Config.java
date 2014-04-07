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
     * Defines the minimum username length.
     */
    public static final int MINIMUM_USERNAME_LENGTH = 3;

    /**
     * Defines the maximum username length.
     */
    public static final int MAXIMUM_USERNAME_LENGTH = 25;

    /**
     * Defines the minimum password length.
     */
    public static final int MINIMUM_PASSWORD_LENGTH = 10;


    /**
     * The regular expression for email address validation.
     * Stole this from
     * http://stackoverflow.com/questions/16295329/email-address-validation-regex
     */
    public static final String EMAIL_REGEX =
            "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@" +
            "[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    /**
     * The regular expression for username validation. The primary
     * concern are white-space characters.
     */
    public static final String USERNAME_REGEX =  "^[_A-Za-z0-9]{" +
                                                 MINIMUM_USERNAME_LENGTH +
                                                 "," +
                                                 MAXIMUM_USERNAME_LENGTH +
                                                 "}$";

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
         * The key name in a session for the password.
         */
        public static final String PASSWORD = "password";

        /**
         * The key name in a session for the password confirmation.
         */
        public static final String PASSWORD_CONFIRMATION =
                "password_confirmation";

        /**
         * First name attribute key.
         */
        public static final String FIRST_NAME = "first_name";

        /**
         * Last name attribute key.
         */
        public static final String LAST_NAME = "last_name";

        /**
         * Email attribute key.
         */
        public static final String EMAIL = "email";

        /**
         * Show real name attribute key.
         */
        public static final String SHOW_REAL_NAME = "show_real_name";

        /**
         * Show email attribute key.
         */
        public static final String SHOW_EMAIL = "show_email";

        /**
         * The key for signed in user. "singed_in".
         */
        public static final String SIGNED_IN_USER_ATTRIBUTE = "signed_in";

        /**
         * The key for IDs passed to different views.
         */
        public static final String ID = "id";

        public static final String DESCRIPTION = "description";
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

        /**
         * The template for getting all the threads with a specified topic ID.
         */
        public static final String FETCH_THREADS_BY_TOPIC_ID =
                "SELECT * FROM threads WHERE topic_id = ?;";

        public static final String CREATE_USER =
                "INSERT INTO users (" +
                "user_id," +
                "username," +
                "salt," +
                "passwd_hash," +
                "first_name," +
                "last_name," +
                "email," +
                "description," +
                "created_at," +
                "updated_at) " +
                "VALUES (" +
                "(SELECT max(user_id) from users) + 1," +
                "?," +                                      // user name.
                "?," +                                      // salt.
                "?," +                                      // passwd_hash.
                "?," +                                      // first_name.
                "?," +                                      // last_name.
                "?," +                                      // email.
                "?," +                                      // description.
                "NOW(), NOW());";

    }
}

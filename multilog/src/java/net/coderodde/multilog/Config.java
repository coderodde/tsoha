package net.coderodde.multilog;

import java.util.HashMap;
import java.util.Map;

/**
 * This class defines the constants for configuring the system.
 *
 * @author Rodion Efremov
 * @version 0.1
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
     * Session related magic data.
     */
    public static final class SESSION_MAGIC {

        /**
         * The key name in a session for user name.
         */
        public static final String USERNAME = "username";

        /**
         * The key name for currnet password.
         */
        public static final String CURRENT_PASSWORD = "current_password";

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
         * The key for signed in user.
         */
        public static final String SIGNED_IN_USER_ATTRIBUTE = "signed_in";

        /**
         * The key for IDs passed to different views.
         */
        public static final String ID = "id";

        /**
         * The key for description attribute.
         */
        public static final String DESCRIPTION = "description";
    }

    /**
     * HTML magic.
     */
    public static final class HTML_MAGIC {

        /**
         * HTML-escaped string "hidden".
         */
        public static final String HIDDEN = "&lt;hidden&gt;";
    }

    /**
     * SQL magic constants.
     */
    public static final class SQL_MAGIC {

        /**
         * The template for fetching a user by username.
         */
        public static final String FETCH_USER_BY_NAME =
                "SELECT * FROM users WHERE username = ? AND removed = FALSE;";

        /**
         * The template for fetching a user by ID.
         */
        public static final String FETCH_USER_BY_ID =
                "SELECT * FROM users WHERE user_id = ? AND removed = FALSE;";

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

        /**
         * The template for inserting a new user row into the database.
         */
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
                "(SELECT max(user_id) FROM users) + 1," +
                "?," +                                      // user name.
                "?," +                                      // salt.
                "?," +                                      // passwd_hash.
                "?," +                                      // first_name.
                "?," +                                      // last_name.
                "?," +                                      // email.
                "?," +                                      // description.
                "NOW(), NOW());";

        /**
         * The template for removing a user.
         */
        public static final String REMOVE_USER =
                "UPDATE users SET removed = TRUE WHERE user_id = ?;";

        /**
         * The template for updating a user.
         */
        public static final String UPDATE_USER =
                "UPDATE users SET " +
                "first_name = ?, " +        // first name.
                "last_name = ?, " +         // last_name.
                "email = ?, " +             // email.
                "show_real_name = ?, " +    // show real name.
                "show_email = ?, " +        // show email.
                "description = ?, " +       // description.
                "updated_at = NOW() " +     // Update the timestamp.
                "WHERE user_id = ?;";       // user ID.

        /**
         * The template for changing a user's password.
         */
        public static final String CHANGE_PASSWORD =
                "UPDATE users SET salt = ?, " +
                                 "passwd_hash = ?, " +
                                 "updated_at = NOW() WHERE user_id = ?;";

        /**
         * The template for fetching a particular thread.
         */
        public static final String FETCH_THREAD =
                "SELECT * FROM threads WHERE thread_id = ?;";

        /**
         * The template for fetching all posts belonging to a thread.
         */
        public static final String FETCH_ALL_POSTS_OF_A_THREAD =
                "SELECT * FROM posts WHERE thread_id = ? " +
                "ORDER BY created_at ASC;";

        /**
         * The template for fetching a post by its ID.
         */
        public static final String FETCH_POST_BY_ID =
                "SELECT * FROM posts WHERE post_id = ?;";

        /**
         * The template for creating a new post.
         */
        public static final String CREATE_NEW_POST =
                "INSERT INTO posts (" +
                "post_id, " +
                "thread_id, " +
                "user_id, " +
                "post_text, " +
                "parent_post, " +
                "created_at, " +
                "updated_at) " +
                "VALUES ((SELECT max(post_id) FROM posts) + 1, " +
                "?, " +
                "?, " +
                "?, " +
                "?, " +
                "NOW(), " +
                "NOW());";

        /**
         * The template for updating a post.
         */
        public static final String UPDATE_POST =
                "UPDATE posts SET " +
                "post_text = ?, " +
                "updated_at = NOW() " +
                "WHERE post_id = ?;";

        /**
         * The template for creating a post read entry.
         */
        public static final String CREATE_POST_READ =
                "INSERT INTO message_reads (user_id, post_id) VALUES (?, ?);";

        /**
         * The template for fetching all the message reads of a user.
         */
        public static final String FETCH_MESSAGE_READS_OF_USER =
                "SELECT * FROM message_reads WHERE user_id = ?;";

        /**
         * The template for deleting all message reads of post.
         */
        public static final String REMOVE_MESSAGE_READS_OF_POST =
                "DELETE FROM message_reads WHERE post_id = ?;";

        /**
         * The template for updating a thread's update timestamp.
         */
        public static final String UPDATE_THREAD_TIMESTAMP =
                "UPDATE threads SET updated_at = NOW() WHERE thread_id = ?;";

        /**
         * The template for updating a topic's update timestamp.
         */
        public static final String UPDATE_TOPIC_TIMESTAMP =
                "UPDATE topics SET updated_at = NOW() WHERE topic_id = ?;";
    }

    /**
     * The markup data.
     */
    public static final class MARK_UP {

        /**
         * The token for bold font.
         */
        public static final char BOLD = '*';

        /**
         * The token for italicized font.
         */
        public static final char ITALIC = '_';

        /**
         * The token for monospaced font.
         */
        public static final char MONO = '#';

        /**
         * The token for beginning an URL.
         */
        public static final char URL_BEGIN = '[';

        /**
         * The token for ending an URL.
         */
        public static final char URL_END = ']';

        /**
         * The token to separating URL and label. (Not required."
         */
        public static final char SEPARATOR = '|';

        /**
         * The token for escaping itself and markup tokens.
         */
        public static final char ESCAPE = '\\'; // backslash.

        /**
         * Used by markup algorithm.
         */
        public static final Map<Character, String> map;

        /**
         * The HTML for closing the span-tags.
         */
        public static final String HTML_CLOSE_MARKUP = "</span>";

        /**
         * The HTML for closing the link.
         */
        public static final String HTML_CLOSE_URL = "</a>";

        static {
            map = new HashMap<Character, String>();

            map.put(BOLD,   "<span class=\"mk_bold\">");
            map.put(ITALIC, "<span class=\"mk_italic\">");
            map.put(MONO,   "<span class=\"mk_mono\">");
        }
    }
}

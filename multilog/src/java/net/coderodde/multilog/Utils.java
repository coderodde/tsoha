package net.coderodde.multilog;

import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.coderodde.multilog.controller.HomeServlet;
import net.coderodde.multilog.model.DB;
import net.coderodde.multilog.model.User;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * This class provides utilities for multilog.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class Utils {

    /**
     * The regular expression pattern for matching valid usernames.
     */
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile(Config.USERNAME_REGEX);

    /**
     * The regular expression pattern for matching valid email addresses.
     */
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(Config.EMAIL_REGEX);

    /**
     * The random machinery.
     */
    private static final Random random = new Random();

    /**
     * Maps an integer to a character. Used for generating password salts.
     */
    private static final Map<Integer, Character> map =
            new HashMap<Integer, Character>();

    /**
     * A singleton for the sake of message digest machinery.
     */
    private static final Utils utils = new Utils();

    static {
        loadMap();
    }

    /**
     * The digest implementation.
     */
    private MessageDigest md;

    /**
     * Constructs the message digest machinery.
     */
    private Utils() {
        try {
            this.md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace(System.err);
        }
    }

    /**
     * Returns the utility object.
     *
     * @return the utility object.
     */
    public static final Utils getUtils() {
        return utils;
    }

    /**
     * This static method returns the current signed in user if there is one.
     *
     * @param request the servlet request object.
     *
     * @return the user who is currently having a session, <code>null</code> if
     * there is no such.
     */
    public static final User getSignedUser(final HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return null;
        }

        return (User) session.getAttribute(Config.
                                           SESSION_MAGIC.
                                           SIGNED_IN_USER_ATTRIBUTE);
    }

    /**
     * Checks the validity of a username.
     *
     * @param username the username to check.
     *
     * @return <code>true</code> if username is valid, <code>false</code>
     * otherwise.
     */
    public static final boolean isValidUsername(final String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    /**
     * Checks the validity of an email address.
     *
     * @param email the email address to check.
     *
     * @return <code>true</code> if the email address is valid,
     * <code>false</code> otherwise.
     */
    public static final boolean isValidEmail(final String email) {
        if (email == null) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Checks the validity of a password.
     *
     * @param password the password to check.
     *
     * @return <code>true</code> if the password is valid, <code>false</code>
     * otherwise.
     */
    public static final boolean isValidPassword(final String password) {
        if (password.length() < Config.MINIMUM_PASSWORD_LENGTH) {
            return false;
        }

        char[] passwd = password.toCharArray();

        int lowercaseLetters = 0;
        int uppercaseLetters = 0;
        int digitCount = 0;

        for (char c : passwd) {
            if (Character.isWhitespace(c)) {
                return false;
            }

            if (Character.isLowerCase(c)) {
                ++lowercaseLetters;
            } else if (Character.isUpperCase(c)) {
                ++uppercaseLetters;
            } else if (Character.isDigit(c)) {
                ++digitCount;
            }
        }

        return uppercaseLetters > 0
                && lowercaseLetters > 2
                && digitCount > 1;
    }

    /**
     * Closes the JDBC - related resources.
     *
     * @param connection the connection to close.
     * @param statement the statement to close.
     * @param resultSet the result set to close.
     */
    public static final void closeResources(final Connection connection,
                                            final Statement statement,
                                            final ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException sqle) {

            }
        }

        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException sqle) {

            }
        }

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException sqle) {

            }
        }
    }

    /**
     * Creates a random password salt.
     *
     * @return a random password salt.
     */
    public static final String createSalt() {
        char[] saltChars = new char[Config.SALT_LENGTH];

        for (int i = 0; i != saltChars.length; ++i) {
            saltChars[i] = map.get(random.nextInt(map.size()));
        }

        return new String(saltChars);
    }

    public static final void prepareNavibar(final HttpServletRequest request) {
        final User user = User.getCurrentlySignedUser(request);

        if (user != null) {
            HomeServlet.prepareNavibarForSingedUser(request, user);
        } else {
            HomeServlet.prepareNavibarForUnsignedUser(request);
        }
    }

    /**
     * Returns the message digest implementation.
     *
     * @return the message digest implementation.
     */
    public final MessageDigest getMessageDigest() {
        return md;
    }

    public static final class Pair<F, S> {
        private final F first;
        private final S second;

        public Pair(F first, S second) {
            this.first = first;
            this.second = second;
        }

        public final F getFirst() {
            return first;
        }

        public final S getSecond() {
            return second;
        }
    }

    public static final Pair<Map<String, String>, FileItem> getFormFields
            (final ServletFileUpload upload, final HttpServletRequest request) {
        Map<String, String> map = new TreeMap<String, String>();
        List<FileItem> fileItems = null;

        try {
            fileItems = upload.parseRequest(request);
        } catch (FileUploadException fue) {
            fue.printStackTrace(System.err);
            return null;
        }

        FileItem avatarFileItem = null;

        for (final FileItem item : fileItems) {
            if (item.isFormField()) {
                map.put(item.getFieldName(), item.getString());
            } else if (avatarFileItem == null) {
                avatarFileItem = item;
            }
        }

        return new Pair<Map<String, String>, FileItem>(map, avatarFileItem);
    }

    /**
     * Attempts to upload the avatar represented by <code>item</code> for the
     * user <code>user</code>.
     *
     * @param item the file item holding the avatar.
     * @param user the user to set the avatar for.
     *
     * @return <code>true</code> upon success, <code>false</code> otherwise.
     */
    public static final boolean processAvatar
            (final FileItem item, final User user) {
        if (item.isFormField()) {
            throw new IllegalArgumentException(
                    "Form field should not get here.");
        }

        InputStream is = null;
        byte[] bytes = item.get();
        Connection connection = DB.getConnection();

        if (connection == null) {
            return false;
        }

        PreparedStatement ps = DB.getPreparedStatement(connection,
                                                       Config.
                                                       SQL_MAGIC.
                                                       SAVE_AVATAR);

        if (ps == null) {
            closeResources(connection, null, null);
            return false;
        }

        try {
            ps.setBytes(1, bytes);
            ps.setString(2, user.getUsername());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, null);
            return false;
        }

        closeResources(connection, ps, null);
        return true;
    }

    /**
     * Loads the map used in salt generation.
     */
    private static final void loadMap() {
        char c = '0';
        int i = 0;

        for (; i != 10; ++i, ++c) {
            map.put(i, c);
        }

        c = 'A';

        for (; i != 36; ++i, ++c) {
            map.put(i, c);
        }

        c = 'a';

        for (; i != 62; ++i, ++c) {
            map.put(i, c);
        }
    }
}

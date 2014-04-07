package net.coderodde.multilog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import net.coderodde.multilog.model.User;

/**
 * This class provides utilities for multilog.
 *
 * @author Rodion Efremov
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
     * The digest implementation.
     */
    private MessageDigest md;

    private static final Utils utils = new Utils();

    public Utils() {
        try {
            this.md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace(System.err);
        }
    }

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

    public static final boolean isValidUsername(final String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }

    public static final boolean isValidEmail(final String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }

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

    public final static byte[] generateRandomSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[Config.SALT_LENGTH];
        r.nextBytes(salt);
        return salt;
    }

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

    public final MessageDigest getMessageDigest() {
        return md;
    }
}

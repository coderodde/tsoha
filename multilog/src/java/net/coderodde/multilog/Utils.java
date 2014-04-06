package net.coderodde.multilog;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
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
}

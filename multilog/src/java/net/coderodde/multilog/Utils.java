package net.coderodde.multilog;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

/**
 * This class provides utilities for multilog.
 *
 * @author Rodion Efremov
 */
public class Utils {

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

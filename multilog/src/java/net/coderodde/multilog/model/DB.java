package net.coderodde.multilog.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.coderodde.multilog.Config;

/**
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class DB {

    /**
     * This static method attempts to establish a connection to the database
     * and, upon success, returns it.
     *
     * @return the connection to the database.
     * @throws NamingException
     * @throws SQLException
     */
    public static final Connection getConnection() throws NamingException,
                                                          SQLException {
        return ((DataSource) new InitialContext()
                 .lookup(Config.DATABASE_LOOKUP_NAME)).getConnection();
    }

    /**
     * Retrieves a user from a database by user name and password.
     *
     * @param username the user name.
     * @param password the password.
     *
     * @return return the user object, or <code>null</code> if the query did not
     * return any rows.
     *
     * @throws NamingException
     * @throws SQLException
     */
    public static final User getUser(final String username,
                                     final String password)
            throws NamingException, SQLException {
        Connection conn = getConnection();
        PreparedStatement ps =
                conn.prepareStatement(Config.SQL_MAGIC.FETCH_USER_BY_NAME);

        // Setting up query.
        ps.setString(1, username);

        // Querying and wrapping.
        return loadUser(ps.executeQuery());
    }

    /**
     * Loads the user object from the result set. (Assumes that there were no
     * <code>next()</code> calls upon the result set.)
     *
     * @param rs the result set.
     *
     * @return the user object, or <code>null</code> if no matches.
     */
    private static final User loadUser(ResultSet rs) throws SQLException {
        if (rs.next() == false) {
            // No luck for the query.
            return null;
        }

        return new User();
    }
}

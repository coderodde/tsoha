package net.coderodde.multilog.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.coderodde.multilog.Config;

/**
 * This class defines the abstraction layer upon a database.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class DB {

    /**
     * This static method attempts to establish a connection to the database
     * and, upon success, returns it. Don't forget to close it.
     *
     * @return the connection to the database.
     *
     * @throws NamingException
     * @throws SQLException
     */
    public static final Connection getConnection() {
        try {
            return ((DataSource) new InitialContext()
                     .lookup(Config.DATABASE_LOOKUP_NAME)).getConnection();
        } catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * Creates and returns a statement object from a connection.
     *
     * @param connection the connection to ask for a statement.
     *
     * @return a statement object, if everything is OK;
     * <code>null</code> otherwise.
     */
    public static final Statement getStatement(final Connection connection) {
        try {
            return connection.createStatement();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * Creates and returns a prepared statement from a connection.
     *
     * @param connection the connection to ask for a prepared statement.
     * @param sql the SQL-template to initialize.
     *
     * @return a prepared statement object, if everything is OK;
     * <code>null</code> otherwise.
     */
    public static final PreparedStatement getPreparedStatement
            (final Connection connection, final String sql) {
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
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

        return new User().setId(rs.getLong("user_id"))
                         .setEmail(rs.getString("email"))
                         .setUsername(rs.getString("username"))
                         .setLastName(rs.getString("last_name"))
                         .setFirstName(rs.getString("first_name"))
                         .setShowEmail(rs.getBoolean("show_email"))
                         .setShowRealName(rs.getBoolean("show_real_name"))
                         .end();
    }
}

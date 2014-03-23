package net.coderodde.multilog.model;

import java.sql.Connection;
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
}

package net.coderodde.multilog.model;

import java.io.Closeable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
     * A sentinel value returned on wrong password.
     */
    public static final User BAD_PASSWORD_USER = new User();

    /**
     * The database abstraction object as a singleton.
     */
    private static final DB db = new DB();

    /**
     * The digest implementation.
     */
    private MessageDigest md;

    /**
     * Returns the database abstraction object.
     *
     * @return the database abstraction object.
     */
    public static final DB getDatabase() {
        return db;
    }

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

    /**
     * Constructs the database abstraction layer.
     *
     * @throws NoSuchAlgorithmException in case there is no support for
     * SHA-256 - algorithm (should not happen).
     */
    private DB() {
        try {
            this.md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace(System.err);
        }
    }

    /**
     * Checks user's password and returns <code>true</code> upon successful
     * authentication, <code>false</code> otherwise. rodde stole it from
     * @see <a href="">http://howtodoinjava.com/2013/07/22/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/</a>
     *
     * @param rs the result set containing the user row.
     *
     * @return <code>true</code> upon successful authentication,
     * <code>false</code> otherwise.
     */
    private final boolean checkPassword(final String passwordText,
                                        final String passwordSalt,
                                        final String passwordHash) {
        String toHash = passwordText + passwordSalt;
        byte[] bytes = md.digest(toHash.getBytes());
        StringBuilder sb = new StringBuilder(1000);

        // Stealing this from
        for (int i = 0; i < bytes.length; ++i) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100,
                                       16).substring(1));
        }

        String resultHash = sb.toString();
        return resultHash.equals(passwordHash);
    }

    /**
     * Retrieves a user from a database by user name and password.
     *
     * @param username the user name.
     * @param password the password.
     *
     * @return returns the user object, or <code>null</code> if the query did
     * not return any rows. (In case the username exists, but the password
     * failed, returns <code>BAD_PASSWORD_USER</code>.)
     *
     * @throws NamingException
     * @throws SQLException
     */
    public final User getUser(final String username, final String password)
            throws NamingException, SQLException {
        Connection conn = getConnection();
        PreparedStatement ps =
                conn.prepareStatement(Config.SQL_MAGIC.FETCH_USER_BY_NAME);

        // Setting up query.
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        if (rs.next() == false) {
            // No rows with user name 'username'.
            rs.close();
            ps.close();
            conn.close();
            return null;
        }

        if (checkPassword(password,
                          rs.getString("salt"),
                          rs.getString("passwd_hash")) == false) {
            rs.close();
            ps.close();
            conn.close();
            return BAD_PASSWORD_USER;
        }

        // Querying and wrapping.
        User user = loadUser(ps.executeQuery());

        // Closing resources.
        rs.close();
        ps.close();
        conn.close();

        // Returning the signed in user.
        return user;
    }
}

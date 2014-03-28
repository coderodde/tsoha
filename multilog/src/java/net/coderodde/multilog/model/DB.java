package net.coderodde.multilog.model;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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
     * The digest implementation.
     */
    private final MessageDigest md;

    /**
     * Used for converting hexadecimals to binary representation.
     */
    private final Map<Character, Byte> map;

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

    public DB() throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance("SHA-256");
        this.map = new HashMap<Character, Byte>();
        preloadMap();
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
            return null;
        }

//        if (checkPassword(rs, password) == false) {
//            return BAD_PASSWORD_USER;
//        }

        // Querying and wrapping.
        return loadUser(ps.executeQuery());
    }

    /**
     * Checks user's password and returns <code>true</code> upon successful
     * authentication, <code>false</code> otherwise.
     *
     * @param rs the result set containing the user row.
     *
     * @return <code>true</code> upon successful authentication,
     * <code>false</code> otherwise.
     */
    private final boolean checkPassword(final String passwordText,
                                        final String passwordSalt,
                                        final String passwordHash)
    throws UnsupportedEncodingException {
        String toHash = passwordText + passwordSalt;
        byte[] bytes = md.digest(toHash.getBytes());
        StringBuilder sb = new StringBuilder(1000);

        for (int i = 0; i < bytes.length; ++i) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        String fun = sb.toString();

        System.out.println("Fun: " + fun);

        return false;
    }

    private final byte[] getHashByteArray(String hash) {
        hash = hash.toLowerCase();

        if ((hash.length() & 1) == 1) {
            // Bad format.
            return null;
        }

        // Divide by 2.
        byte[] ret = new byte[hash.length() >>> 1];

        for (int i = 0; i < hash.length(); i += 2) {
            char c1 = hash.charAt(i);
            char c2 = hash.charAt(i + 1);

            if ((isDigit(c1) == false)
                    && (isLowerCaseEnglishLetter(c1) == false)) {
                return null;
            }

            if ((isDigit(c2) == false)
                    && (isLowerCaseEnglishLetter(c2) == false)) {
                return null;
            }

            byte b1 = map.get(c1);
            byte b2 = map.get(c2);

            ret[i >>> 1] = (byte)((b1 << 4) | b2);
        }

        return ret;
    }

    private static boolean isDigit(final char c) {
        return (c >= '0') && (c <= '9');
    }

    private static boolean isLowerCaseEnglishLetter(final char c) {
        return (c >= 'a') && (c <= 'z');
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

        return new User().setEmail(rs.getString("email"))
                         .setUsername(rs.getString("username"))
                         .setLastName(rs.getString("last_name"))
                         .setFirstName(rs.getString("first_name"))
                         .setShowEmail(rs.getBoolean("show_email"))
                         .setShowRealName(rs.getBoolean("show_real_name"));
    }

    private void preloadMap() {
        char c = '0';

        for (byte i = 0; i < 10; ++i, ++c) {
            map.put(c, i);
        }

        c = 'a';

        for (byte i = 10; i < 16; ++i, ++c) {
            map.put(c, i);
        }
    }

    public static void main(String... args) throws Exception {
        DB db = new DB();
        db.checkPassword("ExploringMu17110g",
                         "abcdefghhgfedcbaabcdefghhgfedcba",
                         "f13dc67158d24876c62ff4dd3694c138a12f0e34f0999b01eb8f8764ab8f00c0");
    }
}

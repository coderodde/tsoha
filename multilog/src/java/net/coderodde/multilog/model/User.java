package net.coderodde.multilog.model;

import net.coderodde.multilog.Utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import net.coderodde.multilog.Config;
import static net.coderodde.multilog.Utils.closeResources;

/**
 * This class implements a user type for <tt>multilog</tt>..
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class User {

    public static final User BAD_PASSWORD_USER = new User();

    /**
     * The ID of this user. Same type as Postgres' BIGSERIAL.
     */
    private long id;

    /**
     * The name of this user.
     */
    private String username;

    /**
     * The user category of this user.
     */
    private UserType userType;

    /**
     * The first name of this user.
     */
    private String firstName;

    /**
     * The last name of this user.
     */
    private String lastName;

    /**
     * The e-mail address of this user.
     */
    private String email;

    /**
     * If set to <code>true</code>, the first and the last names are displayed
     * on profile page.
     */
    private boolean showRealName;

    /**
     * If set to <code>true</code>, the email is displayed on profile page.
     */
    private boolean showEmail;

    /**
     * The free-form description of a user.
     */
    private String description;

    /**
     * The password text.
     */
    private String password;

    /**
     * The user's salt;
     */
    private String salt;

    /**
     * The user's hash through SHA256 of password and salt.
     */
    private String hash;

    /**
     * The user's privilege type.
     */
    private UserType type;

    /**
     * The user's creation timestamp.
     */
    private Timestamp createdAt;

    /**
     * The user's update timestamp.
     */
    private Timestamp updatedAt;

    public static final User getCurrentlySignedUser
            (final HttpServletRequest request) {
       return (User) request.getSession()
                            .getAttribute(Config.
                                          SESSION_MAGIC.
                                          SIGNED_IN_USER_ATTRIBUTE);
    }

    /**
     * Returns the ID of this user.
     *
     * @return the ID of this user.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the user name.
     *
     * @return the user name.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the first name of this user.
     *
     * @return the first name of this user.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of this user.
     *
     * @return the last name of this user.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Returns the email of this user.
     *
     * @return the email of this user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns <code>true</code>, if showing the real name of this user is
     * allowed; <code>false</code> otherwise.
     *
     * @return a boolean value indicating whether showing the real name of this
     * user is allowed.
     */
    public boolean getShowRealName() {
        return showRealName;
    }

    /**
     * Returns <code>true</code>, if showing email of this user is
     * allowed; <code>false</code> otherwise.
     *
     * @return a boolean value indicating whether showing email address of this
     * user is allowed.
     */
    public boolean getShowEmail() {
        return showEmail;
    }

    /**
     * Returns the description of this user.
     *
     * @return the description of this user.
     */
    public String getDescription() {
        return description;
    }

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }

    public String getHash() {
        return hash;
    }

    public UserType getUserType() {
        return type;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Sets the ID of this user.
     *
     * @param id the ID to set.
     *
     * @return itself for chaining.
     */
    public User setId(final long id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the username of this user.
     *
     * @param username the user name to set.
     *
     * @return itself for chaining.
     */
    public User setUsername(final String username) {
        this.username = username;
        return this;
    }

    /**
     * Sets the first name of this user.
     *
     * @param firstName the first name to set.
     *
     * @return itself for chaining.
     */
    public User setFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    /**
     * Sets the last name of this user.
     *
     * @param lastName the last name to set.
     *
     * @return itself for chaining.
     */
    public User setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    /**
     * Sets the email of this user.
     *
     * @param email the email to set.
     *
     * @return itself for chaining.
     */
    public User setEmail(final String email) {
        this.email = email;
        return this;
    }

    /**
     * Sets the boolean value indicating whether showing the email address of
     * this user is allowed.
     *
     * @param showEmail the boolean flag to set.
     *
     * @return itself for chaining.
     */
    public User setShowEmail(final boolean showEmail) {
        this.showEmail = showEmail;
        return this;
    }

    /**
     * Sets the boolean value indicating whether showing the real name of
     * this user is allowed.
     *
     * @param showRealName the boolean flag to set.
     *
     * @return itself for chaining.
     */
    public User setShowRealName(final boolean showRealName) {
        this.showRealName = showRealName;
        return this;
    }

    /**
     * Sets the description for this user.
     *
     * @param description the description text.
     *
     * @return itself for chaining.
     */
    public User setDescription(final String description) {
        this.description = description;
        return this;
    }

    public User setPassword(final String password) {
        this.password = password;
        return this;
    }

    public User setSalt(final String salt) {
        this.salt = salt;
        return this;
    }

    public User setHash(final String hash) {
        this.hash = hash;
        return this;
    }

    public User setUserType(final UserType userType) {
        this.userType = userType;
        return this;
    }

    public User setCreatedAt(final Timestamp createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public User setUpdatedAt(final Timestamp updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Syntactic sugar.
     *
     * @return itself.
     */
    public User end() {
        return this;
    }

    public boolean create() {
        Connection connection = DB.getConnection();

        if (connection == null) {
            return false;
        }

        PreparedStatement ps = DB.getPreparedStatement(connection,
                                                       Config.
                                                       SQL_MAGIC.
                                                       CREATE_USER);

        if (ps == null) {
            closeResources(connection, null, null);
            return false;
        }

        ResultSet rs = null;

        final String passwordHash = getHash(getPassword(), getSalt());

        try {
            ps.setString(1, getUsername());
            ps.setString(2, getSalt());
            ps.setString(3, passwordHash);
            ps.setString(4, getFirstName());
            ps.setString(5, getLastName());
            ps.setString(6, getEmail());
            ps.setString(7, description);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, rs);
            return false;
        }

        closeResources(connection, ps, rs);
        return true;
    }

    /**
     * Checks whether this user has valid password.
     *
     * @return <code>true</code> if the password is valid, <code>false</code>
     * otherwise.
     */
    public boolean currentPasswordIsValid() {
        final String password = getPassword();
        final String salt = getSalt();

        if (password == null || salt == null) {
            return false;
        }

        return getHash(password, salt).equals(this.getHash());
    }

    static final String getHash(final String password,
                                        final String salt) {
        final String toHash = password + salt;
        final byte[] bytes = Utils.getUtils()
                                  .getMessageDigest()
                                  .digest(toHash.getBytes());
        final StringBuilder sb = new StringBuilder(64);

        for (int i = 0; i != bytes.length; ++i) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16)
                             .substring(1));
        }

        return sb.toString();
    }

    public static final User read(final String username) {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return null;
        }

        PreparedStatement ps = DB.getPreparedStatement(conn,
                                                       Config.
                                                       SQL_MAGIC.
                                                       FETCH_USER_BY_NAME);

        if (ps == null) {
            closeResources(conn, null, null);
            return null;
        }

        ResultSet rs = null;

        try {
            ps.setString(1, username);
            rs = ps.executeQuery();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(conn, ps, rs);
            return null;
        }

        User user = extractUser(rs);
        closeResources(conn, ps, rs);
        return user;
    }

    public static final User read(final long id) {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return null;
        }

        PreparedStatement ps = DB.getPreparedStatement(conn,
                                                       Config.
                                                       SQL_MAGIC.
                                                       FETCH_USER_BY_ID);

        if (ps == null) {
            closeResources(conn, null, null);
            return null;
        }

        ResultSet rs = null;

        try {
            ps.setLong(1, id);
            rs = ps.executeQuery();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(conn, ps, rs);
            return null;
        }

        User user = extractUser(rs);
        closeResources(conn, ps, rs);
        return user;
    }
    private static final User extractUser(ResultSet rs) {
        try {
            if (rs.next() == false) {
                return null;
            }

            return new User().setId(rs.getLong("user_id"))
                             .setUsername(rs.getString("username"))
                             .setSalt(rs.getString("salt"))
                             .setHash(rs.getString("passwd_hash"))
                             .setFirstName(rs.getString("first_name"))
                             .setLastName(rs.getString("last_name"))
                             .setEmail(rs.getString("email"))
                             .setShowRealName(rs.getBoolean("show_real_name"))
                             .setShowEmail(rs.getBoolean("show_email"))
                             .setDescription(rs.getString("description"))
                             .setUserType(UserType.valueOf
                                               (rs.getString("user_type")))
                             .setCreatedAt(rs.getTimestamp("created_at"))
                             .setUpdatedAt(rs.getTimestamp("updated_at"));
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }
}

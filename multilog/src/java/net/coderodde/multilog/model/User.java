package net.coderodde.multilog.model;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.coderodde.multilog.Config;
import net.coderodde.multilog.Utils;
import static net.coderodde.multilog.Utils.closeResources;

/**
 * This class implements a user type for <tt>multilog</tt>..
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class User {

    /**
     * A sentinel object.
     */
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
     * The privilege category of this user.
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
     * The user's creation timestamp.
     */
    private Timestamp createdAt;

    /**
     * The user's update timestamp.
     */
    private Timestamp updatedAt;

    /**
     * The timestamp at which this user is not banned.
     */
    private Timestamp bannedUntil;

    /**
     * Returns a currently signed in user, or <code>null</code> if there is no
     * such.
     *
     * @param request the request object.
     *
     * @return a currently signed use, or <code>null</code> if there is no such.
     */
    public static final User getCurrentlySignedUser
            (final HttpServletRequest request) {
       return (User) request.getSession()
                            .getAttribute(Config.
                                          SESSION_MAGIC.
                                          SIGNED_IN_USER_ATTRIBUTE);
    }

    /**
     * Signs out a currently signed in user.
     *
     * @param request the servlet request.
     */
    public static final void signout(final HttpServletRequest request) {
        request.getSession()
               .removeAttribute(Config.SESSION_MAGIC.SIGNED_IN_USER_ATTRIBUTE);
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

    /**
     * Returns the password of this user.
     *
     * @return the password of this user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the password salt of this user.
     *
     * @return the password salt of this user.
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Returns the password hash of this user.
     *
     * @return the password hash of this user.
     */
    public String getHash() {
        return hash;
    }

    /**
     * Returns the user type of this user.
     *
     * @return the user type of this user.
     */
    public UserType getUserType() {
        return userType;
    }

    /**
     * Returns the creation timestamp of this user.
     *
     * @return the creation timestamp of this user.
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the update timestamp of this user.
     *
     * @return the update timestamp of this user.
     */
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns the timestamp at which this user is no longer banned.
     *
     * @return the timestamp or <code>null</code> if there is no such.
     */
    public Timestamp getBannedUntil() {
        return bannedUntil;
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

    /**
     * Sets the password of this user.
     *
     * @param password the password to set.
     *
     * @return itself for chaining.
     */
    public User setPassword(final String password) {
        this.password = password;
        return this;
    }

    /**
     * Sets the password salt for this user.
     *
     * @param salt the password salt to set.
     *
     * @return itself for chaining.
     */
    public User setSalt(final String salt) {
        this.salt = salt;
        return this;
    }

    /**
     * Sets the password hash for this user.
     *
     * @param hash the password hash to set.
     *
     * @return itself for chaining.
     */
    public User setHash(final String hash) {
        this.hash = hash;
        return this;
    }

    /**
     * Sets the user type for this user.
     *
     * @param userType the user type to set.
     *
     * @return itself for chaining.
     */
    public User setUserType(final UserType userType) {
        this.userType = userType;
        return this;
    }

    /**
     * Sets the creation timestamp for this user.
     *
     * @param createdAt the timestamp to set.
     *
     * @return itself for chaining.
     */
    public User setCreatedAt(final Timestamp createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Sets the update timestamp for this user.
     *
     * @param updatedAt the timestamp to set.
     *
     * @return itself for chaining.
     */
    public User setUpdatedAt(final Timestamp updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Sets the timestamp at which this user is no longer time.
     *
     * @param bannedUntil the timestamp.
     *
     * @return this for chaining.
     */
    public User setBannedUntil(final Timestamp bannedUntil) {
        this.bannedUntil = bannedUntil;
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

    /**
     * Tries to persist this user into the database.
     *
     * @return <code>true</code> if persisting succeeded, <code>false</code>
     * otherwise.
     */
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

        final String passwordHash = getHash(getPassword(), getSalt());

        try {
            ps.setString(1, getUsername());
            ps.setString(2, getSalt());
            ps.setString(3, passwordHash);
            ps.setString(4, getFirstName());
            ps.setString(5, getLastName());
            ps.setString(6, getEmail());
            ps.setString(7, getDescription());
            ps.setBoolean(8, getShowRealName());
            ps.setBoolean(9, getShowEmail());
            ps.executeUpdate();
            closeResources(connection, ps, null);
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, null);
            return false;
        }
    }

    private static final long extractId(final ResultSet rs) {
        try {
            if (rs.next() == false) {
                return -1L;
            }

            long id = rs.getLong("user_id");

            if (rs.next() == true) {
                // Current username is not unique.
                return -2L;
            }

            return id;
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return -1L;
        }
    }

    /**
     * This shit is not used (yet?).
     * @param is
     * @return
     */
    public final boolean addAvatar(final InputStream is) {
        if (is == null) {
            return false;
        }

        Connection connection = DB.getConnection();

        if (connection == null) {
            return false;
        }

        PreparedStatement ps = DB.getPreparedStatement(connection,
                                                       Config.
                                                       SQL_MAGIC.
                                                       ADD_AVATAR);
        if (ps == null) {
            closeResources(connection, null, null);
            return false;
        }

        try {
            ps.setBinaryStream(1, is);
            ps.setLong(2, getId());
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

    /**
     * Marks (in database) all the posts in the list as read.
     *
     * @param posts the posts to mark.
     */
    public void addMessageReads(List<Post> posts) {
        for (Post p : posts) {
            new MessageRead().setPostId(p.getId())
                             .setUserId(getId())
                             .create();
        }
    }

    /**
     * Checks whether two object equal.
     *
     * @param o the object to compare against.
     *
     * @return <code>true</code> if the two object equal, <code>false</code>
     * otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof User)) {
            return false;
        }

        return getId() == ((User) o).getId();
    }

    /**
     * Returns the hash of a string resulted from appending salt to the
     * password.
     *
     * @param password the password.
     * @param salt the password salt.
     *
     * @return the hash as a string.
     */
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

    /**
     * Loads a user by username.
     *
     * @param username the username of a desired user.
     *
     * @return a user object, or <code>null</code> if something fails.
     */
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

    /**
     * Updates this user in the database.
     *
     * @return <code>true</code> upon success, <code>false</code> otherwise.
     */
    public boolean update() {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return false;
        }

        PreparedStatement ps = DB.getPreparedStatement(conn,
                                                       Config.
                                                       SQL_MAGIC.
                                                       UPDATE_USER);
        if (ps == null) {
            closeResources(conn, null, null);
            return false;
        }

        try {
            ps.setString(1, getFirstName());
            ps.setString(2, getLastName());
            ps.setString(3, getEmail());
            ps.setBoolean(4, getShowRealName());
            ps.setBoolean(5, getShowEmail());
            ps.setString(6, getDescription());
//            ps.setObject(7, getUserType());
            ps.setString(7, getUserType().getDBName());
            ps.setLong(8, getId());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(conn, ps, null);
            return false;
        }

        closeResources(conn, ps, null);
        return true;
    }

    /**
     * Changes the password of this user in the database.
     *
     * @param newPassword new password.
     *
     * @return <code>true</code> upon success, <code>false</code> otherwise.
     */
    public boolean changePassword(final String newPassword) {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return false;
        }

        PreparedStatement ps = DB.getPreparedStatement(conn,
                                                       Config.
                                                       SQL_MAGIC.
                                                       CHANGE_PASSWORD);

        if (ps == null) {
            closeResources(conn, null, null);
            return false;
        }

        final String newSalt = Utils.createSalt();
        final String newHash = User.getHash(newPassword, newSalt);

        try {
            ps.setString(1, newSalt);
            ps.setString(2, newHash);
            ps.setLong(3, getId());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(conn, ps, null);
            return false;
        }

        closeResources(conn, ps, null);
        setSalt(newSalt);
        setHash(newHash);
        return true;
    }

    /**
     * Loads a user with the desired ID.
     *
     * @param id the ID of the desired user.
     *
     * @return the user with given ID, or <code>null</code> if something fails.
     */
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

    /**
     * Deletes the user from database.
     *
     * @return <code>true</code> upon success, <code>false</code> otherwise.
     */
    public boolean delete() {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return false;
        }

        PreparedStatement ps = DB.getPreparedStatement(conn,
                                                       Config.
                                                       SQL_MAGIC.
                                                       REMOVE_USER);

        if (ps == null) {
            closeResources(conn, null, null);
            return false;
        }

        try {
            ps.setLong(1, getId());
            ps.executeUpdate();
            closeResources(conn, ps, null);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(conn, ps, null);
            return false;
        }

        return true;
    }

    /**
     * Bans a user.
     *
     * @param duration the duration in hours.
     *
     * @return <code>true</code> upon success, <code>false</code> otherwise.
     */
    public boolean ban(final double duration) {
        if (getUserType() == UserType.ADMIN) {
            // Cannot ban an admin.
            return false;
        }

        if (Double.isInfinite(duration)
                || Double.isNaN(duration)
                || duration <= 0.0) {
            // Bad duration.
            return false;
        }

        Connection connection = DB.getConnection();

        if (connection == null) {
            return false;
        }

        Statement statement = null;

        // duration is a double, getId() a long; no danger of SQL-injection.
        String sql = "UPDATE users SET ban_until = NOW() + INTERVAL '" +
                     duration + " HOURS' WHERE user_id = " + getId() + ";";
        try {
            statement = connection.createStatement();
            statement.executeUpdate(sql);
            closeResources(connection, statement, null);
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, statement, null);
            return false;
        }
    }

    /**
     * Checks whether this user is banned.
     *
     * @return <code>true</code> if this user is banned; <code>false</code>
     * if not or SQL fails.
     */
    public final boolean isBanned() {
        final Timestamp ts = getBannedUntil();

        if (ts == null) {
            return false;
        }

        final Timestamp now = new Timestamp(System.currentTimeMillis());
        boolean notBanned = now.after(ts);

        if (notBanned) {
            // Here no banned anymore, clear the entry in DB.
            Connection connection = DB.getConnection();

            if (connection == null) {
                // Do not insist.
                return true;
            }

            PreparedStatement ps =
                    DB.getPreparedStatement(connection,
                                            Config.
                                            SQL_MAGIC.
                                            REMOVE_BAN_TIMESTAMP);

            if (ps == null) {
                closeResources(connection, null, null);
                return true;
            }

            try {
                ps.setLong(1, getId());
                ps.executeUpdate();
                closeResources(connection, ps, null);
                return true;
            } catch (final SQLException sqle) {
                sqle.printStackTrace(System.err);
                closeResources(connection, ps, null);
                // Do not insist.
                return true;
            }
        }

        return !notBanned;
    }

    /**
     * Computes the amount of posts written by this user.
     *
     * @return the amount of posts written by this user or -1 if SQL failed.
     */
    public final int getPostCount() {
        Connection connection = DB.getConnection();

        if (connection == null) {
            return -1;
        }

        PreparedStatement ps =
                DB.getPreparedStatement(connection,
                                        Config.
                                        SQL_MAGIC.
                                        COUNT_POSTS);

        if (ps == null) {
            closeResources(connection, null, null);
            return -1;
        }

        ResultSet rs = null;

        try {
            ps.setLong(1, getId());
            rs = ps.executeQuery();

            if (rs.next() == false) {
                closeResources(connection, ps, rs);
                return -1;
            }

            int count = rs.getInt(1);

            closeResources(connection, ps, rs);
            return count;
        } catch (final SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, rs);
            return -1;
        }
    }

    /**
     * Extracts a single user from a given result set.
     *
     * @param rs the result set to extract from.
     *
     * @return a user object, or <code>null</code> if something fails.
     */
    private static final User extractUser(ResultSet rs) {
        try {
            if (rs.next() == false) {
                return null;
            }

            User user = new User().setId(rs.getLong("user_id"))
                                  .setUsername(rs.getString("username"))
                                  .setSalt(rs.getString("salt"))
                                  .setHash(rs.getString("passwd_hash"))
                                  .setFirstName(rs.getString("first_name"))
                                  .setLastName(rs.getString("last_name"))
                                  .setEmail(rs.getString("email"))
                                  .setShowRealName(
                                        rs.getBoolean("show_real_name"))
                                  .setShowEmail(rs.getBoolean("show_email"))
                                  .setDescription(rs.getString("description"))
                                  .setCreatedAt(rs.getTimestamp("created_at"))
                                  .setUpdatedAt(rs.getTimestamp("updated_at"))
                                  .setUserType(UserType.valueOf(
                                               rs.getString("user_type")))
                                  .setBannedUntil(rs.getTimestamp("ban_until"))
                                  .end();

            return user;
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }
}

package net.coderodde.multilog.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.coderodde.multilog.Config;
import static net.coderodde.multilog.Utils.closeResources;
import net.coderodde.multilog.model.Thread;

/**
 * This class holds the information of a forum thread.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class Thread {

    /**
     * This field holds the ID of this thread.
     */
    private long id;

    /**
     * This field holds the ID of the topic this thread belongs to.
     */
    private long topicId;

    /**
     * This field holds the name of this thread.
     */
    private String name;

    /**
     * This field holds a reference to the topic containing this thread.
     */
    private Topic topic;

    /**
     * This field holds the timestamp at which this thread was created.
     */
    private Timestamp createdAt;

    /**
     * This field holds the timestamp at which this thread was updated last.
     * Basically this is updated every time a thread get a new post.
     */
    private Timestamp updatedAt;

    /**
     * The thread comparator comparing by thread names.
     */
    public static final Comparator<Thread> tc = new Comparator<Thread>() {
        public int compare(Thread o1, Thread o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

    /**
     * Reads a thread from database.
     *
     * @param id the ID of the thread to read.
     *
     * @return a thread upon success, <code>null</code> otherwise.
     */
    public static final Thread read(final long id) {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return null;
        }

        PreparedStatement ps = DB.getPreparedStatement(conn,
                                                       Config.
                                                       SQL_MAGIC.
                                                       FETCH_THREAD);
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

        Thread thread = extractThread(rs);
        closeResources(conn, ps, rs);
        return thread;
    }

    /**
     * Extracts a thread from a result set.
     *
     * @param rs the result set to extract from.
     *
     * @return a thread upon success, or <code>null</code> otherwise.
     */
    private static final Thread extractThread(final ResultSet rs) {
        try {
            if (rs.next() == false) {
                return null;
            }

            Thread thread = new Thread();

            thread.setId(rs.getLong("thread_id"))
                  .setTopicId(rs.getLong("topic_id"))
                  .setName(rs.getString("thread_name"))
                  .setCreatedAt(rs.getTimestamp("created_at"))
                  .setUpdatedAt(rs.getTimestamp("updated_at"));

            thread.setTopic(Topic.read(thread.getTopicId()));

            return thread;
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * Extracts a list of posts from a result set.
     *
     * @param rs the result set to extract from.
     *
     * @return a list of posts upon success, <code>null</code> otherwise.
     */
    private final List<Post> extractPostList(final ResultSet rs) {
        List<Post> postList = new ArrayList<Post>();

        try {
            while (rs.next()) {
                Post post = new Post();
                long parentPostId = rs.getLong("parent_post");

                if (parentPostId > 0L) {
                    post.setParentPost(Post.read(parentPostId));
                }

                post.setId(rs.getLong("post_id"))
                    .setText(rs.getString("post_text"))
                    .setThread(this)
                    .setCreatedAt(rs.getTimestamp("created_at"))
                    .setUpdatedAt(rs.getTimestamp("updated_at"))
                    .setUser(User.read(rs.getLong("user_id")));

                postList.add(post);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }

        return postList;
    }

    /**
     * Checks whether this post and <code>o</code> are considered equal.
     * Implements the needed API for storing threads in a hash map/set.
     *
     * @param o the object to compare to.
     *
     * @return <code>true</code> if the two object equal, <code>false</code>
     * otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Thread)) {
            return false;
        }

        return getId() == ((Thread) o).getId();
    }

    /**
     * Returns the hash code of this thread.
     *
     * @return the hash code of this thread.
     */
    @Override
    public int hashCode() {
        return (int) getId();
    }

    /**
     * Returns a list of all posts in this thread upon success,
     * <code>null</code> otherwise.
     *
     * @return a list of all posts in this thread.
     */
    public List<Post> getAllPosts() {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return null;
        }

        PreparedStatement ps =
                DB.getPreparedStatement(conn,
                                        Config.
                                        SQL_MAGIC.
                                        FETCH_ALL_POSTS_OF_A_THREAD);

        if (ps == null) {
            closeResources(conn, null, null);
            return null;
        }

        ResultSet rs = null;

        try {
            ps.setLong(1, getId());
            rs = ps.executeQuery();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(conn, ps, rs);
            return null;
        }

        List<Post> postList = extractPostList(rs);
        closeResources(conn, ps, rs);
        return postList;
    }

    /**
     * Returns the ID of this thread.
     *
     * @return the ID of this thread.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the ID of the topic this thread belongs to.
     *
     * @return the ID of a topic.
     */
    public long getTopicId() {
        return topicId;
    }

    /**
     * Returns the name of this thread.
     *
     * @return the name of this thread.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the topic object of this thread.
     *
     * @return the topic object of this thread.
     */
    public Topic getTopic() {
        return topic;
    }

    /**
     * Returns the creation timestamp.
     *
     * @return the creation timestamp.
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the update timestamp.
     *
     * @return the update timestamp.
     */
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Updates the 'update' time stamp of this thread.
     *
     * @return <code>true</code> upon success, <code>false</code> otherwise.
     */
    public boolean updateTimestamp() {
        Connection connection = DB.getConnection();

        if (connection == null) {
            return false;
        }

        PreparedStatement ps =
                DB.getPreparedStatement(connection,
                                        Config.
                                        SQL_MAGIC.
                                        UPDATE_THREAD_TIMESTAMP);

        if (ps == null) {
            closeResources(connection, null, null);
            return false;
        }

        try {
            ps.setLong(1, getId());
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
     * Returns <code>true</code> if 'createdAt' and 'updatedAt' timestamps
     * differ, <code>false</code> otherwise.
     *
     * @return (See above.)
     */
    public final boolean isTimestampsDifferent() {
        return !getCreatedAt().equals(getUpdatedAt());
    }

    /**
     * Persists this thread in the database.
     *
     * @return <code>true</code> upon success, <code>false</code> otherwise.
     */
    public boolean create() {
        Connection connection = DB.getConnection();

        if (connection == null) {
            return false;
        }

        PreparedStatement ps =
                DB.getPreparedStatement(connection,
                                        Config.
                                        SQL_MAGIC.
                                        CREATE_THREAD);

        if (ps == null) {
            closeResources(connection, null, null);
            return false;
        }

        try {
            ps.setLong(1, getTopic().getId());
            ps.setString(2, getName());
            ps.executeUpdate();
            closeResources(connection, ps, null);
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, null);
            return false;
        }
    }

    /**
     * Deletes this thread from database.
     *
     * @return <code>true</code> upon success, <code>false</code> otherwise.
     */
    public boolean delete() {
        List<Post> childrenPosts = getAllPosts();

        boolean deleted = true;

        for (Post p : childrenPosts) {
            if (p.delete() == false) {
                deleted = false;
                break;
            }
        }

        if (!deleted) {
            return false;
        }

        Connection connection = DB.getConnection();

        if (connection == null) {
            return false;
        }

        PreparedStatement ps =
                DB.getPreparedStatement(connection,
                                        Config.
                                        SQL_MAGIC.
                                        DELETE_THREAD);

        if (ps == null) {
            closeResources(connection, null, null);
            return false;
        }

        try {
            ps.setLong(1, getId());
            ps.executeUpdate();
            closeResources(connection, ps, null);
            return true;
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, null);
            return false;
        }
    }

    /**
     * Sets the ID of this thread.
     *
     * @param id the ID to set.
     *
     * @return itself for chaining.
     */
    public Thread setId(final long id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the ID of the topic this thread belongs to.
     *
     * @param topicId the topic ID to set.
     *
     * @return itself for chaining.
     */
    public Thread setTopicId(final long topicId) {
        this.topicId = topicId;
        return this;
    }

    /**
     * Sets the name of this thread.
     *
     * @param name the name of this thread.
     *
     * @return itself for chaining.
     */
    public Thread setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the topic of this thread.
     *
     * @param topic the topic of this thread.
     *
     * @return itself for chaining.
     */
    public Thread setTopic(final Topic topic) {
        this.topic = topic;
        return this;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the creation timestamp.
     *
     * @return itself for chaining.
     */
    public Thread setCreatedAt(final Timestamp createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Sets the update timestamp.
     *
     * @param updatedAt the timestamp of an update.
     *
     * @return itself for chaining.
     */
    public Thread setUpdatedAt(final Timestamp updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Syntactic sugar.
     *
     * @return this.
     */
    public Thread end() {
        return this;
    }
}

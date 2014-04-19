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

    public static final Comparator<Thread> tc = new Comparator<Thread>() {
        public int compare(Thread o1, Thread o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };

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

    private static final Thread extractThread(final ResultSet rs) {
        try {
            if (rs.next() == false) {
                return null;
            }

            Thread thread = new Thread();

            thread.setId(rs.getLong("thread_id"))
                  .setTopicId(rs.getLong("topic_id"))
                  .setName(rs.getString("thread_name"))
                  .setCreatedAtTimestamp(rs.getTimestamp("created_at"))
                  .setUpdatedAtTimestamp(rs.getTimestamp("updated_at"));

            return thread;
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }

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

    @Override
    public int hashCode() {
        return (int) getId();
    }

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
    public Timestamp getCreatedAtTimestamp() {
        return createdAt;
    }

    /**
     * Returns the update timestamp.
     *
     * @return the update timestamp.
     */
    public Timestamp getUpdatedAtTimestamp() {
        return updatedAt;
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
    public Thread setCreatedAtTimestamp(final Timestamp createdAt) {
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
    public Thread setUpdatedAtTimestamp(final Timestamp updatedAt) {
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

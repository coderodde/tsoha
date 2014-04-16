package net.coderodde.multilog.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import net.coderodde.multilog.Config;
import static net.coderodde.multilog.Utils.closeResources;

/**
 * This class holds the information of a forum topic.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class Topic {

    /**
     * The ID of this topic.
     */
    private long id;

    /**
     * The name of this topic.
     */
    private String name;

    /**
     * The timestamp at which this topic was created.
     */
    private Timestamp createdAt;

    /**
     * The timestamp at which this topic was updated.
     * This essentially is updated every time a child-thread is updated.
     */
    private Timestamp updatedAt;

    public static final Topic read(final long id) {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return null;
        }

        PreparedStatement ps =
                DB.getPreparedStatement(conn,
                                        Config.SQL_MAGIC.FETCH_TOPIC_BY_ID);

        if (ps == null) {
            closeResources(conn, null, null);
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

        Topic topic = extractTopic(rs);
        closeResources(conn, ps, rs);
        return topic;
    }

    public static final List<Topic> getAllTopics() {
        Connection connection = DB.getConnection();

        if (connection == null) {
            return null;
        }

        Statement statement = DB.getStatement(connection);

        if (statement == null) {
            closeResources(connection, null, null);
            return null;
        }

        ResultSet rs;

        try {
            rs = statement.executeQuery(Config.SQL_MAGIC.FETCH_ALL_TOPICS);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, statement, null);
            return null;
        }

        List<Topic> topicList = extractTopics(rs);
        closeResources(connection, statement, rs);
        return topicList;
    }

    /**
     * Extracts a single topic from an input result set.
     *
     * @param rs the result set to extract from.
     *
     * @return a topic object, or <code>null</code> if something fails.
     */
    private static final Topic extractTopic(ResultSet rs) {
        try {
            if (rs.next() == false) {
                return null;
            }

            Topic topic = new Topic();

            return topic.setId(rs.getLong("topic_id"))
                        .setName(rs.getString("topic_name"))
                        .setCreatedAtTimestamp(rs.getTimestamp("created_at"))
                        .setUpdatedAtTimestamp(rs.getTimestamp("updated_at"))
                        .end();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }

    /**
     * Extracts all topics from an input result set.
     *
     * @param rs the result set to extract from.
     *
     * @return a list of topics, or <code>null</code> if something fails.
     */
    private static final List<Topic> extractTopics(final ResultSet rs) {
        List<Topic> topicList = new ArrayList<Topic>();

        try {
            while (rs.next()) {
                final long ID = rs.getLong("topic_id");
                final String NAME = rs.getString("topic_name");
                final Timestamp CREATED_AT = rs.getTimestamp("created_at");
                final Timestamp UPDATED_AT = rs.getTimestamp("updated_at");

                Topic t = new Topic().setId(ID)
                                     .setName(NAME)
                                     .setCreatedAtTimestamp(CREATED_AT)
                                     .setUpdatedAtTimestamp(UPDATED_AT)
                                     .end();
                topicList.add(t);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }

        return topicList;
    }

    /**
     * Returns the ID of this topic.
     *
     * @return the ID of this topic.
     */
    public long getId() {
        return id;
    }

    /**
     * Returns the name of this topic.
     *
     * @return the name of this topic.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the timestamp describing the creation time of this topic.
     *
     * @return the creation timestamp.
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the timestamp describing the moment this topic was updated the
     * previous time.
     *
     * @return the timestamp this topic was updated the previous time.
     */
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Returns a list of all threads belonging to this topic.
     *
     * @return a list of forum threads or <code>null</code> if something fails.
     */
    public List<Thread> getThreads() {
        Connection connection = DB.getConnection();

        if (connection == null) {
            return null;
        }

        PreparedStatement ps =
                DB.getPreparedStatement(connection,
                                        Config.
                                        SQL_MAGIC.
                                        FETCH_THREADS_BY_TOPIC_ID);

        if (ps == null) {
            closeResources(connection, null, null);
            return null;
        }

        ResultSet rs = null;
        List<Thread> threadList = null;

        try {
            ps.setLong(1, getId());
            rs = ps.executeQuery();
            threadList = extractAllThreads(rs);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, rs);
            return null;
        }

        closeResources(connection, ps, rs);
        return threadList;
    }

    /**
     * Extracts all forum threads from a given result set.
     *
     * @param rs the result set to extract from.
     *
     * @return a list of forum threads, or <code>null</code> if something fails.
     */
    private static final List<Thread> extractAllThreads(ResultSet rs) {
        List<Thread> threadList = new ArrayList<Thread>();

        try {
            while (rs.next()) {
                Thread t = new Thread();
                t.setId(rs.getLong("thread_id"))
                 .setTopicId(rs.getLong("topic_id"))
                 .setName(rs.getString("thread_name"))
                 .setCreatedAtTimestamp(rs.getTimestamp("created_at"))
                 .setUpdatedAtTimestamp(rs.getTimestamp("updated_at"))
                 .end();

                threadList.add(t);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }

        return threadList;
    }

    /**
     * Sets the ID of this topic.
     *
     * @param id the ID value to set.
     *
     * @return itself for chaining.
     */
    public Topic setId(final long id) {
        this.id = id;
        return this;
    }

    /**
     * Sets the name of this topic.
     *
     * @param name the name of this topic.
     *
     * @return itself for chaining.
     */
    public Topic setName(final String name) {
        this.name = name;
        return this;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the timestamp.
     *
     * @return itself for chaining.
     */
    public Topic setCreatedAtTimestamp(final Timestamp createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    /**
     * Sets the update timestamp.
     *
     * @param updatedAt the update timestamp.
     *
     * @return itself for chaining.
     */
    public Topic setUpdatedAtTimestamp(final Timestamp updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Syntactic sugar.
     *
     * @return this.
     */
    public Topic end() {
        return this;
    }
}

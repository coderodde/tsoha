package net.coderodde.multilog.model;

import java.sql.Connection;
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

    public static final Topic getTopicById(final long id) {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return null;
        }

        return null;
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
                                     .setUpdatedAtTimestamp(UPDATED_AT);

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
    public Timestamp getCreatedAtTimestamp() {
        return createdAt;
    }

    /**
     * Returns the timestamp describing the moment this topic was updated the
     * previous time.
     *
     * @return the timestamp this topic was updated the previous time.
     */
    public Timestamp getUpdatedAtTimestamp() {
        return updatedAt;
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

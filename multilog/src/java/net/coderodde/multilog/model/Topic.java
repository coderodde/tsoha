package net.coderodde.multilog.model;

import java.sql.Timestamp;

/**
 * This class holds the information of a forum topic.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class Topic {

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
     * Syntax sugar.
     */
    public void end() {

    }
}

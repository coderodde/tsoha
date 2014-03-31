package net.coderodde.multilog.model;

import java.sql.Timestamp;

/**
 * This class holds the information of a forum thread.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class Thread {

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

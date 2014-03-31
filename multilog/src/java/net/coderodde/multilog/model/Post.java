package net.coderodde.multilog.model;

import java.sql.Timestamp;

/**
 * This class holds all information of a forum post.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class Post {

    /**
     * This field holds the information of an owner of this post.
     */
    private User user;

    /**
     * This field points to a thread containing this post.
     */
    private Thread thread;

    /**
     * This field holds the raw text of this post. Might contain an
     * easy markup.
     */
    private String text;

    /**
     * This field holds the timestamp of this post being created.
     */
    private Timestamp createdAt;

    /**
     * This field holds the timestamp of this post being edited the most
     * recent time.
     */
    private Timestamp updatedAt;

    /**
     * If not null, points to the post being replied by this post.
     */
    private Post parentPost;

    /**
     * Returns the writer of this post.
     *
     * @return the writer of this post.
     */
    public User getUser() {
        return user;
    }

    /**
     * Returns the thread of this post.
     *
     * @return the thread of this post.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Returns the text of this post.
     *
     * @return the text of this post.
     */
    public String getText() {
        return text;
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
     * Returns the latest update timestamp.
     *
     * @return the latest update timestamp.
     */
    public Timestamp getUpdatedAtTimestamp() {
        return updatedAt;
    }

    /**
     * Returns the post that this post replies to, or <code>null</code>
     * if this post replies to none.
     *
     * @return the parent post, or <code>null</code> if there is no such.
     */
    public Post getParentPost() {
        return parentPost;
    }

    /**
     * Sets the user of this post.
     *
     * @param user the user of this post.
     *
     * @return itself for chaining.
     */
    public Post setUser(final User user) {
        this.user = user;
        return this;
    }

    /**
     * Sets the thread of this post.
     *
     * @param thread the thread of this post.
     *
     * @return itself for chaining.
     */
    public Post setThread(final Thread thread) {
        this.thread = thread;
        return this;
    }

    /**
     * Sets the text of this post.
     *
     * @param text the text of this post.
     *
     * @return itself for chaining.
     */
    public Post setText(final String text) {
        this.text = text;
        return this;
    }

    /**
     * Sets the creation timestamp.
     *
     * @param createdAt the creation timestamp.
     *
     * @return itself for chaining.
     */
    public Post setCreatedAtTimestamp(final Timestamp createdAt) {
        this.createdAt = createdAt;
        return this;
    }


    /**
     * Sets the update timestamp.
     *
     * @param updateAt update timestamp.
     *
     * @return this for chaining.
     */
    public Post setUpdatedAtTimestamp(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    /**
     * Sets the parent post of this post.
     *
     * @param parentPost the parent post.
     *
     * @return this for chaining.
     */
    public Post setParentPost(final Post parentPost) {
        this.parentPost = parentPost;
        return this;
    }

    /**
     * Syntactic sugar.
     *
     * @return this.
     */
    public Post end() {
        return this;
    }
}

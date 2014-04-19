package net.coderodde.multilog.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import net.coderodde.multilog.Config;
import static net.coderodde.multilog.Utils.closeResources;

/**
 * This class holds all information of a forum post.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class Post {

    /**
     * This field holds the ID of this post.
     */
    private long id;

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
     * If not <code>null</code>, points to the post being replied by this post.
     */
    private Post parentPost;

    /**
     * Used for indenting the replies.
     */
    private int indent;

    /**
     * <code>true</code> if this post is fresh.
     */
    private boolean fresh;

    public static final Post read(final long postId) {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return null;
        }

        PreparedStatement ps = DB.getPreparedStatement(conn,
                                                       Config.
                                                       SQL_MAGIC.
                                                       FETCH_POST_BY_ID);
        if (ps == null) {
            closeResources(conn, null, null);
            return null;
        }

        ResultSet rs = null;

        try {
            ps.setLong(1, postId);
            rs = ps.executeQuery();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(conn, ps, rs);
            return null;
        }

        Post post = extractPost(rs);
        closeResources(conn, ps, rs);
        return post;
    }

    private static final Post extractPost(final ResultSet rs) {
        try {
            if (rs.next() == false) {
                return null;
            }

            Post post = new Post();

            post.setId(rs.getLong("post_id"))
                .setText(rs.getString("post_text"))
                .setCreatedAt(rs.getTimestamp("created_at"))
                .setUpdatedAt(rs.getTimestamp("updated_at"));

            long parentPostId = rs.getLong("parent_post");

            if (parentPostId > 0L) {
                post.setParentPost(Post.read(parentPostId));
            }

            long threadId = rs.getLong("thread_id");

            if (threadId > 0L) {
                post.setThread(Thread.read(threadId));
            }

            long userId = rs.getLong("user_id");

            if (userId > 0L) {
                post.setUser(User.read(userId));
            }

            return post;
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            return null;
        }
    }

    public boolean create() {
        Connection connection = DB.getConnection();

        if (connection == null) {
            return false;
        }

        PreparedStatement ps = DB.getPreparedStatement(connection,
                                                       Config.
                                                       SQL_MAGIC.
                                                       CREATE_NEW_POST);

        if (ps == null) {
            closeResources(connection, null, null);
            return false;
        }

        try {
            ps.setLong(1, getThread().getId());
            ps.setLong(2, getUser().getId());
            ps.setString(3, getText());

            if (getParentPost() != null) {
                ps.setLong(4, getParentPost().getId());
            } else {
                ps.setLong(4, 0);
            }

            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, null);
            return false;
        }

        closeResources(connection, ps, null);
        return true;
    }

    public long getId() {
        return id;
    }

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
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Returns the latest update timestamp.
     *
     * @return the latest update timestamp.
     */
    public Timestamp getUpdatedAt() {
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

    public int getIndent() {
        return indent;
    }

    public boolean isTimestampsDifferent() {
        return !createdAt.equals(updatedAt);
    }

    public boolean isFresh() {
        return fresh;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof Post)) {
            return false;
        }

        return this.getId() == ((Post) o).getId();
    }

    /**
     * This method returns rather simple a hash for the sake
     * of putting post objects into a hash map.
     *
     * @return the hash value of this <code>Post</code>.
     */
    @Override
    public int hashCode() {
        return (int) getId();
    }

    public Post setId(final long id) {
        this.id = id;
        return this;
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
    public Post setCreatedAt(final Timestamp createdAt) {
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
    public Post setUpdatedAt(Timestamp updatedAt) {
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

    public Post setIndent(final int indent) {
        this.indent = indent;
        return this;
    }

    public Post setFresh(final boolean fresh) {
        this.fresh = fresh;
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

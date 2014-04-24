package net.coderodde.multilog.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import net.coderodde.multilog.Config;
import static net.coderodde.multilog.Utils.closeResources;

/**
 * This class wraps a user ID and thread ID meaning that a user has read a
 * post.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class MessageRead {

    /**
     * This field holds the ID of a user.
     */
    private long userId;

    /**
     * This field holds the ID of a post.
     */
    private long postId;

    /**
     * Constructs an empty message read object.
     */
    public MessageRead() {

    }

    /**
     * Returns the user ID of this message read.
     *
     * @return the user ID of this message read.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * Returns the post ID of this message read.
     *
     * @return the post ID of this message read.
     */
    public long getPostId() {
        return postId;
    }

    /**
     * Sets the user ID for this message read.
     *
     * @param userId the ID to set.
     *
     * @return itself for chaining.
     */
    public MessageRead setUserId(final long userId) {
        this.userId = userId;
        return this;
    }

    /**
     * Sets the post ID for this message read.
     *
     * @param postId the post ID to set.
     *
     * @return itself for chaining.
     */
    public MessageRead setPostId(final long postId) {
        this.postId = postId;
        return this;
    }

    /**
     * Fetches the list of message reads of the specified user.
     *
     * @param user the user to fetch for.
     *
     * @return the list of message reads.
     */
    public static final List<MessageRead>
            getAllMessageReadsOfUser(final User user) {
        Connection connection = DB.getConnection();

        if (connection == null) {
            return null;
        }

        PreparedStatement ps =
                DB.getPreparedStatement(connection,
                                        Config.
                                        SQL_MAGIC.
                                        FETCH_MESSAGE_READS_OF_USER);
        if (ps == null) {
            closeResources(connection, null, null);
            return null;
        }

        ResultSet rs = null;

        try {
            ps.setLong(1, user.getId());
            rs = ps.executeQuery();
        } catch (SQLException sqle) {
            closeResources(connection, ps, rs);
            return null;
        }

        List<MessageRead> list = extractMessageReadList(rs);
        closeResources(connection, ps, rs);
        return list;
    }

    /**
     * Extracts the list of message reads from a result set.
     *
     * @param rs the result set.
     *
     * @return the list of message reads or <code>null</code> if something
     * fails.
     */
    private static final List<MessageRead>
            extractMessageReadList(final ResultSet rs) {
        List<MessageRead> list = new ArrayList<MessageRead>();

        try {
            while (rs.next()) {
                list.add(new MessageRead().setUserId(rs.getLong(1))
                                          .setPostId(rs.getLong(2)));
            }
        } catch (SQLException sqle) {
            return null;
        }

        return list;
    }

    /**
     * Returns a map mapping every thread with unread posts to the amount of
     * posts in the thread.
     *
     * @param user the user to fetch for.
     *
     * @return a map mapping a thread <tt>T</tt> (containing unread posts) to
     * the amount of new posts in <tt>T</tt>.
     */
    public static final Map<Thread, Integer>
            findUpdatedThreads(final User user) {
        final List<MessageRead> messageReads = getAllMessageReadsOfUser(user);
        final List<Post> posts = findAllPostsFromMessageReads(messageReads);
        final List<Thread> threads = findAllThreadsFromPosts(posts);
        final Set<Long> postIdSet = new HashSet<Long>();

        // This also sorts the threads by name.
        final Map<Thread, Integer> map =
                new TreeMap<Thread, Integer>(Thread.tc);

        for (final MessageRead mr : messageReads) {
            postIdSet.add(mr.getPostId());
        }

        for (final Thread t : threads) {
            List<Post> postList = t.getAllPosts();

            for (Post post : postList) {
                if (postIdSet.contains(post.getId()) == false) {
                    if (map.containsKey(t) == false) {
                        map.put(t, 1);
                    } else {
                        map.put(t, map.get(t) + 1);
                    }
                }
            }
        }

        return map;
    }

    /**
     * Extracts all unique posts from the message read list.
     *
     * @param messageReads the message read list to extract from.
     *
     * @return the list of posts.
     */
    private static final List<Post> findAllPostsFromMessageReads
            (final List<MessageRead> messageReads) {
        final Set<Post> postSet = new HashSet<Post>();

        for (final MessageRead mr : messageReads) {
            postSet.add(Post.read(mr.getPostId()));
        }

        return new ArrayList<Post>(postSet);
    }

    /**
     * Extracts all unique threads from the list of posts.
     *
     * @param posts the list of posts to process.
     *
     * @return the list of threads.
     */
    private static final List<Thread>
            findAllThreadsFromPosts(final List<Post> posts) {
        final Set<Thread> threadSet = new HashSet<Thread>();

        for (final Post p : posts) {
            threadSet.add(p.getThread());
        }

        return new ArrayList<Thread>(threadSet);
    }

    /**
     * Persists a message read to the database.
     *
     * @return <code>true</code> if successfully saved to database,
     * <code>false</code> otherwise.
     */
    public boolean create() {
        Connection connection = DB.getConnection();

        if (connection == null) {
            return false;
        }

        PreparedStatement ps = DB.getPreparedStatement(connection,
                                                       Config.
                                                       SQL_MAGIC.
                                                       CREATE_POST_READ);
        if (ps == null) {
            closeResources(connection, null, null);
            return false;
        }

        try {
            ps.setLong(1, getUserId());
            ps.setLong(2, getPostId());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, null);
            return false;
        }

        closeResources(connection, ps, null);
        return true;
    }
}

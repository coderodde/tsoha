package net.coderodde.multilog.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.coderodde.multilog.Config;
import static net.coderodde.multilog.Utils.closeResources;

/**
 * This class wraps a user ID and thread ID, such that
 * @author rodionefremov
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

    public long getUserId() {
        return userId;
    }

    public long getPostId() {
        return postId;
    }

    public MessageRead setUserId(final long userId) {
        this.userId = userId;
        return this;
    }

    public MessageRead setPostId(final long postId) {
        this.postId = postId;
        return this;
    }

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

    public static final List<Thread> findUpdatedThreads(final User user) {
        final List<MessageRead> messageReads = getAllMessageReadsOfUser(user);
        final List<Post> posts = findAllPostsFromMessageReads(messageReads);
        final List<Thread> threads = findAllThreadsFromPosts(posts);
        final Set<Long> postIdSet = new HashSet<Long>();

        for (final MessageRead mr : messageReads) {
            postIdSet.add(mr.getPostId());
        }

        final List<Thread> updatedThreads = new ArrayList<Thread>();

        outer:
        for (final Thread t : threads) {
            List<Post> postList = t.getAllPosts();

            for (Post post : postList) {
                if (postIdSet.contains(post.getId()) == false) {
                    updatedThreads.add(t);
                    continue outer;
                }
            }
        }

        return updatedThreads;
    }

    private static final List<Post> findAllPostsFromMessageReads
            (final List<MessageRead> messageReads) {
        final Set<Post> postSet = new HashSet<Post>();

        for (final MessageRead mr : messageReads) {
            postSet.add(Post.read(mr.getPostId()));
        }

        return new ArrayList<Post>(postSet);
    }

    private static final List<Thread>
            findAllThreadsFromPosts(final List<Post> posts) {
        final Set<Thread> threadSet = new HashSet<Thread>();

        for (final Post p : posts) {
            threadSet.add(p.getThread());
        }

        return new ArrayList<Thread>(threadSet);
    }

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

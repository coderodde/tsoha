package net.coderodde.multilog.contoller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.MessageRead;
import net.coderodde.multilog.model.Post;
import net.coderodde.multilog.model.Thread;
import net.coderodde.multilog.model.User;

/**
 * This servlet is responsible for showing particular threads. The servlet
 * requires an "id"-parameter specifying the thread to be shown.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class ThreadServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request.
     * @param response servlet response.
     *
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final User currentUser = User.getCurrentlySignedUser(request);

        if (currentUser != null) {
            HomeServlet.prepareNavibarForSingedUser(request, currentUser);
        } else {
            HomeServlet.prepareNavibarForUnsignedUser(request);
        }

        final String idAsString = request.getParameter("id");

        if (idAsString == null || idAsString.isEmpty()) {
            request.setAttribute("notice",
                                 "No thread specified. Stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        long threadId = -1;

        try {
            threadId = Long.parseLong(idAsString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace(System.err);
            request.setAttribute("notice", "Invalid thread ID: " + idAsString);
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        Thread thread = Thread.read(threadId);

        if (thread == null) {
            request.setAttribute("notice",
                                 "No thread with ID " + threadId + ".");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        List<Post> posts = thread.getAllPosts();

        if (currentUser != null) {
            request.setAttribute("can_reply", true);
            List<MessageRead> messageReads =
                    MessageRead.getAllMessageReadsOfUser(currentUser);
            setFreshnessFlags(posts, messageReads);
            currentUser.addMessageReads(posts);
            setupOwnEdiblePosts(posts, currentUser);
        }

        posts = sort(posts);
        request.setAttribute("postList", posts);
        request.setAttribute("thread_name", thread.getName());
        request.setAttribute("topic_name", thread.getTopic().getName());
        request.setAttribute("thread_id", thread.getId());
        request.setAttribute("topic_id", thread.getTopicId());

        request.getRequestDispatcher("threadview.jsp")
               .forward(request, response);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request.
     * @param response servlet response.
     *
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request.
     * @param response servlet response.
     *
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of this servlet.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "This servlet is responsible for showing a particular thread.";
    }

    /**
     * Marks the fresh (not seen) posts.
     *
     * @param posts the all posts of a thread.
     * @param reads the all message reads of a current user.
     */
    private static final void setFreshnessFlags
            (final List<Post> posts, final List<MessageRead> reads) {
        Set<Long> postIdSet = new HashSet<Long>(reads.size());

        for (final MessageRead mr : reads) {
            postIdSet.add(mr.getPostId());
        }

        for (final Post p : posts) {
            p.setFresh(!postIdSet.contains(p.getId()));
        }
    }

    /**
     * Mark all own posts as edible.
     *
     * @param postList all the posts of a thread.
     * @param currentUser the current signed in user.
     */
    private static final void setupOwnEdiblePosts
            (final List<Post> postList, final User currentUser) {
        for (final Post p : postList) {
            p.setEdible(p.getUser().equals(currentUser));
        }
    }

    /**
     * Sorts the list of posts such that the reply posts <i>D</i> of post
     * <i>P</i> appear immediately after <i>P</i>, etc.
     *
     * @param postList the list of posts to sort.
     */
    private static final List<Post> sort(List<Post> postList) {
        List<Post> toplevelPostList = new ArrayList<Post>(postList.size());

        // Maps a parent post to the list of its children posts (i.e. replies).
        Map<Post, List<Post>> map =
                new HashMap<Post, List<Post>>(postList.size());

        // Load top-level posts and the (implicit) post tree.
        for (Post post : postList) {
            Post parent = post.getParentPost();

            if (parent != null) {
                List<Post> childrenOfParent = map.get(parent);

                if (childrenOfParent == null) {
                    childrenOfParent = new ArrayList<Post>();
                    childrenOfParent.add(post);
                    map.put(parent, childrenOfParent);
                } else {
                    childrenOfParent.add(post);
                }
            } else {
                toplevelPostList.add(post);
            }
        }

        // Begin: sort evertying.
        Collections.sort(toplevelPostList, pc);

        for (Post p : map.keySet()) {
            List<Post> list = map.get(p);

            if (list != null) {
                Collections.sort(list, pc);
            }
        }
        // End: sort everything.

        List<Post> ret = new ArrayList<Post>(postList.size());
        LinkedList<Iterator<Post>> stack = new LinkedList<Iterator<Post>>();
        stack.addLast(toplevelPostList.iterator());

        // Now load all posts by traversing the post tree in pre-order.
        while (stack.isEmpty() == false) {
            Iterator<Post> it = stack.getLast();

            if (it.hasNext() == false) {
                stack.removeLast();
                continue;
            }

            Post tmp = it.next();
            tmp.setIndent(stack.size() - 1);

            ret.add(tmp);

            if (map.containsKey(tmp)) {
                stack.addLast(map.get(tmp).iterator());
            }
        }

        return ret;
    }

    /**
     * The timestamp comparator for putting the most recent posts to the
     * bottom of a page. (Ascending order.)
     */
    static final Comparator<Post> pc = new Comparator<Post>(){

        @Override
        public int compare(Post p1, Post p2) {
            return p1.getCreatedAt().compareTo(p2.getCreatedAt());
        }
    };
}

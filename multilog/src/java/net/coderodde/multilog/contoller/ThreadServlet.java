package net.coderodde.multilog.contoller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.Post;
import net.coderodde.multilog.model.Thread;
import net.coderodde.multilog.Utils.Pair;

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

        request.setAttribute("postList", thread.getAllPosts());
        request.setAttribute("thread_name", thread.getName());
        request.getRequestDispatcher("threadview.jsp")
               .forward(request, response);
        return;
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
     * Sorts the list of posts such that the reply posts <i>D</i> of post
     * <i>P</i> appear immediately after <i>P</i>.
     *
     * @param postList the list of posts to sort.
     */
    private static final List<Post> resort(List<Post> postList) {
        List<Post> ret = new ArrayList<Post>(postList.size());
        List<Post> topmostPosts = getTopmostPosts(postList);

        if (topmostPosts.size() == postList.size()) {
            // Nothing to resort.
            return postList;
        }

        Collections.sort(topmostPosts, pc);

        List<PostTree> postTree = new ArrayList<PostTree>();

        for (Post p : topmostPosts) {
            postTree.add(new PostTree(p));
        }

        return null;
    }

    private static final List<Post> getTopmostPosts(List<Post> postList) {
        List<Post> ret = new ArrayList(postList.size());

        for (Post post : postList) {
            if (post.getParentPost() == null) {
                ret.add(post);
            }
        }

        return ret;
    }

    private static final class PostTree implements Iterable<Post> {

        private Post post;
        private List<PostTree> children;

        public PostTree(Post post) {
            this.post = post;
            this.children = new ArrayList<PostTree>();
        }

        public boolean add(final Post post) {
            if (post.getParentPost() == null) {
                // Root post already set.
                return false;
            }

            if (post.getParentPost().equals(this.post)) {
                this.children.add(new PostTree(post));
                return true;
            }

            for (PostTree pt : this.children) {
                if (pt.add(post) == true) {
                    return true;
                }
            }

            return false;
        }

        public void sort() {
            Collections.sort(this.children, ptc);

            for (PostTree pt : this.children) {
                pt.sort();
            }
        }

        public Iterator<Post> iterator() {
            return new PostTreeIterator();
        }

        private final class PostTreeIterator implements Iterator<Post> {

            private LinkedList<Pair<PostTree, Integer>> stack;

            PostTreeIterator() {
                this.stack = new LinkedList<Pair<PostTree, Integer>>();
                this.stack.add(new Pair<PostTree, Integer>(PostTree.this, 0));
            }

            public boolean hasNext() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public Post next() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            public void remove() {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
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

    static final Comparator<PostTree> ptc = new Comparator<PostTree>() {

        @Override
        public int compare(PostTree p1, PostTree p2) {
            return p1.post.getCreatedAt().compareTo(p2.post.getCreatedAt());
        }
    };
}

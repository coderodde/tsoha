package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.Post;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.Thread;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * This servlet is responsible for persisting the posts.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class PostServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Simply redirects to home with
     * a warning message.
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
        final User currentUser = User.getCurrentlySignedUser(request);

        if (currentUser != null) {
            HomeServlet.prepareNavibarForSingedUser(request, currentUser);
        } else {
            HomeServlet.prepareNavibarForUnsignedUser(request);
        }

        request.setAttribute("notce",
                             "Posting through GET - method is not supported. " +
                             "Please stop hacking.");
        request.getRequestDispatcher("home").forward(request, response);
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
        final User currentUser = User.getCurrentlySignedUser(request);

        if (currentUser == null) {
            request.setAttribute("notce",
                                 "You must be signed in as to " +
                                 "post on multilog. Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        final String threadIdString = request.getParameter("thread_id");

        long threadId = -1L;

        try {
            threadId = Long.parseLong(threadIdString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace(System.err);
            request.setAttribute("notice",
                                 "Bad thread ID. Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        final Thread ownerThread = Thread.read(threadId);

        if (ownerThread == null) {
            request.setAttribute("notice",
                                 "No thread with ID " + threadId + ". " +
                                 "Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        final String rawPostText = request.getParameter("post_text").trim();
        final String escapedPostText = escapeHtml4(rawPostText);

        long parentPostId = -1;

        final String rawParentPostId = request.getParameter("replied_post_id");

        if (rawParentPostId != null && (rawParentPostId.isEmpty() == false)) {
            try {
                parentPostId = Long.parseLong(rawParentPostId);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace(System.err);

                request.setAttribute("notice",
                                     "Bad parent post ID. Stop hacking.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }
        }

        Post post = new Post().setText(escapedPostText)
                              .setThread(ownerThread)
                              .setUser(currentUser);

        if (parentPostId != -1L) {
            Post parent = Post.read(parentPostId);

            if (parent == null) {
                request.setAttribute("notice",
                                     "No post with ID " + parentPostId + ". " +
                                     "Please stop hacking.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }

            // Once here, parent post is OK.
            post.setParentPost(parent);
        }

        if (post.create()) {
            request.getRequestDispatcher("thread?id=" + ownerThread.getId())
                   .forward(request, response);
        } else {
            request.setAttribute("notice", "Could not insert a post.");
            request.getRequestDispatcher("home").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

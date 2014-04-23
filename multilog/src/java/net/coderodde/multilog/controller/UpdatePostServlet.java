package net.coderodde.multilog.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.Post;
import net.coderodde.multilog.model.User;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import static net.coderodde.multilog.Utils.prepareNavibar;

/**
 * This servlet is responsible for updating the posts.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class UpdatePostServlet extends HttpServlet {

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
        request.setAttribute("notice", "Please don't update the posts " +
                                       "through the GET - method. ");
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
        prepareNavibar(request);

        User currentUser = User.getCurrentlySignedUser(request);

        if (currentUser == null) {
            request.setAttribute("notice",
                                 "You must be logged in as to update posts. " +
                                 "Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        final String postIdString = request.getParameter("edit_post_id");

        if (postIdString == null || postIdString.isEmpty()) {
            request.setAttribute("notice",
                                 "No post ID found. Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        long postId = -1;

        try {
            postId = Long.parseLong(postIdString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace(System.err);
            request.setAttribute("notice",
                                 "Bad post ID: " + postIdString + ". " +
                                 "Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        Post post = Post.read(postId);

        if (post == null) {
            request.setAttribute("notice", "No post with ID " + postId);
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        final String newContentString = request.getParameter("new_content");
        final String escapedContent = escapeHtml4(newContentString);

        post.setText(escapedContent);

        if (post.update()) {
            request.setAttribute("notice", "Post updated.");
            post.removeMessageReads();
        } else {
            request.setAttribute("notice", "Could not update your post!");
        }

        request.getRequestDispatcher("thread?id=" + post.getThread().getId())
               .forward(request, response);
    }

    /**
     * Returns a short description of this servlet.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "This servlet is responsible for updating existing posts.";
    }
}

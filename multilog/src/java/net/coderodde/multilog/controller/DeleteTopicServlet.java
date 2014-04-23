package net.coderodde.multilog.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.Topic;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.UserType;
import static net.coderodde.multilog.Utils.prepareNavibar;

/**
 * This servlet is responsible for deleting topics and all the data related to
 * topics.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class DeleteTopicServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Basically shows a warning.
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
        request.setAttribute("notice",
                             "Deleting topics through GET-method " +
                             "is not supported. Please stop hacking.");
        request.getRequestDispatcher("topic").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method for deleting topics.
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
        User currentUser = User.getCurrentlySignedUser(request);

        prepareNavibar(request);

        if (currentUser == null) {
            request.setAttribute("notice", "Please stop hacking.");
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        if (currentUser.getUserType() != UserType.ADMIN) {
            request.setAttribute("notice",
                                 "You don't have the privileges to delete " +
                                 "topics.");
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        final String topicIdString = request.getParameter("topic_id");

        if (topicIdString == null || topicIdString.isEmpty()) {
            request.setAttribute("notice", "No topic ID.");
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        long id = -1L;

        try {
            id = Long.parseLong(topicIdString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace(System.err);
            request.setAttribute("notice", "Bad topic ID.");
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        Topic topic = Topic.read(id);

        if (topic == null) {
            request.setAttribute("notice", "No topic with ID " + id);
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        // Has the privileges, delete all the shit.
        if (topic.delete()) {
            request.setAttribute("notice",
                                 "Topic '" + topic.getName() + "' deleted.");
        } else {
            request.setAttribute("notice",
                                 "Could not delete topic '" + topic.getName() +
                                 "'.");
        }

        request.getRequestDispatcher("topic").forward(request, response);
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

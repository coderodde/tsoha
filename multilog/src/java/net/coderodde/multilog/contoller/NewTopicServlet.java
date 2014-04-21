package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.Topic;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.UserType;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * This servlet is responsible for saving the new topics.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class NewTopicServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Basically, shows a warning.
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
                             "Submitting a new topic is not supported " +
                             "through the GET-method.");
        request.getRequestDispatcher("home").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request.
     * @param response servlet response.
     *
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {
        final User currentUser = User.getCurrentlySignedUser(request);

        if (currentUser == null) {
            request.setAttribute("notice",
                                 "No user signed in. Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        if (currentUser.getUserType() != UserType.ADMIN) {
            request.setAttribute("notice",
                                 "You don't have administrator privileges. " +
                                 "Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        final String topicName = request.getParameter("topic_name");

        if (topicName == null || topicName.isEmpty()) {
            request.setAttribute("notice", "Empty topic name.");
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        if (topicName.length() < 3) {
            request.setAttribute("notice", "Topic name too short.");
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        final String escapedTopicName = escapeHtml4(topicName);

        Topic t = new Topic().setName(escapedTopicName);

        if (t.create()) {
            request.setAttribute("notice", "Topic \"" + t.getName() +
                                           "\" created.");
        } else {
            request.setAttribute("notice", "Could not create a new topic.");
        }

        request.getRequestDispatcher("topic").forward(request, response);
    }

    /**
     * Returns a short description of this servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "This servlet is responsible for saving new topics.";
    }
}

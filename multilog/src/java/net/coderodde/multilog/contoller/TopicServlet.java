package net.coderodde.multilog.contoller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.Thread;
import net.coderodde.multilog.model.Topic;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.UserType;

/**
 * This servlet processes the topic-related views.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class TopicServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request.
     * @param response servlet response.
     *
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    protected void processRequest(final HttpServletRequest request,
                                  final HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = User.getCurrentlySignedUser(request);
        boolean isAdmin = false;

        if (currentUser == null) {
            HomeServlet.prepareNavibarForUnsignedUser(request);
        } else {
            HomeServlet.prepareNavibarForSingedUser(request, currentUser);

            if (currentUser.getUserType() == UserType.ADMIN) {
                isAdmin = true;
            }
        }

        String topicId = request.getParameter("id");

        // Below: try to show the threads of a particular topic.
        if (topicId != null) {
            long id = -1;

            try {
                id = Long.parseLong(topicId);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace(System.err);
                request.setAttribute("notice", "Bad topic ID.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }

            if (id != -1) {
                if (serveParticularThread(id, request)) {
                    request.getRequestDispatcher("threadsview.jsp")
                           .forward(request, response);
                    return;
                }
            }
        }

        if (isAdmin) {
            request.setAttribute("isAdmin", true);
        }

        request.setAttribute("notice", "isAdmin: " + isAdmin);

        // If here, show all topics.
        List<Topic> topicList = Topic.getAllTopics();
        request.setAttribute("topicList", topicList);
        request.getRequestDispatcher("topicview.jsp").forward(request, response);
    }

    /**
     * Serves a particular topic page if such exists.
     *
     * @param id the ID of the topic to fetch.
     * @param request the servlet request object.
     *
     * @return <code>true</code> if a topic with given ID exists and is renderd,
     * <code>false</code> otherwise.
     */
    private final boolean serveParticularThread
            (final long id, final HttpServletRequest request) {
        Topic topic = Topic.read(id);

        if (topic == null) {
            return false;
        }

        List<Thread> threadList = topic.getThreads();
        request.setAttribute("threadList", threadList);
        request.setAttribute("topic_name", topic.getName());
        return true;
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
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}

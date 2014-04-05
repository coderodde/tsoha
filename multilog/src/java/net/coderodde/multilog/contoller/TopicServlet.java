package net.coderodde.multilog.contoller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.Thread;
import net.coderodde.multilog.model.Topic;

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
        String topicId = request.getParameter("id");

        // Below: try to show the threads of a particular topic.
        if (topicId != null) {
            long id = -1;

            try {
                id = Long.parseLong(topicId);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace(System.err);
            }

            if (id != -1) {
                if (serveParticularThread(id, request)) {
                    request.getRequestDispatcher("threadview.jsp")
                           .forward(request, response);
                    return;
                }
            }
        }

        // If here, show all topics.
        List<Topic> topicList = Topic.getAllTopics();
//
//        List<Topic> shit = new ArrayList<Topic>();
//
//        shit.add(new Topic().setName("Maths").setCreatedAtTimestamp(new Timestamp(System.currentTimeMillis() + 1000000)));
//        shit.add(new Topic().setName("Computer science").setCreatedAtTimestamp(new Timestamp(System.currentTimeMillis())).setUpdatedAtTimestamp(new Timestamp(System.currentTimeMillis() + 500000)));

        request.setAttribute("topicList", topicList);
        request.getRequestDispatcher("topicview.jsp").forward(request, response);
    }

    private final boolean serveParticularThread
            (final long id, final HttpServletRequest request) {
        Topic topic = Topic.getTopicById(id);

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

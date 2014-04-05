package net.coderodde.multilog.contoller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.Topic;
import java.sql.Timestamp;

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

        List<Topic> shit = new ArrayList<Topic>();

        shit.add(new Topic().setName("Maths").setCreatedAtTimestamp(new Timestamp(System.currentTimeMillis() + 1000000)));
        shit.add(new Topic().setName("CSYO").setCreatedAtTimestamp(new Timestamp(System.currentTimeMillis())).setUpdatedAtTimestamp(new Timestamp(System.currentTimeMillis() + 500000)));

        request.setAttribute("topicList", shit);

//        if (topicId == null) {
//            request.setAttribute("topicList", Topic.getAllTopics());
//        }

        request.getRequestDispatcher("topicview.jsp").forward(request, response);
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

package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.Thread;

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
}

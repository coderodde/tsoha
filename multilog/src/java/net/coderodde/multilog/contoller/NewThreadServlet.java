package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.Thread;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * This servlet is responsible for creating new threads.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class NewThreadServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Basically, prints a warning.
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
                             "Creating a thread through GET-method is not " +
                             "supported. Please stop hacking.");
        request.getRequestDispatcher("threadsview.jsp")
               .forward(request, response);
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
        User currentUser = User.getCurrentlySignedUser(request);

        if (currentUser == null) {
            request.setAttribute("notice",
                                 "No current user. Please stop hacking.");
            request.getRequestDispatcher("threadsview.jsp")
                   .forward(request, response);
            return;
        }

        final String threadName = request.getParameter("thread_name");

        if (threadName == null || threadName.isEmpty()) {
            request.setAttribute("notice",
                                 "No thread given. Please stop hacking.");
            request.getRequestDispatcher("threadsview.jsp")
                   .forward(request, response);
            return;
        }

        final String escapedThreadName = escapeHtml4(threadName);

        Thread t = new Thread().setName(escapedThreadName);

        if (t.create()) {
            request.setAttribute("notice",
                                 "Thread '" + t.getName() + "' " +
                                 "created.");
        } else {
            request.setAttribute("notice",
                                 "Could not create thread '" +
                                 t.getName() + "'.");
        }

        request.getRequestDispatcher("threadsview.jsp")
               .forward(request, response);
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

package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.Thread;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.UserType;

/**
 * This servlet is responsible for deleting threads of <tt>multilog</tt>.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class DeleteThreadServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Basically prints a warning.
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
                             "Don't use GET-method for deleting threads. " +
                             "Please stop hacking.");
        request.getRequestDispatcher("threadsview.jsp")
               .forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method. If all input data is valid,
     * deletes a thread.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User currentUser = User.getCurrentlySignedUser(request);

        if (currentUser == null) {
            request.setAttribute("notice",
                                 "You are not signed in. " +
                                 "Please stop hacking.");
            request.getRequestDispatcher("threadsview.jsp")
                   .forward(request, response);
            return;
        }

        if (currentUser.getUserType() == UserType.USER) {
            // No privileges.
            request.setAttribute("notice",
                                 "You do not have the privileges " +
                                 "to delete a thread. " +
                                 "Please stop hacking.");
            request.getRequestDispatcher("threadsview.jsp")
                   .forward(request, response);
            return;
        }

        final String threadIdString = request.getParameter("thread_id");

        if (threadIdString == null || threadIdString.isEmpty()) {
            request.setAttribute("notice",
                                 "No thread ID.");
            request.getRequestDispatcher("threadsview.jsp")
                   .forward(request, response);
            return;
        }

        long id = -1L;

        try {
            id = Long.parseLong(threadIdString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace(System.err);
        }

        if (id == -1L) {
            request.setAttribute("notice",
                                 "Bad thread ID '" +
                                 threadIdString + "'.");
            request.getRequestDispatcher("threadsview.jsp")
                   .forward(request, response);
            return;
        }

        Thread t = Thread.read(id);

        if (t == null) {
            request.setAttribute("notice",
                                 "No thread with ID '" +
                                 id + "'.");
            request.getRequestDispatcher("threadsview.jsp")
                   .forward(request, response);
            return;
        }

        if (t.delete()) {
            request.setAttribute("notice",
                                 "Deleted thread '" +
                                 t.getName() + "'.");
        } else {
            request.setAttribute("notice",
                                 "Could not delete thread '" +
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

package net.coderodde.multilog.contoller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.User;

/**
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class AccountServlet extends HttpServlet {

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
        final String idString = request.getParameter("id");

        long targetId = -1;
        User who = null;

        if (idString == null || idString.isEmpty()) {
            who = User.getCurrentlySignedUser(request);
        } else {
            try {
                targetId = Long.parseLong(idString);
                who = User.read(targetId);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace(System.err);
            }
        }

        if (who == null) {
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        request.setAttribute("username", who.getUsername());
        request.setAttribute("posts", "0");
        request.setAttribute("joined", who.getCreatedAt().toString());
        request.setAttribute("updated", who.getUpdatedAt().toString());

        if (who.getShowEmail()) {
            request.setAttribute("email", who.getEmail());
        } else {
            request.setAttribute("email", "&lt;hidden&gt;");
        }

        if (who.getShowRealName()) {
            request.setAttribute("real_name",
                                 who.getFirstName() + " " + who.getLastName());
        } else {
            request.setAttribute("real_name", "&lt;hidden&gt;");
        }

        request.setAttribute("level", who.getUserType().toString());
        request.setAttribute("description", who.getDescription());

        User current = User.getCurrentlySignedUser(request);

        if (current != who) {
            request.setAttribute("closeBegin", "<!--");
            request.setAttribute("closeEnd", "-->");
        }

        request.getRequestDispatcher("account.jsp").forward(request, response);
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
        return "This servlet forwards to user pages.";
    }
}

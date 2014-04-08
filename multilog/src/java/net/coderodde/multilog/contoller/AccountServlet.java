package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.UserType;

/**
 * This servlet handles the account related activities.
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
        User current = User.getCurrentlySignedUser(request);

        if (idString == null || idString.isEmpty()) {
            who = current;
        } else {
            try {
                targetId = Long.parseLong(idString);
                who = User.read(targetId);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace(System.err);
            }
        }

        if (who == null) {
            request.setAttribute("notice", "Nothing to view.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        if (current != null) {
            HomeServlet.prepareNavibarForSingedUser(request, current);
        } else {
            HomeServlet.prepareNavibarForUnsignedUser(request);
        }

        if (who.equals(current)) {
            request.setAttribute("edit", true);
            request.setAttribute("candelete", true);
        } else {
            if (current.getUserType().equals(UserType.ADMIN)) {
                request.setAttribute("candelete", true);
            }
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

        // Here 'who' is by no means 'null', but 'current' may be.
        if (current == who) {
            request.setAttribute("edit", "true");
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
        return "This servlet coordinates the account page related activities.";
    }
}

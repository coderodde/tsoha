package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Config;
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
            serveAsMyOwnEdibleView(request, who);
        } else {
            if (current.getUserType().equals(UserType.ADMIN)) {
                serveViewForAdmin(request, who, current);
            } else {
                serveAsNonedibleView(request, who);
            }
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

    static void serveAsNonedibleView(final HttpServletRequest request,
                                     final User who) {
        request.setAttribute("view", true);

        // Other attributes.
        request.setAttribute("posts", 0);
        request.setAttribute("joined", who.getCreatedAt());
        request.setAttribute("updated", who.getUpdatedAt());

        if (who.getShowEmail()) {
            request.setAttribute("email", who.getEmail());
        } else {
            request.setAttribute("email", Config.HTML_MAGIC.HIDDEN);
        }

        if (who.getShowRealName()) {
            request.setAttribute("real_name",
                                 who.getFirstName() + " " + who.getLastName());
        } else {
            request.setAttribute("real_name", Config.HTML_MAGIC.HIDDEN);
        }

        request.setAttribute("level", who.getUserType().toString());
        request.setAttribute("description", who.getDescription());
    }

    static void serveAsMyOwnEdibleView(final HttpServletRequest request,
                                       final User who) {
        request.setAttribute("edit", true);
        request.setAttribute("candelete", true);

        request.setAttribute("current_firstname", who.getFirstName());
        request.setAttribute("currnet_lastname", who.getLastName());
        request.setAttribute("current_description", who.getDescription());
        request.setAttribute("current_email", who.getEmail());

        request.setAttribute("current_show_real_name",
                             who.getShowRealName() ? "checked" : "");

        request.setAttribute("current_show_email",
                             who.getShowEmail() ? "checked" : "");
    }

    static void serveViewForAdmin(final HttpServletRequest request,
                                  final User who,
                                  final User admin) {
        serveAsNonedibleView(request, who);
        request.setAttribute("myid", who.getId());
        request.setAttribute("candelete", true);
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

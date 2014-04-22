package net.coderodde.multilog.contoller;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Config;
import net.coderodde.multilog.model.MessageRead;
import net.coderodde.multilog.model.Thread;
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
        User currentUser = User.getCurrentlySignedUser(request);
        User targetUser = null;
        long id = -1;

        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace(System.err);

            if (currentUser != null) {
                serveAsMyOwnEdibleView(request, currentUser);
            }

            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        // Once here, 'id' parsed well.

        targetUser = User.read(id);

        if (targetUser == null) {
            if (currentUser != null) {
                serveAsMyOwnEdibleView(request, currentUser);
            }

            request.setAttribute("notice", "No account with ID '" + id + "'.");
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        // Once here, targetUser is not null.
        if (currentUser != null) {
            // Here neither currentUser nor targetUser are null.

            if (currentUser.equals(targetUser)) {
                serveAsMyOwnEdibleView(request, targetUser);
                request.getRequestDispatcher("account.jsp")
                       .forward(request, response);
                return;
            }

            serveAsNonedibleView(request, targetUser);

            if (currentUser.getUserType() == UserType.MOD) {
                serveViewForModerator(request, targetUser, currentUser);
            } else if (currentUser.getUserType() == UserType.ADMIN) {
                serveViewForAdmin(request, targetUser, currentUser);
            }

            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
        } else {
            serveAsNonedibleView(request, targetUser);
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
        }
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
     * Serves the account as a non-edible view.
     *
     * @param request the request object.
     * @param who the account being viewed.
     */
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

    /**
     * Serves the account of currently signed in user. The resulting view
     * is edible.
     *
     * @param request the request object.
     * @param who the account being viewed/edited.
     */
    static void serveAsMyOwnEdibleView(final HttpServletRequest request,
                                       final User who) {
        request.setAttribute("edit", true);
        request.setAttribute("candelete", true);

        request.setAttribute("current_firstname", who.getFirstName());
        request.setAttribute("current_lastname", who.getLastName());
        request.setAttribute("current_description", who.getDescription());
        request.setAttribute("current_email", who.getEmail());

        request.setAttribute("current_show_real_name",
                             who.getShowRealName() ? "checked" : "");

        request.setAttribute("current_show_email",
                             who.getShowEmail() ? "checked" : "");

        request.setAttribute("target_id", who.getId());

        Map<Thread, Integer> map = MessageRead.findUpdatedThreads(who);

        if (map.size() > 0) {
            request.setAttribute("doshow", true);
            request.setAttribute("post_updates", "Post updates:");
        }

        request.setAttribute("threadMap", map);
    }

    static void serveViewForModerator(final HttpServletRequest request,
                                      final User who,
                                      final User mod) {
        serveAsNonedibleView(request, who);

        if (who.getUserType() == UserType.USER) {
            request.setAttribute("can_ban", true);
            request.setAttribute("can_promote_to_moderator", true);
        }
    }

    /**
     * Serve a view for administrator.
     *
     * @param request the request object.
     * @param who the target account.
     * @param admin the administrator account.
     */
    static void serveViewForAdmin(final HttpServletRequest request,
                                  final User who,
                                  final User admin) {
        serveAsNonedibleView(request, who);

        if (who.getUserType() == UserType.USER) {
            request.setAttribute("can_promote_to_moderator", true);
            request.setAttribute("can_promote_to_admin", true);
            request.setAttribute("can_ban", true);
        } else if (who.getUserType() == UserType.MOD) {
            request.setAttribute("can_promote_to_admin", true);
            request.setAttribute("can_degrade_to_user", true);
            request.setAttribute("can_ban", true);
        }

        request.setAttribute("target_id", who.getId());
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

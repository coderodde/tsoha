package net.coderodde.multilog.contoller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.UserType;

/**
 * This servlet is responsible for banning users.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class BanServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Basically shows a warning.
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
                             "Banning users through GET-method is not " +
                             "supported. Please stop hacking.");
        request.getRequestDispatcher("account.jsp").forward(request, response);
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
        final User currentUser = User.getCurrentlySignedUser(request);

        if (currentUser == null) {
            request.setAttribute("notice",
                                 "You are not signed in. Please stop hacking.");
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        if (currentUser.getUserType() == UserType.USER) {
            request.setAttribute("notice",
                                 "You don't have the privileges to ban " +
                                 "anyone.");
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        final String durationString = request.getParameter("ban_hours");

        if (durationString == null || durationString.isEmpty()) {
            request.setAttribute("notice", "No ban duration given.");
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        double duration = -1;

        try {
            duration = Double.parseDouble(durationString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace(System.err);
            request.setAttribute("notice", "Bad duration: " + durationString);
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        if (Double.isInfinite(duration)
                || Double.isNaN(duration)
                || duration <= 0.0) {
            request.setAttribute("notice", "Bad duration: " + duration);
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        final String targetUserIdString = request.getParameter("banned_id");

        if (targetUserIdString == null || targetUserIdString.isEmpty()) {
            request.setAttribute("notice", "No ID given for banning.");
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        long userId = -1L;

        try {
            userId = Long.parseLong(targetUserIdString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace(System.err);
            request.setAttribute("notice",
                                 "Given ID is not valid: " +
                                 targetUserIdString);
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        final User targetUser = User.read(userId);

        if (targetUser == null) {
            request.setAttribute("notice",
                                 "No user with ID '" + userId + "'.");
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        // At this point current user is mod or admin.
        // (See the beginning of this method.)
        if (currentUser.getUserType() == UserType.MOD) {
            if (targetUser.getUserType() == UserType.USER) {
                // Privileges OK.
                if (targetUser.ban(duration)) {
                    request.setAttribute("notice", "Ban applied.");
                } else {
                    request.setAttribute("notice", "Banning as a mod failed.");
                }
            } else {
                // No privileges.
                request.setAttribute("notice",
                                     "Not enough privileges to ban. " +
                                     "Please stop hacking.");
            }
        } else if (currentUser.getUserType() == UserType.ADMIN) {
            if (targetUser.getUserType() == UserType.USER
                    || targetUser.getUserType() == UserType.MOD) {
                // Privileges OK.
                if (targetUser.ban(duration)) {
                    request.setAttribute("notice", "Ban applied.");
                } else {
                    request.setAttribute("notice",
                                         "Banning as an admin failed.");
                }
            } else {
                // No privileges.
                request.setAttribute("notice",
                                     "Not enough privileges to ban. " +
                                     "Please stop hacking.");
            }
        } else {
            // This should not happen. Ever.
            throw new IllegalStateException("Unknown user type.");
        }

        HomeServlet.prepareNavibarForSingedUser(request, currentUser);
        request.getRequestDispatcher("account.jsp").forward(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "This servlet is responsible for banning users.";
    }
}

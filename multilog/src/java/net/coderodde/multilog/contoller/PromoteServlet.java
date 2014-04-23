package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.UserType;

/**
 * This servlet is responsible for managing privilege levels.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class PromoteServlet extends HttpServlet {

    /**
     * This method handles <code>GET</code>-requests. Basically, shows a
     * warning.
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
                             "No promotions through GET-method. " +
                             "Please stop hacking.");
        request.getRequestDispatcher("home").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method. Does the actual work.
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
                                 "You must be at least signed in as to " +
                                 "share the promotions.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        if (currentUser.getUserType() == UserType.USER) {
            request.setAttribute("notice",
                                 "Sorry, but you must be at least a " +
                                 "moderator as to share promotions.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        final String promotionLevel = request.getParameter("promotion_level");

        if (promotionLevel == null || promotionLevel.isEmpty()) {
            request.setAttribute("notice",
                                 "Target privilege level unknown. " +
                                 "Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        final String targetUserIdString = request.getParameter("promoted_id");

        if (targetUserIdString == null || targetUserIdString.isEmpty()) {
            request.setAttribute("notice",
                                 "Target user ID missing. " +
                                 "Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        long targetUserId = -1L;

        try {
            targetUserId = Long.parseLong(targetUserIdString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace(System.err);
            request.setAttribute("notice",
                                 "Invalid user ID: " + targetUserIdString);
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        final User targetUser = User.read(targetUserId);

        if (targetUser == null) {
            request.setAttribute("notice",
                                 "No user with ID '" + targetUserId + "'.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        } else if (targetUser.getUserType() == UserType.ADMIN) {
            request.setAttribute("notice",
                                 "You cannot tamper with administrators.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        // Here, currentUser is either mod or admin.
        // targetUser is not admin.
        if (currentUser.getUserType() == UserType.MOD) {
            if (targetUser.getUserType() == UserType.USER) {
                if ("mod".equals(promotionLevel) == false) {

                    request.setAttribute("notice",
                                         "Cannot promote to anything else " +
                                         "but moderator. Stop hacking.");
                    request.getRequestDispatcher("home")
                           .forward(request, response);
                    return;
                }

                // Valid. Promote user to moderator.
                promote(targetUser, UserType.MOD, request);

                request.getRequestDispatcher(
                        "account?id=" + targetUser.getId())
                        .forward(request, response);
                return;
            } else {
                request.setAttribute("notice",
                                     "Cannot promote. Please stop hacking.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }
        } else if (currentUser.getUserType() == UserType.ADMIN) {
            if (targetUser.getUserType() == UserType.USER) {
                if ("mod".equals(promotionLevel)) {

                    promote(targetUser, UserType.MOD, request);

                } else if ("admin".equals(promotionLevel)) {

                    promote(targetUser, UserType.ADMIN, request);

                } else {
                    throw new IllegalStateException(
                            "Unknown promotion level: " + promotionLevel);
                }
            } else if (targetUser.getUserType() == UserType.MOD) {
                if ("admin".equals(promotionLevel)) {

                    promote(targetUser, UserType.ADMIN, request);

                } else if ("user".equals(promotionLevel)) {

                    promote(targetUser, UserType.USER, request);

                } else {
                    throw new IllegalArgumentException(
                            "Unknown promotion level: " + promotionLevel);
                }
            }
        } else {
            throw new IllegalStateException(
                    "Unknown user type: " + currentUser.getUserType());
        }

        request.getRequestDispatcher("account?id=" + targetUser.getId())
               .forward(request, response);
    }

    private static final void promote(final User targetUser,
                                      final UserType type,
                                      final HttpServletRequest request) {
        targetUser.setUserType(type);

        String verbSuccess = (type.toString().equalsIgnoreCase("user") ?
                              "downgraded" : "promoted");

        String verbFailure = (type.toString().equalsIgnoreCase("user") ?
                              "downgrade" : "promote");

        if (targetUser.update()) {
            request.setAttribute("notice",
                                 "'" + targetUser.getUsername() + "' " +
                                 verbSuccess + " to " +
                                 type.toString().toLowerCase() + ".");
        } else {
            request.setAttribute("notice",
                                 "Could not " + verbFailure + " '" +
                                 targetUser.getUsername() + "' to " +
                                 type.toString().toLowerCase() + ".");
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "This servlet manages the privilege levels.";
    }
}

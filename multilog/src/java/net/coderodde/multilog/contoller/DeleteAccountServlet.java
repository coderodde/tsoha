package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Utils;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.UserType;

/**
 * This servlet handles the requests to delete an account.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class DeleteAccountServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Basically redirects back to
     * the home view with a notice.
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
                             "Deleting an account is not supported through " +
                             "the GET - method.");
        request.getRequestDispatcher("home").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method as to delete an account.
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
        User current = Utils.getSignedUser(request);

        if (current == null) {
            request.setAttribute("notice", "Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        final String whoId = request.getParameter("user_id");
        User userToDelete = null;

        if (whoId == null || whoId.isEmpty()) {
            userToDelete = current;
        } else {
            long id = -1;

            try {
                id = Long.parseLong(whoId);
            } catch (NumberFormatException nfe) {
                nfe.printStackTrace(System.err);
                request.setAttribute("notice",
                                     whoId + " is not a valid user ID.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }

            userToDelete = User.read(id);

            if (userToDelete == null) {
                request.setAttribute("notice",
                                     id + " is not a valid user ID.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }
        }

        // Here 'userToDelete' is by no means 'null'.
        if (current.equals(userToDelete)) {
            // Deleting himself.
            boolean removed = userToDelete.delete();

            if (removed) {
                request.setAttribute("notice",
                                     userToDelete.getUsername() + " removed." + userToDelete.getId());
            } else {
                request.setAttribute("notice",
                                     "Could not remove " +
                                     userToDelete.getUsername() + "." + userToDelete.getId());
            }

            request.getRequestDispatcher("home").forward(request, response);
            return;
        } else {
            // Check that current user has administrator privileges.
            if (current.getUserType().equals(UserType.ADMIN)) {
                // Privilege level OK.
                boolean removed = userToDelete.delete();

                if (removed) {
                    request.setAttribute("notice",
                                         "You successfully removed " +
                                         userToDelete.getUsername() + ".");
                } else {
                    request.setAttribute("notice",
                                         "Could not removed " +
                                         userToDelete.getUsername() + ".");
                }

                request.getRequestDispatcher("home").forward(request, response);
                return;
            } else {
                request.setAttribute("notice",
                                     "You don't have the privileges to " +
                                     "delete other accounts.");
                request.getRequestDispatcher("home").forward(request, response);
                return;
            }
        }
    }

    /**
     * Returns a short description of this servlet.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "This servlet is responsible for removing the users.";
    }
}

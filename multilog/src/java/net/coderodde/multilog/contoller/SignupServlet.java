package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Config;
import net.coderodde.multilog.Utils;
import net.coderodde.multilog.model.User;

/**
 * This servlet handles the sign up process.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class SignupServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method which renders the sign up page.
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
        if (Utils.getSignedUser(request) != null) {
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        HomeServlet.prepareNavibarForUnsignedUser(request);
        request.getRequestDispatcher("signup.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method which denotes the attempt to
     * sign up.
     *
     * @param request servlet request.
     * @param response servlet response.
     *
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void doPost
            (final HttpServletRequest request,
             final HttpServletResponse response)
            throws ServletException, IOException {
        if (Utils.getSignedUser(request) != null) {
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        HomeServlet.prepareNavibarForUnsignedUser(request);

        // Try to sign up a new user.
        final String username =
                (String) request.getAttribute(Config.SESSION_MAGIC.USERNAME);

        final User other = User.getByUsername(username);

        if (other != null) {
            // Once here, the username is already occupied.
            request.setAttribute("bad_username", "Username already in use.");
            request.getRequestDispatcher("singup.jsp")
                   .forward(request, response);
            return;
        }

        if (username.length() < Config.MINIMUM_USERNAME_LENGTH) {
            request.setAttribute("bad_username",
                                 "User name must be at least " +
                                 Config.MINIMUM_USERNAME_LENGTH +
                                 " characters long.");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        final String password =
                (String) request.getAttribute(Config.SESSION_MAGIC.PASSWORD);

        if (Utils.isValidPassword(password) == false) {
            request.setAttribute("bad_password", "Invalid password.");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }
    }

    /**
     * Returns a short description of this servlet.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "This servlet facilitates the sign up process.";
    }
}

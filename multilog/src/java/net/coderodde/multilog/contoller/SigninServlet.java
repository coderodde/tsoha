package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Config;
import net.coderodde.multilog.model.User;

/**
 * This servlet authenticates the users, and upon successful authentication
 * creates a session for the user.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class SigninServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Basically, shows a message
     * discouraging authentication through <tt>GET</tt> - method.
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

        // Kinda rough, but nevertheless log out the user once he/she is here.
        request.getSession()
               .removeAttribute(Config.SESSION_MAGIC.SIGNED_IN_USER_ATTRIBUTE);

        HomeServlet.prepareNavibarForUnsignedUser(request);
        request.getRequestDispatcher("signin.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method containing the signing data.
     *
     * @param request servlet request.
     * @param response servlet response.
     *
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter(Config.SESSION_MAGIC.USERNAME);
        String password = request.getParameter(Config.SESSION_MAGIC.PASSWORD);

        if (username == null
                || password == null
                || username.isEmpty()
                || password.isEmpty()) {
            StringBuilder sb = new StringBuilder(1024);
            int c = 0;

            if (username == null || username.isEmpty()) {
                sb.append("User name is missing.");
                c++;
            }

            if (password == null || password.isEmpty()) {
                if (c == 1) {
                    sb.append("<br>");
                }

                sb.append("Password is missing.");
            }

            request.setAttribute("notice", sb.toString());
            request.getRequestDispatcher("signin.jsp")
                   .forward(request, response);
            return;
        }

        User user = User.read(username);

        if (user == null) {
            request.setAttribute("notice",
                                 "Authentication failed! User " + username +
                                 " does not exist.");
            request.getRequestDispatcher("signin.jsp")
                   .forward(request, response);
            return;
        }

        user.setPassword(password);

        if (user.currentPasswordIsValid()) {
            request.getSession().setAttribute(Config.
                                              SESSION_MAGIC.
                                              SIGNED_IN_USER_ATTRIBUTE, user);
            request.setAttribute("notice",
                                 "Signed in as " + user.getUsername() + "!");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        request.setAttribute("notice",
                             "Authentication failed! Check username and " +
                             "password combination.");
        request.getRequestDispatcher("signin").forward(request, response);
    }

    /**
     * Returns a short description of this servlet.
     *
     * @return a servlet description.
     */
    @Override
    public String getServletInfo() {
        return "This servlet checks the login data and manages the access to " +
               "multilog.";
    }
}

package net.coderodde.multilog.contoller;

import java.io.IOException;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.coderodde.multilog.Config;
import net.coderodde.multilog.model.DB;
import net.coderodde.multilog.model.User;

/**
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class LoginServlet extends HttpServlet {

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
        response.getOutputStream().println(
              "<h1>Please don't use GET method for signing in multilog.</h1>");
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
    protected void doPost(final HttpServletRequest request,
                          final HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter(Config.SESSION_MAGIC.USERNAME);
        String password = request.getParameter(Config.SESSION_MAGIC.PASSWORD);

        if (username == null || password == null) {
            request.setAttribute("notice", "Line 58");
            request.getRequestDispatcher("signin.jsp")
                   .forward(request, response);
            return;
        }

        User user = null;

        try {
            user = DB.getDatabase().getUser(username, password);
        } catch (NamingException ne) {
            ne.printStackTrace(System.err);
            request.setAttribute("notice", "Line 70");
            request.getRequestDispatcher("signin.jsp")
                   .forward(request, response);
            return;
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            request.setAttribute("notice", "Line 77");
            request.getRequestDispatcher("signin.jsp")
                   .forward(request, response);
            return;
        }

        if (user == null || user == DB.BAD_PASSWORD_USER) {
            request.setAttribute("notice", "Line 86");
            request.getRequestDispatcher("signin.jsp")
                   .forward(request, response);
            return;
        }

        HttpSession session = request.getSession();

        session.setAttribute(Config.SESSION_MAGIC.SIGNED_IN_USER_ATTRIBUTE,
                             user);

        request.setAttribute("notice", "Success!");
        request.getRequestDispatcher("home").forward(request, response);
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

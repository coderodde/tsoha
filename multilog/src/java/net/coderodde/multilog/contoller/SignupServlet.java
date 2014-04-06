package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Config;
import net.coderodde.multilog.Utils;

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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (Utils.getSignedUser(request) != null) {
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        HomeServlet.prepareNavibarForUnsignedUser(request);

        // Try to sign up a new user.
        String username =
                (String) request.getAttribute(Config.SESSION_MAGIC.USERNAME);
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

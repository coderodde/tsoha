package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import net.coderodde.multilog.Config;
import net.coderodde.multilog.model.User;

/**
 *
 * @author rodionefremov
 */
public class HomeServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            prepareNavibarForUnsignedUser(request);
        } else {
            User user = (User) session.getAttribute(Config.
                                                    SESSION_MAGIC.
                                                    SIGNED_IN_USER_ATTRIBUTE);

            if (user == null) {
                prepareNavibarForUnsignedUser(request);
            } else {
                prepareNavibarForSingedUser(request, user);
            }
        }

        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    private void prepareNavibarForUnsignedUser
            (final HttpServletRequest request) {
            request.setAttribute("right_left", "Sign in");
            request.setAttribute("right_left_url", "http://www.ya.ru");
            request.setAttribute("right_right", "Sign up");
            request.setAttribute("right_right_url", "http://www.yandex.ru");
    }

    private void prepareNavibarForSingedUser
            (final HttpServletRequest request, final User user) {
        request.setAttribute("right_left", "Sign out");
        request.setAttribute("right_left_url", "http://www.ya.ru");
        request.setAttribute("right_right", "Account");
        request.setAttribute("right_right_url", "http://www.yandex.ru");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}

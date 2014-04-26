package net.coderodde.multilog.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Config;
import static net.coderodde.multilog.Utils.closeResources;
import net.coderodde.multilog.model.DB;

/**
 *
 * @author rodionefremov
 */
public class AvatarServlet extends HttpServlet {

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
        response.setContentType("image/png");

        final String idString = request.getParameter("id");

        if (idString == null || idString.isEmpty()) {
            return;
        }

        long id = -1L;

        try {
            id = Long.parseLong(idString);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace(System.err);
            return;
        }

        Connection connection = DB.getConnection();

        if (connection == null) {
            return;
        }

        PreparedStatement ps = DB.getPreparedStatement(connection,
                                                       Config.
                                                       SQL_MAGIC.
                                                       GET_AVATAR_BY_USER_ID);

        if (ps == null) {
            closeResources(connection, null, null);
            return;
        }

        ResultSet rs = null;
        byte[] bytes = null;

        try {
            ps.setLong(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                bytes = rs.getBytes("image");
            }

            closeResources(connection, ps, rs);
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, rs);
            return;
        }

        if (bytes != null) {
            response.getOutputStream().write(bytes);
        }
    }

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
    }
}

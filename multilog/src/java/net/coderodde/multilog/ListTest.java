package net.coderodde.multilog;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.model.DB;

/**
 *
 * @author rodionefremov
 */
public class ListTest extends HttpServlet {

    private static final String SELECT_ALL_USERS = "SELECT username, email FROM users;";

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        /* TODO output your page here. You may use following sample code. */
        out.println("<html>");
        out.println("<head>");
        out.println("<title>ConnectionTest</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet ConnectionTest at " + request.getContextPath() + "</h1>");

        out.println(getUsersFromDB());

        try {
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    protected String getUsersFromDB() {
        Connection conn = DB.getConnection();

        if (conn == null) {
            return "Connection failed.";
        }

        Statement statement = null;

        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(SELECT_ALL_USERS);
            String resultText = getResults(rs);
            rs.close();
            statement.close();
            conn.close();
            return resultText;
        } catch (SQLException sqle) {
            try {
                conn.close();
            } catch (SQLException sqle2) {
                // ignore.
            }

            return "SQLException 2: " + sqle.getMessage();
        }
    }

    String getResults(ResultSet rs) {
        StringBuilder sb = new StringBuilder(8192);
        sb.append("<table>\n")
          .append("<tr><td>Username</td><td>email</td></tr>\n");

        try {
            while (rs.next()) {
                sb.append("<tr><td>")
                  .append(rs.getString(1))
                  .append("</td><td>")
                  .append(rs.getString(2))
                  .append("</td></tr>\n");
            }
        } catch (SQLException sqle) {
            return "SQLException (beef): " + sqle.getMessage();
        }

        sb.append("</table>\n");
        return sb.toString();
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
        return "This is a simple DB-connection test.";
    }// </editor-fold>
}

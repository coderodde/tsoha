import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.postgresql.ds.PGPoolingDataSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author rodionefremov
 */
public class ArticleServlet extends HttpServlet {

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
        out.println("<title>Servlet ArticleServlet</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>Servlet ArticleServlet at " + request.getContextPath() + "</h1>");

        out.println(getArticlesFromDB());

        try {
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }

    protected String getArticlesFromDB() {
        InitialContext ic = null;
        DataSource ds = null;

        try {
            ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:/comp/env/jdbc/mlogDB");
        } catch (NamingException ne) {
            return "NamingException: " + ne.getMessage();
        }

        Connection conn = null;

        try {
            conn = ds.getConnection();
        } catch (SQLException sqle) {
            return "SQLException: " + sqle.getMessage();
        }

        Statement statement = null;

        try {
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM article;");



            String resultText = getResults(rs);
            statement.close();
            return resultText;
        } catch (SQLException sqle) {
            try {
                conn.close();
            } catch (SQLException sqle2) {
                // ignore.
            }

            return "SQLException: " + sqle.getMessage();
        }
    }

    String getResults(ResultSet rs) {
        StringBuilder sb = new StringBuilder(8192);
        sb.append("<table>\n");

        try {
            while (rs.next()) {
                sb.append("<tr><td>")
                  .append(rs.getString(2))
                  .append("</td><td>")
                  .append(rs.getString(3))
                  .append("</td><td>")
                  .append(rs.getDate(4))
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
        return "Short description";
    }// </editor-fold>
}

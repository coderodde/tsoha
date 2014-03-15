import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.postgresql.ds.PGPoolingDataSource;

import java.io.IOException;
import java.io.PrintWriter;
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
//        PGPoolingDataSource source = new PGPoolingDataSource();
//        source.setDataSourceName("PGDSfdsdfa");
//        source.setServerName("users.cs.helsinki.fi");
//        source.setDatabaseName("rodionef");
//        source.setUser("rodionef");
//        source.setPassword("ccac8eaa12c53175");
//        source.setMaxConnections(100);
//
//        Connection conn = null;
//
//        try {
//            conn = source.getConnection();
//        } catch (SQLException e) {
//            return "SQLException: " + e.getMessage();
//        }
//
//        try {
//            conn.close();
//        } catch (SQLException e) {
//            return "SQLException (close): " + e.getMessage();
//        }
//
//        return "DB is working!";

//        Context cxt = null;
//        Context cxt2 = null;
//        DataSource ds = null;
//
//        try {
//            cxt = new InitialContext();
//            cxt2 = (Context) cxt.lookup("java:/comp/env");
//            ds = (DataSource) cxt2.lookup("jdbc/rodionef");
////            ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/rodionef");
//        } catch (NamingException ne) {
//            return "NamingException: " + ne.getMessage();
//        }
//
//        Connection conn = null;
//
//        try {
//            conn = ds.getConnection();
//        } catch (SQLException sqle) {
//            return "SQLException: " + sqle.getMessage();
//        }
//
//        try {
//            conn.close();
//        } catch (SQLException e) {
//            return "SQLException (closing): " + e.getMessage();
//        }
//
//        return "Database is here!";

        InitialContext ic = null;
        DataSource ds = null;

        try {
            ic = new InitialContext();
            ds = (DataSource) ic.lookup("java:comp/env/jdbc/mlogDB");
        } catch (NamingException ne) {
            return "NamingException: " + ne.getMessage();
        }
        
        Connection conn = null;

        try {
            conn = ds.getConnection();
        } catch (SQLException sqle) {
            return "SQLException: " + sqle.getMessage();
        }

        try {
            conn.close();
        } catch (SQLException e) {
            return "SQLException (closing): " + e.getMessage();
        }

        return "Database is here!";
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

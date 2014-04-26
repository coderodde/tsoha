package net.coderodde.multilog.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Utils;
import net.coderodde.multilog.model.Post;
import net.coderodde.multilog.model.Thread;

/**
 * This servlet is responsible for searching content from <tt>multilog</tt>.
 *
 * @author Rodion Efremov
 * @version 1.6
 */
public class SearchServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request.
     * @param response servlet response.
     *
     * @throws ServletException if a servlet-specific error occurs.
     * @throws IOException if an I/O error occurs.
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Utils.prepareNavibar(request);
        final String query = request.getParameter("query");

        if (query == null || query.isEmpty()) {
            request.setAttribute("notice", "No query given.");
            request.getRequestDispatcher("search.jsp")
                   .forward(request, response);
            return;
        }

        List<Post> postList = Post.getPostsByRegex(query);
        Map<Thread, Integer> map = new TreeMap<Thread, Integer>(Thread.tc);

        for (final Post post : postList) {
            final Thread thread = post.getThread();

            if (map.containsKey(thread) == false) {
                map.put(thread, 1);
            } else {
                map.put(thread, map.get(thread) + 1);
            }
        }

        if (map.isEmpty()) {
            request.setAttribute("title", "No results.");
        } else if (map.size() == 1) {
            request.setAttribute("title", "1 result.");
        } else if (map.size() > 1) {
            request.setAttribute("title", map.size() + " results.");
        }

        // Carry the query to the next view.
        request.setAttribute("query_value", query);
        request.setAttribute("result_map", map);
        request.getRequestDispatcher("search.jsp").forward(request, response);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
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
        processRequest(request, response);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "This servlet is responsible for searching content.";
    }
}

package net.coderodde.multilog.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        final String query = request.getParameter("query");

        if (query == null || query.isEmpty()) {
            request.setAttribute("notice", "No query given.");
            request.getRequestDispatcher("search.jsp").forward(request, response);
            return;
        }

        Set<Thread> threadSet = new HashSet<Thread>();
        List<Post> postList = Post.getPostsByRegex(query);

        for (final Post post : postList) {
            threadSet.add(post.getThread());
        }

        if (threadSet.isEmpty()) {
            request.setAttribute("title", "No results.");
        } else if (threadSet.size() == 1) {
            request.setAttribute("title", "1 result.");
        } else if (threadSet.size() > 1) {
            request.setAttribute("title", threadSet.size());
        }

        List<Thread> threadList = new ArrayList<Thread>(threadSet);
        Collections.sort(threadList, Thread.tc);
        request.setAttribute("resultList", threadList);
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

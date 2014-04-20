package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Config;
import net.coderodde.multilog.Utils;
import net.coderodde.multilog.model.User;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * This servlet is responsible for updating the user accounts.
 *
 * @author Rodion Efremov
 * @version 0.1
 */
public class UpdateAccountServlet extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method. Basically, shows a warning.
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
        request.setAttribute("notice",
                             "Updating an account is not supported through " +
                             "the GET - method.");
        request.getRequestDispatcher("home").forward(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method. Does the actual job.
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
        User currentUser = Utils.getSignedUser(request);

        if (currentUser == null) {
            request.setAttribute("notice", "Please stop hacking.");
            request.getRequestDispatcher("home").forward(request, response);
            return;
        }

        // Once here, 'currentUser' is not null.
        HomeServlet.prepareNavibarForSingedUser(request, currentUser);

        boolean hasErrors = false;

        String firstName =
                request.getParameter(Config.SESSION_MAGIC.FIRST_NAME);

        String lastName =
                request.getParameter(Config.SESSION_MAGIC.LAST_NAME);

        String description =
                request.getParameter(Config.SESSION_MAGIC.DESCRIPTION);

        final String email =
                request.getParameter(Config.SESSION_MAGIC.EMAIL);

        if (Utils.isValidEmail(email) == false) {
            request.setAttribute("bad_email", "Invalid email address.");
            hasErrors = true;
        }

        // HMTL-escaping.
        firstName = escapeHtml4(firstName).trim();
        lastName = escapeHtml4(lastName).trim();
        description = escapeHtml4(description).trim();

        final String showRealName =
                request.getParameter(Config.SESSION_MAGIC.SHOW_REAL_NAME);

        final String showEmail =
                request.getParameter(Config.SESSION_MAGIC.SHOW_EMAIL);

        final boolean doShowRealName = "on".equalsIgnoreCase(showRealName);
        final boolean doShowEmail = "on".equalsIgnoreCase(showEmail);

        final String password =
                request.getParameter(Config.SESSION_MAGIC.PASSWORD);

        final String passwordConfirmation = request
                .getParameter(Config.SESSION_MAGIC.PASSWORD_CONFIRMATION);

        boolean doChangePassword = false;

        if (password != null && !password.isEmpty()) {
            doChangePassword = true;

            if (!Utils.isValidPassword(password)) {
                request.setAttribute("bad_new_password", "Invalid password.");
                doChangePassword = false;
            }

            if (!password.equals(passwordConfirmation)) {
                request.setAttribute("bad_new_password_confirmation",
                                     "Confirmation differs from password.");
                doChangePassword = false;
            }
        } else if (passwordConfirmation != null
                && !passwordConfirmation.isEmpty()) {
            request.setAttribute("bad_new_password", "Password is missing.");
        }

        if (hasErrors) {
            AccountServlet.serveAsMyOwnEdibleView(request, currentUser);
            request.getRequestDispatcher("account.jsp")
                   .forward(request, response);
            return;
        }

        StringBuilder sb = new StringBuilder();

        if (doChangePassword) {
            if (currentUser.changePassword(password)) {
                sb.append("Password changed.");
            } else {
                sb.append("Could not change the password.");
            }
        }

        currentUser.setFirstName(firstName)
                   .setLastName(lastName)
                   .setEmail(email)
                   .setShowRealName(doShowRealName)
                   .setShowEmail(doShowEmail)
                   .setDescription(description);

        sb.append("<br>");

        if (currentUser.update()) {
            sb.append("Account updated.");
        } else {
            sb.append("Could not update the account.");
        }

        request.setAttribute("notice", sb.toString());
        AccountServlet.serveAsMyOwnEdibleView(request, currentUser);
        request.getRequestDispatcher("account.jsp")
               .forward(request, response);
    }

    /**
     * Returns a short description of this servlett.
     *
     * @return a String containing servlet description.
     */
    @Override
    public String getServletInfo() {
        return "This servlet is responsible for updating the user accounts.";
    }
}

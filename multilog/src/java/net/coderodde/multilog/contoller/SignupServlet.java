package net.coderodde.multilog.contoller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Config;
import net.coderodde.multilog.Utils;
import net.coderodde.multilog.model.User;

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
    protected void doPost
            (final HttpServletRequest request,
             final HttpServletResponse response)
            throws ServletException, IOException {
        if (Utils.getSignedUser(request) != null) {
            request.getRequestDispatcher("topic").forward(request, response);
            return;
        }

        HomeServlet.prepareNavibarForUnsignedUser(request);

        // Try to sign up a new user.
        // Get all the form data as to send it back to the view in case some
        // crap happens.
        final String username =
                request.getParameter(Config.SESSION_MAGIC.USERNAME);

        final String password =
                request.getParameter(Config.SESSION_MAGIC.PASSWORD);

        final String passwordConfirmation =
                request.getParameter(Config.
                                     SESSION_MAGIC.
                                     PASSWORD_CONFIRMATION);

        final String email = request.getParameter(Config.SESSION_MAGIC.EMAIL);

        final String firstName =
                request.getParameter(Config.SESSION_MAGIC.FIRST_NAME);

        final String lastName =
                request.getParameter(Config.SESSION_MAGIC.LAST_NAME);

        final String showRealName =
                request.getParameter(Config.SESSION_MAGIC.SHOW_REAL_NAME);

        final String showEmail =
                request.getParameter(Config.SESSION_MAGIC.SHOW_EMAIL);

        if (username == null || username.isEmpty()) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail);

            request.setAttribute("bad_username", "Username is empty.");
            request.setAttribute("su_error_username", "su_error");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        final User other = User.getByUsername(username);

        if (other != null) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail);

            // Once here, the username is already occupied.
            request.setAttribute("bad_username", "Username already in use.");
            request.setAttribute("su_error_username", "su_error");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        if (Utils.isValidUsername(username) == false) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail);

            request.setAttribute("bad_username", "Invalid username.");
            request.setAttribute("su_error_username", "su_error");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        if (password == null || password.isEmpty()) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail);

            request.setAttribute("bad_password", "Password is missing.");
            request.setAttribute("su_error_password", "su_error");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        if (Utils.isValidPassword(password) == false) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail);

            request.setAttribute("bad_password", "Invalid password.");
            request.setAttribute("su_error_password", "su_error");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        if (passwordConfirmation == null || passwordConfirmation.isEmpty()) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail);

            request.setAttribute("bad_password_confirmation",
                                 "Password is missing.");
            request.setAttribute("su_error_confirm", "su_error");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        if (Utils.isValidPassword(passwordConfirmation) == false) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail);

            request.setAttribute("bad_password_confirmation",
                                 "Invalid password.");
            request.setAttribute("su_error_confirm", "su_error");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        if (password.equals(passwordConfirmation) == false) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail);

            request.setAttribute("bad_password_confirmation",
                                 "Confirmation and password differ.");
            request.setAttribute("su_error_confirm", "su_error");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        if (email == null || email.isEmpty()) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail);

            request.setAttribute("bad_email", "Email address is missing.");
            request.setAttribute("su_error_email", "su_error");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        if (Utils.isValidEmail(email) == false) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail);

            request.setAttribute("bad_email", "Invalid email address.");
            request.setAttribute("su_error_email", "su_error");
            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }

        request.getRequestDispatcher("home").forward(request, response);
    }

    private static final void saveIntermediateData(
            final HttpServletRequest request,
            final String username,
            final String password,
            final String passwordConfirmation,
            final String firstName,
            final String lastName,
            final String email,
            final String showName,
            final String showEmail) {
        request.setAttribute("im_username", username);
        request.setAttribute("im_password", password);
        request.setAttribute("im_confirm", passwordConfirmation);
        request.setAttribute("im_first", firstName);
        request.setAttribute("im_last", lastName);
        request.setAttribute("im_email", email);

        if (showName == null || !showName.equals("on")) {
            request.setAttribute("im_show_name", "");
        } else {
            request.setAttribute("im_show_name", "checked");
        }

        if (showEmail == null || !showEmail.equals("on")) {
            request.setAttribute("im_show_email", "");
        } else {
            request.setAttribute("im_show_email", "checked");
        }
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

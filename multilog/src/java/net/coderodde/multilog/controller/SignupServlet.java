package net.coderodde.multilog.controller;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.coderodde.multilog.Config;
import net.coderodde.multilog.Utils;
import net.coderodde.multilog.model.DB;
import net.coderodde.multilog.model.User;
import net.coderodde.multilog.model.UserType;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import static net.coderodde.multilog.Utils.closeResources;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import net.coderodde.multilog.Utils.Pair;

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

        boolean multipart = ServletFileUpload.isMultipartContent(request);

        if (multipart == false) {
            throw new IllegalStateException(
                    "Form enctype should be multipart.");
        }

        ServletFileUpload sfu =
                new ServletFileUpload(new DiskFileItemFactory());

        HomeServlet.prepareNavibarForUnsignedUser(request);

        Pair<Map<String, String>, FileItem> pair = getFormFields(sfu, request);
        Map<String, String> map = pair.getFirst();

        // Try to sign up a new user.
        // Get all the form data as to send it back to the view in case some
        // crap happens.
        final String username = map.get(Config.SESSION_MAGIC.USERNAME);

        final String password = map.get(Config.SESSION_MAGIC.PASSWORD);

        final String passwordConfirmation =
                map.get(Config.SESSION_MAGIC.PASSWORD_CONFIRMATION);

        final String email = map.get(Config.SESSION_MAGIC.EMAIL);

        final String firstName =
                escapeHtml4(map.get(Config.SESSION_MAGIC.FIRST_NAME));

        final String lastName =
                escapeHtml4(map.get(Config.SESSION_MAGIC.LAST_NAME));

        final String showRealName =
                map.get(Config.SESSION_MAGIC.SHOW_REAL_NAME);

        final String showEmail = map.get(Config.SESSION_MAGIC.SHOW_EMAIL);

        final String description =
                escapeHtml4(map.get(Config.SESSION_MAGIC.DESCRIPTION));

        final boolean bShowEmail = (showEmail != null &&
                                    showEmail.equalsIgnoreCase("on"));

        final boolean bShowRealName = (showRealName != null &&
                                       showRealName.equalsIgnoreCase("on"));

        boolean hasErrors = false;

        if (username == null || username.isEmpty()) {
            hasErrors = true;

            request.setAttribute("bad_username", "Username is empty.");
            request.setAttribute("su_error_username", "su_error");
        }

        final User other = User.read(username);

        if (other != null) {
            hasErrors = true;

            // Once here, the username is already occupied.
            request.setAttribute("bad_username", "Username already in use.");
            request.setAttribute("su_error_username", "su_error");
        }

        if (Utils.isValidUsername(username) == false) {
            hasErrors = true;

            request.setAttribute("bad_username", "Invalid username.");
            request.setAttribute("su_error_username", "su_error");
        }

        if (password == null || password.isEmpty()) {
            hasErrors = true;

            request.setAttribute("bad_password", "Password is missing.");
            request.setAttribute("su_error_password", "su_error");
        }

        if (Utils.isValidPassword(password) == false) {
            hasErrors = true;

            request.setAttribute("bad_password", "Invalid password.");
            request.setAttribute("su_error_password", "su_error");
        }

        if (passwordConfirmation == null || passwordConfirmation.isEmpty()) {
            hasErrors = true;

            request.setAttribute("bad_password_confirmation",
                                 "Password is missing.");
            request.setAttribute("su_error_confirm", "su_error");
        }

        if (Utils.isValidPassword(passwordConfirmation) == false) {
            hasErrors = true;

            request.setAttribute("bad_password_confirmation",
                                 "Invalid password.");
            request.setAttribute("su_error_confirm", "su_error");
        }

        if (password.equals(passwordConfirmation) == false) {
            hasErrors = true;

            request.setAttribute("bad_password_confirmation",
                                 "Confirmation and password differ.");
            request.setAttribute("su_error_confirm", "su_error");
        }

        if (email == null || email.isEmpty()) {
            hasErrors = true;

            request.setAttribute("bad_email", "Email address is missing.");
            request.setAttribute("su_error_email", "su_error");
        }

        if (Utils.isValidEmail(email) == false) {
            hasErrors = true;

            request.setAttribute("bad_email", "Invalid email address.");
            request.setAttribute("su_error_email", "su_error");
        }

        if (hasErrors) {
            saveIntermediateData(request,
                                 username,
                                 password,
                                 passwordConfirmation,
                                 firstName,
                                 lastName,
                                 email,
                                 showRealName,
                                 showEmail,
                                 description);

            request.getRequestDispatcher("signup.jsp")
                   .forward(request, response);
            return;
        }
        // Once here, all form data is OK, so create a user.
        User user = new User().setUsername(username)
                              .setPassword(password)
                              .setSalt(Utils.createSalt())
                              .setEmail(email)
                              .setFirstName(firstName)
                              .setLastName(lastName)
                              .setShowEmail(bShowEmail)
                              .setShowRealName(bShowRealName)
                              .setDescription(description)
                              .setUserType(UserType.USER)
                              .end();

        boolean created = user.create();
        StringBuilder sb = new StringBuilder();

        if (created) {
            if (pair.getSecond() != null) {
                // Try to persist the avatar.
                if (processAvatar(pair.getSecond(), user.getId()) == false) {
                    sb.append("Could not persist the avatar.<br/>");
                }
            }

            request.setAttribute("notice",
                                 sb.toString() + "User " + user.getUsername() +
                                 " created.");
            request.getRequestDispatcher("home").forward(request, response);
        } else {
            request.setAttribute("notice", "Could not create a user.");
            request.getRequestDispatcher("signup").forward(request, response);
        }
    }

    private static final Pair<Map<String, String>, FileItem> getFormFields
            (final ServletFileUpload upload, final HttpServletRequest request) {
        Map<String, String> map = new TreeMap<String, String>();
        List<FileItem> fileItems = null;

        try {
            fileItems = upload.parseRequest(request);
        } catch (FileUploadException fue) {
            fue.printStackTrace(System.err);
            return null;
        }

        FileItem avatarFileItem = null;

        for (final FileItem item : fileItems) {
            if (item.isFormField()) {
                map.put(item.getFieldName(), item.getString());
            } else if (avatarFileItem == null) {
                avatarFileItem = item;
            }
        }

        return new Pair<Map<String, String>, FileItem>(map, avatarFileItem);
    }

    private static final boolean processAvatar(final FileItem item,
                                               final long userId) {
        if (item.isFormField()) {
            throw new IllegalArgumentException(
                    "Form field should not get here.");
        }

        InputStream is = null;

        try {
            is = item.getInputStream();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
            return false;
        }

        Connection connection = DB.getConnection();

        if (connection == null) {
            return false;
        }

        PreparedStatement ps = DB.getPreparedStatement(connection,
                                                       Config.
                                                       SQL_MAGIC.
                                                       SAVE_AVATAR);

        if (ps == null) {
            closeResources(connection, null, null);
        }

        try {
            ps.setBinaryStream(1, is);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace(System.err);
            closeResources(connection, ps, null);

            try {
                is.close();
            } catch (IOException ioe) {
                ioe.printStackTrace(System.err);
            }

            return false;
        }

        closeResources(connection, ps, null);

        try {
            is.close();
        } catch (IOException ioe) {
            ioe.printStackTrace(System.err);
        }

        return true;
    }

    /**
     * Saves the intermediate form data.
     *
     * @param request the servlet request object.
     * @param username the username to save.
     * @param password the password to save.
     * @param passwordConfirmation the password confirmation to save.
     * @param firstName the first name to save.
     * @param lastName the last name to save.
     * @param email the email address to save.
     * @param showName the show name trigger to save.
     * @param showEmail the show email trigger to save.
     * @param description the description to save.
     */
    private static final void saveIntermediateData(
            final HttpServletRequest request,
            final String username,
            final String password,
            final String passwordConfirmation,
            final String firstName,
            final String lastName,
            final String email,
            final String showName,
            final String showEmail,
            final String description) {
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

        request.setAttribute("im_description", description);
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

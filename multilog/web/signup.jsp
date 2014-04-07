<%@page import="net.coderodde.multilog.Config"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/multilog.css">
        <link rel="stylesheet" media="screen" href="http://openfontlibrary.org/face/teknik" type="text/css"/>
        <title>multilog</title>
    </head>
    <body>
        <div id="main-content">
            <div id="header">
                <img id="logo" src="img/multilogo.png" />
            </div>

            <%@ include file="navibar.jspf" %>

            <hr/>

            <div id="sign_up_div">
                <h2>Sign up</h2>
                <form action="signup" method="POST">
                    <table style="width: 100%;">
                        <tr> <td> User name:                             </td> <td> <input type="text" name="<%= Config.SESSION_MAGIC.USERNAME %>"                  value="${im_username}" > </td> <td class="${su_error_username}"> ${bad_username}              </td> </tr>
                        <tr> <td> Password:                              </td> <td> <input type="password" name="<%= Config.SESSION_MAGIC.PASSWORD %>"              value="${im_password}" > </td> <td class="${su_error_password}"> ${bad_password}              </td> </tr>
                        <tr> <td> Password confirmation:                 </td> <td> <input type="password" name="<%= Config.SESSION_MAGIC.PASSWORD_CONFIRMATION %>" value="${im_confirm}"  > </td> <td class="${su_error_confirm}">  ${bad_password_confirmation} </td> </tr>
                        <tr> <td> First name:                            </td> <td> <input type="text" name="<%= Config.SESSION_MAGIC.FIRST_NAME %>"                value="${im_first}"    > </td> <td>                                                           </td> </tr>
                        <tr> <td> Last name:                             </td> <td> <input type="text" name="<%= Config.SESSION_MAGIC.LAST_NAME %>"                 value="${im_last}"     > </td> <td>                                                           </td> </tr>
                        <tr> <td> E-mail:                                </td> <td> <input type="text" name="<%= Config.SESSION_MAGIC.EMAIL %>"                     value="${im_email}"    > </td> <td class="${su_error_email}"> ${bad_email}                    </td> </tr>
                        <tr> <td> Show real name:                        </td> <td> <input type="checkbox" name="<%= Config.SESSION_MAGIC.SHOW_REAL_NAME %>"        ${im_show_name}        > </td> <td>                                                           </td> </tr>
                        <tr> <td> Show email:                            </td> <td> <input type="checkbox" name="<%= Config.SESSION_MAGIC.SHOW_EMAIL %>"            ${im_show_email}       > </td> <td>                                                           </td> </tr>
                        <tr> <td> Description:                           </td> <td> <textarea rows="4">${im_description}</textarea>                                                          </td> <td>                                                           </td> </tr>
                        <tr> <td> <input type="submit" value="Sign up!"> </td> <td>                                                                                                          </td> <td>                                                           </td> </tr>
                    </table>
                    <p style="color: red;">${notice}</p>
                </form>
            </div>

            <div>
                <p>
                    <ul>
                        <li>The username must have between <%= Config.MINIMUM_USERNAME_LENGTH %> and <%= Config.MAXIMUM_USERNAME_LENGTH %> characters. Only alphanumeric characters allowed.</li>
                        <li>The password must have at least <%= Config.MINIMUM_PASSWORD_LENGTH %> alphanumeric characters. At least one capital letter, three lower case letters and two digits required.</li>
                    </ul>
                </p>
            </div>
        </div>
    </body>
</html>

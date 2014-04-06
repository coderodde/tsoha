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
                        <tr> <td> User name:                             </td> <td> <input type="text" name="<%= Config.SESSION_MAGIC.USERNAME %>"                 </td> <td class="su_error"> ${bad_username}             </td> </tr>
                        <tr> <td> Password:                              </td> <td> <input type="password" name="<%= Config.SESSION_MAGIC.PASSWORD %>"             </td> <td class="su_error"> ${bad_password}             </td> </tr>
                        <tr> <td> Password confirmation:                 </td> <td> <input type="password" name"<%= Config.SESSION_MAGIC.PASSWORD_CONFIRMATION %>" </td> <td class="su_error"> ${bad_password_confirmaion} </td> </tr>
                        <tr> <td> Last name:                             </td> <td> <input type="text" name="<%= Config.SESSION_MAGIC.LAST_NAME %>">               </td> <td>                                              </td> </tr>
                        <tr> <td> First name:                            </td> <td> <input type="text" name="<%= Config.SESSION_MAGIC.FIRST_NAME %>">              </td> <td>                                              </td> </tr>
                        <tr> <td> E-mail:                                </td> <td> <input type="text" name="<%= Config.SESSION_MAGIC.EMAIL %>">                   </td> <td class="su_error"> ${bad_email}                </td> </tr>
                        <tr> <td> Show real name:                        </td> <td> <input type="checkbox" name="<%= Config.SESSION_MAGIC.SHOW_REAL_NAME %>" >     </td> <td>                                              </td> </tr>
                        <tr> <td> Show email:                            </td> <td> <input type="checkbox" name="<%= Config.SESSION_MAGIC.SHOW_EMAIL %>" >         </td> <td>                                              </td> </tr>
                        <tr> <td> <input type="submit" value="Sign in!"> </td> <td>                                                                                 </td> <td>                                              </td> </tr>
                    </table>
                </form>
                <p style="color: red;">${notice}</p>
            </div>
        </div>
    </body>
</html>

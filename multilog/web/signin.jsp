<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="net.coderodde.multilog.Config"%>
<!DOCTYPE html>
<html>
    <head>
        <title>multilog</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/multilog.css">
        <link rel="stylesheet" media="screen" href="http://openfontlibrary.org/face/teknik" type="text/css"/>
    </head>
    <body>
        <div id="main-content">
            <div id="header">
                <img id="logo" src="img/multilogo.png" />
            </div>

            <%@ include file="navibar.jspf" %>

            <hr/>

            <div id="sign_in_div">
                <h2>Sign in:</h2>
                <form action="signin" method="POST">
                    <table>
                        <tr><td>User name:</td><td><input type="text" name="<%= Config.SESSION_MAGIC.USERNAME %>"</td></tr>
                        <tr><td>Password: </td><td><input type="password" name="<%= Config.SESSION_MAGIC.PASSWORD %>"</td></tr>
                        <tr><td><input type="submit" value="Sign in!"></td><td></td></tr>
                    </table>
                </form>
                <p style="color: red;">${notice}</p>
            </div>
        </div>
    </body>
</html>

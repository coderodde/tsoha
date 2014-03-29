<%@page import="net.coderodde.multilog.Config"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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

            <div id="navibar">
            </div>

            <hr/>

            <h2>Sign in:</h2>
            <form action="login" method="POST">
                User name: <input type="text"     name="<%= Config.SESSION_MAGIC.USERNAME %>"><br>
                Password:  <input type="password" name="<%= Config.SESSION_MAGIC.PASSWORD %>"><br>
                <input type="submit" value="Sign in!">
            </form>
                <p>${notice}</p>
        </div>
    </body>
</html>

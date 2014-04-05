<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
                <ul id="navilist" class="no_bullets">
                    <li class="list-left-item navibar-item"><div onclick="window.location='search.html'">Search</div></li>
                    <li class="list-left-item navibar-item"><div onclick="window.location='browse.html'">Browse</div></li>
                    <li class="list-right-item navibar-item"><div onclick="window.location='https://www.google.fi'">Sign out</div></li>
                    <li class="list-right-item navibar-item"><div onclick="window.location='home.html'">Account</div></li>
                </ul>
            </div>
            <hr/>

            <div class="thread_container">
                <h2>Threads on ${topic_name}</h2>
                <c:forEach var="thread" items="${requestScope.threadList}" >
                    <div class="thread_item" onclick="window.location='www.google.fi'">${thread.name}</div>
                </c:forEach>
            </div>
        </div>
    </body>
</html>
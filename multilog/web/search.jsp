<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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

            <%@ include file="navibar.jspf" %>

            <hr/>

            <form action="search" method="get">
                <input type="text" name="query" value="${query_value}">
                <input type="submit" value="Search!">
            </form>

            <div class="result_container">
                <h2>${title}</h2>
                <c:forEach var="entry" items="${requestScope.result_map}">
                    <div class="result_item" onclick="window.location='thread?id=${entry.key.id}'">${entry.key.name} - ${entry.value}</div>
                </c:forEach>
            </div>

            <p id="notice" style="color: red; font-style: italic; text-align: center;">${notice}</p>
        </div>
    </body>
</html>

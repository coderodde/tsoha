<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
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

            <div class="thread_container">
                <h2>Threads on ${topic_name}</h2>
                <c:forEach var="thread" items="${requestScope.threadList}" >
                    <div class="thread_item" onclick="window.location='thread?id=${thread.id}'">
                        ${thread.name} at <fmt:formatDate pattern="yyyy.MM.dd  HH:mm:ss z" value="${thread.createdAt}" />
                        <c:if test="${thread.timestampsDifferent}">
                            , updated at <fmt:formatDate pattern="yyyy.MM.dd HH:mm:ss z" value="${thread.updatedAt}" />
                        </c:if>
                    </div>
                </c:forEach>
            </div>
        </div>
    </body>
</html>
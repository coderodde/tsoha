<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>multilog | root</title>
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

            <div class="topic_container">
                <h2>Topics</h2>
                <c:forEach var="topic" items="${requestScope.topicList}">
                    <div class="topic_item" onclick="window.location='topic?id=${topic.id}'">
                        ${topic.name}
                        <span class="float_right" style="font-style: normal; font-weight: normal;">
                            at <fmt:formatDate pattern="yyyy.MM.dd HH:mm:ss z" value="${topic.createdAt}" />
                            <c:if test="${topic.timestampsDifferent}">
                                , updated at <fmt:formatDate pattern="yyyy.MM.dd HH:mm:ss z" value="${topic.updatedAt}" />
                            </c:if>
                        </span>
                        <c:if test="${isAdmin}">
                            <form action="deletetopic" method="post">
                                <input type="hidden" name="topic_id" value="${topic.id}">
                                <input type="submit" value="Delete">
                            </form>
                        </c:if>
                    </div>
                </c:forEach>
            </div>
            <c:if test="${isAdmin}">
            <div>
                <h2>Create new topic</h2>
                <form action="newtopic" method="post">
                    <input type="text" name="topic_name">
                    <input type="submit" value="Create">
                </form>
            </div>
            </c:if>
            <p id="notice" style="text-align: center; color: red;">${notice}</p>
        </div>
    </body>
</html>

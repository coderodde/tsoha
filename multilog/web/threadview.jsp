<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
    <head>
        <title>A thread</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="css/multilog.css">
        <link rel="stylesheet" media="screen" href="http://openfontlibrary.org/face/teknik" type="text/css"/>
    </head>
    <body>
        <div id="main-content">
            <div id="header">
                <img id="logo" src="img/multilogo.png" />
            </div>

            <%@include file="navibar.jspf" %>

            <hr />

            <div class="post_container">
                <h2>Posts on ${thread_name}</h2>
                <c:forEach var="post" items="${requestScope.postList}">
                    <div class="post_item" data-id="${post.id}">
                        <div class="post_intro">${post.user.username} at
                        <fmt:formatDate pattern="yyyy.MM.dd  HH:mm:ss z" value="${post.createdAt}" />
                        <c:if test="!post.createdAt.equals(post.updatedAt)" >
                            , updated at <fmt:formatDate pattern="yyyy.MM.dd HH:mm:ss z" value="${post.updatedAt}" />
                        </c:if>
                        </div>
                        <div class="post_text">${post.text}</div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </body>
</html>

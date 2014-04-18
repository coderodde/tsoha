<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html>
    <head>
        <title>multilog | ${thread_name}</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script language="javascript" type="text/javascript" src="js/multilog.js"></script>
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

                    <div class="post_item" data-id="${post.id}" style="margin-left: <c:out value="${6 + 10 * post.indent}" />px;">
                        <div class="post_intro">${post.user.username} at
                        <fmt:formatDate pattern="yyyy.MM.dd  HH:mm:ss z" value="${post.createdAt}" />
                        <c:if test="${post.timestampsDifferent}" >
                            , updated at <fmt:formatDate pattern="yyyy.MM.dd HH:mm:ss z" value="${post.updatedAt}" />
                        </c:if>

                        <c:if test="${can_reply == true}">
                            <div class="reply_button" onclick="setReplyTarget(${post.id});">Reply</div>
                        </c:if>
                        </div>
                        <div class="post_text">${post.text}</div>
                    </div>

                </c:forEach>
            </div>

            <c:if test="${can_reply == true}" >

            <hr />

            <div class="reply_area">
                <div id="reply_to" style="color: #ff3300;"></div>
                <form action="dopost" method="post">
                    <textarea rows="4" id="post_textarea"></textarea>
                    <input type="hidden" id="hidden_input" name="replied_post_id" value="">
                    <input type="submit" value="Post now!">
                </form>
            </div>
            </c:if>

        </div>
    </body>
</html>

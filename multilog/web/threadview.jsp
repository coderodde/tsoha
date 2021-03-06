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
            <div style="float: left; margin-right: 8px; margin-left: 8px;" onclick="window.location='topic?id=${topic_id}'">${topic_name}</div> &gt; ${thread_name}
            <hr />

            <p style="color: red; font-style: italic; text-align: center;">${notice}</p>

            <div class="post_container">
                <h2>Posts on ${thread_name}</h2>
                <c:forEach var="post" items="${requestScope.postList}">

                    <div class="post_item" style="margin-left: <c:out value="${6 + 10 * post.indent}" />px;">
                        <div class="post_intro" > <a href="account?id=${post.user.id}">${post.user.username}</a>

                        <c:if test="${can_reply == true}">
                            <div class="reply_button" onclick="setReplyTarget(${post.id}, '${post.user.username}');">Reply</div>
                        </c:if>

                        <c:if test="${post.fresh}">
                            <div class="fresh_label">New!</div>
                        </c:if>
                            <span class="float_right" style="font-style: normal; font-weight: normal;"> at
                                <fmt:formatDate pattern="yyyy.MM.dd  HH:mm:ss z" value="${post.createdAt}" />
                                <c:if test="${post.timestampsDifferent}" >
                                    , updated at <fmt:formatDate pattern="yyyy.MM.dd HH:mm:ss z" value="${post.updatedAt}" />
                                </c:if>
                            </span>
                        </div>

                        <div class="post_text">${post.html}</div>

                        <c:if test="${post.edible == true}" >
                            <form action="updatepost" method="post">
                                <textarea rows="4" cols="80" name="new_content">${post.text}</textarea>
                                <input type="hidden" name="edit_post_id" value="${post.id}">
                                <input type="submit" value="Update post" >
                            </form>
                        </c:if>

                    </div>

                </c:forEach>
            </div>

            <c:if test="${can_reply == true}" >

            <hr />

            <div class="reply_area" id="reply_anchor">
                <div id="reply_to" style="color: #ff3300;"></div>
                <div class="reply_button" id="dont_reply" style="color: #ff3300;" onclick="forget();">Forget</div>

                <form action="dopost" method="post">
                    <textarea rows="10" cols="80" id="post_textarea" name="post_text"></textarea>
                    <input type="hidden" id="hidden_input" name="replied_post_id" value="">
                    <input type="hidden" name="thread_id" value="${thread_id}">
                    <input type="submit" value="Post now!">
                </form>
            </div>

            <div style="border: 1px solid black; margin: 8px auto 8px auto; padding: 10px;">
                Use * for bold font.
                <br>
                Use _ for italic.
                <br>
                Use # for monospaced font.
                <br>
                Use [http://...] for a link.
                <br>
                Use [http://...|Name] for a named link.
            </div>
            </c:if>

        </div>
    </body>
</html>

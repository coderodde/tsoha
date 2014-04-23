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

            <div class="user_info">
                <div id="account_name_div">${username}</div>

                <c:if test="${edit == true}" >
                    <%@ include file="accountedit.jspf" %>
                </c:if>

                <c:if test="${view == true}" >
                    <%@ include file="accountview.jspf" %>
                </c:if>

                <c:if test="${candelete == true}">
                <form action="accountdelete" method="post">
                    <input type="hidden" value="${target_id}" name="user_id">
                    <input type="submit" value="Delete account!" >
                </form>
                </c:if>

                <p style="color: red; text-align: center;">${notice}</p>
            </div>

            <c:if test="${edit == true}">
                <%@ include file="thread_updates.jspf" %>
            </c:if>
            <c:if test="${can_ban}">
                Can ban.
                <form action="ban" method="post">
                    <input type="text" name="ban_hours">
                    <input type="hidden" name="banned_id" value="${target_id}">
                    <input type="submit" value="Ban">
                </form>
            </c:if>
            <c:if test="${can_promote_to_moderator}">
                Can promote to moderator.
                <form action="promote" method="post">
                    <input type="hidden" name="promoted_id" value="${target_id}">
                    <inpyt type="hidden" name="promotion_level" value="mod">
                    <input type="submit" value="Promote to moderator">
                </form>
            </c:if>
            <c:if test="${can_promote_to_admin}">
                Can promote to administrator.
                <form action="promote" method="post">
                    <input type="hidden" name="promoted_id" value="${target_id}">
                    <input type="hidden" name="promotion_level" value="admin">
                    <input type="submit" value="Promote to administrator">
                </form>
            </c:if>
            <c:if test="${can_degrade_to_user}">
                Can degrade to user.
                <!-- Degrading a moderator to user. -->
                <form action="promote" method="post">
                    <input type="hidden" name="promoted_id" value="${target_id}"
                    <input type="hidden" name="promotion_level" value="user">
                    <input type="submit" value="Degrade to user">
                </form>
            </c:if>
        </div>
    </body>
</html>

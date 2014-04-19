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
        </div>
    </body>
</html>

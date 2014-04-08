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
                <div id="account_name_div">  </div>

                <table class="info_table">
                    <tr ><td><h2>${username}</h2></td><td></td></tr>
                    <tr>
                        <td class="info_td"></td>
                        <td class="info_td">
                            <table class="info_map_table">
                                <tr>
                                    <td class="info_map_td">Posts:</td><td class="info_map_td">${posts}</td>
                                </tr>
                                <tr>
                                    <td class="info_map_td">Joined:</td><td class="info_map_td">${joined}</td>
                                </tr>
                                <tr>
                                    <td class="info_map_td">Updated:</td><td class="info_map_td">${updated}</td>
                                </tr>
                                <tr>
                                    <td class="info_map_td">Email:</td><td class="info_map_td">${email}</td>
                                </tr>
                                <tr>
                                    <td class="info_map_td">Real name:</td><td class="info_map_td">${real_name}</td>
                                </tr>
                                <tr>
                                    <td class="info_map_td">Privilege level:</td><td class="info_map_td">${level}</td>
                                </tr>
                                <tr>
                                    <td class="info_map_td">Description:</td><td><p>${description}</p></td>
                                </tr>
                                <tr>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>

                <c:if test="${edit == true}">
                <form action="update">
                    <input type="button" value="Update account">
                </form>

                <form action="delete">
                    <input type="button" value="Delete account!" >
                </form>
                This must be printed! :)
                </c:if>
            </div>
        </div>
    </body>
</html>

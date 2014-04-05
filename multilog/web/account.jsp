<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>multilog</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="../css/multilog.css">
        <link rel="stylesheet" media="screen" href="http://openfontlibrary.org/face/teknik" type="text/css"/>
    </head>
    <body>
        <div id="main-content">
            <div id="header">
                <img id="logo" src="../img/multilogo.png" />
            </div>

            <%@ include file="navibar.jspf" %>
            
            <hr/>

            <div class="user_info">
                <div id="account_name_div">  </div>

                <table class="info_table">
                    <tr>
                        <td class="info_td"><img src="img/loldog.jpeg" width="200px" height="200px"></td>
                        <td class="info_td">
                            <table class="info_map_table">
                                <tr>
                                    <td class="info_map_td">Posts:</td><td class="info_map_td">24</td>
                                </tr>
                                <tr>
                                    <td class="info_map_td">Joined:</td><td class="info_map_td">12.05.2011</td>
                                </tr>
                                <tr>
                                    <td class="info_map_td">email:</td><td class="info_map_td"><input type="text" value="loldog@wwg.edu"></td>
                                </tr>
                                <tr>
                                    <td class="info_map_td">Privilege level:</td><td class="info_map_td">Moderator</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr><td colspan="2" align="center"><button type="button">Update</button><button type="button">Delete account</button></td></tr>
                </table>
            </div>
        </div>
    </body>
</html>

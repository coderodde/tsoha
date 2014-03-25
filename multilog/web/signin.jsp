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

            <div id="navibar">
                <ul id="navilist" class="no_bullets">
                    <li class="list-left-item navibar-item"><div onclick="window.location='https://www.google.fi'">Search</div></li>
                    <li class="list-left-item navibar-item"><div onclick="window.location='https://www.google.fi'">Browse</div></li>
                    <li class="list-right-item navibar-item"><div onclick="window.location='https://www.google.fi'">Sign in</div></li>
                    <li class="list-right-item navibar-item"><div onclick="window.location='https://www.google.fi'">Sign up</div></li>
                </ul>
            </div>
            <hr/>
            <h2>Latest posts:</h2>
            <div class="post_list_container">
                <div class="post_list_item">
                    <div class="post_info">loldog @ 12.03.2011 (14:35)</div>
                    <br>
                    <div class="post_text">Fibonacci heaps are faster in asymptotic sense, yet they are very hard to implement. There is the code: ...</div>
                </div>

                <div class="post_list_item">
                    <div class="post_info">goldy @ 12.03.2011 (14:10)</div>
                    <br>
                    <div class="post_text">I tried to run Dijkstra's algorithm on 100000 node graph and it never terminates! Help! :&lt;</div>
                </div>
                <p>.</p>
                <p>.</p>
                <p>.</p>
            </div>
        </div>
    </body>
</html>

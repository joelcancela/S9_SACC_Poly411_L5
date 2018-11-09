<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="fr.unice.polytech.si5.cc.l5.HelloAppEngine" %>
<html>
<head>
    <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
    <title>Hello App Engine Standard Java 8</title>
</head>
<body>
<h1>Download file</h1>


<form method="post" action="/download?bucket=<%= request.getParameter("bucket") %>&file=<%= request.getParameter("file") %>">
    <div>
        <label for="username">Username</label>
        <input type="text" id="username" name="username">
    </div>
    <div>
        <input type="submit" value="Submit">
    </div>
</form>
</c:if>

</body>
</html>

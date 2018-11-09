<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="fr.unice.polytech.si5.cc.l5.HelloAppEngine" %>
<html>
<head>
    <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
    <title>Hello App Engine Standard Java 8</title>
</head>
<body>
<h1>Upload file</h1>

<form method="post" enctype="multipart/form-data" action="/gcs/polar-winter-218511">
    <div>
        <label for="file">Select file to upload</label>
        <input type="file" id="file" name="file">
    </div>
    <div>
        <input type="submit" value="Submit">
    </div>
</form>

</body>
</html>

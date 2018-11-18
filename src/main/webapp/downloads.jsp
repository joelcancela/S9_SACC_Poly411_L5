<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
    <title>Poly411 - Download file</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

</head>
<body>
<h1>Download file</h1>


<form id="downloadFile" method="post" action="">
    <div>
        <label for="username">Username</label>
        <input type="text" id="username" name="username" required>
        <label for="file">Filename</label>
        <input type="text" id="file" name="file" required>
    </div>
    <div>
        <input type="submit" value="Submit">
    </div>
</form>
</c:if>
<script>

    function downloadFile() {

        var user = document.getElementById("username").value;
        var file = document.getElementById("file").value;
        if (user == null || file === undefined || user == null || user === undefined) {
            alert("file and username are required");
            return false;
        } else {
            document.getElementById("downloadFile").action = "/download?bucket=polar-winter-218511&file=" + file;
            document.getElementById("downloadFile").submit();
        }
    }

    $('#downloadFile').submit(function (e) {
        e.preventDefault();
        downloadFile();
    });
</script>
</body>
</html>

<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
    <title>Poly411 - Upload file</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>
<h1>Upload file</h1>
<iframe name="hiddenFrame" width="0" height="0" border="0" style="display: none;"></iframe>
<form name="submitFile" id="submitFile" enctype="multipart/form-data" target="hiddenFrame">
    <div>
        <label for="user">Username :</label>
        <input type="text" id="user" name="user" required><br>
        <label for="file">Select file to upload (15Mo Max, don't use spaces in filename)</label>
        <input type="file" id="file" name="file" required>
    </div>
    <div>
        <input type="submit" value="Submit">
    </div>
</form>
<script>

    function uploadFile() {
        var filename = document.getElementById("file").files[0].name;
        var user = document.getElementById("user").value;
        if (filename == null || filename === undefined || user == null || user === undefined) {
            alert("file and username are required");
            return false;
        } else {
            var currentFile = document.getElementById("file").files[0];
            var reader = new FileReader();
            reader.onload = function (e) {
                var request = new XMLHttpRequest();
                request.open("POST", "/gcs/" + filename + "?user=" + user, false);
                request.send(e.target.result);
            };
            reader.readAsArrayBuffer(currentFile);

        }
    }

    $('#submitFile').submit(function (e) {
        e.preventDefault();
        uploadFile();
    });
</script>
</body>
</html>

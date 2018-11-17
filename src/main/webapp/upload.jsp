<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link href='//fonts.googleapis.com/css?family=Marmelad' rel='stylesheet' type='text/css'>
    <title>Hello App Engine Standard Java 8</title>
</head>
<body>
<h1>Upload file</h1>
<iframe name="hiddenFrame" width="0" height="0" border="0" style="display: none;"></iframe>
<form name="submitFile" enctype="multipart/form-data" target="hiddenFrame">
    <div>
        <label for="file">Select file to upload</label>
        <input type="file" id="file" name="file">
    </div>
    <div>
        <input type="submit" onclick='changeGetPath(this)' value="Submit">
    </div>
</form>
<script>
    function changeGetPath() {
        var filename = document.getElementById("file").files[0].name;
        if (filename == null || filename === undefined) {
            alert("file is required");
            return false;
        } else {
           var currentFile = document.getElementById("file").files[0];
            var reader = new FileReader();
            reader.onload = function (e) {
                var request = new XMLHttpRequest();
                request.open("POST", "/gcs/polar-winter-218511/" + filename, false);
                request.send(e.target.result);
            };
            reader.readAsArrayBuffer(currentFile);

        }
    }
</script>
</body>
</html>

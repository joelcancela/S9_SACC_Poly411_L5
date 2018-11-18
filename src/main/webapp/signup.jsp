<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Poly411 - Sign up</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
</head>
<body>
<iframe name="hiddenFrame" width="0" height="0" border="0" style="display: none;"></iframe>
<form name="createUser" id="createUser" target="hiddenFrame">
    <div>
        <label for="name">Username :</label>
        <input type="text" id="name" name="name" required><br>
        <label for="email">Email:</label>
        <input type="text" id="email" name="email" required>
    </div>
    <div>
        <input type="submit" onclick="submitform()" value="Submit">
    </div>
</form>
<script type="text/javascript">
    function submitform() {
        var formData = $("#createUser").serializeArray();
        var newJson = {};
        for (var i = 0; i < formData.length; i++) {
            var obj = formData[i];
            console.log(obj);
            newJson[obj.name] = obj.value;
        }
        console.log(newJson);
        $.ajax({
            type: "POST",
            url: "/users",
            data: JSON.stringify(newJson),
            dataType: "json",
            contentType: "application/json"
        });
        window.setTimeout(function () {
            var x = document.createElement("SPAN");
            var t = document.createTextNode("You are registered !");
            x.appendChild(t);
            document.body.appendChild(x);
            window.setTimeout(function () {
                window.location.href = "https://polar-winter-218511.appspot.com/users";
            }, 3000);
        }, 3000);
    }
</script>
</body>
</html>

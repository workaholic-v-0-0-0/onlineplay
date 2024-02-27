<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
    <p>
        Authentication page
    </p>
    <p>
        to debug
        </br>
        sessionScope.userConnection.id : <c:out value="${sessionScope.userConnection.id}" />
        </br>
        sessionScope.userConnection.ipAddress : <c:out value="${sessionScope.userConnection.ipAddress}" />
        </br>
        sessionScope.userConnection.timestamp : <c:out value="${sessionScope.userConnection.timestamp}" />
        </br>
        sessionScope.userConnection.userId : <c:out value="${sessionScope.userConnection.userId}" />
        </br>
    </p>
    <form method="post" action="authentication">
        <label for="username"> Username : </label>
        <input type="text" id="username" name="username">
        <label for="password"> Password : </label>
        <input type="password" id="password" name="password">
        <input type="submit">
    </form>
    <p>
        <a href="home">home</a>
        </br>
        <a href="registration">registration</a>
    </p>
</body>
</html>
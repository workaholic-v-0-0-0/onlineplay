<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>home</title>
</head>

<body>
    <p>
        Minimal version of the web application onlineplay.
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

    <p>
        <a href="register">Sign up</a>
        </br>
        <a href="authentification">log in</a>
    </p>

    <c:if test="${sessionScope.userConnection.userId != -1}">
        <p>
            You are authenticated
            </br>
            Your user'id is <c:out value="${sessionScope.userConnection.userId}" />
        </p>
    </c:if>


</body>
</html>

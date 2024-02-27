<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Onlineplay homepage</title>
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
        sessionScope.userConnection.isAllowed : <c:out value="${sessionScope.userConnection.isAllowed}" />
        </br>
    </p>

    <p>
        <a href="registration">Sign up</a>
        </br>
        <a href="authentication">log in</a>
        <c:if test="${sessionScope.userConnection.userId !=-1}">
            </br>
            <a href="logout">log out</a>
        </c:if>
    </p>

    <c:if test="${sessionScope.userConnection.userId != -1}">
        <p>
            You are authenticated as <c:out value="${sessionScope.user.username}" />
            </br>
            Your user'id is <c:out value="${sessionScope.userConnection.userId}" />
        </p>
    </c:if>

</body>
</html>

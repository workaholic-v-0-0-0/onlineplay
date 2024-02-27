<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Onlineplay registration page</title>
</head>
<body>
    <p>
        Registration page
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

    <form method="post" action="registration">
        <label for="username"> Username : </label>
        <input type="text" id="username" name="username">
        <label for="password"> Password : </label>
        <input type="password" id="password" name="password">
        <label for="email"> Email : </label>
        <input type="text" id="email" name="email">
        <label for="message"> Message : </label>
        <input type="text" id="message" name="message">
        <input type="submit">
    </form>
    <p>
        <a href="home">home</a>
        </br>
        <a href="authentication">authentication</a>
    </p>
    <c:if test="${not empty registrationProblemEncountred}">
        <p>
            registration failed due to :
            <c:out value="${registrationProblemEncountred}" />
        </p>
    </c:if>
    <c:if test="${not empty hasJustBeenRegistred}">
        <p>
            A user account with username <c:out value="${hasJustBeenRegistred}" /> has just been created.
        </p>
    </c:if>

</body>
</html>
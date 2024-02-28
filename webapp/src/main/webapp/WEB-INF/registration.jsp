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

    <!-- begin to debug -->
    <c:if test="${not empty sessionScope.userConnection}">
        <p>
            sessionScope.userConnection is defined :
            <ul>
            <li>
            sessionScope.userConnection.id : <c:out value="${sessionScope.userConnection.id}" />
            </li>
            <li>
            sessionScope.userConnection.ipAddress : <c:out value="${sessionScope.userConnection.ipAddress}" />
            </li>
            <li>
            sessionScope.userConnection.timestamp : <c:out value="${sessionScope.userConnection.timestamp}" />
            </li>
            <li>
            sessionScope.userConnection.userId : <c:out value="${sessionScope.userConnection.userId}" />
            </li>
            <li>
            sessionScope.userConnection.isAllowed : <c:out value="${sessionScope.userConnection.isAllowed}" />
            </li>
            </ul>
        </p>
    </c:if>
    <c:if test="${not empty sessionScope.user}">
        <p>
            sessionScope.user is defined :
            <ul>
            <li>
            sessionScope.user.id : <c:out value="${sessionScope.user.id}" />
            </li>
            <li>
            sessionScope.user.username : <c:out value="${sessionScope.user.username}" />
            </li>
            <li>
            sessionScope.user.passwordHash : <c:out value="${sessionScope.user.passwordHash}" />
            </li>
            <li>
            sessionScope.user.email : <c:out value="${sessionScope.user.email}" />
            </li>
            <li>
            sessionScope.user.message : <c:out value="${sessionScope.user.message}" />
            </li>
            </ul>
        </p>
    </c:if>
    <!-- end to debug -->

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
        <c:if test="${sessionScope.userConnection.userId !=-1}">
            </br>
            <a href="logout">log out</a>
        </c:if>
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
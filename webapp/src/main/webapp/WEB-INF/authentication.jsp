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

    <c:if test="${not empty sessionScope.user}">
        <p>
            You are already authenticated as <c:out value="${sessionScope.user.username}" />
            </br>
            You have to log out before authenticate.
        </p>
    </c:if>
    <c:if test="${empty sessionScope.user}">
        <form method="post" action="authentication">
            <label for="username"> Username : </label>
            <input type="text" id="username" name="username">
            <label for="password"> Password : </label>
            <input type="password" id="password" name="password">
            <input type="submit">
        </form>
    </c:if>

    <c:if test="${not empty hasJustBeenAuthenticated}">
        <p>
            Authentication succeeded !
            </br>
            Hello <c:out value="${hasJustBeenAuthenticated}" /> !
        </p>
    </c:if>

    <c:if test="${not empty authenticationFailed}">
        <p>
            Authentication failed. The username or the password is incorrect.
        </p>
    </c:if>

    <c:if test="${not empty authenticationProblemEncountred}">
        <p>
            Authentication failed : <c:out value="${authenticationProblemEncountred}" />
        </p>
    </c:if>

    <p>
        <a href="home">home</a>
        </br>
        <a href="registration">registration</a>
        <c:if test="${sessionScope.userConnection.userId !=-1}">
            </br>
            <a href="logout">log out</a>
        </c:if>
    </p>

</body>
</html>
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

    <form method="post" action="register">
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
        <a href="authentification">authentification</a>
    </p>
    <c:if test="${not empty registrationProblemEncountred}">
        <c:choose>
            <c:when test="${registrationProblemEncountred == 'USERNAMEALREADYUSED'}">
                <p>
                    This username is already used.
                </p>
            </c:when>
            <c:when test="${registrationProblemEncountred == 'NONE'}">
                 <p>
                     Your user account has been created.
                 </p>
            </c:when>
            <c:otherwise>
                <!-- Traitement par défaut -->
            </c:otherwise>
        </c:choose>
    </c:if>

</body>
</html>
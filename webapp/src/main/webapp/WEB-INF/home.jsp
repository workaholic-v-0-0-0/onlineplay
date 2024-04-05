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
        Sylvain's minimal version of the web application onlineplay.
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

    <p>
        <a href="registration">Sign up</a>
        </br>
        <a href="authentication">log in</a>
        <c:if test="${sessionScope.userConnection.userId !=-1}">
            </br>
            <a href="logout">log out</a>
        </c:if>
        </br>
        <a href="https://caltuli.online/docs/index.html">Project site generated by maven-site-plugin</a>
        </br>
        <a href="https://github.com/workaholic-v-0-0-0/onlineplay">Project sources on GitHub</a>
    </p>

    <c:if test="${sessionScope.userConnection.userId != -1}">
        <p>
            You are authenticated as <c:out value="${sessionScope.user.username}" />
            </br>
            Your user'id is <c:out value="${sessionScope.userConnection.userId}" />
        </p>
    </c:if>

    <p>
        Connected users list :
        <ul>
            <c:forEach var="entry" items="${connectedUserList}">
            <li>
                <c:out value="${entry.value.username}" />
            </li>
            </c:forEach>
        </ul>
    <p>

    <script>
    document.addEventListener('DOMContentLoaded', function() {
        function fetchUsers() {
            fetch('user-activities')
                .then(function(response) {
                    return response.json();
                })
                .then(function(users) {
                    const userListUl = document.querySelector('ul');
                    userListUl.innerHTML = ''; // Effacer les utilisateurs existants
                    users.forEach(function(user) {
                        const li = document.createElement('li');
                        li.textContent = user.username;
                        userListUl.appendChild(li);
                    });
                })
                .catch(function(error) {
                    console.error('Error:', error);
                });
        }

        // Appeler fetchUsers toutes les 5 secondes
        setInterval(fetchUsers, 5000);
    });
    </script>


</body>
</html>

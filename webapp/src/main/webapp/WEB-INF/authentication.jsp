<!--
    FILE: authentication.jsp
    PURPOSE: Serves as the user authentication interface for the Onlineplay web application. This
             page allows users to log in using their credentials and provides feedback on the
             success or failure of login attempts.
    FUNCTIONALITIES:
        1) Authentication Form: Provides a form for users to input their username and password if
           they are not already logged in.
        2) Authentication Feedback: Informs users whether their authentication attempt was
           successful or failed, and provides specific error messages if applicable.
        3) Session Handling: Checks if the user is already logged in and prevents
           re-authentication, advising logged-in users to log out before attempting to log in
           again.
        4) Navigation Links: Offers links to the home page and registration page, and a logout
           option for users who are already logged in.
    AUTHOR: Sylvain Labopin
    TAGLIBS USED:
        - Jakarta Core Tags: Utilized for conditional content rendering based on the user's
          authentication state.
    REQUEST VARIABLES:
        - authenticationFailed: Boolean flag set if username or password is incorrect.
        - hasJustBeenAuthenticated: Contains the username of the user if authentication succeeds.
        - authenticationProblemEncountred: Contains error messages from business logic or
        user-specific exceptions.
    SESSION VARIABLES:
        - sessionScope.user: Represents the authenticated user. This is set in the session by the
          Authentication servlet when a user successfully authenticates via
          userManager.authenticateUser(username, password). Contains user details such as username,
          user ID, etc., and is used to check authentication status and display user-specific
          information.
        - sessionScope.userConnection: Managed by webapp.filter.SessionManagement.java. This object
          represents the connection between the client and server. It's initialized via
          userManager.logUserConnection(ipAddress, timestamp) and set in the session by
          session.setAttribute("userConnection", userConnection). It is essential for
          webapp.filter.SessionCheckFilter to maintain session integrity and user connectivity.
          It is updated with the user ID when authentication is successful.
    FILES THIS USES:
        - Contains links to home.jsp, registration.jsp and logout.jsp to facilitate
          user navigation and account management directly from the main page.
    FILES THAT USE THIS FILE:
        - Authentication.java: This servlet is directly responsible for processing the login
          requests from the authentication form present on this page. It handles user credentials,
          performs authentication checks, and redirects users based on the outcome. The servlet
          also manages session attributes that are crucial for conditional rendering on this page,
          such as user authentication status.
        - Pages that provide links to this page are home.jsp, registration.jsp and logout.jsp.
    EXAMPLES OF USE:
        - A user visits this page to log in to their account.
        - A logged-in user is redirected here to log out or is informed to do so if they attempt
          to access the login form again.
    FORM DATA SUBMISSIONS:
      - authentication.jsp contains a form that facilitates user interactions by sending data to
        the server through POST requests. Here are the details of the form and its respective
        variables:
        User Authentication:
           - Form action: authentication
           - Method: POST
           - Variables:
             * username: [input from the user] (collects the username entered by the user)
             * password: [input from the user] (collects the password entered by the user)
           - User action: This form allows a user to authenticate by entering their username and
                          password and clicking the submit button. It sends a request to
                          authenticate the user based on the credentials provided.
    MAVEN AND SERVLET CONFIGURATION:
      - The Authentication servlet is configured in the web.xml file to handle requests to
        '/authentication'. This servlet processes the user login attempts. Below is the relevant
        servlet configuration in web.xml:
            <servlet>
            <servlet-name>Authentication</servlet-name>
            <servlet-class>online.caltuli.webapp.servlet.gui.Authentication</servlet-class>
            </servlet>
            <servlet-mapping>
            <servlet-name>Authentication</servlet-name>
            <url-pattern>/authentication</url-pattern>
            </servlet-mapping>
        This configuration directs requests to '/authentication' to be handled by the
        Authentication servlet, which is responsible for validating user credentials and managing
        session state based on authentication success or failure.
-->

<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Authentication</title>
</head>
<body>
<p>
    Authentication page
</p>

<!-- Check if the user is already logged in -->
<c:if test="${not empty sessionScope.user}">
    <p>
        You are already authenticated as <c:out value="${sessionScope.user.username}" />
        </br>
        You have to log out before authenticate.
    </p>
</c:if>

<!-- Display the login form if the user is not logged in -->
<c:if test="${empty sessionScope.user}">
    <form method="post" action="authentication">
        <label for="username"> Username : </label>
        <input type="text" id="username" name="username">
        <label for="password"> Password : </label>
        <input type="password" id="password" name="password">
        <input type="submit">
    </form>
</c:if>

<!-- Display a success message if the user has just been authenticated -->
<c:if test="${not empty hasJustBeenAuthenticated}">
    <p>
        Authentication succeeded !
        </br>
        Hello <c:out value="${hasJustBeenAuthenticated}" /> !
    </p>
</c:if>

<!-- Display an error message if the authentication failed -->
<c:if test="${not empty authenticationFailed}">
    <p>
        Authentication failed. The username or the password is incorrect.
    </p>
</c:if>

<!-- Display an error message if there was a problem during the authentication process -->
<c:if test="${not empty authenticationProblemEncountred}">
    <p>
        Authentication failed : <c:out value="${authenticationProblemEncountred}" />
    </p>
</c:if>

<!-- Links to other pages -->
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
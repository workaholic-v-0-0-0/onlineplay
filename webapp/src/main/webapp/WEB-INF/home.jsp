<!--
    FILE: homepage.jsp
    PURPOSE: Serves as the main landing page for the Onlineplay web application.
    FUNCTIONALITIES:
         1) Displays information about currently authenticated users, users awaiting opponents
            for games, and ongoing games. These lists are updated in real-time through GET
            requests to the UserActivities servlet, which fetches updated data every 5 seconds.
         2) Provides links to other pages of the web application for user account creation,
            authentication, and logging out.
         3) Offers external links: a GitHub link to the project's source code and a link to
            the project's documentation site.
         4) Supports actions via POST requests: for authenticated users to join games proposed
            by others or to propose new games.
         5) Enables interactive gameplay through WebSockets and the React library: users can
            view and play Power 4 games interactively if they are authenticated and involved
            in a game.
    AUTHOR: Sylvain Labopin
    TAGLIBS USED:
      - Jakarta Core Tags: Used for conditional rendering, loops, and basic control structures.
      - Jakarta Functions Tags: Used for string manipulation and data formatting.
    REQUEST VARIABLES:
      - authenticatedUsers: List of all authenticated users, used to populate user list.
      - waitingToPlayUsers: List of users awaiting game match-ups.
      - games: Active games information displayed dynamically.
    SESSION VARIABLES:
      - sessionScope.userConnection: Managed by webapp.filter.SessionManagement.java. This object
        represents the connection between the client and server. It's initialized via
        userManager.logUserConnection(ipAddress, timestamp) and set in the session by
        session.setAttribute("userConnection", userConnection). It is essential for
        webapp.filter.SessionCheckFilter to maintain session integrity and user connectivity.
      - sessionScope.user: Represents the user. It is optionally initialized in
        webapp.servlet.gui.Authentication.java when a user successfully authenticates
        via userManager.authenticateUser(username, password). The user object is then
        set in the session with session.setAttribute("user", user). If
        sessionScope.userConnection.userId equals -1, the user is not authenticated.
    DEPENDENCIES:
      - CSS: home.css, react.css
      - JS: updateLists.js (manages dynamic list updates), fakeUsersManagement.js (simulates
            user interactions)
      - External links to project site and GitHub repository.
    FILES THIS USES:
      - React components managed through react.js and react.chunk.js for dynamic client-side
        behavior.
      - Backend endpoints defined in servlet configurations for user and game management.
      - Interacts with Registration.java, registration.jsp, Authentication.java,
        authentication.jsp, Logout.java, and Logout.jsp for user account management functions.
      - Contains links to registration.jsp, authentication.jsp, and logout.jsp to facilitate
        user navigation and account management directly from the main page.
      - Utilizes the UserActivities servlet to perform scheduled GET requests for fetching
        updated user and game data, which is displayed dynamically on the homepage.
    FILES THAT USE THIS FILE:
      - This JSP page is a standalone page but directly interacts with React components for
        dynamically displaying interactive game grids.
      - Pages that provide links to this page are authentication.jsp, registration.jsp and
        logout.jsp.
    INTERACTIONS:
      - The React application rendered in a div of this page is built using App.js and
        GameBoard.js, which handle the application logic and the interactive display of the Power 4
        game grid, respectively.
    FORM DATA SUBMISSIONS:
      - home.jsp contains forms that facilitate user interactions by sending data to the server
        through POST requests. Here are the details of the forms and their respective variables:
        1) Join a Game:
           - Form action: home
           - Method: POST
           - Variables:
             * action: 'play_with' (specifies the type of action to be taken)
             * user_id: [dynamically filled with the ID of another user] (identifies the user
                        with whom to start the game)
           - User action: This form allows a user to join a game proposed by another user, using
             the 'Play with [username]' button.
        2) Propose a New Game:
           - Form action: home
           - Method: POST
           - Variables:
             * action: 'new_game' (specifies that a new game is to be initiated)
           - User action: Users can start a new game by clicking the 'New game' button, which sends
                          a request to initiate a new game session.
    MAVEN AND SERVLET CONFIGURATION:
      - The Home servlet is configured in the web.xml file to handle requests to '/home'. This
        servlet serves home.jsp. Below is the relevant servlet configuration in web.xml:
          <servlet>
            <servlet-name>Home</servlet-name>
            <servlet-class>online.caltuli.webapp.servlet.gui.Home</servlet-class>
          </servlet>
          <servlet-mapping>
            <servlet-name>Home</servlet-name>
            <url-pattern>/home</url-pattern>
          </servlet-mapping>
        This configuration directs requests to '/home' to be handled by the Home servlet, which is
        responsible for loading and managing home.jsp.
-->

<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Onlineplay homepage</title>
    <link rel="stylesheet" href="./resources/css/home.css">
    <link rel="stylesheet" href="${react.css}">
</head>

<body>
<div class="container">
    <div class="half left">
        <p>Sylvain's minimal version of the web application onlineplay.</p>

        <!-- Allow users to perform actions via POST requests -->
        <p>
            <a href="registration">Sign up</a>
            </br>
            <a href="authentication">log in</a>
            <c:if test="${sessionScope.userConnection.userId !=-1}">
                </br>
                <a href="logout">log out</a>
            </c:if>
            </br>
            <!-- Provides functionality for clients to propose new games -->
            <form action="home" method="post">
                <input type="hidden" name="action" value="new_game">
                <input type="submit" value="New game"/>
            </form>
            </br>
            <a href="javascript:void(0);" id="sendDummyUser01">Launch fake-user_01</a>
            </br>
            <a href="https://caltuli.online/docs/index.html">
                Project site generated by maven-site-plugin
            </a>
            </br>
            <a href="https://github.com/workaholic-v-0-0-0/onlineplay">
                Project sources on GitHub
            </a>
        </p>

        <!-- Display information about the current user -->
        <c:if test="${sessionScope.userConnection.userId != -1}">
            <p>
                You are authenticated as <c:out value="${sessionScope.user.username}" />.
                </br>
                Your user'id is <c:out value="${sessionScope.userConnection.userId}" />.
            </p>
        </c:if>

        <!-- Display lists of user activities -->
        <div id="authenticatedUsersList">
            <p>Authenticated users list:</p>
            <ul>
                <!--
                    On page load, fetch and display authenticated user names from JSP
                    authenticatedUsers parameter
                -->
                <c:forEach var="entry" items="${authenticatedUsers}">
                    <li
                        data-username="${entry.value.username}"
                        data-message="${entry.value.message}"
                        data-id="${entry.value.id}"
                    >
                    <c:out value="${entry.value.username} (id: ${entry.value.id}),
                            message: ${entry.value.message})" />
                    </li>
                </c:forEach>
                <!-- This list will be updated via JavaScript as new users authenticate -->
            </ul>
        </div>
        <div id="waitingToPlayUsersList">
            <p>Users who want to play a game:</p>
            <ul>
                <!--
                    On page load, fetch and display names of users waiting to play from JSP
                    waitingToPlayUsers parameter
                -->
                <c:forEach var="entry" items="${waitingToPlayUsers}">
                    <li
                        data-username="${entry.value.username}"
                        data-message="${entry.value.message}"
                        data-id="${entry.value.id}"
                    >
                        <form action="home" method="post">
                            <input type="hidden" name="action" value="play_with">
                            <input type="hidden" name="user_id" value="${entry.value.id}">
                            <input type="submit" value="Play with ${entry.value.username}"/>
                        </form>
                    </li>
                </c:forEach>
                <!--
                    This list will be dynamically updated via JavaScript as users express
                    interest to play
                -->
            </ul>
        </div>
        <div id="gamesList">
            <p>Games:</p>
            <ul>
                <!--
                    On page load, game summaries are fetched and displayed from JSP games
                    parameter
                -->
                <c:forEach var="entry" items="${games}">
                    <li
                        data-id="${entry.value.id}"
                        data-firstPlayerUsername="${entry.value.firstPlayerUsername}"
                        data-secondPlayerUsername="${entry.value.secondPlayerUsername}"
                    >
                        <c:choose>
                            <c:when test="${not empty entry.value.firstPlayerUsername}">
                                <c:out value="${entry.value.firstPlayerUsername}" />
                            </c:when>
                            <c:otherwise>
                                <c:out value="#nobody#" />
                            </c:otherwise>
                        </c:choose>
                        " vs "
                        <c:choose>
                            <c:when test="${not empty entry.value.secondPlayerUsername}">
                                <c:out value="${entry.value.secondPlayerUsername}" />
                            </c:when>
                            <c:otherwise>
                                <c:out value="#nobody#" />
                            </c:otherwise>
                        </c:choose>
                    </li>
                </c:forEach>
                <!-- This list will be updated via JavaScript as games progress -->
            </ul>
        </div>

        <!--
            This script periodically refreshes and updates the lists displayed on the page,
            including the list of authenticated users, users waiting for an opponent to play
            a game, and the list of ongoing games. These updates help ensure that the user
            interface reflects the most current data without needing to reload the entire page.
        -->
        <script src="resources/js/updateLists.js"></script>

        <!--
            This script is utilized for simulating user interactions to perform functional
            testing on the web application.
            It manages events that trigger GET requests for three simulated users, helping
            validate the application's functionality.
            These requests are processed by an HTTP server which runs from a JAR file
            generated by the 'batch.userInteractionSimulation' module.
            This setup allows developers to test and observe the application's response and
            error handling capabilities under simulated user activities.
        -->
        <script src="resources/js/fakeUsersManagement.js"></script>

    </div>

    <!-- React application area -->
    <div class="half right">

        <!-- Initializing global variables for React application -->
        <script type="text/javascript">
          var game = '${game}';
          var gameId = '${fn:escapeXml(gameId)}';
          var playerId = '${fn:escapeXml(playerId)}';
        </script>

        <!-- The 'root' div serves as the mounting point for the React application -->
        <div id="root"></div>

        <!--
            Load React runtime scripts which bootstrap the React application defined in App.js
            and utilize components like GameBoard.js. These scripts set up the initial React
            environment, enabling the React components to render dynamically within the root
            div. App.js defines the main application logic, managing states and WebSocket
            connections for live game updates, while GameBoard.js handles the rendering of the
            game s interactive grid.
        -->

        <script src="${react.chunk.js}"></script>
        <script src="${react.js}"></script>
    </div>
</div>
</body>
</html>

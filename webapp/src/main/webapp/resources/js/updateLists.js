/******************************************************************************
 * NAME: updateLists.js
 * ROLE: Manages dynamic lists of users and games by fetching data from backend.
 * AUTHOR: Sylvain Labopin
 * LICENSE: Educational use in "Réalisation de programme" course.
 * COMPILATION: No compilation needed. Include in HTML for web browser execution.
 * USAGE: Embed via <script src="updateLists.js"></script> in an HTML file.
 *        Ensure the backend 'user-activities' endpoint is correctly set up.
 ******************************************************************************/

// listen for the DOM content to be fully loaded before executing the script
document.addEventListener('DOMContentLoaded', function() {

    // fetch user activity data from the server
    function fetchData() {
        fetch('user-activities') // make request to user-activities endpoint
            .then(function(response) {
                return response.json(); // parse the response as Json
            })
            .then(function(data) {
                // update lists displayed on the webpage
                updateAuthenticatedUsersList(data.authenticatedUsers);
                updateWaitingToPlayUsers(data.waitingToPlayUsers);
                updateGamesList(data.games);
            })
            .catch(function(error) {
                console.error('Error:', error);
            });
    }

    // updates the list of authenticated users displayed on the webpage
    function updateAuthenticatedUsersList(authenticatedUsers) {

        // selects unordered list (ul) where authenticated users are displayed
        const authenticatedUsersListUl =
            document.querySelector('#authenticatedUsersList ul');

        // log error and exit if the list element is not found
        if (!authenticatedUsersListUl) {
            console.error(
                "Failed to find the element with selector:",
                '#authenticatedUsersList ul'
            );
            return;
        }

        // map existing list items to manage updates and removals efficiently
        const existingAuthenticatedUsers =
            Array.from(authenticatedUsersListUl.children)
                .reduce(
                    (acc, li) => {
                        acc[parseInt(li.dataset.id, 10)] = li;
                        return acc;
                    },
                    {}
                );

        // update or add new items for each authenticated user
        authenticatedUsers.forEach(authenticatedUser => {
            let li = existingAuthenticatedUsers[authenticatedUser.id];
            if (!li) {
                li = document.createElement('li');
                li.dataset.id = authenticatedUser.id.toString();
                li.dataset.username = authenticatedUser.username;
                li.dataset.message = authenticatedUser.message;
                li.textContent =
                    authenticatedUser.username
                    + " (id: "
                    + authenticatedUser.id
                    + "), message: "
                    + authenticatedUser.message
                    + ")";
                authenticatedUsersListUl.appendChild(li);
            }
        });

        // remove users no longer authenticated
        Object.keys(existingAuthenticatedUsers).forEach(id => {
            if (!authenticatedUsers.some(p => p.id === parseInt(id, 10))) {
                authenticatedUsersListUl
                    .removeChild(existingAuthenticatedUsers[id]);
            }
        });
    }

    // updates the list of users waiting to play a game
    function updateWaitingToPlayUsers(waitingToPlayUsers) {

        // selects the unordered list (ul) where waiting users are displayed
        const waitingToPlayUsersUl =
            document.querySelector('#waitingToPlayUsersList ul');

        // log error and exit if the list element is not found
        if (!waitingToPlayUsersUl) {
            console.error(
                "Failed to find the element with selector:",
                 "#waitingToPlayUsersList ul");
            return;
        }

        // map existing list items to manage updates and removals efficiently
        const existingWaitingToPlayUsers =
            Array
                .from(waitingToPlayUsersUl.children)
                .reduce(
                    (acc, li) => {
                        acc[parseInt(li.dataset.id, 10)] = li;
                        return acc;
                    },
                    {}
                );

        // update or add new items with forms for each user waiting to play
        waitingToPlayUsers.forEach(
            waitingToPlayUser => {
                let li = existingWaitingToPlayUsers[waitingToPlayUser.id];
                if (!li) {

                    li = document.createElement('li');
                    li.dataset.id = waitingToPlayUser.id.toString();

                    // make form
                    const form = document.createElement('form');
                    form.action = "home";
                    form.method = "post";

                    /* make hidden field indicating a user wants to play
                     * against the one who corresponds to this list item */
                    const inputAction = document.createElement('input');
                    inputAction.type = "hidden";
                    inputAction.name = "action";
                    inputAction.value = "play_with";

                    // make hidden field indicating a user id of the user who
                    // corresponds to this list item
                    const inputUserId = document.createElement('input');
                    inputUserId.type = "hidden";
                    inputUserId.name = "user_id";
                    inputUserId.value = waitingToPlayUser.id.toString();

                    // make submit input
                    const inputSubmit = document.createElement('input');
                    inputSubmit.type = "submit";
                    inputSubmit.value =
                        "Play with " + waitingToPlayUser.username;

                    // assemble the form
                    form.appendChild(inputAction);
                    form.appendChild(inputUserId);
                    form.appendChild(inputSubmit);

                    // add the form to this list item element
                    li.appendChild(form);

                    waitingToPlayUsersUl.appendChild(li);
                }
            }
        );

        // remove users no longer waiting to play.
        Object.keys(existingWaitingToPlayUsers).forEach(id => {
            if (!waitingToPlayUsers.some(p => p.id === parseInt(id, 10))) {
                waitingToPlayUsersUl
                    .removeChild(existingWaitingToPlayUsers[id]);
            }
        });
    }

    // updates the games list displayed on the webpage
    function updateGamesList(games) {

        // selects the unordered list (ul) where games are displayed
        const gameListUl = document.querySelector('#gamesList ul');

        // log error and exit if the list element is not found
        if (!gameListUl) {
            console.error(
                "Failed to find the element with selector:",
                '#gamesList ul'
            );
            return;
        }

        // map existing list items to manage updates and removals efficiently
        const existingGames =
            Array.from(gameListUl.children)
                .reduce(
                    (acc, li) => {
                        acc[parseInt(li.dataset.id, 10)] = li;
                        return acc;
                    },
                    {}
                );

        // update or add new list items for each game
        games.forEach(
            game => {
                let li = existingGames[game.id];
                if (!li) {
                    li = document.createElement('li');
                    li.dataset.id = game.id.toString();
                    gameListUl.appendChild(li);
                }
                li.dataset.firstPlayerUsername = game.firstPlayer ? game.firstPlayer.username : "#nobody#";
                li.dataset.secondPlayerUsername = game.secondPlayer ? game.secondPlayer.username : "#nobody#";
                li.textContent = li.dataset.firstPlayerUsername + " vs " + li.dataset.secondPlayerUsername;
            }
        );

        // remove games no longer current
        Object.keys(existingGames).forEach(id => {
            if (!games.some(g => g.id === parseInt(id, 10))) {
                gameListUl.removeChild(existingGames[id]);
            }
        });
    }

    // refresh data every 5 seconds to keep the display updated
    setInterval(fetchData, 5000);
});
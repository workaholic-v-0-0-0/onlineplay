
document.addEventListener('DOMContentLoaded', function() {
    function fetchData() {
        fetch('user-activities')
            .then(function(response) {
                return response.json();
            })
            .then(function(data) {
                updateAuthenticatedUsersList(data.authenticatedUsers);
                updateWaitingToPlayUsers(data.waitingToPlayUsers);
                updateGamesList(data.games);
            })
            .catch(function(error) {
                console.error('Error:', error);
            });
    }

    function updateAuthenticatedUsersList(authenticatedUsers) {

        // fetch the ul component to update
        const authenticatedUsersListUl = document.querySelector('#authenticatedUsersList ul');
        if (!authenticatedUsersListUl) {
            console.error("Failed to find the element with selector:", '#authenticatedUsersList ul');
            return;
        }

        // catch displayed list in a map
        const existingAuthenticatedUsers =
            Array.from(authenticatedUsersListUl.children)
                .reduce(
                    (acc, li) => {
                        acc[parseInt(li.dataset.id, 10)] = li;
                        return acc;
                    },
                    {}
                );

        // add new authenticated users
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

        // suppress authenticatedUsers not in the list anymore
        Object.keys(existingAuthenticatedUsers).forEach(id => {
            if (!authenticatedUsers.some(p => p.id === parseInt(id, 10))) {
                authenticatedUsersListUl
                    .removeChild(existingAuthenticatedUsers[id]);
            }
        });
    }

    function updateWaitingToPlayUsers(waitingToPlayUsers) {

        // fetch the ul component to update
        const waitingToPlayUsersUl = document.querySelector('#waitingToPlayUsersList ul');
        if (!waitingToPlayUsersUl) {
            console.error("Failed to find the element with selector: #waitingToPlayUsersList ul");
            return;
        }

        // catch displayed list in a map
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

        // Add new waiting to play users
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

                    // make hidden field indicating a user wants to play against the one who
                    // corresponds to this list item
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
                    inputSubmit.value = "Play with " + waitingToPlayUser.username;

                    // Assemble the form
                    form.appendChild(inputAction);
                    form.appendChild(inputUserId);
                    form.appendChild(inputSubmit);

                    // add the form to this list item element
                    li.appendChild(form);

                    waitingToPlayUsersUl.appendChild(li);
                }
            }
        );

        // suppress users not waiting anymore to play
        Object.keys(existingWaitingToPlayUsers).forEach(id => {
            if (!waitingToPlayUsers.some(p => p.id === parseInt(id, 10))) {
                waitingToPlayUsersUl.removeChild(existingWaitingToPlayUsers[id]);
            }
        });
    }

    function updateGamesList(games) {

        // fetch the ul component to update
        const gameListUl = document.querySelector('#gamesList ul');
        if (!gameListUl) {
            console.error("Failed to find the element with selector:", '#gamesList ul');
            return;
        }

        // catch displayed list in a map
        const existingGames =
            Array.from(gameListUl.children)
                .reduce(
                    (acc, li) => {
                        acc[parseInt(li.dataset.id, 10)] = li;
                        return acc;
                    },
                    {}
                );

        // add new games
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

        // suppress games not in the list anymore
        Object.keys(existingGames).forEach(id => {
            if (!games.some(g => g.id === parseInt(id, 10))) {
                gameListUl.removeChild(existingGames[id]);
            }
        });
    }

    // Refresh the data every 5 seconds
    setInterval(fetchData, 5000);
});
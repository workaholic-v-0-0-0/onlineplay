package online.caltuli.model;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CurrentModel {

    private Map<Integer, User> authenticatedUsers = new ConcurrentHashMap<>();
    private Map<Integer, User> waitingToPlayUsers = new ConcurrentHashMap<>();
    private Map<Integer, Game> games = new ConcurrentHashMap<>();

    private Map<Game, Object> gameManagers = new ConcurrentHashMap<>();

    public Map<Integer, User> getAuthenticatedUsers() {
        return authenticatedUsers;
    }

    public Map<Integer, User> getWaitingToPlayUser() {
        return waitingToPlayUsers;
    }

    public Map<Integer, Game> getGames() {
        return games;
    }

    public Map<Game, Object> getGameManagers() {
        return gameManagers;
    }

    public void setGameManagers(Map<Game, Object> gameManagers) {
        this.gameManagers = gameManagers;
    }

    public void addAuthenticatedUser(User user) {
        this.authenticatedUsers.put(user.getId(), user);
    }

    public void addWaitingToPlayUser(User user) {
        this.waitingToPlayUsers.put(user.getId(), user);
    }

    public void addGame(Game game, Object gameManager) {
        this.games.put(game.getId(), game);
        this.gameManagers.put(game, gameManager);
    }


    public void removeAuthenticatedUser(User user) { this.authenticatedUsers.remove(user.getId()); }
    public void removeWaitingToPlayUser(User user) { this.waitingToPlayUsers.remove(user.getId()); }
    public void removeGame(Game game) { this.games.remove(game.getId()); }


}

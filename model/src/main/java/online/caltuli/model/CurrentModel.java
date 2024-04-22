package online.caltuli.model;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CurrentModel {

    private Map<Integer, User> authenticatedUsers = new ConcurrentHashMap<>();
    private Map<Integer, User> waitingToPlayUsers = new ConcurrentHashMap<>();
    private Map<Integer, Game> games = new ConcurrentHashMap<>();


    public Map<Integer, User> getAuthenticatedUsers() {
        return authenticatedUsers;
    }

    public Map<Integer, User> getWaitingToPlayUser() {
        return waitingToPlayUsers;
    }

    public Map<Integer, Game> getGames() {
        return games;
    }

    public void addAuthenticatedUser(User user) {
        this.authenticatedUsers.put(user.getId(), user);
    }

    public void addWaitingToPlayUser(User user) {
        this.waitingToPlayUsers.put(user.getId(), user);
    }

    public void addGames(Game game) {
        this.games.put(game.getId(), game);
    }
    /*
    public CurrentModel() {
        authenticatedUsers = new ConcurrentHashMap<>();
        waitingToPlayUser = new ConcurrentHashMap<>();
        games = new ConcurrentHashMap<>();
    }

     */

}

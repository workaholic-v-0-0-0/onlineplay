package online.caltuli.model;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class CurrentModel {


    private Map<Integer, User> authenticatedUsers = new ConcurrentHashMap<>();
    private Map<Integer, User> waitingToPlayUser = new ConcurrentHashMap<>();
    private Map<Integer, Game> games = new ConcurrentHashMap<>();

    /*
    private final Map<Integer, User> authenticatedUsers;
    private final Map<Integer, User> waitingToPlayUser;
    private final Map<Integer, Game> games;

     */

    public Map<Integer, User> getAuthenticatedUsers() {
        return authenticatedUsers;
    }

    public Map<Integer, User> getWaitingToPlayUser() {
        return waitingToPlayUser;
    }

    public Map<Integer, Game> getGames() {
        return games;
    }

    /*
    public CurrentModel() {
        authenticatedUsers = new ConcurrentHashMap<>();
        waitingToPlayUser = new ConcurrentHashMap<>();
        games = new ConcurrentHashMap<>();
    }

     */

}

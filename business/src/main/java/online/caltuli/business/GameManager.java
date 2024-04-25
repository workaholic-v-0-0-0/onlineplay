package online.caltuli.business;

import online.caltuli.model.Game;

public class GameManager {

    private Game game;

    public GameManager(Game game) {
        this.game = game;
    }

    // des méthodes pour piloter la partie
    // par exemple :
    // public void playMove(int columnNumber) throws IllegalMoveException {...}

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

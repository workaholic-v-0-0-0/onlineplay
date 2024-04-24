package online.caltuli.business;

import online.caltuli.model.Game;

public class GameManager {

    private Game game;

    public GameManager(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

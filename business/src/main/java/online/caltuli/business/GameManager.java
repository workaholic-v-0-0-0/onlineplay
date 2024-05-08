package online.caltuli.business;

import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GameManager {

    private Game game;
    private EvolutiveGridParser egp;

    public GameManager(Game game) {
        this.game = game;
        this.egp = new EvolutiveGridParser();
    }

    public boolean isLegalMove(int column) {
        return egp.getNextLine()[column] != 6;
    }

    private void updateGameWithMove(Coordinates coordinatesPlayed) {

        // game.colorsGrid update
        game.setColorWithCoordinates(
                coordinatesPlayed,
                (game.getGameState() == GameState.WAIT_FIRST_PLAYER_MOVE) ?
                    CellState.RED
                    :
                    CellState.GREEN
        );

        // game.gameState update
        game.setGameState(
                (game.getGameState() == GameState.WAIT_FIRST_PLAYER_MOVE) ?
                        GameState.WAIT_SECOND_PLAYER_MOVE
                        :
                        GameState.WAIT_FIRST_PLAYER_MOVE
        );
        if (egp.detectDraw()) {
            game.setGameState(GameState.DRAW);
        }
        CellState color = egp.detectWinningColor();
        if (color == CellState.RED) {
            game.setGameState(GameState.FIRST_PLAYER_WON);
        }
        if (color == CellState.GREEN) {
            game.setGameState(GameState.SECOND_PLAYER_WON);
        }

    }

    public void playMove(int column) throws BusinessException {
        if (!isLegalMove(column)) {
                throw new BusinessException(
                        "Illegal move : Column " + column + " is full."
        );
        } else  {
            Coordinates coordinatesPlayed = egp.updateWithMove(column);
            updateGameWithMove(coordinatesPlayed);
        }
    }

    public void switchPlayer() {
        Player playerMemory = game.getSecondPlayer();
        game.setSecondPlayer(game.getFirstPlayer());
        game.setFirstPlayer(playerMemory);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}

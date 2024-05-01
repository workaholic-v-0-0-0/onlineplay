package online.caltuli.business;

import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.CellState;
import online.caltuli.model.Game;
import online.caltuli.model.GameState;
import online.caltuli.model.User;

public class GameManager {

    private Game game;
    private EvolutiveGridParser egp;

    public GameManager(Game game) {
        this.game = game;
        this.egp = new EvolutiveGridParser();
    }

    public void switchPlayer() {
        User playerMemory = game.getSecondPlayer();
        game.setSecondPlayer(game.getFirstPlayer());
        game.setFirstPlayer(playerMemory);
    }

    public boolean isLegalMove(int column) {
        return egp.getNextLine()[column] != 6;
    }

    public boolean isWinningMove(int column) {
        /*
        TO DO
        */
        return false;
    }

    public void playMove(int column) throws BusinessException {
        if (!isLegalMove(column)) {
                throw new BusinessException(
                        "Illegal move : Column " + column + " is full."
        );
        } else  {
            /*
             TO DO
             */
            /*
            egp.updateWithMove(column);
            game.updateWithMove(column);
            if egp.detectsWin() {

            }
             */
        }
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    // des méthodes pour piloter la partie

    // par exemple :
    // public void playMove(int columnNumber) throws IllegalMoveException {...}
    // swithchPlayer

    // pour voir si un joueur a gagné, regarder
    // egp.getRedRowsToNbOfRedCoordinates() et
    // egp.getGreenRowsToNbOfGreenCoordinates()

    // pour voir si on peut jouer dans une colonne, regarder
    // egp.getNextLine()

    // remarque : lors de la création d'une instance de GameManager,
    // un pointeur à une instance de Game est passé au constructeur
    // (il n'y a pas de copie en mémoire).
    // Donc une modification de l'attribut game faite par une méthode
    // de l'instance de GameManager créée modifie l'instance passée
    // lors de la création de l'instnace de GameManager.
}

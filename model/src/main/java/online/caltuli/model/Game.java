package online.caltuli.model;

import java.util.HashMap;

import static online.caltuli.model.GameState.WAIT_OPPONENT;

public class Game {

    private int id;
    private HashMap<Coordinates, CellState> colorsGrid;
    private Player firstPlayer;
    private Player secondPlayer;
    private GameState gameState;

    public Game() {

    }

    /*
     * Constructs a new Game instance with the specified initial settings. This
     * constructor initializes a game with an ID and the first player already
     * defined, setting the stage for the addition of a second player. The game
     * state is initialized to WAIT_OPPONENT, indicating that the game is ready
     * but waiting for a second player to join. The game grid is also initialized
     * but starts empty.
     * @param id The unique identifier for the new game, automatically generated
     *           as the primary key when a new record is inserted into the 'games'
     *           table. This identifier must be passed to the constructor once it
     *           is obtained from the database.
     * @param firstPlayer The User object representing the player who proposed the
     *                    game. This user is initially set to play first. However,
     *                    once a second player joins the game, there may be a
     *                    reassignment of roles, allowing for the possibility of the
     *                    first and second players to swap their playing order.
     */
    public Game(int id, Player firstPlayer) {
        this.id = id;
        this.colorsGrid = new HashMap<Coordinates, CellState>();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = null;
        this.gameState = WAIT_OPPONENT;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Coordinates, CellState> getColorsGrid() {
        return colorsGrid;
    }

    public void setColorsGrid(HashMap<Coordinates, CellState> colorsGrid) {
        this.colorsGrid = colorsGrid;
    }

    public void setColorWithCoordinates(Coordinates coordinates, CellState color) {
        this.colorsGrid.put(coordinates, color);
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Player firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Player getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Player secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}

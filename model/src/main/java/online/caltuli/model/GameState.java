package online.caltuli.model;

public enum GameState {
    WAIT_OPPONENT,
    WAIT_FIRST_PLAYER_MOVE,
    WAIT_SECOND_PLAYER_MOVE,
    FIRST_PLAYER_WON,
    SECOND_PLAYER_WON,
    DRAW,
    TO_BE_CANCELLED
}

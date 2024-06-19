package online.caltuli.model;

public class Move {
    int id;
    int gameId;
    int userId;
    int number;
    int playedColumn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getPlayedColumn() {
        return playedColumn;
    }

    public void setPlayedColumn(int playedColumn) {
        this.playedColumn = playedColumn;
    }
}

package online.caltuli.model;

public class GameSummary {
    private int id;
    private String firstPlayerUsername;
    private String secondPlayerUsername;

    public GameSummary(int id, String firstPlayerUsername, String secondPlayerUsername) {
        this.id = id;
        this.firstPlayerUsername = firstPlayerUsername;
        this.secondPlayerUsername = secondPlayerUsername;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstPlayerUsername() {
        return firstPlayerUsername;
    }

    public void setFirstPlayerUsername(String firstPlayerUsername) {
        this.firstPlayerUsername = firstPlayerUsername;
    }

    public String getSecondPlayerUsername() {
        return secondPlayerUsername;
    }

    public void setSecondPlayerUsername(String secondPlayerUsername) {
        this.secondPlayerUsername = secondPlayerUsername;
    }
}

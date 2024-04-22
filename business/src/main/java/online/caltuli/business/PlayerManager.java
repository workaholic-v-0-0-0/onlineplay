package online.caltuli.business;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Specializes;
import online.caltuli.consumer.dao.exceptions.DatabaseConnectionException;
import online.caltuli.consumer.dao.interfaces.GamesDao;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.consumer.dao.DaoFactory;
import online.caltuli.consumer.dao.exceptions.DaoException;
import online.caltuli.consumer.dao.interfaces.UserConnectionsDao;
import online.caltuli.model.Game;
import online.caltuli.model.GameSummary;
import online.caltuli.model.Player;
import online.caltuli.model.User;

import java.util.HashMap;
import java.util.Map;

@Specializes
@ApplicationScoped
public class PlayerManager extends UserManager {

    public Game makeUserProposeGame(User user_who_proposed_the_game) throws BusinessException {
        GamesDao gamesDao = null;
        Game game = null;
        try {
            gamesDao = DaoFactory.getInstance().getGamesDao();
        } catch (DatabaseConnectionException e) {
            logger.info(e.getMessage());
            throw new BusinessException("Can't communicate with database.");
        }
        try {
            logger.info("here 1");
            int id = gamesDao.newRecord();
            game = new Game(id, user_who_proposed_the_game);
            logger.info("here 2");
            gamesDao.updateGame(game);
            logger.info("here 3");
        } catch (DaoException e) {
            logger.info(e.getMessage());
            throw new BusinessException("Can't log game in games table.");
        }
        currentModel.addGames(game);
        currentModel.addWaitingToPlayUser(user_who_proposed_the_game);
        currentModel.addGames(game);
        return game;
    }

    public Map<Integer, Player> usersToPlayer(Map<Integer, User> users) {
        Map<Integer, Player> players = new HashMap<Integer, Player>();
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            Integer id = entry.getKey();
            User user = entry.getValue();
            Player player = new Player(
                    id,
                    user.getUsername(),
                    user.getMessage()
            );
            players.put(id, player);
        }
        return players;
    }

    public Map<Integer, GameSummary> gamesToGameSummaries(Map<Integer, Game> games) {
        Map<Integer, GameSummary> gameSummaries = new HashMap<Integer, GameSummary>();
        for (Map.Entry<Integer, Game> entry : games.entrySet()) {
            Integer id = entry.getKey();
            Game game = entry.getValue();
            User user;
            GameSummary gameSummary =
                    new GameSummary(
                            id,
                            (user = game.getFirstPlayer()) != null ?
                                    user.getUsername()
                                    :
                                    null
                            ,
                            (user = game.getSecondPlayer()) != null ?
                                    user.getUsername()
                                    :
                                    null
                    );
            gameSummaries.put(id, gameSummary);
        }
        return gameSummaries;
    }

    public Map<Integer, User> getWaitingToPlayUser() {
        return currentModel.getWaitingToPlayUser();
    }

    public Map<Integer, Game> getGames() {
        return currentModel.getGames();
    }

}

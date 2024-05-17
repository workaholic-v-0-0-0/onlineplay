package online.caltuli.business;

import online.caltuli.consumer.dao.exceptions.DatabaseConnectionException;
import online.caltuli.consumer.dao.interfaces.GamesDao;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.consumer.dao.DaoFactory;
import online.caltuli.consumer.dao.exceptions.DaoException;
import online.caltuli.consumer.dao.interfaces.UsersDao;
import online.caltuli.model.*;

import java.util.HashMap;
import java.util.Map;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Specializes;

@Specializes
@ApplicationScoped
public class PlayerManager extends UserManager {

    public GameManager makeUserProposeGame(User user_who_proposed_the_game) throws BusinessException {
        GameManager gameManager = null;
        GamesDao gamesDao = null;
        Game game = null;
        try {
            gamesDao = DaoFactory.getInstance().getGamesDao(currentModel);
        } catch (DatabaseConnectionException e) {
            logger.info(e.getMessage());
            throw new BusinessException("Can't communicate with database.");
        }
        try {
            int id = gamesDao.newRecord();
            game = new Game(
                    id,
                    (Player) user_who_proposed_the_game
            );
            gamesDao.updateGame(game);
        } catch (DaoException e) {
            logger.info(e.getMessage());
            throw new BusinessException("Can't log game in games table.");
        }

        // GameManager instance construction
        gameManager = new GameManager(game);

        currentModel.addWaitingToPlayUser(user_who_proposed_the_game);
        currentModel.addGame(game, gameManager);

        return gameManager;
    }

    // User instance of user with id id_of_user_who_proposed_the_game
    // has to be fetched because only information contained in Player instances
    // is accessible from front-end for security reasons
    public GameManager makeUserPlayWithUser(
            int id_of_user_who_proposed_the_game,
            User user_who_joins_the_game)
        throws BusinessException {

        GameManager gameManager = null;
        User user_who_proposed_the_game = null;
        UsersDao usersDao = null;
        GamesDao gamesDao = null;
        Game game = null;

        // fetch DAOs
        try {
            usersDao = DaoFactory.getInstance().getUsersDao(currentModel);
            gamesDao = DaoFactory.getInstance().getGamesDao(currentModel);
        } catch (DatabaseConnectionException e) {
            logger.info(e.getMessage());
            throw new BusinessException("Can't communicate with database.");
        }

        // fetch User instance related to the user who proposed the game
        try {
            user_who_proposed_the_game = usersDao.getUserById(id_of_user_who_proposed_the_game);
        } catch (DaoException e) {
            logger.info(e.getMessage());
            throw new BusinessException(e.getMessage());
        }

        // fetch Game instance related to the game proposed by user
        // whose id is id_of_user_who_proposed_the_game
        game = getGameProposedByUser(user_who_proposed_the_game);
        if (game == null) {
            String error_message = "User "
                    + user_who_joins_the_game.getId()
                    + " wants to play with user "
                    + id_of_user_who_proposed_the_game
                    + " but this one did not propose a game.";
            logger.info(error_message);
            throw new BusinessException(error_message);
        }

        // update CurrentModel CDI bean
        game.setSecondPlayer((Player) user_who_joins_the_game);
        game.setGameState(GameState.WAIT_FIRST_PLAYER_MOVE);
        getWaitingToPlayUser().remove(id_of_user_who_proposed_the_game);

        // fetch the gameManager related to game in CurrentModel CDI bean
        gameManager = (GameManager) (currentModel.getGameManagers().get(game));

        return gameManager;
    }

    public Game getGameProposedByUser(User user_who_proposed_game) {
        for (Game game : getGames().values()) {
            if (game.getFirstPlayer() == (Player) user_who_proposed_game) {
                return game;
            }
        }
        return null;
    }

    public Map<Integer, Player> usersToPlayer(Map<Integer, User> users) {
        Map<Integer, Player> players = new HashMap<Integer, Player>();
        for (Map.Entry<Integer, User> entry : users.entrySet()) {
            Integer id = entry.getKey();
            User user = entry.getValue();
            Player player = (Player) user;
            players.put(id, player);
        }
        return players;
    }

    public Map<Integer, GameSummary> gamesToGameSummaries(Map<Integer, Game> games) {
        Map<Integer, GameSummary> gameSummaries = new HashMap<Integer, GameSummary>();
        for (Map.Entry<Integer, Game> entry : games.entrySet()) {
            Integer id = entry.getKey();
            Game game = entry.getValue();
            Player player;
            GameSummary gameSummary =
                    new GameSummary(
                            id,
                            (player = game.getFirstPlayer()) != null ?
                                    player.getUsername()
                                    :
                                    null
                            ,
                            (player = game.getSecondPlayer()) != null ?
                                    player.getUsername()
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

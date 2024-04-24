package online.caltuli.consumer.dao.implementations;

import online.caltuli.consumer.dao.DaoFactory;

import jakarta.inject.Inject;
import online.caltuli.consumer.dao.exceptions.DaoException;
import online.caltuli.consumer.dao.exceptions.UserDataAccessException;
import online.caltuli.consumer.dao.interfaces.GamesDao;
import online.caltuli.consumer.dao.interfaces.UsersDao;
import online.caltuli.model.CurrentModel;
import online.caltuli.model.Game;
import online.caltuli.model.GameState;
import online.caltuli.model.User;
import online.caltuli.model.exceptions.user.UserException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Map;

public class GamesDaoImpl implements GamesDao {

    private final DaoFactory daoFactory;

    private CurrentModel currentModel;

    private final Logger logger = LogManager.getLogger(GamesDaoImpl.class);

    /**
     * CONSTRUCTOR
     */
    public GamesDaoImpl(DaoFactory daoFactory, CurrentModel currentModel) {
        this.daoFactory = daoFactory;
        this.currentModel = currentModel;
    }

    /**
     * Creates a new game record in the database with default or NULL values,
     * and retrieves the automatically generated ID of the new record. This method:
     * - Connects to the database and prepares an SQL statement for insertion.
     * - Executes the insertion statement to create a new row in the 'games' table.
     * - Retrieves the generated primary key of the new record if the insertion
     *   is successful.
     * - Commits the transaction to ensure the record is permanently saved.
     * If any part of this process fails, such as a database connectivity issue or
     * an SQL error during insertion, the transaction is rolled back to prevent
     * partial data corruption and an exception is thrown to indicate the failure.
     *
     * @return The integer ID generated for the new game record, which is 0 if no
     *         record is created.
     * @throws DaoException If there is a failure in creating the record or during
     *         the closing of database resources.
     */
    public int newRecord() throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int generatedId = 0;
        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO games () VALUES ();",
                    Statement.RETURN_GENERATED_KEYS
            );

            // fetch primary key id of the new record
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException e2) {
                logger.info("WARNING: Partial registration might have been done in games table.");
            }
            throw new DaoException("Failed to add game due to database error.");

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DaoException("Failed to close connection with database.");
            }
        }
        return generatedId;
    }

    /**
     * Retrieves a game record by its ID from the database or from the cached
     * games in the CurrentModel. The method performs the following steps:
     * - First, it checks the cached games within the CurrentModel bean. If the
     *   game is found in the cache, it is immediately returned.
     * - If the game is not in the cache, the method queries the games table in
     *   the database using the game ID.
     * - Constructs a new Game instance using:
     *   - Game ID
     *   - Game state (converted from string to enum)
     *   - First player's ID, fetched and converted to a User object
     *   - Second player's ID, fetched and converted to a User object if not null
     * The method ensures the Game instance is fully initialized with all necessary
     * data, except the game grid which is not set here.
     *
     * @param id The unique identifier of the game to be retrieved.
     * @return The Game instance with the specified ID, fully constructed with all
     *         relevant data.
     * @throws DaoException If the game is not found, or if a database access error
     *         occurs.
     */
    public Game getGameById(int id) throws DaoException {

        // try to find the Game instance in the CurrentModel @ApplicationScoped bean ;
        // return it if it is found
        Game game = ((Map<Integer, Game>) currentModel.getGames()).get(id);
        if (game != null) {
            return game;
        }

        // try to construct the Game instance with respect to information in the games table
        String sql = "SELECT id, state, first_player_id, second_player_id FROM games WHERE id = ?";
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                UsersDao usersDao = DaoFactory.getInstance().getUsersDao(currentModel);
                User firstPlayer = usersDao.getUserById(resultSet.getInt("first_player_id"));
                User secondPlayer = usersDao.getUserById(resultSet.getInt("second_player_id"));

                game = new Game();
                game.setId(resultSet.getInt("id"));
                // do not initialize game.grid yet
                game.setFirstPlayer(firstPlayer);
                game.setFirstPlayer(secondPlayer);
                game.setGameState(
                        GameState.valueOf(resultSet.getString("state"))
                );
                return game;
            } else {
                throw new DaoException("No game found with ID " + id);
            }

        } catch (SQLException e) {
            throw new DaoException("Error fetching game with ID " + id + ": " + e.getMessage());
        }
    }

    /**
     * Updates the game record in the database. This method modifies the game state,
     * first player ID, and second player ID in the 'games' table for a given game ID.
     * The steps involved are:
     * - Prepare an SQL statement to update the 'games' table.
     * - Set the game state, first player ID, and second player ID using the provided
     *   Game object.
     * - Execute the SQL update command.
     * - Commit the transaction to ensure data consistency.
     * This method assumes that the game object passed as a parameter is fully initialized
     * and contains valid and existing user IDs for both the first and second players.
     * If the SQL operation fails for any reason, such as a connection issue or SQL syntax error,
     * a DaoException is thrown encapsulating the error message.
     *
     * @param game The Game object containing the updated data for the game record.
     * @throws DaoException If an SQL error occurs or if the update operation affects no rows,
     *                      indicating that the game ID may not exist.
     */
    public void updateGame(Game game) throws DaoException {
        String sql = "UPDATE games SET state = ?, first_player_id = ?, second_player_id = ? WHERE id = ?;";
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, game.getGameState().name());
            if (game.getFirstPlayer() != null) {
                statement.setInt(2, game.getFirstPlayer().getId());
            } else {
                statement.setNull(2, java.sql.Types.INTEGER);  // Set NULL if no first player
            }

            if (game.getSecondPlayer() != null) {
                statement.setInt(3, game.getSecondPlayer().getId());
            } else {
                statement.setNull(3, java.sql.Types.INTEGER);  // Set NULL if no second player
            }
            statement.setInt(4, game.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
            /*
        } catch (SQLException e) {
            throw new DaoException("Not able to update game " + game.getId());
        }

             */
    }

    /* useless ?
    public Game getGameByFirstPlayerId(int firstPlayerId) throws DaoException {
        Game game = null;

        return game;
    }
     */
}

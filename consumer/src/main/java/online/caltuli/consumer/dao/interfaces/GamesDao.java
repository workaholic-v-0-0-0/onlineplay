package online.caltuli.consumer.dao.interfaces;

import online.caltuli.consumer.dao.exceptions.DaoException;
import online.caltuli.model.Game;
import online.caltuli.model.User;

public interface GamesDao {

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
    public int newRecord() throws DaoException;


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
    public Game getGameById(int id) throws DaoException;


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
    public void updateGame(Game game) throws DaoException;

    /* useless ?
    public Game getGameByFirstPlayerId(int firstPlayerId) throws DaoException;
     */
}

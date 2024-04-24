package online.caltuli.consumer.dao.implementations;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.inject.Inject;
import online.caltuli.consumer.dao.DaoFactory;
import online.caltuli.consumer.dao.exceptions.UserDataAccessException;
import online.caltuli.consumer.dao.interfaces.UsersDao;
import online.caltuli.consumer.dao.exceptions.DaoException;

import online.caltuli.model.CurrentModel;
import online.caltuli.model.Game;
import online.caltuli.model.User;
import online.caltuli.model.exceptions.user.*;
import online.caltuli.model.UserConnection;
import online.caltuli.model.exceptions.user.UserException;

import java.sql.*;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UsersDaoImpl implements UsersDao {

    protected CurrentModel currentModel;

    private final DaoFactory daoFactory;

    private final Logger logger = LogManager.getLogger(UsersDaoImpl.class);

    @Inject
    public UsersDaoImpl(DaoFactory daoFactory, CurrentModel currentModel) {
        this.daoFactory = daoFactory;
        this.currentModel = currentModel;
    }

    // add a record in users table ; return primary key
    public User addUser(String username, String passwordHash, String email, String message) throws DaoException, UserException {

        User user = null;

        try {
            user = new User(username, passwordHash, email, message);
        } catch (UserException e) {
            logger.info("Failed to create user due to validation rule violation." + e.getMessage());
            throw e;
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int generatedId = -1;
        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO users (username, password_hash, email, message) VALUES (?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, passwordHash);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, message);

            // fetch user id
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1);
                        user.setId(generatedId);
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
                // WARNING : users table might contain a partial registration
                // users table have to be cleaned ;
                // this warning has to be logged
                // TO DO
            }
            throw new UserDataAccessException("Failed to add user due to database error.");

        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DaoException("Failed to close connection with database.");
            }
        }
        return user;
    }

    public User getUserById(int id) throws DaoException {

        // try to find the User instance in the CurrentModel @ApplicationScoped bean ;
        // return it if it is found
        logger.info("currentModel: " + currentModel);
        User user = ((Map<Integer, User>) currentModel.getAuthenticatedUsers()).get(id);
        if (user != null) {
            return user;
        }

        // try to construct the User instance with respect to information in the users table
        String sql = "SELECT id, username, password_hash, email, message FROM users WHERE id = ?";
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                try {
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setEmail(resultSet.getString("email"));
                    user.setMessage(resultSet.getString("message"));
                } catch (UserException e) {

                }
                return user;
            } else {
                throw new DaoException("No user found with ID " + id);
            }

        } catch (SQLException e) {
            throw new DaoException("Error fetching user with ID " + id + ": " + e.getMessage());
        }
    }

    public void update(User user) throws DaoException {
        String sql = "UPDATE users SET username = ?, password_hash = ?, email = ?, message = ? WHERE id = ?;";
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPasswordHash());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getMessage());
            statement.setInt(5, user.getId());
            statement.executeQuery();
        } catch (SQLException e) {
            throw new DaoException("Not able to update user " + user.getId());
        }
    }

    /*
     * Returns a User instance for the username provided; propagates UserException
     * thrown by User's constructor if a mutator detects an invalid value for its
     * attribute. Therefore, it should only throw UserException instances that are
     * actually instances of InvalidUserNameException, assuming the user information
     * table in the database only contains valid data. Raises a DaoException if it
     * catches a SQLException during database communication.
     */
    public User getUserByUsername(String username) throws DaoException, UserException {
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ?;";
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeQuery();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                try {
                    user.setId(resultSet.getInt("id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setPasswordHash(resultSet.getString("password_hash"));
                    user.setEmail(resultSet.getString("email"));
                    user.setMessage(resultSet.getString("message"));
                } catch (UserException e) {
                    logger.info(
                            "getUserByUsername("
                            + username
                            + ") handle UserException with message "
                            + e.getMessage()
                    );
                    throw e;
                }
            }
            return user;

        } catch (SQLException e) {
            logger.info("Error fetching user with username " + username + ": " + e.getMessage());
            throw new DaoException("Error fetching user with username " + username + ": " + e.getMessage());
        }
    }
}

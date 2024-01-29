package online.caltuli.consumer;

import online.caltuli.model.User;
import online.caltuli.model.UserConnection;

import java.sql.*;

public class UsersDaoImpl implements UsersDao {

    private final DaoFactory daoFactory;

    public UsersDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // add a record in users table ; return primary key
    public int addUser(String username, String passwordHash, String email, String message) throws DaoException {
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

            // fetch id
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

            }
            throw new DaoException("Database communication is impossible.");
            // Handle or rethrow the exception as needed
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                throw new DaoException("Database communication is impossible.");
            }
        }
        return generatedId;
    }
    public User getUserById(int id) throws DaoException {
        UserConnection userConnection = null;
        String sql = "SELECT id, username, password_hash, email, message FROM users WHERE id = ?";
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setEmail(resultSet.getString("email"));
                user.setMessage(resultSet.getString("message"));
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

    public User getUserByUsername(String username) throws DaoException {
        User user = null;
        String sql = "SELECT * FROM users WHERE username = ?;";
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.executeQuery();
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setEmail(resultSet.getString("email"));
                user.setMessage(resultSet.getString("message"));
            }
            return user;

        } catch (SQLException e) {
            throw new DaoException("Error fetching user with username " + username + ": " + e.getMessage());
        }
    }
}

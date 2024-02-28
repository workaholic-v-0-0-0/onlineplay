package online.caltuli.consumer.dao.implementations;

import online.caltuli.consumer.api.abuseipdb.IpValidator;
import online.caltuli.consumer.api.abuseipdb.exception.AbuseIpDbException;
import online.caltuli.consumer.dao.DaoFactory;
import online.caltuli.consumer.dao.interfaces.UserConnectionsDao;
import online.caltuli.consumer.dao.exceptions.DaoException;

import online.caltuli.model.UserConnection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class UserConnectionsDaoImpl implements UserConnectionsDao {

    private final DaoFactory daoFactory;

    private static Logger logger = LogManager.getLogger(UserConnectionsDaoImpl.class); // to debug

    public UserConnectionsDaoImpl(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    // add a record in connections table ; return primary key
    public int addUserConnection(String ipAddress, Timestamp timestamp) throws DaoException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int generatedId = 0;
        try {
            connection = daoFactory.getConnection();
            preparedStatement = connection.prepareStatement(
                    "INSERT INTO connections (ip_address, timestamp, user_id, is_allowed) VALUES (?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, ipAddress);
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.setNull(3, java.sql.Types.INTEGER);
            try {
                preparedStatement.setInt(4, IpValidator.isAllowedIp(ipAddress) ? 1 : 0);
            } catch (AbuseIpDbException e) {
                // if the check can't be done by AbuseIpDb API, make it allowed
                preparedStatement.setNull(4, java.sql.Types.INTEGER);
            }

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

    public UserConnection getUserConnectionByUserId(int id) throws DaoException {
        UserConnection userConnection = null;
        String sql = "SELECT id, ip_address, timestamp, user_id, is_allowed FROM connections WHERE id = ?";
        int userId = 0;
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userConnection = new UserConnection();
                userConnection.setId(resultSet.getInt("id"));
                userConnection.setIpAddress(resultSet.getString("ip_address"));
                userConnection.setTimestamp(resultSet.getTimestamp("timestamp"));
                //userConnection.setUserId(resultSet.getInt("user_id"));
                userId = resultSet.getInt("user_id");
                if (resultSet.wasNull()) {
                    userId = -1;
                }
                userConnection.setUserId(userId);
                Integer isAllowedInt = resultSet.getInt("is_allowed");
                userConnection.setIsAllowed((isAllowedInt == null) ? null : isAllowedInt == 0 ? false : true);
                return userConnection;
            } else {
                throw new DaoException("No connection found with ID " + id);
            }

        } catch (SQLException e) {
            throw new DaoException("Error fetching connection with ID " + id + ": " + e.getMessage());
        }
    }

    public void updateUserConnection(UserConnection userConnection) throws DaoException {
        String sql = "UPDATE connections SET ip_address = ?, timestamp = ?, user_id = ? WHERE id = ?;";
        try (Connection connection = daoFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userConnection.getIpAddress());
            statement.setTimestamp(2, userConnection.getTimestamp());
            statement.setInt(3, userConnection.getUserId());
            statement.setInt(4, userConnection.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
            /*
        } catch (SQLException e) {
            throw new DaoException("Not able to update connection " + userConnection.getId());
        }

             */
    }
}

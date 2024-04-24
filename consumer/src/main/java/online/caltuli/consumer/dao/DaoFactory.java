package online.caltuli.consumer.dao;

import online.caltuli.consumer.dao.exceptions.DatabaseConnectionException;
import online.caltuli.consumer.dao.implementations.GamesDaoImpl;
import online.caltuli.consumer.dao.interfaces.GamesDao;
import online.caltuli.consumer.dao.interfaces.UsersDao;
import online.caltuli.consumer.dao.interfaces.UserConnectionsDao;
import online.caltuli.consumer.dao.implementations.UsersDaoImpl;
import online.caltuli.consumer.dao.implementations.UserConnectionsDaoImpl;
import online.caltuli.model.CurrentModel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Currency;

public class DaoFactory {
    final private String url;
    final private String username;
    final private String password;
    private static DaoFactory instance;

    private DaoFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DaoFactory getInstance() throws DatabaseConnectionException {
        if (instance == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver"); // driver loading
                instance = new DaoFactory(
                        "jdbc:mysql://localhost:3306/onlineplay",
                        "root",
                        "Lips*Jordan#77");
            } catch (ClassNotFoundException e) {
                throw new DatabaseConnectionException("The class com.mysql.cj.jdbc.Driver is not found.");
            }
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
        return connection;
    }

    public UsersDao getUsersDao(CurrentModel currentModel) {
        return new UsersDaoImpl(this, currentModel);
    }

    public UserConnectionsDao getUserConnectionsDao() {
        return new UserConnectionsDaoImpl(this);
    }

    public GamesDao getGamesDao(CurrentModel currentModel) {
        return new GamesDaoImpl(this, currentModel);
    }
}

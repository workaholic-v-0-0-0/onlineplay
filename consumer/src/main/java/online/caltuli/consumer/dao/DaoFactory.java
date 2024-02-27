package online.caltuli.consumer.dao;

import online.caltuli.consumer.dao.exceptions.DatabaseConnectionException;
import online.caltuli.consumer.dao.interfaces.UsersDao;
import online.caltuli.consumer.dao.interfaces.UserConnectionsDao;
import online.caltuli.consumer.dao.implementations.UsersDaoImpl;
import online.caltuli.consumer.dao.implementations.UserConnectionsDaoImpl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
                        <the password of root> );
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

    public UsersDao getUsersDao() {return new UsersDaoImpl(this);}

    public UserConnectionsDao getUserConnectionsDao() {
        return new UserConnectionsDaoImpl(this);
    }
}

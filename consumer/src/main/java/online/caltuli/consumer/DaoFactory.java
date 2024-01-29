package online.caltuli.consumer;

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

    public static DaoFactory getInstance() {
        if (instance == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                instance = new DaoFactory(
                        "jdbc:mysql://localhost:3306/onlineplay",
                        "root",
                        "Lips*Jordan#77" );
            } catch (ClassNotFoundException e) {

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

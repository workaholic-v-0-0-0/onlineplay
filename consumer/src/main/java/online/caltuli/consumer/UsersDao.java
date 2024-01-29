package online.caltuli.consumer;

import java.sql.Timestamp;
import java.util.List;
import online.caltuli.model.User;
import online.caltuli.model.UserConnection;

public interface UsersDao {

    public int addUser(String username, String passwordHash, String email, String message) throws DaoException;
    public User getUserById(int id) throws DaoException;
    public User getUserByUsername(String username) throws DaoException;
}

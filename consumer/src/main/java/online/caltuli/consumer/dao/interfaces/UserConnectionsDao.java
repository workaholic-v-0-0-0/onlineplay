package online.caltuli.consumer.dao.interfaces;

import online.caltuli.consumer.dao.exceptions.DaoException;

import java.sql.Timestamp;
import java.util.List;
import online.caltuli.model.UserConnection;

public interface UserConnectionsDao {
    public int addUserConnection(String ipAddress, Timestamp timestamp) throws DaoException;
    public UserConnection getUserConnectionByUserId(int id) throws DaoException;
    public void updateUserConnection(UserConnection userConnection) throws DaoException;
}

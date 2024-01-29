package online.caltuli.consumer;

import java.sql.Timestamp;
import java.util.List;
import online.caltuli.model.UserConnection;

public interface UserConnectionsDao {
    public int addUserConnection(String ipAddress, Timestamp timestamp, Integer userId) throws DaoException;
    public UserConnection getUserConnectionByUserId(int id) throws DaoException;
    public void updateUserConnection(UserConnection userConnection) throws DaoException;
}

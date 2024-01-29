package online.caltuli.business;

import online.caltuli.consumer.UserConnectionsDaoImpl;
import online.caltuli.model.*;
import online.caltuli.consumer.*;

import java.sql.Timestamp;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UserManager {
    private static List<User> connectedUserList;

    public static UserConnection
    logUserConnection(
            String ipAddress,
            Timestamp timestamp,
            Integer userId)
            throws BusinessException {
        UserConnectionsDao userConnectionsDao = null;
        int id = 0;
        // add a record in connections table ; get primary key in id
        try {
            userConnectionsDao = DaoFactory.getInstance().getUserConnectionsDao();
            id = userConnectionsDao.addUserConnection(ipAddress, timestamp, userId);
        } catch (DaoException e) {
            throw new BusinessException("Can't register connection in database.");
        }

        // fetch the record from connections table ; returned in a UserConnection instance
        try {
            return userConnectionsDao.getUserConnectionByUserId(id);
        } catch (DaoException e) {
            throw new BusinessException("Can't fetch connection in database.");
        }
    }
    public static void
    updateUserConnection(UserConnection userConnection) throws BusinessException {
        UserConnectionsDao userConnectionsDao = null;
        try {
            userConnectionsDao = DaoFactory.getInstance().getUserConnectionsDao();
            userConnectionsDao.updateUserConnection(userConnection);
        } catch (DaoException e) {
            //throw new BusinessException("Can't update connection in database.");
            throw new BusinessException(e.getMessage());
        }
    }

    public static int
    registerUser(
            String username,
            String password,
            String email,
            String message)
            throws BusinessException {
        UsersDao usersDao = null;
        // add a record in users table ; get primary key in id
        try {
            usersDao = DaoFactory.getInstance().getUsersDao();
            // Générer un Salt et hasher le mot de passe
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
            if (usersDao.getUserByUsername(username) != null) return -1;
            return usersDao.addUser(username, passwordHash, email, message);
        } catch (DaoException e) {
            throw new BusinessException("Can't register user in database.");
        }

        // ATTENTION IL FAUDRA AUSSI S'ASSURER QUE LE NOM D'UTILISATEUR N'EST PAS DÉJÀ PRÉSENT
        // DANS LA TABLE
    }

    public static User authenticateUser(String username, String password) throws BusinessException {
        UsersDao usersDao = null;
        try {
            usersDao = DaoFactory.getInstance().getUsersDao();
            User user = usersDao.getUserByUsername(username);
            return (BCrypt.checkpw(password, user.getPasswordHash())) ? user : null;
        } catch (DaoException e) {
            throw new BusinessException("Authentication failed");
        }
    }

}

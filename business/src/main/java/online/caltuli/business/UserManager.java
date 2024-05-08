package online.caltuli.business;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import online.caltuli.business.exception.BusinessException;
import online.caltuli.model.*;

import online.caltuli.consumer.dao.DaoFactory;
import online.caltuli.consumer.dao.interfaces.UserConnectionsDao;
import online.caltuli.consumer.dao.interfaces.UsersDao;
import online.caltuli.consumer.dao.exceptions.DaoException;

import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import online.caltuli.model.exceptions.user.UserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mindrot.jbcrypt.BCrypt;

@ApplicationScoped
public class UserManager {

    @Inject
    protected CurrentModel currentModel;

    protected final Logger logger = LogManager.getLogger(UserManager.class);

    public UserConnection
    logUserConnection(
            String ipAddress,
            Timestamp timestamp)
            throws BusinessException {

        UserConnectionsDao userConnectionsDao = null;
        int id = 0;
        // add a record in connections table ; get primary key in id
        try {
            userConnectionsDao = DaoFactory.getInstance().getUserConnectionsDao();
            id = userConnectionsDao.addUserConnection(ipAddress, timestamp);
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

    public void
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

    public User
    registerUser(
            String username,
            String password,
            String email,
            String message)
            throws BusinessException, UserException {
        Logger logger = LogManager.getLogger(UserManager.class);
        User user = null;
        UsersDao usersDao = null;
        // add a record in users table ; get primary key in id
        try {
            usersDao = DaoFactory.getInstance().getUsersDao(currentModel);
            // generate a Salt and hash the password
            String passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
            // check if username is already used
            try {
                if (usersDao.getUserByUsername(username) != null) {
                    logger.info("try to registrer but username is already used");
                    return null;
                }
            } catch (UserException e) {
                // the validity of the username and other values fetched from the users table
                // will be verified by catching exceptions in the addUser method
            }
            // the message of the exception thrown by addUser contains all the reasons
            // for the registration failure
            user = usersDao.addUser(username, passwordHash, email, message);
        } catch (DaoException e) {
            throw new BusinessException("Can't register user in database.");
        }
        return user;
    }

    public User authenticateUser(String username, String password) throws BusinessException, UserException {
        UsersDao usersDao = null;
        try {
            usersDao = DaoFactory.getInstance().getUsersDao(currentModel);
            User user = usersDao.getUserByUsername(username);
            currentModel.getAuthenticatedUsers().put(user.getId(), user);
            return (BCrypt.checkpw(password, user.getPasswordHash())) ? user : null;
        } catch (DaoException e) {
            throw new BusinessException("Authentication failed");
        }
    }

    public void disconnectUser(int userId) {
        logger.info("user " + userId + " wants to disconnect.");
        currentModel.getAuthenticatedUsers().remove(userId);
        currentModel.getWaitingToPlayUser().remove(userId);
        for (Game game : currentModel.getGames().values()) {
            if ((game.getFirstPlayer() != null) && (game.getFirstPlayer().getId() == userId)) {
                game.setFirstPlayer(null);
            }
            if ((game.getSecondPlayer() != null) && (game.getSecondPlayer().getId() == userId)) {
                game.setSecondPlayer(null);
            }
            if ((game.getFirstPlayer() == null) && (game.getSecondPlayer() == null)) {
                currentModel.removeGame(game);
            }
        }
    }

    public Map<Integer, User> getAuthenticatedUsers() {
        return currentModel.getAuthenticatedUsers();
    }

    public CurrentModel getCurrentModel() {
        return currentModel;
    }
}

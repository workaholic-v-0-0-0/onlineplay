package online.caltuli.business;

import online.caltuli.business.exception.BusinessException;
import online.caltuli.consumer.api.abuseipdb.IpValidator;
import online.caltuli.consumer.api.abuseipdb.exception.AbuseIpDbException;
import online.caltuli.model.*;

import online.caltuli.consumer.dao.DaoFactory;
import online.caltuli.consumer.dao.interfaces.UserConnectionsDao;
import online.caltuli.consumer.dao.interfaces.UsersDao;
import online.caltuli.consumer.dao.exceptions.DaoException;

import java.sql.Timestamp;
import java.util.List;

import online.caltuli.model.exceptions.user.UserException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.mindrot.jbcrypt.BCrypt;

public class UserManager {
    private static List<User> connectedUserList;

    public static UserConnection
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

    public static User
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
            usersDao = DaoFactory.getInstance().getUsersDao();
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

    public static User authenticateUser(String username, String password) throws BusinessException, UserException {
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

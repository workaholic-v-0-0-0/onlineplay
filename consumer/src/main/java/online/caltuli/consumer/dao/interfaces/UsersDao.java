package online.caltuli.consumer.dao.interfaces;

import online.caltuli.consumer.dao.exceptions.DaoException;

import online.caltuli.model.User;
import online.caltuli.model.exceptions.user.UserException;

public interface UsersDao {
    User addUser(String username, String passwordHash, String email, String message) throws DaoException, UserException;
    User getUserById(int id) throws DaoException;
    User getUserByUsername(String username) throws DaoException, UserException;
}

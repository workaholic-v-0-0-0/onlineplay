package online.caltuli.consumer.dao.interfaces;

import online.caltuli.consumer.dao.exceptions.DaoException;
import online.caltuli.model.Move;

public interface MovesDao {
    public int newRecord() throws DaoException;
    public Move getMoveById(int id) throws DaoException;
    public void updateMove(Move move) throws DaoException;
}

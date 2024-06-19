package online.caltuli.consumer.dao.implementations;

import online.caltuli.consumer.dao.DaoFactory;
import online.caltuli.consumer.dao.exceptions.DaoException;
import online.caltuli.consumer.dao.interfaces.MovesDao;
import online.caltuli.model.CurrentModel;
import online.caltuli.model.Move;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MovesDaoImpl implements MovesDao {

    private final DaoFactory daoFactory;

    private CurrentModel currentModel;

    private final Logger logger = LogManager.getLogger(GamesDaoImpl.class);

    /**
     * CONSTRUCTOR
     */
    public MovesDaoImpl(DaoFactory daoFactory, CurrentModel currentModel) {
        this.daoFactory = daoFactory;
        this.currentModel = currentModel;
    }

    public int newRecord() throws DaoException {
        // TO DO
        return 0; // placeholder
    }

    public Move getMoveById(int id) throws DaoException {
        // TO DO
        return null; // placeholder
    }

    public void updateMove(Move move) throws DaoException {
        // TO DO
    }
}

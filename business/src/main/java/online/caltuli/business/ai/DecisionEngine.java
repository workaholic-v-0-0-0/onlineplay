package online.caltuli.business.ai;

import online.caltuli.model.Column;

public interface DecisionEngine {
    void updateWithMove(Column column);
    Column getBestMove();
}

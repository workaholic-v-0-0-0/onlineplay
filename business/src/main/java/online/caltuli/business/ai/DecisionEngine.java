package online.caltuli.business.ai;

import java.util.Map;

public interface DecisionEngine {
    public void updateWithMove(Column column);
    public Column getBestMove();
}

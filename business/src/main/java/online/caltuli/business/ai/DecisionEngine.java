package online.caltuli.business.ai;

public interface DecisionEngine {
    void updateWithMove(Column column);
    Column getBestMove();
}

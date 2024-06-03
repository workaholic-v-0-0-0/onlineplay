package online.caltuli.business.ai;

import online.caltuli.business.EvolutiveGridParser;

public class DecisionEngine {
    private Tree tree;

    public DecisionEngine(Tree tree) {
        this.tree = tree;
    }

    public void updateWithMove(int column) {
        // as it's called
    }

    public int evaluatePosition(EvolutiveGridParser evolutiveGridParser) {
        return 0; // placeholder
    }

    public int getBestMove() {
        return 0; // placeholder
    }
}
package online.caltuli.business.ai;

import online.caltuli.model.CellState;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class MinMaxDecisionEngine implements DecisionEngine {
    private TreeManager treeManager;
    private static final Logger logger = LogManager.getLogger(MinMaxDecisionEngine.class);

    public MinMaxDecisionEngine() {
        this.treeManager = new TreeManager();
    }

    public void updateWithMove(Column column) {
        treeManager.prune(column);
        treeManager.growOneGeneration();
    }

    public Column getBestMove() {
        Column bestMove = Column.C0;
        long minMaxEvaluation;
        long maximum = EvaluatedEvolutiveGridParser.MINUS_INFINITY;
        for (Map.Entry<Column, Tree> entry : this.treeManager.getTree().getBranches().entrySet()) {
            minMaxEvaluation = minMaxEvaluation(entry.getValue());
            if (minMaxEvaluation > maximum) {
                maximum = minMaxEvaluation;
                bestMove = entry.getKey();
            }
        }
        return bestMove;
    }

    private long minMaxEvaluation(Tree tree) {
        long evaluation;
        long minOrMax = 0;
        if (tree.isOnBorder() || (! tree.canGrow())) {
            return tree.getRoot().getEvaluation();
        }
        if (tree.getRoot().getNextColor() == CellState.RED) {
            minOrMax = EvaluatedEvolutiveGridParser.MINUS_INFINITY;
            for (Tree child : tree.getBranches().values()) {
                evaluation = minMaxEvaluation(child);
                if (evaluation > minOrMax) {
                    minOrMax = evaluation;
                }
            }
        }
        if (tree.getRoot().getNextColor() == CellState.GREEN) {
            minOrMax = EvaluatedEvolutiveGridParser.INFINITY;
            for (Tree child : tree.getBranches().values()) {
                evaluation = minMaxEvaluation(child);
                if (evaluation < minOrMax) {
                    minOrMax = evaluation;
                }
            }
        }
        return minOrMax;
    }

    public TreeManager getTreeManager() {
        return treeManager;
    }

    public void setTreeManager(TreeManager treeManager) {
        this.treeManager = treeManager;
    }
}

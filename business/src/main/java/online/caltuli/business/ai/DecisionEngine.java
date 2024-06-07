package online.caltuli.business.ai;

import online.caltuli.model.CellState;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class DecisionEngine {
    private TreeManager treeManager;
    private static final Logger logger = LogManager.getLogger(DecisionEngine.class);

    // to debug
    public String s;

    public DecisionEngine() {
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

    public long minMaxEvaluation(Tree tree) {
        long evaluation;
        long minOrMax = 0;
        if (tree.isOnBorder() || (! tree.canGrow())) {
            return tree.getRoot().getEvaluation();
        }
        if (tree.getRoot().getNextColor() == CellState.RED) {
            minOrMax = EvaluatedEvolutiveGridParser.MINUS_INFINITY;
            for (Tree child : tree.getBranches().values()) {
            //for (Column column : tree.getBranches().keySet()) {
                //Tree child = tree.getBranches().get(column);
                evaluation = minMaxEvaluation(child);
                if (evaluation > minOrMax) {
                    minOrMax = evaluation;
                }
            }
        }
        if (tree.getRoot().getNextColor() == CellState.GREEN) {
            minOrMax = EvaluatedEvolutiveGridParser.INFINITY;
            for (Tree child : tree.getBranches().values()) {
            //for (Column column : tree.getBranches().keySet()) {
                //Tree child = tree.getBranches().get(column);
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

    /* USELESS because
    // on peut évaluer la position en fonction de l'évaluation de la position
    // précédente et du coup joué
    public long evaluation(EvaluatedEvolutiveGridParser eegp) {
        long evaluation = 0;
        for (
                Map.Entry<Coordinates[], Integer> entry :
                eegp.getRedRowsToNbOfRedCoordinates().entrySet()) {
            evaluation +=
                EvaluatedEvolutiveGridParser
                        .coordinatesRowToWeight
                        .get(entry.getKey())
                        *
                        entry.getValue();
        }
        for (
                Map.Entry<Coordinates[], Integer> entry :
                eegp.getGreenRowsToNbOfGreenCoordinates().entrySet()) {
            evaluation -=
                EvaluatedEvolutiveGridParser
                    .coordinatesRowToWeight
                    .get(entry.getKey())
                *
                entry.getValue();
        }
        return evaluation;
    }
     */

    // to debug
    /*
    private String indent(int indent) {
        if (indent == 0) {
            return "";
        }
        return "    " + indent(indent - 1);
    }
    private void printEval(Tree tree, int indent) {
        if (this.s == null) this.s = "";
        if (tree.isOnBorder() || (! tree.canGrow())) {
            this.s = this.s + "\n" + indent(indent) + tree.getRoot().getEvaluation();
        } else {
            this.s = this.s + "\n" + indent(indent) + minMaxEvaluation(tree);
            for (int column=0;column<7;column++) {
                printEval(tree.getBranches().get(Column.fromIndex(column)), indent + 1);
            }
        }
    }
     */
}

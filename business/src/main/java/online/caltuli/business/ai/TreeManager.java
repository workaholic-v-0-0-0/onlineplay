package online.caltuli.business.ai;

import online.caltuli.model.Coordinates;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TreeManager {
    private Tree tree;

    private Logger logger = LogManager.getLogger(TreeManager.class);

    public TreeManager() {
        this.tree =
            new Tree(new EvaluatedEvolutiveGridParser());
        grow();
    }

    public void grow() {
        for (int depth = 0 ; depth < Tree.DEPTH ; depth ++) {
            growOneGeneration();
        }
    }

    public void growOneGeneration() {
        Queue<Tree> queue = new LinkedList<>();
        queue.offer(this.tree);
        while (! queue.isEmpty()) {
            Tree tree = queue.poll();
            if (! tree.canGrow()) {
                continue;
            }
            if (tree.isOnBorder()) {
                EvaluatedEvolutiveGridParser root = tree.getRoot();
                for (Column column : Column.values()) {
                    if (root.isLegalMove(column.getIndex())) {
                        EvaluatedEvolutiveGridParser newNode =
                                duplicateExceptCoordinatesRow(root);
                        newNode.updateWithMove(column.getIndex());
                        Tree newChild = new Tree(newNode);
                        tree.getBranches().put(column,newChild);
                    }
                }
            } else {
                for (Tree child : tree.getBranches().values()) {
                    queue.offer(child);
                }
            }
            tree.incrementDepth();
        }
    }

    private EvaluatedEvolutiveGridParser duplicateExceptCoordinatesRow(
            EvaluatedEvolutiveGridParser eegp) {
        EvaluatedEvolutiveGridParser duplicated =
                new EvaluatedEvolutiveGridParser();
        duplicated.setNextLine(
                Arrays.copyOf(
                        eegp.getNextLine(),
                        eegp.getNextLine().length
                )
        );
        duplicated.setNextColor(eegp.getNextColor());
        for (Map.Entry<Coordinates[],Integer> entry : eegp.getRedRowsToNbOfRedCoordinates().entrySet()) {
            duplicated.getRedRowsToNbOfRedCoordinates().put(
                    entry.getKey(),
                    Integer.valueOf(entry.getValue())
            );
        }
        for (Map.Entry<Coordinates[],Integer> entry : eegp.getGreenRowsToNbOfGreenCoordinates().entrySet()) {
            duplicated.getGreenRowsToNbOfGreenCoordinates().put(
                    entry.getKey(),
                    Integer.valueOf(entry.getValue())
            );
        }
        for (Coordinates[] coordinatesRow : eegp.getUnWinnableCoordinatesRowsSet()) {
            duplicated.getUnWinnableCoordinatesRowsSet().add(
                    coordinatesRow
            );
        }
        duplicated.setEvaluation(eegp.getEvaluation());
        return duplicated;
    }

    public void prune(Column column) {
        this.tree = this.tree.getBranches().get(column);
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }
}
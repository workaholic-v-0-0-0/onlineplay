package online.caltuli.business.ai;

import online.caltuli.model.CellState;
import online.caltuli.model.Coordinates;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Tree {
    public static int DEPTH = 4;
    private EvaluatedEvolutiveGridParser root;
    private int depth;
    private HashMap<Column, Tree> branches;

    // in order to make iterative evaluation computation ???
    /*
    private long minBranchesEvaluations;
    private long maxBranchesEvaluations;
    private Tree parent;

     */
    private Logger logger = LogManager.getLogger(Tree.class);

    public Tree(EvaluatedEvolutiveGridParser root) {
        this.root = root;
        this.depth = 0;
        this.branches =
            (
                (! this.root.detectDraw())
                &&
                (! (this.root.detectWinningColor() != CellState.NULL))
            ) ?
                new HashMap<Column,Tree>()
                :
                null;
        /*
        this.minBranchesEvaluations =
            EvaluatedEvolutiveGridParser.INFINITY;
        this.maxBranchesEvaluations =
                EvaluatedEvolutiveGridParser.MINUS_INFINITY;
        this.parent = null;

         */
    }

    public boolean canGrow() {
        return this.branches != null;
    }

    public boolean isOnBorder() {
        return this.depth == 0;
    }

    public void incrementDepth() {
        this.depth++;
    }

    public EvaluatedEvolutiveGridParser getRoot() {
        return root;
    }

    public void setRoot(EvaluatedEvolutiveGridParser root) {
        this.root = root;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public HashMap<Column, Tree> getBranches() {
        return branches;
    }

    public void setBranches(HashMap<Column, Tree> branches) {
        this.branches = branches;
    }

}

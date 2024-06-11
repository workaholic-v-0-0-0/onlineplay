package online.caltuli.business.ai;

import online.caltuli.model.CellState;

import java.util.HashMap;

public class Tree {
    public static int DEPTH = 4;
    private EvaluatedEvolutiveGridParser root;
    private int depth;
    private HashMap<Column, Tree> branches;

    public Tree(EvaluatedEvolutiveGridParser root) {
        this.root = root;
        this.depth = 0;
        this.branches =
            (
                (! this.root.detectDraw())
                &&
                (this.root.detectWinningColor() == CellState.NULL)
            ) ?
                new HashMap<>()
                :
                null;
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

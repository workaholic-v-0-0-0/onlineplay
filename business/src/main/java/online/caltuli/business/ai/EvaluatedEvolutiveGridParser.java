package online.caltuli.business.ai;

import online.caltuli.business.EvolutiveGridParser;

public class EvaluatedEvolutiveGridParser extends EvolutiveGridParser {
    private int evaluation;

    public EvaluatedEvolutiveGridParser() {
        super();
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }
}

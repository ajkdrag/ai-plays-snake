package ai.snake.learner;

import java.util.ArrayList;
import java.util.List;

public class QTable {
    private int numStates, numActions;
    private List<List<Double>> qTable;

    public QTable(int numStates, int numActions) {
        this.numStates = numStates;
        this.numActions = numActions;
        this.qTable = new ArrayList<>();
    }

    public void initEmptyQTable() {
        for (int i = 0; i < this.numStates; ++i) {
            List<Double> rewards = new ArrayList<>();
            for (int j = 0; j < this.numActions; ++j) {
                rewards.add(0.0);
            }
            this.qTable.add(rewards);
        }
    }

    public void resetQTable() {
        for (int i = 0; i < this.numStates; ++i) {
            this.qTable.get(i).clear();
        }
    }

    // setters

    public void setQValue(int idxState, int idxAction, double newValue) {
        this.qTable.get(idxState).set(idxAction, newValue);
    }

    public void setQTable(List<List<Double>> table) {
        this.qTable = table;
    }

    // getters

    public List<List<Double>> getQTable() {
        return this.qTable;
    }

    public double max(List<Double> x) {
        double mx = x.get(0);
        for (double val : x)
            mx = val > mx ? val : mx;
        return mx;
    }

    public int argMax(List<Double> x) {
        int idx = 0;
        double mx = x.get(idx);
        for (int i = 0, end = x.size(); i < end; ++i) {
            double curr = x.get(i);
            if (curr > mx) {
                idx = i;
                mx = curr;
            }
        }
        return idx;
    }

    public double getQValue(int idxState, int idxAction) {
        return this.qTable.get(idxState).get(idxAction);
    }

    public double getMaxRewardForThisState(int idxState) {
        return max(this.qTable.get(idxState));
    }

    public int getArgMaxRewardForThisState(int idxState) {
        return argMax(this.qTable.get(idxState));
    }
}
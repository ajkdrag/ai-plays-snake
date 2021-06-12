package src.learner;

import java.util.Random;

public class QLearner {
    QTable qTable;
    Random random;

    private static final double GAMMA = 0.95;
    private static final double LEARNING_RATE = 0.6;
    public static double EPSILON = 0.5;

    public QLearner(int numStates, int numActions) {
        this.qTable = new QTable(numStates, numActions);
        this.random = new Random();
    }

    public void initQTable() {
        this.qTable.initEmptyQTable();
    }

    public int getNextAction(int currState) {
        double exploreVsExploitDecision = random.nextDouble();
        if (exploreVsExploitDecision <= EPSILON) {
            return random.nextInt(3);
        } else
            return this.qTable.getArgMaxRewardForThisState(currState);
    }

    public void updateQTable(int currState, int action, double reward, int nextState) {
        double currentQTableReward = this.qTable.getQValue(currState, action);
        if (nextState >= 0) {
            reward += GAMMA * this.qTable.getMaxRewardForThisState(nextState);
        }
        currentQTableReward += LEARNING_RATE * (reward - currentQTableReward);
        this.qTable.setQValue(currState, action, currentQTableReward);
    }
}

package src.learner;

import java.util.Random;

public class QLearner {
    private QTable qTable;
    private Random random;

    private static final double GAMMA = 0.85;
    public static double LEARNING_RATE = 0.75;
    public static double EPSILON = 0.9;

    public QLearner(int numStates, int numActions) {
        System.out.println("Num states -> " + numStates + ", Num actions -> " + numActions);
        this.qTable = new QTable(numStates, numActions);
        this.random = new Random();
    }

    public void initQTable() {
        this.qTable.initEmptyQTable();
    }

    public void updateQTable(int currState, int action, double reward, int nextState) {
        double currentQTableReward = this.qTable.getQValue(currState, action);
        if (nextState >= 0) {
            reward += GAMMA * this.qTable.getMaxRewardForThisState(nextState);
        }
        currentQTableReward += LEARNING_RATE * (reward - currentQTableReward);
        this.qTable.setQValue(currState, action, currentQTableReward);
    }

    // getters

    public int getNextAction(int currState) {
        double exploreVsExploitDecision = random.nextDouble();
        if (exploreVsExploitDecision <= EPSILON) {
            return random.nextInt(3);
        } else
            return this.qTable.getArgMaxRewardForThisState(currState);
    }
}

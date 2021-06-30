package src.learner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class QLearner {
    private QTable qTable;
    private Random random;
    private PApplet sketch;

    private static final double GAMMA = 0.85;
    public static double LEARNING_RATE = 0.75;
    public static final int MAX_EPISODES = 5000;
    public static double EPSILON;
    public static int EPISODES;

    public QLearner(PApplet sketch, int numStates, int numActions) {
        System.out.println("Num states -> " + numStates + ", Num actions -> " + numActions);
        this.sketch = sketch;
        this.qTable = new QTable(numStates, numActions);
        this.random = new Random();
        EPSILON = 0.9;
        EPISODES = 0;
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

    public void saveStateJSON() {
        List<List<Double>> table = this.qTable.getQTable();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0, end = table.size(); i < end; ++i) {
            JSONArray newArray = new JSONArray();
            for (double reward : table.get(i)) {
                newArray.append(reward);
            }
            jsonArray.setJSONArray(i, newArray);
        }
        JSONObject state = new JSONObject();
        state.setJSONArray("QTable", jsonArray);
        state.setDouble("Epsilon", EPSILON);
        state.setInt("Episodes", EPISODES);
        this.sketch.saveJSONObject(state, "test.json");
    }

    public void loadStateJSON() {
        JSONObject state = this.sketch.loadJSONObject("test.json");
        EPISODES = state.getInt("Episodes");
        EPSILON = state.getDouble("Epsilon");
        JSONArray jsonArray = state.getJSONArray("QTable");
        List<List<Double>> table = new ArrayList<>();
        for (int i = 0, end = jsonArray.size(); i < end; ++i) {
            List<Double> newArray = new ArrayList<>();
            for (double reward : jsonArray.getJSONArray(i).getDoubleArray()) {
                newArray.add(reward);
            }
            table.add(newArray);
        }
        this.qTable.setQTable(table);
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

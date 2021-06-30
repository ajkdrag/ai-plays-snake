package src.learner;

import java.util.HashMap;

import src.utils.Position;

public class AgentStateHandler {
    HashMap<Long, Integer> states;
    int stateReprLength = 7;
    int foodFBInfoStart = stateReprLength - 2;
    int foodLRInfoStart = stateReprLength - 4;

    public AgentStateHandler() {
        this.states = new HashMap<>();
        this.buildStates();
    }

    private void buildStates() {
        // new state repr = [F_B, F_F, F_L, F_R, W_F, W_L, W_R]
        long start = 0L;
        int stateNum = 0;
        // 00 : same, 01: back, 10: front
        for (int i = 0; i < 3; ++i) {
            // back and front
            if (i < 2)
                start ^= 1 << (foodFBInfoStart + i);
            for (int j = 0; j < 3; ++j) {
                // left and right
                if (j < 2)
                    start ^= 1 << (foodLRInfoStart + j);
                for (int k = 0; k < (1 << 3); ++k) {
                    // walls
                    start += k;
                    this.states.put(start, stateNum++);
                    start -= k;
                }
                if (j < 2)
                    start ^= 1 << (foodLRInfoStart + j);
            }
            if (i < 2)
                start ^= 1 << (foodFBInfoStart + i);
        }
    }

    // getters

    public int getNumStates() {
        return this.states.size();
    }

    public int getStateId(Position snakeHeadPosition, Position snakeTailPosition, int snakeDirection, int snakeBodySize,
            Position foodPosition, int wallAtFront, int wallAtLeft, int wallAtRight) {

        int foodLeftOrRight = snakeHeadPosition.getRelation(snakeDirection, foodPosition);
        int foodDownOrUp = snakeHeadPosition.getRelation((snakeDirection + 3) % 4, foodPosition);
        long start = 0L;
        if (foodDownOrUp >= 0)
            start ^= 1 << (foodFBInfoStart + foodDownOrUp);
        if (foodLeftOrRight >= 0)
            start ^= 1 << (foodLRInfoStart + foodLeftOrRight);
        start ^= wallAtFront << 2;
        start ^= wallAtLeft << 1;
        start ^= wallAtRight;
        return this.states.get(start);
    }

    public int getStateId(Long state) {
        return this.states.get(state);
    }
}

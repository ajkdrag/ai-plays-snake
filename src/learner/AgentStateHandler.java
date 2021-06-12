package src.learner;

import java.util.HashMap;

import src.utils.Position;

public class AgentStateHandler {
    HashMap<Long, Integer> states;
    int stateReprLength = 11;
    int foodFBInfoStart = stateReprLength - 2;
    int foodLRInfoStart = stateReprLength - 4;
    int tailFBInfoStart = stateReprLength - 6;
    int tailLRInfoStart = stateReprLength - 8;

    public AgentStateHandler() {
        this.states = new HashMap<>();
        this.buildStates();
    }

    private void buildStates() {
        // customized state building logic.
        // state repr = [F_B, F_F, F_L, F_R, T_B, T_F, T_L, T_R, W_F, W_L, W_R]
        // F_F = Food at Front
        // F_B = Food at Back
        // F_L = Food at Left
        // F_R = Food at Right
        // T_F = Tail at Front
        // T_B = Tail at Back
        // T_L = Tail at Left
        // T_R = Tail at Right
        // W_F = Wall at Front
        // W_L = Wall at Left
        // W_R = Wall at Right
        // example = [1, 0, 0, 1, 0, 1, 0, 1, 0, 0, 0]
        long start = 0L;
        int stateNum = 0;
        // for food back and front
        for (int i = 0; i < 2; ++i) {
            start ^= 1 << (foodFBInfoStart + i);
            // for food left and right
            for (int j = 0; j < 2; ++j) {
                start ^= 1 << (foodLRInfoStart + j);
                // for tail back and front
                for (int k = 0; k < 2; ++k) {
                    start ^= 1 << (tailFBInfoStart + k);
                    // for tail left and right
                    for (int l = 0; l < 2; ++l) {
                        start ^= 1 << (tailLRInfoStart + l);
                        for (int m = 0; m < (1 << 3); ++m) {
                            start += m;
                            states.put(start, stateNum++);
                            start -= m;
                        }
                        start ^= 1 << (tailLRInfoStart + l);
                    }
                    start ^= 1 << (tailFBInfoStart + k);
                }
                start ^= 1 << (foodLRInfoStart + j);
            }
            start ^= 1 << (foodFBInfoStart + i);
        }

    }

    public int getNumStates() {
        return this.states.size();
    }

    public int getStateId(Position snakeHeadPosition, Position snakeTailPosition, int snakeDirection, int snakeBodySize,
            Position foodPosition, int wallAtFront, int wallAtLeft, int wallAtRight) {
        int foodLeftOrRight = snakeHeadPosition.getRelation(snakeDirection, foodPosition);
        int foodDownOrUp = snakeHeadPosition.getRelation((snakeDirection + 3) % 4, foodPosition);
        int tailLeftOrRight = snakeHeadPosition.getRelation(snakeDirection, snakeTailPosition);
        int tailDownOrUp = snakeHeadPosition.getRelation((snakeDirection + 3) % 4, snakeTailPosition);
        long start = 0L;
        start ^= 1 << (this.foodFBInfoStart + foodDownOrUp);
        start ^= 1 << (this.foodLRInfoStart + foodLeftOrRight);
        start ^= 1 << (this.tailFBInfoStart + tailDownOrUp);
        start ^= 1 << (this.tailLRInfoStart + tailLeftOrRight);
        start ^= wallAtFront << 2;
        start ^= wallAtLeft << 1;
        start ^= wallAtRight;
        return this.states.get(start);
    }

    public int getStateId(Long state) {
        return this.states.get(state);
    }
}

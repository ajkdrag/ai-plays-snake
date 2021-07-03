package src.learner;

import java.util.HashMap;

import src.utils.Position;

public class AgentStateHandler {
    HashMap<Long, Integer> states;
    int stateReprLength = 12;
    int foodFBInfoStart = stateReprLength - 4;
    int foodLRInfoStart = stateReprLength - 6;

    public AgentStateHandler() {
        this.states = new HashMap<>();
        this.buildStates();
    }

    private void buildStates() {
        // new state repr = [Dir, Dir, F_F, F_B, F_R, F_L, W_F, W_L, W_R, S_F, S_L, S_R]
        long start = 0L;
        int stateNum = 0;
        // 00 : same, 01: back, 10: front
        for (int m = 0; m < 4; ++m) {
            start += m << (this.stateReprLength - 2);
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
                        start += (k << 3);
                        for (int l = 0; l < (1 << 3); ++l) {
                            start += l;
                            this.states.put(start, stateNum++);
                            start -= l;
                        }
                        start -= (k << 3);
                    }
                    if (j < 2)
                        start ^= 1 << (foodLRInfoStart + j);
                }
                if (i < 2)
                    start ^= 1 << (foodFBInfoStart + i);
            }
            start -= m << (this.stateReprLength - 2);
        }
    }

    // getters

    public int getNumStates() {
        return this.states.size();
    }

    public int getStateId(Position snakeHeadPosition, Position snakeTailPosition, int snakeDirection, int prevDirection,
            int snakeBodyPartSize, Position foodPosition, int wallAtFront, int wallAtLeft, int wallAtRight,
            int bodyPartAtFront, int bodyPartAtLeft, int bodyPartAtRight) {

        int foodLeftOrRight = snakeHeadPosition.getRelation(snakeDirection, foodPosition);
        int foodDownOrUp = snakeHeadPosition.getRelation((snakeDirection + 3) % 4, foodPosition);
        long start = 0L;
        start += prevDirection << (stateReprLength - 2);
        if (foodDownOrUp >= 0)
            start ^= 1 << (foodFBInfoStart + foodDownOrUp);
        if (foodLeftOrRight >= 0)
            start ^= 1 << (foodLRInfoStart + foodLeftOrRight);
        start ^= wallAtFront << 5;
        start ^= wallAtLeft << 4;
        start ^= wallAtRight << 3;
        start ^= bodyPartAtFront << 2;
        start ^= bodyPartAtLeft << 1;
        start ^= bodyPartAtRight;
        return this.states.get(start);
    }

    public int getStateId(Long state) {
        return this.states.get(state);
    }
}

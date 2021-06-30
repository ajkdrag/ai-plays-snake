package src.fsm.modes;

import src.controller.GameController;
import src.evt.State;
import src.fsm.states.GameState;
import src.learner.QLearner;

public class AITrainMode implements GameMode {
    int numEpisodes;
    int maxScore;
    static final double FOOD_REWARD = 15.0;
    static final double CRASH_REWARD = -10.0;
    static final double MOVEMENT_REWARD = -1.5;

    @Override
    public void enter() {
        this.numEpisodes = 1000;
    }

    @Override
    public void handleInput(GameController game, int keyCode) {
        switch (keyCode) {
            case '\t':
                game.eventHandler.setEventState(State.KEY_PRESSED_TAB);
                game.eventHandler.handleEvent();
                break;
            case 'R':
                game.resetGame();
                game.gameMode = GameMode.manualMode;
                game.gameMode.enter();
                break;
            default:
                game.eventHandler.setEventState(State.KEY_INVALID);
                break;
        }
        game.gameState.handleInput(game, keyCode);
    }

    @Override
    public void update(GameController game) {
        if (game.gameState == GameState.endedState && this.numEpisodes-- > 0) {
            System.out.println(
                    this.numEpisodes + ", " + QLearner.EPSILON + ", " + this.maxScore + ", " + game.getCurrentScore());
            if (this.numEpisodes % 100 == 0) {
                if (QLearner.EPSILON >= 0.001)
                    QLearner.EPSILON /= 1.1;

            }
            game.resetGame();
        }

        // Q-Learning invocation
        int currentAgentState = game.getAgentStateId();
        int currentAgentScore = game.getCurrentScore();
        int action = game.qLearner.getNextAction(currentAgentState);
        switch (game.getDirectionFromAgentAction(action)) {
            case 0:
                game.eventHandler.setEventState(State.KEY_PRESSED_UP);
                break;
            case 1:
                game.eventHandler.setEventState(State.KEY_PRESSED_RIGHT);
                break;
            case 2:
                game.eventHandler.setEventState(State.KEY_PRESSED_DOWN);
                break;
            case 3:
                game.eventHandler.setEventState(State.KEY_PRESSED_LEFT);
                break;
            default:
                game.eventHandler.setEventState(State.KEY_INVALID);
                break;
        }
        game.eventHandler.handleEvent();
        game.gameState.update(game);
        double reward = MOVEMENT_REWARD;
        int nextAgentState = game.getAgentStateId();
        int newScore = game.getCurrentScore();

        // todo: game can end when there's no food to add as well. Fix this check
        if (game.gameState == GameState.endedState) {
            nextAgentState = -1;
            reward = CRASH_REWARD;
            this.maxScore = newScore > this.maxScore ? newScore : this.maxScore;
        } else if (newScore > currentAgentScore) {
            reward = FOOD_REWARD;
        }
        game.qLearner.updateQTable(currentAgentState, action, reward, nextAgentState);
    }

}

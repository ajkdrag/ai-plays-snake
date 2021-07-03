package src.fsm.modes;

import src.controller.GameController;
import src.evt.State;
import src.fsm.states.GameState;
import src.learner.QLearner;

public class AITrainMode implements GameMode {
    int maxScore;
    static final double FOOD_REWARD = 500.0;
    static final double CRASH_REWARD = -100.0;
    static final double MOVEMENT_REWARD = -10.0;

    @Override
    public void enter(GameController game) {
        // reset
        if (QLearner.EPISODES == QLearner.MAX_EPISODES) {
            QLearner.EPISODES = 0;
        }
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
                game.gameMode.enter(game);
                break;
            case 'S':
                game.qLearner.saveStateJSON();
                break;
            default:
                game.eventHandler.setEventState(State.KEY_INVALID);
                break;
        }
        game.gameState.handleInput(game, keyCode);
    }

    @Override
    public void update(GameController game) {
        if (game.gameState == GameState.endedState && QLearner.EPISODES < QLearner.MAX_EPISODES) {
            QLearner.EPISODES++;
            System.out.println(
                    QLearner.EPISODES + ", " + QLearner.EPSILON + ", " + this.maxScore + ", " + game.getCurrentScore());
            if (QLearner.EPISODES % 100 == 0) {
                game.qLearner.saveStateJSON();
                if (QLearner.EPSILON >= 0.1)
                    QLearner.EPSILON /= 1.1;
                if (QLearner.LEARNING_RATE >= 0.1)
                    QLearner.LEARNING_RATE /= 1.1;
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

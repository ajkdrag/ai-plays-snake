package src.fsm.modes;

import src.controller.GameController;
import src.evt.State;
import src.fsm.states.GameState;
import src.learner.QLearner;

public class AITrainMode implements GameMode {
    int numEpisodes;

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
            QLearner.EPSILON -= 0.01;
            game.resetGame();
        }
        int currentAgentState = game.getAgentStateId();
        int currentAgentScore = game.getCurrentScore();
        int action = game.qLearner.getNextAction(currentAgentState);
        switch (game.convertAgentActionToDirection(action)) {
            case 0:
                game.eventHandler.setEventState(State.KEY_PRESSED_UP);
                game.eventHandler.handleEvent();
                break;
            case 1:
                game.eventHandler.setEventState(State.KEY_PRESSED_RIGHT);
                game.eventHandler.handleEvent();
                break;
            case 2:
                game.eventHandler.setEventState(State.KEY_PRESSED_DOWN);
                game.eventHandler.handleEvent();
                break;
            case 3:
                game.eventHandler.setEventState(State.KEY_PRESSED_LEFT);
                game.eventHandler.handleEvent();
                break;
            default:
                game.eventHandler.setEventState(State.KEY_INVALID);
                break;
        }
        game.gameState.update(game);
        double reward = -10.0;
        int nextAgentState = game.getAgentStateId();
        // TODO : game can end when there's no food to add as well. Fix this check
        if (game.gameState == GameState.endedState) {
            nextAgentState = -1;
            reward = -100.0;
        } else if (game.getCurrentScore() > currentAgentScore)
            reward = 500.0;
        game.qLearner.updateQTable(currentAgentState, action, reward, nextAgentState);
    }

}

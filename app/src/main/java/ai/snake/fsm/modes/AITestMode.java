package ai.snake.fsm.modes;

import ai.snake.controller.GameController;
import ai.snake.evt.State;
import ai.snake.fsm.states.GameState;

public class AITestMode implements GameMode {
    @Override
    public void enter(GameController game) {

    }

    @Override
    public void handleInput(GameController game, int keyCode) {
        switch (keyCode) {
            case '\t':
                game.eventHandler.setEventState(State.KEY_PRESSED_TAB);
                game.eventHandler.handleEvent();
                break;
            case 'Q':
                game.resetGame();
                break;
            case 'R':
                game.resetGame();
                game.gameMode = GameMode.manualMode;
                game.gameMode.enter(game);
                break;
            default:
                game.eventHandler.setEventState(State.KEY_INVALID);
                break;
        }
        game.gameState.handleInput(game, keyCode);
    }

    @Override
    public void update(GameController game) {
        if (game.gameState == GameState.endedState)
            return;
        int currentAgentState = game.getAgentStateId();
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
    }

}

package src.fsm.modes;

import src.controller.GameController;
import src.evt.State;
import src.fsm.states.GameState;

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
        int nextAction = game.qLearner.getNextAction(game.getAgentStateId());
        switch (nextAction) {
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
    }

}

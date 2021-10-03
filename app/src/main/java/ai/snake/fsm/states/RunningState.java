package ai.snake.fsm.states;

import ai.snake.controller.GameController;
import ai.snake.evt.State;

public class RunningState implements GameState {

    @Override
    public void handleInput(GameController game, int keyCode) {
        switch (keyCode) {
            case 38:
                // up
                game.setSpeedUp();
                break;
            case 40:
                // down
                game.setSpeedDown();
                break;
            case ' ':
                game.eventHandler.setEventState(State.KEY_PRESSED_SPACE);
                game.gameState = GameState.pausedState;
                break;
        }
    }

    @Override
    public void update(GameController game) {
        if (game.hasGameEnded()) {
            game.gameState = GameState.endedState;
            return;
        }
        game.updateGameBoard();
        game.getAgentStateId();
        game.updateTileStates();
    }

}

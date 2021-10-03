# AI-Plays-Snake
<p align="center">
<img src="extras/snake.gif" width=256px height=200px alt="AI playing snake demo">
</p>

Classic Snake game in Java | Processing, with Q-Learning.

## Usage
Clone/Download this repo
```bash
git clone https://github.com/ajkdrag/AI-Plays-Snake.git
```

Go to the root dir and run the gradle wrapper script
```bash
./gradlew run
```

This will launch a Processing sketch in *manual* mode. More about this under [Game Modes](#game-modes)

## Reinforcement Learning
The repo uses vanilla Q-Learning (no libraries) as the policy for the snake to learn *survival*.
<br>Eating the food and not dying is the goal for the snake in this environment.

## Game Modes
The sketch has 3 game modes - *manual*, *train*, *test*.
<br>Keybindings allow the user to transition across modes.

### Manual Mode
User controls the snake and plays the **classic** snake game.
<br>Keybindings :<br>
* W | S | A | D - Standard movement keys
* Q - Enter *test* mode
* T - Enter *train* mode
* R - Restart sketch with *manual* mode

### Test Mode
The snake follows the policy and moves in the environment accordingly.
<br>Keybindings :<br>
* Q - Restart sketch with *test* mode
* R - Restart sketch with *manual* mode

### Train Mode
The snake follows and **updates** the policy, thereby learning to *survive*.
<br>Keybindings :<br>
* S - Save current policy to file
* R - Restart sketch with *manual* mode


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * IMPORTANT: DO NOT EDIT THIS CLASS.
 *
 * Battleship backend game logic class.
 *
 * This class represents the overall game logic for Battleship.
 * You are the player, and you are battling the enemy. You and the enemy will take
 * turns to sink all of each other's battleships. Whoever sinks all ships first
 wins!
 *
 * The player's starting game state is represented in the text file whose path is
 PLAYER_GAMESTATE_PATH.
 * The enemy's starting game state is represented in the text file whose path is
 ENEMY_GAMESTATE_PATH.
 *
 * THIS CLASS IS PROVIDED FOR USE IN HW09.
 * You don't need to write the Battleship game logic;
 * Instead use the provided classes and focus on making a cool user interface!
 *
 * @author CS1331 TAs
 * @version 13.31
 */
public class BattleshipBackend {
    private static final String PLAYER_GAMESTATE_PATH = "player.txt";
    private static final String ENEMY_GAMESTATE_PATH = "enemy.txt";
    private final Ocean playerOcean;
    private final Ocean enemyOcean;
    private final List<Integer> enemyGuesses;
    private final Random random;
    /**
     * Initializes a Battleship game instance.
     */
    public BattleshipBackend() {
        playerOcean = new Ocean(PLAYER_GAMESTATE_PATH);
        enemyOcean = new Ocean(ENEMY_GAMESTATE_PATH);
        enemyGuesses = new ArrayList<>();
        for (int i = 0; i < Ocean.NUM_ROWS * Ocean.NUM_COLS; i++) {
            enemyGuesses.add(i);
        }
        random = new Random();
    }
    /**
     * This method performs a guess on the enemy's Ocean. This method is called on
     a Player's turn.
     *
     * Important: The rows are 1-indexed, and columns are alphabetically indexed
     starting at 'A'.
     *
     * @param rowIdx the row of the cell to guess.
     * @param colChar the column of the cell to guess.
     *
     * @return the outcome of the guess
     */
    public GuessOutcome attackEnemy(int rowIdx, char colChar) {
        return new GuessOutcome(rowIdx, colChar, enemyOcean.guess(rowIdx,
                colChar));
    }
    /**
     * This method performs a random guess on the player's Ocean. This method is
     called on a Enemy's turn.
     *
     * The enemy will query a random cell that has not been guessed before. If
     there are no more
     * cells left to query, the method will return as if the given cell has been
     already guessed.
     *
     * @return the outcome of the guess
     */
    public GuessOutcome attackPlayer() {
        if (enemyGuesses.isEmpty()) {
            return new GuessOutcome(1, 'A', CellState.ALREADY_GUESSED);
        }
        int guess = enemyGuesses.remove(random.nextInt(enemyGuesses.size()));
        int rowIdx = guess / Ocean.NUM_ROWS + 1;
        int colIdx = guess % Ocean.NUM_COLS;
        char colChar = (char) (colIdx + 65);
        return new GuessOutcome(rowIdx, colChar, playerOcean.guess(rowIdx,
                colChar));
    }
    /**
     * Checks if the player has a ship at (rowIdx, colChar) in their ocean.
     *
     * Important: The rows are 1-indexed, and columns are alphabetically indexed
     starting at 'A'.
     *
     * This method may be useful when displaying the player's ocean in the user
     interface!
     *
     * @param rowIdx the row to check
     * @param colChar the column to check
     *
     * @return true if the player has a ship at (rowIdx, colChar), false otherwise
     */
    public boolean playerHasShip(int rowIdx, char colChar) {
        return playerOcean.hasShip(rowIdx, colChar);
    }
    /**
     * Checks if the player has won (enemy has lost).
     *
     * @return true if the player has won, false otherwise.
     */
    public boolean hasPlayerWon() {
        return enemyOcean.hasLost();
    }
    /**
     * Checks if the enemy has won (player has lost).
     *
     * @return true if the player has lost, false otherwise.
     */
    public boolean hasEnemyWon() {
        return playerOcean.hasLost();
    }
}
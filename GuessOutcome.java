/**
 * IMPORTANT: DO NOT EDIT THIS CLASS.
 *
 * This class represents the outcome of a guess in Battleship.
 *
 * This class wraps the outcome of the player or the enemy's guess into
 * one class for easy access when building your JavaFX application.
 *
 * THIS CLASS IS PROVIDED FOR USE IN HW09.
 * You don't need to write the Battleship game logic;
 * Instead use the provided classes and focus on making a cool user interface!
 *
 * @author CS1331 TAs
 * @version 13.31
 */
public class GuessOutcome {
    private final int rowIdx;
    private final char colChar;
    private final CellState state;
    /**
     * Constructs an instance of GuessOutcome with 3-arguments.
     *
     * @param rowIdx the row of this guess
     * @param colChar the column of this guess
     * @param state the outcome of this guess
     */
    public GuessOutcome(int rowIdx, char colChar, CellState state) {
        this.rowIdx = rowIdx;
        this.colChar = colChar;
        this.state = state;
    }
    /**
     * Returns the row of this guess.
     *
     * @return the row of this guess
     */
    public int getRow() {
        return rowIdx;
    }
    /**
     * Returns the column of this guess.
     *
     * @return the column of this guess
     */
    public char getColumn() {
        return colChar;
    }
    /**
     * Returns the outcome state of this guess.
     *
     * For example, if a guess was a hit, this method will return CellState.HIT.
     * If the guess was a miss, this method will return CellState.MISS.
     * Use this method to determine what color a cell should be after a guess.
     *
     * @return the outcome cell state of this guess
     */
    public CellState getCellState() {
        return state;
    }
}
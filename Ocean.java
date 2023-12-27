import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/**
 * IMPORTANT: DO NOT EDIT THIS CLASS.
 * Ocean game logic class.
 * This class represents one navy's ocean.
 * An ocean is represented by a 10x10 2D grid with cells containing either ship
 parts or water.
 * The rows are 1-indexed, and columns are alphabetically indexed starting at 'A'.
 * The starting game configuration for an Ocean instance is specified in text files
 (see BattleshipBackend.java).
 * You may assume that hat the ship configuration text files will always be 10x10
 grids of characters.
 * THIS CLASS IS PROVIDED FOR USE IN HW09.
 * You don't need to write the Battleship game logic;
 * Instead use the provided classes and focus on making a cool user interface!
 *
 * @author CS1331 TAs
 * @version 13.31
 */
public class Ocean {
    public static final int NUM_ROWS = 10;
    public static final int NUM_COLS = 10;
    /**
     * Backend tiles. true represents ship, false represents water.
     */
    private final boolean[][] backend = new boolean[NUM_ROWS][NUM_COLS];
    /**
     * Frontend tiles.
     */
    private final CellState[][] frontend = new CellState[NUM_ROWS][NUM_COLS];
    /**
     * Number of ship parts left in this ocean.
     */
    private int numShipPartsLeft = 0;
    /**
     * 1-arg constructor for an Ocean instance.
     *
     * @param pathname the path to the game state text file
     */
    public Ocean(String pathname) {
        parseShipConfiguration(pathname);
    }
    /**
     * Parses the ship configuration text file.
     * <p>
     * This method sets the backend array according to the configuration of ships
     (X)
     * and water (~) in the input text file. The frontend array is populated with
     UNKNOWN.
     *
     * @param pathname the path of the ship configuration file to parse
     */
    private void parseShipConfiguration(String pathname) {
        File file = new File(pathname);
        Scanner scan = null;
        try {
            scan = new Scanner(file);
            int i = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                for (int j = 0; j < NUM_COLS; ++j) {
                    backend[i][j] = line.charAt(j) == 'X';
                    frontend[i][j] = CellState.UNKNOWN;
                    if (hasShip(i + 1, (char) (j + 65))) {
                        ++numShipPartsLeft;
                    }
                }
                ++i;
            }
        } catch (FileNotFoundException e) {
            System.out.printf("Exception was thrown while parsing file: %s\n",
                    e.getMessage());
        } finally {
            if (scan != null) {
                scan.close();
            }
        }
    }
    /**
     * Play a guess at (rowIdx, colChar) and updates the frontend state of this
     Ocean.
     *
     * @param rowIdx row to guess
     * @param colChar column to guess
     * @return the state of the cell after guessing.
     */
    public CellState guess(int rowIdx, char colChar) {
        Ocean.validateIndices(rowIdx, colChar);
        if (hasGuessed(rowIdx, colChar)) {
            return CellState.ALREADY_GUESSED;
        }
        CellState outcome = hasShip(rowIdx, colChar) ? CellState.HIT :
                CellState.MISS;
        if (outcome == CellState.HIT) {
            --numShipPartsLeft;
        }
        frontend[rowIdx - 1][colChar - 65] = outcome; // 65 is ASCII representation of 'A'
        return outcome;
    }
    /**
     * Check if there is a ship at (rowIdx, colChar) in the backend.
     *
     * @param rowIdx the row the query
     * @param colChar the column the query
     * @return true if there is a ship at (rowIdx, colChar), false otherwise
     */
    public boolean hasShip(int rowIdx, char colChar) {
        Ocean.validateIndices(rowIdx, colChar);
        return backend[rowIdx - 1][colChar - 65];
    }
    /**
     * Determines if we have previously guessed at (rowIdx, colChar).
     *
     * @param rowIdx the row to guess
     * @param colChar the column to guess
     * @return true if we have guessed at (rowIdx, colChar), false otherwise
     */
    private boolean hasGuessed(int rowIdx, char colChar) {
        Ocean.validateIndices(rowIdx, colChar);
        return frontend[rowIdx - 1][colChar - 65] != CellState.UNKNOWN;
    }
    /**
     * Returns if this Ocean represents a losing state.
     * <p>
     * A losing state is when you have no ship parts left: all your ships have been
     destroyed!
     *
     * @return true if this Ocean represents a losing state, false otherwise
     */
    public boolean hasLost() {
        return numShipPartsLeft == 0;
    }
    /**
     * Validates whether (rowIdx, colChar) is in-bounds and throws an exception if
     it is not.
     *
     * @param rowIdx the row index
     * @param colChar the column index
     */
    private static void validateIndices(int rowIdx, char colChar) {
        if (rowIdx < 1 || rowIdx > NUM_ROWS) {
            throw new IndexOutOfBoundsException(String.format("Row %d is out of bounds!", rowIdx));
        }
        if (colChar < 'A' || colChar > 'J') {
            throw new IndexOutOfBoundsException(String.format("Column %c is out of bounds!", colChar));
        }
    }
}
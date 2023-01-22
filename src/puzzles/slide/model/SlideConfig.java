package puzzles.slide.model;

import puzzles.common.solver.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.*;

// TODO: implement your SlideConfig for the common solver

public class SlideConfig implements Configuration {

    private static int ROWS;
    private static int COLS;
    private static final int EMPTY = 0;
    private int[][] grid;
    private int emptyRow;
    private int emptyCol;


    /**
     * Initial constructor to set up the board.
     *
     * @param filename
     * @throws IOException
     */
    public SlideConfig(String filename) throws IOException {
        Scanner file = new Scanner(new File(filename));

        // First value is the row length, second is the column length
        ROWS = file.nextInt();
        COLS = file.nextInt();

        // Creates a 2d integer array with ROWS and COLS
        this.grid = new int[ROWS][COLS];

        // Populates the integer array
        for (int r=0; r<ROWS; r++) {
            for (int c=0; c<COLS; c++) {

                // Gets the values from the file
                String next = file.next();

                // Tries to convert to integer
                // If it can't, we found the blank and put a 0 in its spot
                try {
                    this.grid[r][c] = Integer.parseInt(next);
                }
                catch (Exception e) {
                    //System.out.println("Found empty at: int[" + r + "][" + c + "]" );
                    this.emptyRow = r;
                    this.emptyCol = c;

                    this.grid[r][c] = EMPTY;
                }
            }
        }
    }

    /**
     * Copy constructor for the children.
     *
     * @param parent
     * @param newEmptyRow
     * @param newEmptyCol
     */
    public SlideConfig(int[][] parent, int newEmptyRow, int newEmptyCol) {

        // Copying 2d arrays is such a pain
        this.grid = new int[ROWS][COLS];
        for (int r=0; r<ROWS; r++) {
            this.grid[r] = parent[r].clone();
        }

        this.emptyRow = newEmptyRow;
        this.emptyCol = newEmptyCol;
    }

    @Override
    public boolean isSolution() {

        // Assumes true
        boolean result = true;

        // Converts the grid to an arrayList of integers
        // Excludes the "." aka "0"
        ArrayList<Integer> list = new ArrayList<>();

        for (int row=0; row<ROWS; row++) {
            for (int col=0; col<COLS; col++) {
                if (this.grid[row][col] != EMPTY) {
                    list.add(this.grid[row][col]);
                }
            }
        }

        // Checks if the integer in front is larger than the current integer
        // If so, then this is not the solution
        for (int i=0; i<(list.size()-1); i++) {
            if (list.get(i) > list.get(i+1)) {
                result = false;
            }
        }

        // The empty spot is not at the end
        if (this.emptyRow != ROWS-1 || this.emptyCol != COLS-1) {
            result = false;
        }

        return result;
    }

    @Override
    public Collection<Configuration> getNeighbors() {

        // Storing successors
        List<Configuration> successors = new LinkedList<>();

        if (this.emptyRow-1 >= 0) {
            // Ok to switch with above value

            // Gets the original values of the spots
            int A = this.grid[this.emptyRow-1][this.emptyCol];

            // Swaps the values of the spots
            this.grid[this.emptyRow-1][this.emptyCol] = EMPTY;
            this.grid[this.emptyRow][this.emptyCol] = A;

            // Adds the new configuration to the successors
            successors.add(new SlideConfig(this.grid, this.emptyRow-1, this.emptyCol));

            // Swaps the values of the spots back
            this.grid[this.emptyRow-1][this.emptyCol] = A;
            this.grid[this.emptyRow][this.emptyCol] = EMPTY;
        }
        if (this.emptyRow+1 < ROWS) {
            // Ok to switch with below value

            // Gets the original values of the spots
            int A = this.grid[this.emptyRow+1][this.emptyCol];

            // Swaps the values of the spots
            this.grid[this.emptyRow+1][this.emptyCol] = EMPTY;
            this.grid[this.emptyRow][this.emptyCol] = A;

            // Adds the new configuration to the successors
            successors.add(new SlideConfig(this.grid, this.emptyRow+1, this.emptyCol));

            // Swaps the values of the spots back
            this.grid[this.emptyRow+1][this.emptyCol] = A;
            this.grid[this.emptyRow][this.emptyCol] = EMPTY;
        }
        if (this.emptyCol-1 >= 0) {
            // Ok to switch with left value

            // Gets the original values of the spots
            int A = this.grid[this.emptyRow][this.emptyCol-1];

            // Swaps the values of the spots
            this.grid[this.emptyRow][this.emptyCol-1] = EMPTY;
            this.grid[this.emptyRow][this.emptyCol] = A;

            // Adds the new configuration to the successors
            successors.add(new SlideConfig(this.grid, this.emptyRow, this.emptyCol-1));

            // Swaps the values of the spots back
            this.grid[this.emptyRow][this.emptyCol-1] = A;
            this.grid[this.emptyRow][this.emptyCol] = EMPTY;
        }
        if (this.emptyCol+1 < COLS) {
            // Ok to switch with right value

            // Gets the original values of the spots
            int A = this.grid[this.emptyRow][this.emptyCol+1];
            int Empty = this.grid[this.emptyRow][this.emptyCol];

            // Swaps the values of the spots
            this.grid[this.emptyRow][this.emptyCol+1] = Empty;
            this.grid[this.emptyRow][this.emptyCol] = A;

            // Adds the new configuration to the successors
            successors.add(new SlideConfig(this.grid, this.emptyRow, this.emptyCol+1));

            // Swaps the values of the spots back
            this.grid[this.emptyRow][this.emptyCol+1] = A;
            this.grid[this.emptyRow][this.emptyCol] = Empty;
        }

        return successors;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof SlideConfig temp) {
            result = (this.emptyRow == temp.emptyRow) && (this.emptyCol == temp.emptyCol);
        }
        return result;
    }

    @Override
    public int hashCode() {
        // The hash is just all the numbers concatenated from the grid including the "." which is a "0"

        String hash = "";
        for (int row=0; row<ROWS; row++) {
            for (int col=0; col<COLS; col++) {
                hash += this.grid[row][col];
            }
        }
        return hash.hashCode();
    }

    @Override
    public String toString() {
        String print = "";

        for (int row=0; row<ROWS; row++) {
            for (int col=0; col<COLS; col++) {
                if (this.grid[row][col] == EMPTY) {
                    print += " .";
                } else {
                    if (this.grid[row][col] < 10) {
                        print += " " + this.grid[row][col];
                    } else {
                        print += this.grid[row][col];
                    }
                }
                print += " ";
            }
            print += "\n";
        }

        return print;
    }

    public static int getROWS() {
        return ROWS;
    }

    public static int getCOLS() {
        return COLS;
    }

    public int[][] getGrid() {
        return this.grid;
    }

    public int getEmptyRow() {
        return emptyRow;
    }

    public int getEmptyCol() {
        return emptyCol;
    }

    public void makeMove(int aSelectionRow, int aSelectionCol, int bSelectionRow, int bSelectionCol) {

        // Gets the values
        int A = this.grid[aSelectionRow][aSelectionCol];
        int B = this.grid[bSelectionRow][bSelectionCol];

        // Switches the values
        this.grid[aSelectionRow][aSelectionCol] = B;
        this.grid[bSelectionRow][bSelectionCol] = A;

        // Setting the new empty spot
        if (this.grid[aSelectionRow][aSelectionCol] == EMPTY) {
            this.emptyRow = aSelectionRow;
            this.emptyCol = aSelectionCol;
        }
        else if (this.grid[bSelectionRow][bSelectionCol] == EMPTY) {
            this.emptyRow = bSelectionRow;
            this.emptyCol = bSelectionCol;
        }
    }
}

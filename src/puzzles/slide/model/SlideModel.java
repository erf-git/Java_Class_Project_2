package puzzles.slide.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.slide.ptui.SlidePTUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SlideModel {
    /** the collection of observers of this model */
    private final List<Observer<SlideModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    private SlideConfig currentConfig;

    private String file;

    private boolean is1stSelection = true;
    private int aSelectionRow;
    private int aSelectionCol;
    private int bSelectionRow;
    private int bSelectionCol;

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<SlideModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    public SlideModel(String filename) throws IOException {

        this.currentConfig = new SlideConfig(filename);
        this.file = filename;
    }

    public void load(String filename) throws IOException {

        String file = "data/slide/" + filename;
        this.currentConfig = new SlideConfig(file);
        this.file = file;
        alertObservers("Loaded: " + filename);
    }

    public void reset() throws IOException {

        this.currentConfig = new SlideConfig(this.file);
        alertObservers("Puzzle Reset!");
    }

    public void solveNextStep() {

        if (this.currentConfig.isSolution()) {
            // First sees if it's the solution already
            alertObservers("Game is solved!");
        }
        else {
            // Since it is not the solution, the algorithm will try to solve

            // Returns steps to solve the currentConfig
            List<Configuration> answer = Solver.solve(this.currentConfig);

            // Checks if there is no solution
            // Else, sets the currentConfig as the new config
            if (answer == null) {
                alertObservers("No solution");
            }
            else
            {
                // Sets the currentConfig as the next configuration
                this.currentConfig = (SlideConfig) answer.get(1);

                // If the move solves the grid, then show it
                if (this.currentConfig.isSolution()) {
                    alertObservers("Game is solved!");
                }
                else {
                    alertObservers("Next step!");
                }
            }
        }
    }

    public void makeSelection(int row, int col) {

        // If the grid is solved, then you can't make any more moves
        if (this.currentConfig.isSolution()) {
            alertObservers("Board is solved!");
        }
        else {
            if (isValidCoordinates(row, col)) {
                // Checks if the coordinates are within the grid

                if (is1stSelection) {
                    // First selection
                    aSelectionRow = row;
                    aSelectionCol = col;

                    is1stSelection = false;
                    alertObservers("Selected (" + aSelectionRow + "," + aSelectionCol + ")");

                } else {
                    // Second selection
                    bSelectionRow = row;
                    bSelectionCol = col;

                    is1stSelection = true;

                    if (isValidMove(aSelectionRow, aSelectionCol, bSelectionRow, bSelectionCol)) {
                        // Successful move
                        this.currentConfig.makeMove(aSelectionRow, aSelectionCol, bSelectionRow, bSelectionCol);

                        // If the move solves the grid, then show it
                        if (this.currentConfig.isSolution()) {
                            alertObservers("Game is solved!");
                        }
                        else {
                            alertObservers("Moved from (" + aSelectionRow + "," + aSelectionCol + ") to (" + bSelectionRow + "," + bSelectionCol + ")");
                        }

                    } else {
                        // Failed move
                        alertObservers("Invalid selection, please try again");

                    }
                }
            }
        }
    }

    public boolean isValidCoordinates(int coordRow, int coordCol) {

        // coordinate in bounds
        return coordRow < SlideConfig.getROWS() && coordRow >= 0 && coordCol < SlideConfig.getCOLS() && coordCol >= 0;
    }

    public boolean isValidMove(int aSelectionRow, int aSelectionCol, int bSelectionRow, int bSelectionCol) {

        boolean result = false;

        if (aSelectionRow == bSelectionRow && aSelectionCol ==bSelectionCol) {
            // Both coordinates are the same
            return false;
        }

        if (aSelectionRow == this.currentConfig.getEmptyRow() && aSelectionCol == this.currentConfig.getEmptyCol()) {
            // First select is the empty spot

            if (bSelectionRow == aSelectionRow-1 && bSelectionCol == aSelectionCol) {
                // Picked above
                result = true;
            } else if (bSelectionRow == aSelectionRow+1 && bSelectionCol == aSelectionCol) {
                // Picked below
                result = true;
            } else if (bSelectionRow == aSelectionRow && bSelectionCol == aSelectionCol-1) {
                // Picked left
                result = true;
            } else if (bSelectionRow == aSelectionRow && bSelectionCol == aSelectionCol+1) {
                // Picked right
                result = true;
            }

        } else if (bSelectionRow == this.currentConfig.getEmptyRow() && bSelectionCol == this.currentConfig.getEmptyCol()) {
            // Second select is the empty spot

            if (aSelectionRow == bSelectionRow-1 && aSelectionCol == bSelectionCol) {
                // Picked above
                result = true;
            } else if (aSelectionRow == bSelectionRow+1 && aSelectionCol == bSelectionCol) {
                // Picked below
                result = true;
            } else if (aSelectionRow == bSelectionRow && aSelectionCol == bSelectionCol-1) {
                // Picked left
                result = true;
            } else if (aSelectionRow == bSelectionRow && aSelectionCol == bSelectionCol+1) {
                // Picked right
                result = true;
            }
        }

        return result;
    }


    @Override
    public String toString() {

        String print = "  ";

        // Adds top row
        for (int i = 0; i< SlideConfig.getCOLS(); i++) {
            if (i < 10) {
                print += "  " + i;
            } else {
                print += " " + i;
            }
        }
        print += "\n  ";

        // Adds the dashes
        for (int i = 0; i< SlideConfig.getCOLS(); i++) {
            print += "---";
        }
        print += "\n";

        for (int row = 0; row< SlideConfig.getROWS(); row++) {
            print += row + "| ";
            for (int col = 0; col< SlideConfig.getCOLS(); col++) {
                if (this.currentConfig.getGrid()[row][col] == 0) {
                    print += " .";
                } else {
                    if (this.currentConfig.getGrid()[row][col] < 10) {
                        print += " " + this.currentConfig.getGrid()[row][col];
                    } else {
                        print += this.currentConfig.getGrid()[row][col];
                    }
                }
                print += " ";
            }
            print += "\n";
        }

        return print;
    }

    public SlideConfig getCurrentConfig() {
        return currentConfig;
    }
}

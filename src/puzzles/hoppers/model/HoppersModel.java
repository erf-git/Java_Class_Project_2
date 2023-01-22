package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.*;

/**
 * model that is used for the ptui and gui
 * @Author Jared Hugo
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();

    /** the current configuration */
    public HoppersConfig currentConfig;
    /** keeps track of the row used for the select method */
    private int startRow = -1;
    /** keeps track of the column for the select method */
    private int startCol = -1;
    /** the last file that was started used to refresh the puzzle */
    public static String lastRefresh;

    /**
     * Hoppers model constructor
     * @param filename filename path of the puzzle that will be loaded
     * @throws IOException
     */
    public HoppersModel(String filename) throws IOException {
        lastRefresh = filename;
        this.currentConfig = new HoppersConfig(filename);
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * Selects a column and either saves the point if it is a first move or, makes a move if it is a second point
     * @param r
     * @param c
     */
    public void select(int r, int c){
        if (r >= 0 && r <= currentConfig.getRows() && c >= 0 && c <= currentConfig.getCols()) {
            char[][] grid = currentConfig.getGrid();
            if (startRow == -1) {
                if (grid[r][c] != 'G' && grid[r][c] != 'R'){
                    alertObservers("No frog at (" + r + ", " + c + ")");
                }
                else {
                    startRow = r;
                    startCol = c;
                    alertObservers("Selected (" + r + ", " + c + ")");
                }
            } else {
                makeMove(startRow, startCol, r, c);
                startRow = -1;
                startCol = -1;
            }
        }
        else{
            alertObservers("Invalid Position!!!");
        }
    }

    /**
     * Loads a puzzle with given path
     * @param text pathway to load the puzzle
     * @throws IOException
     */
    public void load(String text) throws IOException{
        lastRefresh = text;
        this.currentConfig = new HoppersConfig(text);
        String[] wordList = text.split("/");
        alertObservers("Loaded: " + wordList[2]);
    }

    /**
     * Gives a hint for the current puzzle
     */
    public void hint(){
        if (this.currentConfig.isSolution()){
            alertObservers("Already Solved!!!");
        }
        else{
            List<Configuration> solution = Solver.solve(currentConfig);
            try{HoppersConfig hinter = (HoppersConfig) solution.get(1);
                this.currentConfig = hinter;
                alertObservers("Next step!");
            }
            catch(NullPointerException e) {
                alertObservers("No solution!!!");
            }
        }
    }

    /**
     * Resets the puzzle
     * @throws IOException
     */
    public void reset() throws IOException{
        this.currentConfig = new HoppersConfig(lastRefresh);
        alertObservers("Puzzle Reset!!!");
    }

    /**
     * Checks to see if a move is valid
     * @param sr Starting row
     * @param sc Starting column
     * @param er Ending row
     * @param ec Ending column
     * @return T/F value for if the move is valid
     */
    private boolean isValid(int sr, int sc, int er, int ec){
        boolean result = false;
        char[][] grid = currentConfig.getGrid();
        if ((grid[sr][sc] == 'G' || grid[sr][sc] == 'R') && grid[er][ec] == '.' && grid[averageRow(sr,er)][averageCol(sc,ec)] == 'G'){
            if (sr >= 0 && er <= currentConfig.getRows() && sc >= 0 && ec <= currentConfig.getCols()){
                if (sr % 2 == 0){
                    if (er - sr == 2 && ec - sc == 2){
                        result = true;
                    }
                    else if (er - sr == 2 && sc - ec == 2){
                        result = true;
                    }
                    else if (sr - er == 2 && sc - ec == 2){
                        result = true;
                    }
                    else if (sr - er == 2 && ec - sc == 2){
                        result = true;
                    }
                    else if (er - sr == 4 && ec - sc == 0){
                        result = true;
                    }
                    else if (sr - er == 4 && sc - ec == 0){
                        result = true;
                    }
                    else if (sr - er == 0 && sc - ec == 4){
                        result = true;
                    }
                    else if (sr - er == 0 && ec - sc == 4){
                        result = true;
                    }
                }
                else{
                    if (er - sr == 2 && ec - sc == 2){
                        result = true;
                    }
                    else if (er - sr == 2 && sc - ec == 2){
                        result = true;
                    }
                    else if (sr - er == 2 && sc - ec == 2){
                        result = true;
                    }
                    else if (sr - er == 2 && ec - sc == 2){
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Makes a move in the puzzle
     * @param sr Starting row
     * @param sc Starting column
     * @param er Ending row
     * @param ec Ending column
     */
    private void makeMove(int sr, int sc, int er, int ec){
        char[][] grid = currentConfig.getGrid();
        if (isValid(sr,sc,er,ec)){
            this.currentConfig.makeMove(sr,sc,er,ec,averageRow(sr,er), averageCol(sc,ec), grid[sr][sc]);
            alertObservers("Jumped from (" + sr + ", " + sc + ") to (" + er + ", " + ec + ")");
        }
        else{
            alertObservers("Cannot jump from (" + sr + ", " + sc + ")" + " to (" + er + ", " + ec + ")");
        }
    }

    /**
     * Finds the average of the starting row and ending row
     * @param sr starting row
     * @param er ending row
     * @return the average of the rows
     */
    public int averageRow(int sr, int er){
        return (sr+er)/2;
    }

    /**
     * Finds the average of the starting column and ending column
     * @param sc starting column
     * @param ec ending column
     * @return the average of the columns
     */
    public int averageCol(int sc, int ec){
        return (sc+ec)/2;
    }

    /**
     * Sets the last refreshed model
     * @param last String containing the path to the configuration
     */
    public static void setLastRefresh(String last){
        lastRefresh = last;
    }

    /**
     * toString for the models
     * @return String representation of the model
     */
    public String toString(){
        StringBuilder result = new StringBuilder();
        result.append("   ");
        for (int i = 0; i < currentConfig.getCols(); i++){
            result.append(i + " ");
        }
        result.append("\n");
        result.append("  ");
        for (int i = 0; i < currentConfig.getCols(); i++){
            result.append("--");
        }
        result.append("\n");
        char[][] grid = currentConfig.getGrid();
        for (int i = 0; i < currentConfig.getRows(); i++){
            result.append(i + "|");
            for (int j = 0; j < currentConfig.getCols(); j++){
                result.append(" " + grid[i][j]);
            }
            result.append("\n");
        }
        return result.toString();
    }
}

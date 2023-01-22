package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;


/**
 * class that is responsible for creating and checking all the neighbors for a hoppers config
 * @Author Jared Hugo
 */
public class HoppersConfig implements Configuration{
    /** Number of rows in a hopper config */
    private static int rows;
    /** Number of columns in a hopper config */
    private static int cols;
    /** Grid that resembles the board that is used for a config */
    private char[][] grid;

    /**
     * Constructor to create the initial configuration for the hoppers puzzle
     * @param filename The file that is read to get the initial configuration
     * @throws IOException
     */
    public HoppersConfig(String filename) throws IOException {
        Scanner f = new Scanner(new File(filename));
        rows = f.nextInt();
        cols = f.nextInt();
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = f.next().charAt(0);
                }
            }
    }

    /**
     * Copy constructor that is used to make every neighbor in the puzzle
     * @param copy previous configuration that will be used for copying
     * @param newrow the row that is being changed
     * @param newcol the column that is being changed
     * @param direction the direction of the jump
     */
    public HoppersConfig(HoppersConfig copy, int newrow, int newcol, String direction){
        this.grid = new char[rows][cols];
        for (int row = 0; row < rows; row++){
            System.arraycopy(copy.grid[row], 0, this.grid[row], 0, cols);
        }
        if (direction.equals("left")){
            this.grid[newrow][newcol-4] = this.grid[newrow][newcol];
            this.grid[newrow][newcol] = '.';
            this.grid[newrow][newcol-2] = '.';
        }
        else if(direction.equals("up-left")){
            this.grid[newrow-2][newcol-2] = this.grid[newrow][newcol];
            this.grid[newrow][newcol] = '.';
            this.grid[newrow-1][newcol-1] = '.';
        }
        else if(direction.equals("up")){
            this.grid[newrow-4][newcol] = this.grid[newrow][newcol];
            this.grid[newrow][newcol] = '.';
            this.grid[newrow-2][newcol] = '.';
        }
        else if(direction.equals("up-right")){
            this.grid[newrow-2][newcol+2] = this.grid[newrow][newcol];
            this.grid[newrow][newcol] = '.';
            this.grid[newrow-1][newcol+1] = '.';
        }
        else if(direction.equals("right")){
            this.grid[newrow][newcol+4] = this.grid[newrow][newcol];
            this.grid[newrow][newcol] = '.';
            this.grid[newrow][newcol+2] = '.';
        }
        else if(direction.equals("down-right")){
            this.grid[newrow+2][newcol+2] = this.grid[newrow][newcol];
            this.grid[newrow][newcol] = '.';
            this.grid[newrow+1][newcol+1] = '.';
        }
        else if(direction.equals("down")){
            this.grid[newrow+4][newcol] = this.grid[newrow][newcol];
            this.grid[newrow][newcol] = '.';
            this.grid[newrow+2][newcol] = '.';
        }
        else{
            this.grid[newrow+2][newcol-2] = this.grid[newrow][newcol];
            this.grid[newrow][newcol] = '.';
            this.grid[newrow+1][newcol-1] = '.';
        }
    }

    @Override
    /**
     * Checks to see if the configuration is a solution
     * @return T/F value for if it is the solution
     */
    public boolean isSolution() {
        boolean result = false;
        int counter = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
              if (grid[i][j] == 'G'){
                  counter++;
              }
            }
        }
        if (counter == 0){
            result = true;
        }
        return result;
    }

    @Override
    /**
     * Creates all the neighbors needed to find the fastest solution to the puzzle
     * @return collection of all the neighbors
     */
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> lst = new LinkedList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == 'G' || grid[i][j] == 'R'){
                    if (i % 2 == 0){
                        for (int z = 0; z < 8; z++){
                            if (z==0 && j-4>=0 && grid[i][j-2] == 'G' && grid[i][j-4] == '.'){
                                lst.add(new HoppersConfig(this,i,j, "left"));
                            }
                            else if (z==1 && j-2>=0 && i-2>=0 && grid[i-1][j-1] == 'G' && grid[i-2][j-2] == '.'){
                                lst.add(new HoppersConfig(this,i,j, "up-left"));
                            }
                            else if (z==2 && i-4>=0 && grid[i-2][j] == 'G' && grid[i-4][j] == '.'){
                                lst.add(new HoppersConfig(this,i,j,"up"));
                            }
                            else if (z==3 && j+2<=cols-1 && i-2>=0 && grid[i-1][j+1] == 'G' && grid[i-2][j+2] == '.'){
                                lst.add(new HoppersConfig(this,i,j,"up-right"));
                            }
                            else if (z==4 && j+4<=cols-1 && grid[i][j+2] == 'G' && grid[i][j+4] == '.'){
                                lst.add(new HoppersConfig(this,i,j,"right"));
                            }
                            else if (z==5 && j+2<=cols-1 && i+2<=rows-1 && grid[i+1][j+1] == 'G' && grid[i+2][j+2] == '.'){
                                lst.add(new HoppersConfig(this,i,j,"down-right"));
                            }
                            else if (z==6 && i+4<=rows-1 && grid[i+2][j] == 'G' && grid[i+4][j] == '.'){
                                lst.add(new HoppersConfig(this,i,j,"down"));
                            }
                            else if (z==7 && j-2>=0 && i+2<=rows-1 && grid[i+1][j-1] == 'G' && grid[i+2][j-2] == '.'){
                                lst.add(new HoppersConfig(this,i,j,"down-left"));
                            }
                        }
                    }
                    else{
                        for (int z = 0; z < 4; z++){
                            if (z==0 && j-2>=0 && i-2>=0 && grid[i-1][j-1] == 'G' && grid[i-2][j-2] == '.'){
                                lst.add(new HoppersConfig(this,i,j,"up-left"));
                            }
                            else if (z==1 && j+2<=cols-1 && i-2>=0 && grid[i-1][j+1] == 'G' && grid[i-2][j+2] == '.'){
                                lst.add(new HoppersConfig(this,i,j,"up-right"));
                            }
                            else if (z==2 && j+2<=cols-1 && i+2<=rows-1 && grid[i+1][j+1] == 'G' && grid[i+2][j+2] == '.'){
                                lst.add(new HoppersConfig(this,i,j,"down-right"));
                            }
                            else if (z==3 && j-2>=0 && i+2<=rows-1 && grid[i+1][j-1] == 'G' && grid[i+2][j-2] == '.'){
                                lst.add(new HoppersConfig(this,i,j,"down-left"));
                            }
                        }
                    }
                }
            }
        }
        return lst;
    }

    /**
     * Gets the grid of the current config
     * @return grid
     */
    public char[][] getGrid(){
        return this.grid;
    }

    /**
     * Gets the number of columns of the current config
     * @return number of columns
     */
    public int getCols(){
        return cols;
    }

    /**
     * Gets the number of rows of the current config
     * @return number of rows
     */
    public int getRows(){
        return rows;
    }

    /**
     * Makes a move for a single config
     * @param sr starting row
     * @param sc starting column
     * @param er end row
     * @param ec end column
     * @param mr middle row
     * @param mc middle column
     * @param frogger frog that is jumping
     */
    public void makeMove(int sr, int sc, int er, int ec, int mr, int mc, char frogger){
        this.grid[sr][sc] = '.';
        this.grid[er][ec] = frogger;
        this.grid[mr][mc] = '.';
    }

    /**
     * to string for the hoppers configurations
     * @return string representation of the configuration
     */
    public String toString(){
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            result.append("\n");
            for (int j = 0; j < cols; j++) {
                result.append(this.grid[i][j] + " ");
            }
        }
        result.append("\n");
        return result.toString();
    }

    /**
     * Creates a hashcode for the configuration
     * @return hash code of configuration
     */
    public int hashCode(){
        return this.toString().hashCode();
    }

    /**
     * Checks to see if two configurations are the same
     * @param other the other configuration that will be compared with
     * @return T/F value for if the configurations are equal
     */
    public boolean equals(Object other){
        boolean result = false;
        if (other instanceof HoppersConfig){
            HoppersConfig otherc = (HoppersConfig) other;
            if (this.toString().equals(otherc.toString())){
                result = true;
            }
        }
        return result;
    }
}

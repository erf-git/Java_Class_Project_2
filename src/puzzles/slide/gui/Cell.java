package puzzles.slide.gui;

import javafx.scene.control.Button;

public class Cell extends Button {

    // Stores the cell's number
    private int number;

    // Stores button's row and column position
    private int rowPos;
    private int colPos;

    public Cell(int number, int rowPos, int colPos) {
        this.number = number;
        this.rowPos = rowPos;
        this.colPos = colPos;
    }

    // Gets the cell's number
    public int getNumber() { return this.number; }

    // You can get the cell's row and column position
    public int getRowPos() { return this.rowPos; }
    public int getColPos() { return this.colPos; }

}

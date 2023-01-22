package puzzles.crossing;

import puzzles.common.solver.Configuration;
import puzzles.strings.StringsConfig;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Class that is responsible for creating and checking all of the neighbors for a crossing config
 * @Author Jared Hugo
 */
public class CrossingConfig implements Configuration {
    /** Boolean that tells if the boat is on the left */
    private boolean boatLeft;
    /** The amount of pups on the left side of the river */
    private int pupsLeft;
    /** The amount of pups on the right side of the river */
    private int pupsRight;
    /** The amount of wolves on the left side of the river */
    private int wolvesLeft;
    /** The amount of wolves on the right of the river */
    private int wolvesRight;
    @Override
    /**
     * Checks to see if a crossing config is a solution
     * @return T/F value for if config is a solution
     */
    public boolean isSolution() {
        return pupsLeft == 0 && wolvesLeft == 0;
    }

    /**
     * Constructor for a crossing config
     * @param pups The number of total pups
     * @param wolves The number of total wolves
     */
    public CrossingConfig(int pups, int wolves){
        this.pupsLeft = pups;
        this.wolvesLeft = wolves;
        this.wolvesRight = 0;
        this.pupsRight = 0;
        this.boatLeft = true;
    }

    /**
     * Copy constructor for a crossing config, used in creating neighbors
     * @param newPups The new amount of pups
     * @param newWolves The new amount of wolves
     * @param copy copy of the previous configuration
     */
    public CrossingConfig(int newPups, int newWolves, CrossingConfig copy){
        if (copy.boatLeft) {
            this.pupsRight = newPups + copy.pupsRight;
            this.wolvesRight = newWolves + copy.wolvesRight;
            this.pupsLeft = copy.pupsLeft - newPups;
            this.wolvesLeft = copy.wolvesLeft - newWolves;
        }
        else{
            this.pupsRight = copy.pupsRight - newPups;
            this.wolvesRight = copy.wolvesRight - newWolves;
            this.pupsLeft = copy.pupsLeft + newPups;
            this.wolvesLeft = copy.wolvesLeft + newWolves;
        }
        this.boatLeft = !copy.boatLeft;
    }

    @Override
    /**
     * Creates the neighbors for a crossing config
     * @return A collection of all of the configurations
     */
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> lst = new LinkedList<>();
        if (this.boatLeft) {
            if (this.pupsLeft > 1 && this.wolvesLeft >= 1) {
                lst.add(new CrossingConfig(2, 0, this));
                lst.add(new CrossingConfig(1, 0, this));
                lst.add(new CrossingConfig(0, 1, this));
            }
            else if (this.pupsLeft > 1){
                lst.add(new CrossingConfig(2, 0, this));
                lst.add(new CrossingConfig(1, 0, this));
            }
            else if (this.wolvesLeft >= 1){
                lst.add(new CrossingConfig(0, 1, this));
            }
            else{
                lst.add(new CrossingConfig(1, 0, this));
            }
        }
        else{
            if (this.pupsRight > 1 && this.wolvesRight >= 1){
                lst.add(new CrossingConfig(2, 0, this));
                lst.add(new CrossingConfig(1, 0, this));
                lst.add(new CrossingConfig(0, 1, this));
            }
            else if (this.pupsRight > 1){
                lst.add(new CrossingConfig(2, 0, this));
                lst.add(new CrossingConfig(1, 0, this));
            }
            else if (this.wolvesRight >= 1){
                lst.add(new CrossingConfig(0, 1, this));
            }
            else{
                lst.add(new CrossingConfig(1, 0, this));
            }
        }
        return lst;
    }

    @Override
    /**
     * Checks to see if 2 crossing configs are equal
     * @return T/F value for if the 2 configs are equal
     */
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof CrossingConfig){
            CrossingConfig otherC = (CrossingConfig) other;
            if (this.pupsLeft == otherC.pupsLeft && this.pupsRight == otherC.pupsRight &&
                    this.wolvesLeft == otherC.wolvesLeft && this.wolvesRight == otherC.wolvesRight && this.boatLeft == otherC.boatLeft){
                result = true;
            }
        }
        return result;
    }

    @Override
    /**
     * Creates a hashcode for a crossing config
     * @return The hashcode for a crossing config
     */
    public int hashCode() {
        int hash = 0;
        if (this.boatLeft){
            hash = 1;
        }
        return this.wolvesRight + this.wolvesLeft + this.pupsRight + this.pupsLeft + hash;
    }

    @Override
    /**
     * Creates a string representation for a crossing config
     * @return the string representation of the crossing config
     */
    public String toString() {
        return "left=[" + this.pupsLeft + ", " + this.wolvesLeft + "], right=[" + this.pupsRight + ", " + this.wolvesRight + "]";
    }
}

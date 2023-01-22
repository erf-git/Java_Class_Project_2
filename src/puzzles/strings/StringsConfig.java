package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Class that is responsible for creating and checking all of the different neighbors for the string configurations
 * @Author Jared Hugo
 */
public class StringsConfig implements Configuration {
    /** Start string that will be transformed */
    private static String start;
    /** End string that we want to get to */
    private static String end;
    /** Current string that the configuration is on */
    private String current;
    @Override
    /**
     * Checks to see if configuration is the solution
     * @return T/F value for if config is a solution
     */
    public boolean isSolution() {
        return current.equals(end);
    }

    /**
     * Constructor for the string sconfig
     * @param initStart Start string
     * @param initEnd End string
     */
    public StringsConfig(String initStart, String initEnd){
        start = initStart;
        end = initEnd;
        this.current = start;
    }

    /**
     * Copy constructor for the strings configuration, used in creating neighbors
     * @param copy Copy of the previous configuration
     * @param index index that we want to change the letter for in the string
     * @param direction Direction that we are changing the string, either forward or backward
     */
    public StringsConfig(StringsConfig copy, int index, String direction){
        StringBuilder sb = new StringBuilder(copy.current);
        if (direction.equals("Forward")) {
            if (sb.charAt(index) == 'A') {
                sb.setCharAt(index, 'B');
            } else if (sb.charAt(index) == 'Z') {
                sb.setCharAt(index, 'A');
            } else {
                int asciiValue = (int) sb.charAt(index);
                ++asciiValue;
                char newChar = (char)asciiValue;
                sb.setCharAt(index, newChar);
            }
        }
        else{
            if (sb.charAt(index) == 'A') {
                sb.setCharAt(index, 'Z');
            } else if (sb.charAt(index) == 'Z') {
                sb.setCharAt(index, 'Y');
            } else {
                int asciiValue = (int) sb.charAt(index);
                --asciiValue;
                char newChar = (char)asciiValue;
                sb.setCharAt(index, newChar);
            }
        }
        String newString = sb.toString();
        this.current = newString;

    }
    @Override
    /**
     * Creates neighbors for the current string
     * @return a collection of all of the string configurations
     */
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> lst = new LinkedList<>();
        for (int i = 0; i < start.length(); i++){
            for (int j = 0; j < 2; j++){
                if (j == 0){
                    lst.add(new StringsConfig(this, i, "Forward"));
                }
                else{
                    lst.add(new StringsConfig(this, i, "Backward"));
                }
            }
        }
        return lst;
    }

    @Override
    /**
     * Checks to see if 2 strings are equal
     * @return T?F value for if 2 string configurations are equal
     */
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof StringsConfig){
            StringsConfig otherC = (StringsConfig) other;
            if (this.current.equals(otherC.current)){
                result = true;
            }
        }
        return result;
    }

    @Override
    /**
     * Creates a hashcode for a string configuration
     * @return The hashcode for a string configuration
     */
    public int hashCode() {
        return current.hashCode();
    }

    @Override
    /**
     * Creates the string representation for the string configuration
     * @return The string representation of the string configuration
     */
    public String toString() {
        return current;
    }
}

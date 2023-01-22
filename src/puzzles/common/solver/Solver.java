package puzzles.common.solver;

import java.util.*;

public class Solver {
    /** The total number of configurations that were created */
    private static int totalConfigs = 0;
    /** The total number of unique configurations that were created */
    private static int uniqueConfigs = 0;

    /**
     * Solve method that is used to solve any simple puzzle
     * @param start The starting configuration of the puzzle
     * @return List representation of the path to solution
     */
    public static List<Configuration> solve(Configuration start){

        // Predecessor map and queue
        Map<Configuration,Configuration> predecessor = new HashMap<>();
        Queue<Configuration> toVisit = new LinkedList<>();

        // Adds the starting configuration
        // First node is put in with the parent of "null"
        toVisit.offer(start);
        predecessor.put(start, null);

        // The algorithm itself
        while(!toVisit.isEmpty() && !toVisit.peek().isSolution()){

            // Gets current config
            Configuration current = toVisit.remove();

            for (Configuration config: current.getNeighbors()){
                totalConfigs++;
                if (!predecessor.containsKey(config)){
                    uniqueConfigs++;
                    predecessor.put(config,current);
                    toVisit.offer(config);
                }
            }
        }

        if (toVisit.isEmpty()){
            return null;
        }
        else {
            // Path list
            List<Configuration> path = new LinkedList<>();

            // Adds the goal first
            path.add(0,toVisit.peek());

            // Goes backwards to find the path to the start
            // Once it hits the starting node's "null" parent, it will stop
            Configuration configuration = predecessor.get(toVisit.remove());
            while (configuration != null) {
                path.add(0, configuration);
                configuration = predecessor.get(configuration);
            }
            return path;
        }
    }

    public static int getTotalConfigs() {
        return totalConfigs;
    }

    public static int getUniqueConfigs() {
        return uniqueConfigs;
    }
}

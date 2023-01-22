package puzzles.crossing;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

/**
 * Class for the crossing config that is responsible for putting the answer together and printing out the result
 * @Author Jared Hugo
 */
public class Crossing {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Crossing pups wolves"));
        } else {
            int pups = Integer.parseInt(args[0]);
            int wolves = Integer.parseInt(args[1]);
            CrossingConfig cc = new CrossingConfig(pups, wolves);
            System.out.println("Pups: " + args[0] + ", Wolves: " + args[1]);
            Solver solver = new Solver();
            List<Configuration> solution = solver.solve(cc);
            if (solution != null){
                System.out.println("Total configs: " + solver.getTotalConfigs());
                System.out.println("Unique configs: " + solver.getUniqueConfigs());
                for (int i = 0; i < solution.size(); i++){
                    if (i % 2 == 0){
                        System.out.println("Step " + i + ": " + "(BOAT) " + solution.get(i));
                    }
                    else {
                        System.out.println("Step " + i + ": " + "       " + solution.get(i) + " (BOAT)");
                    }
                }
            }
            else{
                System.out.println("No solution!");
            }
        }
    }
}

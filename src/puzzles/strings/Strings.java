package puzzles.strings;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

/**
 * Class for the strings config that is responsible for putting the answer together and outputting the results
 * @Author Jared Hugo
 */
public class Strings {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Strings start finish"));
        } else {
            StringsConfig sc = new StringsConfig(args[0], args[1]);
            System.out.println("Start: " + args[0] + ", End: " + args[1]);
            Solver solver = new Solver();

            List<Configuration> solution = solver.solve(sc);
            if (solution != null){
                System.out.println("Total configs: " + solver.getTotalConfigs());
                System.out.println("Unique configs: " + solver.getUniqueConfigs());
                for (int i = 0; i < solution.size(); i++){
                    System.out.println("Step " + i + ": " + solution.get(i));
                }
            }
            else{
                System.out.println("No solution!");
            }
        }
    }
}

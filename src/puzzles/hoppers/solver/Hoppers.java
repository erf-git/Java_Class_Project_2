package puzzles.hoppers.solver;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;

import java.io.IOException;
import java.util.*;

/**
 * Class for the hoppers config that is responsible for putting the answer together and outputting the results
 * @Author Jared Hugo
 */
public class Hoppers {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }
        else{
            try {
                HoppersConfig hc = new HoppersConfig(args[0]);
                Solver solver = new Solver();
                List<Configuration> solution = solver.solve(hc);
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
            catch (IOException e){}
        }
    }
}

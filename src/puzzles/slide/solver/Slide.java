package puzzles.slide.solver;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.slide.model.SlideConfig;
import java.io.IOException;
import java.util.Collection;

public class Slide {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: java Slide filename");
        } else {

            // Pass filename to constructor to read initial board
            String fileName = args[0];
            SlideConfig init = new SlideConfig(fileName);
            System.out.println("File: " + fileName);
            System.out.println(init);

            Solver brain = new Solver();
            Iterable<Configuration> answer = brain.solve(init);

            // Checks if there is no solution
            // Else, print steps to goal
            if (answer == null) {
                System.out.println("No solution");
            }
            else {

                System.out.println("Total configs: " + brain.getTotalConfigs());
                System.out.println("Unique configs: "+ brain.getUniqueConfigs());

                int i = 0;
                for (Configuration s : answer) {
                    System.out.println("Step " + i + ": \n" + s);
                    i++;
                }
            }

        }
    }
}

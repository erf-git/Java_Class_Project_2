package puzzles.slide.ptui;

import puzzles.common.Observer;
import puzzles.slide.model.SlideModel;

import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class SlidePTUI implements Observer<SlideModel, String> {
    private SlideModel model;
    private String filename;

    public void init(String filename) throws IOException {
        this.model = new SlideModel(filename);
        this.filename = filename;
        this.model.addObserver(this);

        String name = filename.substring(11);
        System.out.println("Loaded: " + name);
        System.out.println(this.model);

        displayHelp();
    }

    @Override
    public void update(SlideModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );

            if (words.length > 0) {

                if (words[0].startsWith( "q" )) {
                    break;
                }

                else if (words[0].startsWith( "h" )) {
                    if (words.length > 1) {
                        System.out.println("Too many arguments");
                    }
                    else {
                        this.model.solveNextStep();
                    }
                }

                else if (words[0].startsWith( "l" )) {
                    try {

                        // You can either type date/... or just the file name
                        String name = "";
                        if (words[1].contains("data")) {
                            name = words[1].substring(11);
                        }
                        else {
                            name = words[1];
                        }

                        this.model.load(name);

                    } catch (IOException e) {
                        System.out.println("File does not exist");
                    }
                }

                else if (words[0].startsWith( "r" )) {
                    try {
                        this.model.reset();
                    } catch (IOException e) {
                        System.out.println("Issue with loading the filename to reset the model");
                    }
                }

                else if (words[0].startsWith( "s" )) {
                    if (words.length > 3) {
                        System.out.println("Too many arguments");
                    }
                    else {
                        try {
                            this.model.makeSelection(Integer.parseInt(words[1]), Integer.parseInt(words[2]));
                        } catch (Exception e) {
                            System.out.println("Not enough arguments or invalid coordinates");
                        }
                    }
                }

                else {
                    System.out.println("Not recognized command, try again");
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java JamPTUI filename");
        } else {
            try {
                SlidePTUI ptui = new SlidePTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}


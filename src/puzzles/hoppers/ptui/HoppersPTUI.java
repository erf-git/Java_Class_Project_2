package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;
import puzzles.slide.model.SlideModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * ptui class for hoppers puzzle
 * @Author Jared Hugo
 */
public class HoppersPTUI implements Observer<HoppersModel, String> {
    private HoppersModel model;

    @Override
    public void update(HoppersModel model, String msg) {
        System.out.println(msg);
        System.out.println(model);
    }

    public void init(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        String[] wordList = filename.split("/");
        update(model, "Loaded: " + wordList[2]);
        displayHelp();
    }

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    public void run() throws IOException{
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith("q")){
                    break;
                }
                else if (words[0].startsWith("l")){
                    model.load(words[1]);
                }
                else if (words[0].startsWith("s")){
                    if (words.length == 3){
                        model.select(Integer.parseInt(words[1]),Integer.parseInt(words[2]));
                    }
                    else{
                        update(this.model, "Inappropriate number of points chosen!!!");
                    }
                }
                else if (words[0].startsWith("h")){
                    model.hint();
                }
                else if (words[0].startsWith("r")){
                    model.reset();
                }
                else {
                    displayHelp();
                }
            }
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        }
        else{
            HoppersPTUI ptui = new HoppersPTUI();
            try{
                ptui.init(args[0]);
                HoppersModel.setLastRefresh(args[0]);
                ptui.run();
            }
            catch(IOException e){}
        }
    }
}

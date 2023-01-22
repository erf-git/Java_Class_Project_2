package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


/**
 * gui class for hoppers puzzle
 * @Author Jared Hugo
 */
public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";
    /** model that will be used */
    private HoppersModel model;
    private Label Title = new Label();
    /** The main borderpane that contains everything */
    private BorderPane mainPane = new BorderPane();
    /** Red frog picture */
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    /** Green frog picture */
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    /** Lily pad picture */
    private Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    /** Water picture */
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));

    /**
     * Method that will recreate the gridpane after every move
     * @return new gridpane
     */
    private GridPane makeGridPane() {
        GridPane gridPane = new GridPane();
        char[][] grid = model.currentConfig.getGrid();
        for (int row = 0; row < model.currentConfig.getRows(); ++row) {
            for (int col = 0; col < model.currentConfig.getCols(); ++col) {
                PlayerButton button = new PlayerButton(row, col);
                if (grid[row][col] == '.'){
                    button.setGraphic(new ImageView((lilyPad)));
                }
                else if (grid[row][col] == '*') {
                    button.setGraphic(new ImageView(water));
                }
                else if (grid[row][col] == 'R'){
                    button.setGraphic(new ImageView(redFrog));
                }
                else{
                    button.setGraphic(new ImageView(greenFrog));
                }
                button.setOnAction(event -> {this.model.select(button.row, button.col);});
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }

    /**
     * Playerbutton class that is used to create the buttons used in the grid
     */
    private class PlayerButton extends Button{
        private int col;
        private int row;

        private PlayerButton(int row, int col){
            this.col = col;
            this.row = row;

            setStyle("-fx-background-color: #1291E3; -fx-background-radius : 0");
        }
    }

    /**
     * Initialization method for gui
     * @throws IOException
     */
    public void init() throws IOException {
        String filename = getParameters().getRaw().get(0);
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
    }

    @Override
    /**
     * Start method for gui
     */
    public void start(Stage stage) throws Exception {
        GridPane gridPane = makeGridPane();
        mainPane.setCenter(gridPane);

        Button load = new Button("Load");
        load.setOnAction(event -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose puzzle to load!");
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/hoppers"));
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files","*.txt"));
            File selected = chooser.showOpenDialog(stage);
            try{model.load("data/hoppers/" + selected.getName());}catch(IOException e){}
        });

        Button reset = new Button("Reset");
        reset.setOnAction((event -> {try{this.model.reset();} catch(IOException e){}}));

        Button hint = new Button("Hint");
        hint.setOnAction((event -> {this.model.hint();}));

        HBox hbox = new HBox(load, reset, hint);
        hbox.setAlignment(Pos.CENTER);
        mainPane.setBottom(hbox);

        String[] words = HoppersModel.lastRefresh.split("/");
        String text = "Loaded: " + words[2];
        this.Title = new Label();
        this.Title.setText(text);
        FlowPane flowPane = new FlowPane(Title);
        flowPane.setAlignment(Pos.CENTER);
        mainPane.setTop(flowPane);

        Scene scene = new Scene(mainPane);
        stage.setScene(scene);
        stage.setTitle("Hoppers GUI");
        stage.show();
    }

    @Override
    /**
     * Update method for gui
     */
    public void update(HoppersModel hoppersModel, String msg) {
        this.Title.setText(msg);
        mainPane.setCenter(makeGridPane());
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}

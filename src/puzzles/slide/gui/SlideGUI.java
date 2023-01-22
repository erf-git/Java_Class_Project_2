package puzzles.slide.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.common.Observer;
import puzzles.slide.model.SlideModel;

import java.io.File;
import java.io.IOException;

public class SlideGUI extends Application implements Observer<SlideModel, String> {
    private SlideModel model;

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** the font size for labels and buttons */
    private final static int BUTTON_FONT_SIZE = 20;
    private final static int FONT_SIZE = 12;
    private final static int NUMBER_FONT_SIZE = 24;
    /** Colored buttons */
    private final static String EVEN_COLOR = "#ADD8E6";
    private final static String ODD_COLOR = "#FED8B1";
    private final static String EMPTY_COLOR = "#FFFFFF";

    private Stage stage;
    private GridPane grid;
    private Label statusDisplay;
    private Button loadButton;
    private Button resetButton;
    private Button hintButton;

    @Override
    public void init() throws IOException {
        // get the file name from the command line
        String filename = getParameters().getRaw().get(0);
        this.model = new SlideModel(filename);
        this.model.addObserver(this);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // Everything in a boarder pane
        BorderPane pane = new BorderPane();


        // The top text is in a flow pane
        FlowPane top = new FlowPane();
        top.setAlignment(Pos.CENTER);

        statusDisplay = new Label("Loaded: " + getParameters().getRaw().get(0).substring(11));
        top.getChildren().add(statusDisplay);

        // The bottom text is in a flow pane
        FlowPane bottom = new FlowPane();
        bottom.setAlignment(Pos.CENTER);

        // Loads files
        loadButton = new Button("Load");
        loadButton.setOnAction(event -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Slides File");

            // Sets directory and only selects .txt files
            fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")+"/data/slide"));
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("TXT", "*.txt"));

            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {

                try {
                    this.model.load(selectedFile.getName());

                } catch (IOException e) {
                    statusDisplay.setText("Invalid file selected");

                }
            }
        });

        // Resets the puzzle
        resetButton = new Button("Reset");
        resetButton.setOnAction(event -> {
            try {
                this.model.reset();
            } catch (IOException e) {
                statusDisplay.setText("Failed puzzle reset");
            }
        });

        // Solves the next step or tells you there is no solution
        hintButton = new Button("Hint");
        hintButton.setOnAction(event -> {
            try {
                this.model.solveNextStep();
            } catch (Exception e) {
                statusDisplay.setText("Failed puzzle hint");
            }
        });

        bottom.getChildren().addAll(loadButton, resetButton, hintButton);

        // The board is in a grid pane
        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);

        // Builds the grid
        refresh(this.model);

        pane.setTop(top);
        pane.setCenter(grid);
        pane.setBottom(bottom);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Slides GUI");
        stage.show();
    }

    /**
     * Refreshes the grid by building it over again
     *
     * @param model
     */
    private void refresh(SlideModel model) {

        // Clears the game grid
        grid.getChildren().clear();
        grid.setAlignment(Pos.CENTER);

        // Adds buttons and sets their images
        for (int r=0; r<model.getCurrentConfig().getROWS(); r++) {
            for (int c=0; c<model.getCurrentConfig().getCOLS(); c++) {

                int n = model.getCurrentConfig().getGrid()[r][c];

                Cell button = new Cell(n,r,c);

                if (n==0) {
                    // Empty spot
                    button.setText(".");
                    button.setStyle(
                            "-fx-font-family: Arial;" +
                                    "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                    "-fx-background-color: " + EMPTY_COLOR + ";" +
                                    "-fx-font-weight: bold;");

                } else {
                    button.setText(String.valueOf(n));

                    if (n%2 == 0) {
                        // Even number
                        button.setStyle(
                                "-fx-font-family: Arial;" +
                                        "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                        "-fx-background-color: " + EVEN_COLOR + ";" +
                                        "-fx-font-weight: bold;");

                    } else {
                        // Odd number
                        button.setStyle(
                                "-fx-font-family: Arial;" +
                                        "-fx-font-size: " + BUTTON_FONT_SIZE + ";" +
                                        "-fx-background-color: " + ODD_COLOR + ";" +
                                        "-fx-font-weight: bold;");
                    }
                }

                // More formating
                button.setMinSize(ICON_SIZE, ICON_SIZE);
                button.setMaxSize(ICON_SIZE, ICON_SIZE);

                // Gives the model.makeSelection() action to the buttons
                int finalR = r;
                int finalC = c;
                button.setOnAction(event -> {
                    this.model.makeSelection(finalR, finalC);
                });

                // It will disable all buttons if the game has ended isSolution()
                if (model.getCurrentConfig().isSolution()) {
                    button.setDisable(true);
                }

                grid.add(button, c, r);
            }
        }
    }

    @Override
    public void update(SlideModel model, String data) {
        // for demonstration purposes
        //System.out.println(data);
        //System.out.println(model);

        // Sets status
        statusDisplay.setText(data);

        // Refreshes the grid
        if ( Platform.isFxApplicationThread() ) {
            this.refresh(model);
        }
        else {
            Platform.runLater( () -> this.refresh(model) );
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}

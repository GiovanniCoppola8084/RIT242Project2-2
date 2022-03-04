package puzzles.tipover.gui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.tipover.model.TipOverModel;
import util.Observer;

import java.io.File;
import java.util.ArrayList;

/**
 * This is the class that will use the model in order to make a usable GUI to play the tipOver game
 *
 * @author Giovanni Coppola
 * November 2021
 */
public class TipOverGUI extends Application
        implements Observer< TipOverModel, Object > {

    // Private state variables that will be used to make the scene and stage, and the variables to change them
    private ArrayList<Button> integerButtons = new ArrayList<>();
    private TipOverModel model;
    private Label topLabel;
    private BorderPane mainBorderPane;
    private BorderPane insideBorderPane;
    private BorderPane moveBorderPane;
    private GridPane gridPane;

    @Override
    public void init() {
        String[] line = new String[1];
        if (getParameters().getRaw().size() != 0) {
            // If there are command line arguments, then make a model based on what was passed in
            line[0] = getParameters().getRaw().get(0);
            this.model = new TipOverModel(line);
            topLabel = new Label("New file loaded");
        } else {
            // Otherwise, just make an empty config
            this.model = new TipOverModel(new String[0]);
            topLabel = new Label("Please load a file");
        }
        // Add an observer to the model
        this.model.addObserver(this);
    }

    /**
     * Method that will make the gridPane, used in both start and update
     *
     * @return - the gridPane made from the model grid
     */
    public GridPane makeGridPane() {
        if (!model.isConfigNull()) {
            gridPane = new GridPane();
            // if the model is valid, then loop through each element and add it as a button to the button list
            for (int row = 0; row < model.getNumberOfRows(); row++) {
                for (int col = 0; col < model.getNumberOfColumns(); col++) {
                    Button button = new Button(String.valueOf(model.getGridArray()[row][col]));
                    if (row == model.getTipper().row() && col == model.getTipper().col()) {
                        // Make the tipper blue
                        button.setStyle("-fx-background-color: #0000FF; ");
                    } else if (row == model.getGoalCrate().row() && col == model.getGoalCrate().col()) {
                        // Make the goal red
                        button.setStyle("-fx-background-color: #ff0000; ");
                    } else {
                        // Make every other number white
                        button.setStyle("-fx-background-color: #ffffff; ");
                    }
                    // Add the button to the button list and the gridPane
                    integerButtons.add(button);
                    gridPane.add(button, col, row);
                }
            }

            // Return the gridPane that was made
            return gridPane;
        } else {
            // Return an empty gridPane if the model was empty
            return null;
        }
    }

    /**
     * Method that will run and start the GUI
     *
     * @param stage - the stage for the GUI
     */
    @Override
    public void start( Stage stage ) {
        // Set all the Panes and boxes to new Panes and boxes
        mainBorderPane = new BorderPane();
        moveBorderPane = new BorderPane();
        insideBorderPane = new BorderPane();
        VBox rightBox = new VBox();

        if (model == null) {
            // Set the main borderPane center to null if the model is null
            mainBorderPane.setCenter(null);
        } else {
            // Otherwise, make the new gridPane and pass it to the borderPane
            mainBorderPane.setTop(topLabel);
            mainBorderPane.setCenter(makeGridPane());
        }

        // Create the button for load, which will include a file chooser for the user
        Button load = new Button("LOAD");
        FileChooser fileChooser = new FileChooser();
        load.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // Tell the user to select a file
                fileChooser.setTitle("Please select input file");
                // Pass in the path to select the file from
                fileChooser.setInitialDirectory(new File("./data/tipover"));
                // Set the file to what the user picked
                File file = fileChooser.showOpenDialog(stage);
                if (file != null) {
                    // Load the model passed on the file passed in
                    model.load(file.getPath());
                }
            }
        });
        rightBox.getChildren().add(load);

        // Set the reload button and call the reload method to do this
        Button reload = new Button("RELOAD");
        reload.setOnAction(event -> {this.model.reload();});
        rightBox.getChildren().add(reload);

        // Set the hint button and call the hint method to do this
        Button hint = new Button("HINT");
        hint.setOnAction(event -> {this.model.hint();});
        rightBox.getChildren().add(hint);

        // Set the right side of the inside border pane to the load, reload,and hint buttons
        insideBorderPane.setCenter(rightBox);

        // Set the north button by making the tipper move north
        Button moveNorth = new Button("North");
        moveNorth.setOnAction(event -> {this.model.moveTipper("north");});
        moveBorderPane.setTop(moveNorth);
        BorderPane.setAlignment(moveNorth, Pos.CENTER);

        // Set the south button by making the tipper move south
        Button moveSouth = new Button("South");
        moveSouth.setOnAction(event -> {this.model.moveTipper("south");});
        moveBorderPane.setBottom(moveSouth);
        BorderPane.setAlignment(moveSouth, Pos.CENTER);

        // Set the east button by making the tipper move east
        Button moveEast = new Button(" East");
        moveEast.setOnAction(event -> {this.model.moveTipper("east");});
        moveBorderPane.setRight(moveEast);

        // Set the west button by making the tipper move west
        Button moveWest = new Button("West ");
        moveWest.setOnAction(event -> {this.model.moveTipper("west");});
        moveBorderPane.setLeft(moveWest);

        Button centerButton = new Button("     ");
        centerButton.setStyle("-fx-background-color: #ffffff; ");
        moveBorderPane.setCenter(centerButton);

        // Pass the movement buttons into the insideBorderPane
        insideBorderPane.setTop(moveBorderPane);

        // Pass the insideBorderPane to the mainBorderPane
        mainBorderPane.setRight(insideBorderPane);

        // Set the title of the stage, make the scene, and show the stage
        stage.setTitle( "Tip Over" );
        Scene scene = new Scene( mainBorderPane, 300, 250 );
        stage.setScene( scene );
        stage.show();
    }

    @Override
    public void update( TipOverModel tipOverModel, Object arg ) {
        // Create a new gridPane of the model and pass it into the mainBorderPane
        gridPane = makeGridPane();
        mainBorderPane.setCenter(gridPane);

        // Set the label based on what the model announced
        topLabel.setText((String) arg);
    }

    public static void main( String[] args ) {
        Application.launch( args );
    }
}

package puzzles.lunarlanding.gui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import puzzles.lunarlanding.model.*;
import java.io.FileNotFoundException;
import java.util.*;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import puzzles.lunarlanding.model.LunarLandingModel;
import util.Observer;

import java.io.File;

/**
 * The GUI that will allow the play of lunar landing
 * @author Romant Bhattarai
 * November 2021
 */
public class LunarLandingGUI extends Application
       implements Observer< LunarLandingModel, Object > {

    // state variables for the GUI
    private LunarLandingModel model;
    private GridPane board;
    private BorderPane window;
    private Label announcement;
    private String file;

    private Image explorer = new Image((getClass().getResourceAsStream("resources/explorer.png")));
    private Image lander = new Image((getClass().getResourceAsStream("resources/lander.png")));
    private Image robotBlue = new Image((getClass().getResourceAsStream("resources/robot-blue.png")));
    private Image robotGreen = new Image((getClass().getResourceAsStream("resources/robot-green.png")));
    private Image robotOrange = new Image((getClass().getResourceAsStream("resources/robot-orange.png")));
    private Image robotPink = new Image((getClass().getResourceAsStream("resources/robot-pink.png")));
    private Image robotWhite = new Image((getClass().getResourceAsStream("resources/robot-white.png")));
    private Image robotYellow = new Image((getClass().getResourceAsStream("resources/robot-yellow.png")));
    private Image robotlightBlue = new Image((getClass().getResourceAsStream("resources/robot-lightblue.png")));

    /**
     * initializes things before the GUI runs
     * @throws FileNotFoundException
     */
    @Override
    public void init() throws FileNotFoundException {
        List<String> args = getParameters().getRaw();
        this.model = new LunarLandingModel(args.get(0));
        this.model.addObserver(this);
        this.file = args.get(0);
        announcement = new Label ("Going ON");
    }

    /**
     * Method that runs
     * @param stage: the stage
     */
    @Override
    public void start( Stage stage ) {
        // Initialize the window
        window = new BorderPane();
        //Create a gridPane for the board
        board = makeBoard();
        //Create a grid pane for the direction keys
        GridPane directionKeys = makeDirectionArrows();
        //Create a new VBox for LoadNew, Reload, and hint buttons
        VBox vbox = new VBox();

        // Create the load new button
        Button loadNew = new Button("LOAD NEW");
        // Create a new file chooser
        FileChooser fChooser = new FileChooser();
        // action event for the button
        loadNew.setOnAction(new EventHandler<>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // prompt to select a file
                fChooser.setTitle("SELECT A FILE");
                // the path for the file
                fChooser.setInitialDirectory(new File("./data/lunarlanding"));
                // set the file to what the user picked
                File file = fChooser.showOpenDialog(stage);
                try {
                    // load that file to create the model
                    model.load(file.getPath());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        // Create the reload button
        Button reload = new Button("RELOAD");
        reload.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // reload the model
                    model.reload();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        // Create the hint button
        Button hint = new Button("HINT");
        hint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // generate the next step
                model.hint();
            }
        });

        // add the buttons to the vbox
        vbox.getChildren().addAll(reload, loadNew, hint);



        // Create a border Pane where the board sits in the center
        window.setTop(announcement);
        BorderPane.setAlignment(announcement, Pos.CENTER);
        window.setCenter(board);
        board.setAlignment(Pos.CENTER);
        window.setLeft(vbox);  // Add the option buttons to the left of the border pane
        window.setBottom(directionKeys);
        directionKeys.setAlignment(Pos.CENTER);


        // Create a Scene with the border pane in it
        Scene scene = new Scene(window, 535, 600);
        stage.setTitle(file);  // Set the title
        stage.setScene(scene);

        stage.show();
    }

    /**
     * creates a board for the current model and returns it
     * @return the board
     */
    public GridPane makeBoard() {
        List<Figures> x = new ArrayList<>(this.model.getFiguresList());
        board = new GridPane();
        for (int i = 0; i < model.getRows(); i++) {
                for (int j = 0; j < model.getColumns(); j++) {
                    Image image = null;
                    for (Figures figure : x) {
                        if (i == figure.getRow() && j == figure.getCol()) {
                            if (figure.getName().equals("E")) {
                                image = explorer;
                            }
                            else if (figure.getName().equals("B")) {
                                image = robotBlue;;
                            }
                            else if (figure.getName().equals("G")) {
                                image = robotGreen;
                            }
                            else if (figure.getName().equals("O")) {
                                image = robotOrange;
                            }
                            else if (figure.getName().equals("P")) {
                                image = robotPink;
                            }
                            else if (figure.getName().equals("W")) {
                                image = robotWhite;
                            }
                            else if (figure.getName().equals("Y")) {
                                image = robotYellow;
                            }
                            else {
                                image = robotlightBlue;
                            }
                        }
                        else if (i == model.getGoalRow() && j == model.getGoalColumn()) {
                            image = lander;
                        }
                    }
                    if (image != null) {
                        Button button = new Button();
                        ImageView imageView  = new ImageView(image);
                        button.setGraphic(imageView);
                        int finalI = i;
                        int finalJ = j;
                        button.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                // pass in the row and column to the model
                                model.choose(finalI, finalJ);
                            }
                        });
                        button.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        board.add(button, j, i);
                    }
                    else {
                        Button button = new Button();
                        button.setPrefHeight(83);
                        button.setPrefWidth(91);
                        button.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                        board.add(button, j, i);
                    }
                }
            }
        return board;
    }

    /**
     * creates a grid of direction keys and returns it
     * @return grid of direction buttons
     */
    private GridPane makeDirectionArrows() {
        GridPane directionKeys = new GridPane();
        Image arrow = new Image((getClass().getResourceAsStream("resources/LeftArrow.png")));

        // Create a button and insert left arrow image on it
        Button arrowLeft = new Button();
        ImageView leftArrowView = new ImageView(arrow);
        leftArrowView.setFitWidth(50);
        leftArrowView.setFitHeight(50);
        arrowLeft.setGraphic(leftArrowView);
        arrowLeft.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.go("LEFT");
            }
        });

        // Create the right arrow button
        Button arrowRight = new Button();
        ImageView rightArrowView = new ImageView(arrow);
        rightArrowView.setFitWidth(50);
        rightArrowView.setFitHeight(50);
        rightArrowView.setRotate(180);
        arrowRight.setGraphic(rightArrowView);
        arrowRight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.go("RIGHT");
            }
        });


        // Create the up arrow button
        Button arrowUp = new Button();
        ImageView upArrowView = new ImageView(arrow);
        upArrowView.setFitWidth(50);
        upArrowView.setFitHeight(50);
        upArrowView.setRotate(90);
        arrowUp.setGraphic(upArrowView);
        arrowUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.go("UP");
            }
        });


        // Create the down arrow button
        Button arrowDown = new Button();
        ImageView downArrowView = new ImageView(arrow);
        downArrowView.setFitWidth(50);
        downArrowView.setFitHeight(50);
        downArrowView.setRotate(-90);
        arrowDown.setGraphic(downArrowView);
        arrowDown.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.go("DOWN");
            }
        });

        // add the created buttons to the directionKeys
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i == 0 && j == 1) {
                    directionKeys.add(arrowUp, j, i);
                }
                else if (i == 1 && j == 0) {
                    directionKeys.add(arrowLeft, j, i);
                }
                else if (i == 1 && j == 2) {
                    directionKeys.add(arrowRight, j, i);
                }
                else if (i == 2 && j == 1) {
                    directionKeys.add(arrowDown, j, i);
                }
            }
        }

        return directionKeys;
    }

    @Override
    public void update( LunarLandingModel lunarLandingModel, Object o ) {
        board = makeBoard();
        window.setCenter(board);
        announcement.setText((String) o);
    }

    public static void main( String[] args ) throws FileNotFoundException {
        Application.launch( args );

    }
}

package puzzles.tipover.model;

import solver.Configuration;
import solver.Solver;
import util.Coordinates;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * This class will represent the model of the config that will be used for both the PTUI and GUI.
 * It will include helper methods that will be used by the PTUI and GUi as well.
 * The methods include move, load, reload, show, hint, and quit. Some methods will only be used by the PTUI and
 *      and not the GUI.
 *
 * @author Giovanni Coppola
 * November 2021
 */
public class TipOverModel {

    // Private state variables for the model including the list of observers, the current config, and the filename
    private List<Observer< TipOverModel, Object>> observers;
    private TipOverConfig currentConfig;
    private String filename;

    /**
     * Constructor for the tipover model. This will set the config based on the args pass in
     *
     * @param args - command line arguments
     */
    public TipOverModel(String[] args) {
        this.observers = new LinkedList<>();
        this.currentConfig = null;

        if (args.length >= 1) {
            this.filename = args[0];
            this.currentConfig = loadConfig(filename);
        } else {
            this.currentConfig = null;
        }
    }

    /**
     * This method will load all the data in from the input file (filename passed in) and return the config made
     *
     * @param filename - the String representing the filename
     * @return - the loaded config
     */
    public TipOverConfig loadConfig (String filename) {
        try {
            // Set the input file and scanner
            File inputFile = new File(filename);
            Scanner scanner = new Scanner(inputFile);

            // Scan in the first line of data to represent the current grid state
            int numOfRows = scanner.nextInt();
            int numOfCols = scanner.nextInt();
            int tipperRow = scanner.nextInt();
            int tipperCol = scanner.nextInt();
            int goalRow = scanner.nextInt();
            int goalCol = scanner.nextInt();

            // Set the coordinates of the tipper and goal
            Coordinates tipperPosition = new Coordinates(tipperRow, tipperCol);
            Coordinates goalPosition = new Coordinates(goalRow, goalCol);

            // Create the current array for the game board
            int[][] gameBoardArray = new int[numOfRows][numOfCols];
            int row = 0;
            int col = 0;
            // Set the integers for the board by scanning in the next integer from the input file until there are
            //      none left
            while (scanner.hasNextInt()) {
                gameBoardArray[row][col] = scanner.nextInt();
                col++;
                if (col >= numOfCols) {
                    col = 0;
                    row++;
                }
            }

            // Create a new instance of the TipOver class and call the solver on it to find the solution by BFS
            return new TipOverConfig(numOfRows, numOfCols, gameBoardArray, tipperPosition, goalPosition);
        } catch (FileNotFoundException ignored) {

        }
        return null;
    }

    public boolean isConfigNull() {
        return currentConfig == null;
    }

    //Accessor methods for the model
    public String getGridString() {
        if (currentConfig != null) {
            return currentConfig.toString();
        } else {
            return "No file loaded.";
        }
    }

    public int[][] getGridArray() {
        if (currentConfig != null) {
            return currentConfig.getGameBoard();
        } else {
            return null;
        }
    }

    public int getNumberOfRows() {
        return currentConfig.getNumberOfRows();
    }

    public int getNumberOfColumns() {
        return currentConfig.getNumberOfColumns();
    }

    public Coordinates getTipper() {
        return currentConfig.getTipper();
    }

    public Coordinates getGoalCrate() {
        return currentConfig.getGoalCrate();
    }

    public List<Configuration> getCheatList() {
        Configuration tipOverSolver = new TipOverConfig(currentConfig);
        Solver solve = new Solver(tipOverSolver);
        return solve.solver();
    }

    /**
     * Method to move the tipper based on what direction the user chooses to move it
     *
     * @param direction - the direction to move the tipper in
     */
    public void moveTipper(String direction) {
        if (currentConfig != null) {
            // Move the tipper in the direction that was input by the user
            String message = currentConfig.moveTipper(direction);
            if (currentConfig.isSolution()) {
                announce("YOU WIN!");
            } else {
                announce(message);
            }
        } else {
            // Announce no file was loaded if there was none
            announce("No file loaded.");
        }
    }

    /**
     * Method to reload the file that was previously used, if there was one already made
     */
    public void reload() {
        if (currentConfig != null) {
            // Only load the file if there was one previously present
            this.currentConfig = loadConfig(filename);
            announce("File has been reloaded.");
        } else {
            announce("No file loaded.");
        }
    }

    /**
     * Method to load a new config based on a new file that was input
     *
     * @param filename
     */
    public void load(String filename) {
        this.filename = filename;
        // Create a tempConfig to hold the value of the possible new config
        TipOverConfig tempConfig = loadConfig(this.filename);
        if (tempConfig != null) {
            // Create a valid config if a valid file was presented
            this.currentConfig = tempConfig;
            announce("New file loaded.");
        } else {
            announce("Invalid file loaded.");
        }
    }

    /**
     * Method to show the grid (used only by the PTUI)
     */
    public void show() {
        announce("");
    }

    /**
     * Method to show the user the next move for the tipper
     */
    public void hint() {
        List<Configuration> cheatList = this.getCheatList();
        if (cheatList.size() == 1) {
            // If there is only one nod ein the cheat list, then announce that it is the solution
            announce("Already solved.");
        } else if (cheatList.isEmpty()) {
            // If there were no elements found, announce that there is no solution
            announce("No solution.");
        } else {
            // Set the new current config to the next step in the cheat list
            currentConfig = (TipOverConfig) cheatList.get(1);
            if (cheatList.size() == 2) {
                announce("You win!");
            } else {
                announce("");
            }
        }
    }

    public void addObserver(Observer<TipOverModel, Object> obs) {
        this.observers.add(obs);
    }

    private void announce(String arg) {
        for (var obs : this.observers) {
            obs.update(this, arg);
        }
    }
}

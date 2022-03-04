package puzzles.lunarlanding.model;

import puzzles.lunarlanding.LunarLanding;
import solver.Configuration;
import solver.Solver;
import util.Observer;

import java.util.LinkedList;
import java.util.*;
import java.io.*;

/**
 * This class represents the model for GUI and the PTUI.
 * @author Romant Bhattarai
 * November 2021
 */
public class LunarLandingModel {

    // state variables for the model
    private LunarLandingConfig currentConfig;
    private String file;
    private List<Observer<LunarLandingModel, Object>> observers;
    private int row;
    private int col;

    /**
     * Takes in a file name and creates a new LunarLandingModel
     * @param fileName: the file name to build the model from
     * @throws FileNotFoundException
     */
    public LunarLandingModel(String fileName) throws FileNotFoundException {
        this.observers = new LinkedList<>();
        this.file = fileName;
        this.currentConfig = loadLunarLander(this.file);
    }

    /**
     * Creates a new LunarLandingConfig from a file
     * @param fileName: the file name to load from
     * @return LunarLandingConfig
     * @throws FileNotFoundException
     */
    public LunarLandingConfig loadLunarLander(String fileName) throws FileNotFoundException {
        return new LunarLandingConfig(fileName);
    }

    /**
     * takes in a file name and loads a new configuration
     * @param fileName: the file name to load from
     * @throws FileNotFoundException
     */
    public void load(String fileName) throws FileNotFoundException {
        this.file = fileName;
        LunarLandingConfig lunar = new LunarLandingConfig(fileName);
        this.currentConfig = lunar;
        announce("File loaded");
    }

    /**
     * reloads the current configuration
     * @throws FileNotFoundException
     */
    public void reload() throws FileNotFoundException {
        this.currentConfig = loadLunarLander(this.file);
        announce("File reloaded");
    }

    /**
     * takes in two integer values and sets them as the row and column
     * @param row: the row of the current figure chosen
     * @param col: the column of the current figure chosen
     */
    public void choose(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * takes in a direction and moves a figure on the board toward that direction
     * @param direction: the direction to move toward
     */
    public void go(String direction) {
        String name = "";
        // iterate through the list of figures on the board
        for (Figures figure : this.currentConfig.getFigures()) {
            // grab the name of the figure from the board with the selected row and column
            if (figure.getRow() == this.row && figure.getCol() == this.col) {
                name = figure.getName();
                break;
            }
        }
        // create a new figure with the name and row and column
        Figures fig = new Figures(name, this.row, this.col);

        // move the figure to that direction
        this.currentConfig.moveFigure(fig, direction);
        if (currentConfig.isSolution()) {
            announce("WINNER");
        }
        else {
            announce("Game in Progress");
        }
    }

    /**
     * shows the next move to reach the goal
     */
    public void hint() {
        Configuration lunar = new LunarLandingConfig(this.currentConfig);
        Solver lunarSolver = new Solver(lunar);
        List<Configuration> path = lunarSolver.solver();
        if (path.size() > 0) {
            this.currentConfig = (LunarLandingConfig) path.get(1);
            if (currentConfig.isSolution()) {
                announce ("WINNER");
            }
            else {
                announce ("");
            }
        }
        else {
            announce ("CANNOT BE SOLVED");
        }
    }

    /**
     * for the PTUI to get the string representation of the current config
     * @return the current config
     */
    public String getBoard() {
        return this.currentConfig.toString();
    }

    /**
     * method to show the board for the PTUI
     */
    public void show() {
        announce("");
    }

    /**
     * returns a list of all the figures on the board
     * @return the list of figures on the board
     */
    public List<Figures> getFiguresList() {
        return this.currentConfig.getFigures();
    }

    /**
     * returns the number of rows on the board
     * @return the number of rows on the board
     */
    public int getRows() {
        return this.currentConfig.getNumRows();
    }

    /**
     * returns the number of columns on the board
     * @return the number of columns on the board
     */
    public int getColumns() {
        return this.currentConfig.getNumColumns();
    }

    /**
     * returns the goal row
     * @return the goal row
     */
    public int getGoalRow() {
        return this.currentConfig.getGoalRow();
    }

    /**
     * returns the goal column
     * @return the goal column
     */
    public int getGoalColumn() {
        return this.currentConfig.getGoalColumn();
    }

    public void addObserver(Observer<LunarLandingModel, Object> observer) {
        this.observers.add(observer);
    }

    private void announce(String arg) {
        for (var obs : this.observers) {
            obs.update(this, arg);
        }
    }
}

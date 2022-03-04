package puzzles.lunarlanding.model;

import solver.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

/**
 * This is a class that represents a LunarLandingConfiguration
 * @author Romant Bhattarai
 * November 2021
 */
public class LunarLandingConfig implements Configuration {
    private static int numRows;    // The number of rows on the board
    private static int numColumns; // The number of columns on the board
    private static int goalRow;    // The goalRow for the lunar landing
    private static int goalColumn; // The goalColumn for the lunar landing
    final static String emptyStr = ""; // An empty String
    private List<Figures> figures = new ArrayList<>(); // List of all the figures

    /**
     * takes in an input file and reads line by line and creates a LunarLandingConfig
     * @param filename: the file to read from
     * @throws FileNotFoundException
     */
    public LunarLandingConfig(String filename) throws FileNotFoundException {
        // create a scanner tied to the input file
        Scanner in = new Scanner(new File(filename));

        // read the number of rows on the board
        numRows = in.nextInt();

        // read the number of columns on the board
        numColumns = in.nextInt();

        // read the goal row
        goalRow = in.nextInt();

        //read the goal column
        goalColumn = in.nextInt();

        in.nextLine();
        // read till the end of the file
        while(in.hasNextLine()) {
            String line = in.nextLine();
            //exit out of the loop if an empty line is encountered
            if (line.equals(emptyStr)) {
                break;
            }
            String lineSplit[] = line.split(" ");
            String letter = lineSplit[0];
            int rowPos = Integer.parseInt(lineSplit[1]);
            int colPos = Integer.parseInt(lineSplit[2]);
            // Create a new figure with the arguments
            Figures figure = new Figures (letter, rowPos, colPos);
            // add figure to the list of figures
            this.figures.add(figure);
        }

        // close the scanner/file
        in.close();
    }

    /**
     * This constructor takes in a list of Figures and creates a new LunarLandingConfig
     * @param figures: The list of Figures
     */
    public LunarLandingConfig (List<Figures> figures) {
        this.figures = new ArrayList<>(figures);
    }

    /**
     * This constructor takes in another LunarLandingConfiguration and makes a new copy of that config
     * @param other: The LunarLandingConfig to make a copy of
     */
    public LunarLandingConfig(LunarLandingConfig other) {
        this.figures = new ArrayList<>(other.figures);
    }

    /**
     * gets all the neighbors and returns the list of neighbors
     * @return: list of all LunarLandingConfigurations that are neighbors of the current config
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        neighbors.addAll(getRightNeighbors());
        neighbors.addAll(getLeftNeighbors());
        neighbors.addAll(getUpNeighbors());
        neighbors.addAll(getDownNeighbors());
        return neighbors;

    }

    /**
     * returns a list of Configurations that can be created by moving all pieces on the current board to the right
     * @return: list of all the right neighbors
     */
    public List<Configuration> getRightNeighbors() {
        List <Configuration> rightNeighbors = new ArrayList<>();

        // Iterate through the list of figures
        for (int i = 0; i < this.figures.size(); i++) {
            // Copy the contents of current list to a temp list
            List <Figures> x = new ArrayList<>(this.figures);
            // Create an empty list of Integers
            List <Integer> y = new ArrayList<>();
            // Iterate through the list of figures again
            for (int j = 0; j < this.figures.size(); j++) {
                //If there are other figures in the list in the same row with greater columns
                if (i != j && x.get(i).getRow() == x.get(j).getRow() && x.get(i).getCol() < x.get(j).getCol()) {
                    // add those columns to the list of Integers
                    y.add(x.get(j).getCol());
                }
            }
            // if there is something in the integer list
            if (y.size() > 0) {
                // Sort the list of columns that are greater than the column of the current figure
                Collections.sort(y);
                // if the smallest column in list y is greater than the column of the figure in the ith index of list x by 2 or more, then
                if (y.get(0) - x.get(i).getCol() > 1) {
                    // Create a new copy figure of the figure in index i of list x
                    Figures a = new Figures(x.get(i).getName(), x.get(i).getRow(), x.get(i).getCol());
                    // set the column to 1 less than the smallest column list y
                    a.setCol(y.get(0) - 1);

                    // replace the old figure in index i of list x with the new figure with updated column
                    x.set(i, a);

                    // Create a new LunarLandingConfig by passing in list x and add the new config to the rightNeighbors list
                    rightNeighbors.add(new LunarLandingConfig(x));
                }
            }
        }
        return rightNeighbors;
    }

    /**
     * returns a list of Configurations that can be created by moving all pieces on the current board to the left
     * @return: list of all the left neighbors
     */
    public List<Configuration> getLeftNeighbors() {
        List <Configuration> leftNeighbors = new ArrayList<>();

        // Iterate through the list of figures
        for (int i = 0; i < this.figures.size(); i++) {
            // Copy the contents of current list to a temp list
            List <Figures> x = new ArrayList<>(this.figures);
            // Create an empty list of Integers
            List <Integer> y = new ArrayList<>();
            // Iterate through the list of figures again
            for (int j = 0; j < this.figures.size(); j++) {
                // If there are other figures in the list with same row and smaller columns
                if (i != j && x.get(i).getRow() == x.get(j).getRow() && x.get(i).getCol() > x.get(j).getCol()) {
                    // add those columns to the list of Integers
                    y.add(x.get(j).getCol());
                }
            }
            // if there is something in the integer list
            if (y.size() > 0) {
                // Sort the list of columns that are smaller than the column of the current figure
                Collections.sort(y);
                // if the largest column in the list y is smaller than the column of the figure in index i of list x by 2 or more, then
                if (x.get(i).getCol() - y.get(y.size() - 1) > 1) {
                    // Create a new copy of figure of the figure in index i of list x
                    Figures a = new Figures(x.get(i).getName(), x.get(i).getRow(), x.get(i).getCol());
                    // set the column to 1 more than the largest column in list y
                    a.setCol(y.get(y.size() - 1) + 1);
                    // replace the old figure in index i of list with the new figure with updated column
                    x.set(i, a);
                    // Create a new LunarLandingConfig by passing in list x and add the new config to the leftNeighbors list
                    leftNeighbors.add(new LunarLandingConfig(x));
                }
            }
        }
        return leftNeighbors;
    }

    /**
     * returns a list of Configurations that can be created by moving all pieces on the current board upward
     * @return: list of all the up neighbors
     */
    public List<Configuration> getUpNeighbors() {
        List <Configuration> upNeighbors = new ArrayList<>();

        // Iterate through the list of figures
        for (int i = 0; i < this.figures.size(); i++) {
            // Copy the contents of current list to a temp list
            List<Figures> x = new ArrayList<>(this.figures);
            // Create an empty list of Integers
            List<Integer> y = new ArrayList<>();
            // Iterate through the list of figures again
            for (int j = 0; j < this.figures.size(); j++) {
                // If there are other figures in the list with same column and smaller rows
                if (j != i && x.get(i).getCol() == x.get(j).getCol() && x.get(i).getRow() > x.get(j).getRow()) {
                    // add those rows to the list of Integers
                    y.add(x.get(j).getRow());
                }
            }
            // if there is something in the integer list of rows
            if (y.size() > 0) {
                // Sort the list of rows that are smaller than the row of the current figure
                Collections.sort(y);
                // if the largest row in list y is smaller than the row of the current figure by 2
                if (x.get(i).getRow() - y.get(y.size() -1) > 1) {
                    // Create a new copy of the current figure
                    Figures a = new Figures(x.get(i).getName(), x.get(i).getRow(), x.get(i).getCol());
                    // set the row to 1 more than the largest row in the list
                    a.setRow(y.get(y.size() - 1) + 1);
                    // replace the current figure with the newly created figure with the updated row
                    x.set(i, a);
                    // Create a new LunarLandingConfig by passing in list x and add the new config to the upNeighbors list
                    upNeighbors.add(new LunarLandingConfig(x));
                }
            }
        }
        return upNeighbors;
    }

    /**
     * returns a list of Configurations that can be created by moving all pieces on the current board downward
     * @return: list of all the down Neighbors
     */
    public List<Configuration> getDownNeighbors() {
        List <Configuration> downNeighbors = new ArrayList<>();

        // Iterate through the list of figures
        for (int i = 0; i < this.figures.size(); i++) {
            // Copy the contents of current list to a temp list
            List<Figures> x = new ArrayList<>(this.figures);
            // Create an empty list of Integers
            List<Integer> y = new ArrayList<>();
            // Iterate through the list of figures again
            for (int j = 0; j < this.figures.size(); j++) {
                // If there are other figures in the list with same column and greater rows
                if (j != i && x.get(i).getCol() == x.get(j).getCol() && x.get(i).getRow() < x.get(j).getRow()) {
                    // add those rows to the list of Integers
                    y.add(x.get(j).getRow());
                }
            }
            // if there is something in the Integer list of rows
            if (y.size() > 0) {
                // Sort the list of rows
                Collections.sort(y);
                // If the smallest row in the list y is greater than the row of the current figure by 2
                if (y.get(0) - x.get(i).getRow() > 1) {
                    // Create a new copy of the current figure
                    Figures a = new Figures(x.get(i).getName(), x.get(i).getRow(), x.get(i).getCol());
                    // set the row to 1 less than the smallest row in the list
                    a.setRow(y.get(0) - 1);
                    // replace the current figure with the newly created figure with updated row
                    x.set(i, a);
                    // Create a new LunarLandingConfig by passing in list x and add the new config to the downNeighbors
                    downNeighbors.add(new LunarLandingConfig(x));
                }
            }
        }
        return downNeighbors;
    }

    /**
     * Moves a figure to the desired direction
     * @param toMove: The figure to move
     * @param direction: The direction to move toward
     */
    public void moveFigure(Figures toMove, String direction) {
        // if figure need to move up
        if (direction.equals("UP")) {
            // if it is a valid move
            if (isValidMove(toMove, direction)) {
                // Create a new list of Integer
                List<Integer> toSort = new ArrayList<>();
                // Iterate through the list of current figures
                for (Figures fig : this.getFigures()) {
                    // if other figures in the list are in the same column but smaller rows
                    if (toMove.getRow() > fig.getRow() && toMove.getCol() == fig.getCol()) {
                        // add that row to the list of Integer
                        toSort.add(fig.getRow());
                    }
                }
                // Sort the list
                Collections.sort(toSort);
                // Initialize a variable and store a value that is one greater than the largest value in the list
                int row = toSort.get(toSort.size() - 1) + 1;
                // Iterate through the list of figures
                for (Figures figures : this.getFigures()) {
                    // if one of the figures in the current configs figure list equals the piece to move
                    if (figures.equals(toMove)) {
                        // update the row of that piece in the current config
                        figures.setRow(row);
                    }
                }
            }
        }

        // if figure needs to move down
        else if (direction.equals("DOWN")) {
            // if it is a valid move
            if (isValidMove(toMove, direction)) {
                // Create a new list of Integer
                List<Integer> toSort = new ArrayList<>();
                // Iterate through the list of current figures on the board
                for (Figures fig : this.getFigures()) {
                    // if another figure on the board had the same column and greater row
                    if (toMove.getRow() < fig.getRow() && toMove.getCol() == fig.getCol()) {
                        // add that row to the list of Integer
                        toSort.add(fig.getRow());
                    }
                }
                // sort the list
                Collections.sort(toSort);
                // Initialize a variable by storing a value that is one less than the lowest value in the list
                int row = toSort.get(0) - 1;
                // Iterate through the list of figures on the board
                for (Figures figures : this.getFigures()) {
                    // if one of the figures on the board equals the piece to move
                    if (figures.equals(toMove)) {
                        // update the row of that piece on the board
                        figures.setRow(row);
                    }
                }
            }
        }

        // if figure needs to move left
        else if (direction.equals("LEFT")) {
            // if it is a valid move
            if (isValidMove(toMove, direction)) {
                // Create a new list of Integer
                List<Integer> toSort = new ArrayList<>();
                // Iterate through the list of current figures on the board
                for (Figures fig : this.getFigures()) {
                    // if another figure on the board has the same row and smaller column
                    if (toMove.getRow() == fig.getRow() && toMove.getCol() > fig.getCol()) {
                        // add that column to the list of Integer
                        toSort.add(fig.getCol());
                    }
                }
                // sort the list
                Collections.sort(toSort);
                // Initialize a variable by storing a value that is one more than the largest value in the list of Integer
                int col = toSort.get(toSort.size() - 1) + 1;
                // Iterate through the list of figures on the board
                for (Figures figures : this.getFigures()) {
                    // if one of the figures on the board equals the piece to move
                    if (figures.equals(toMove)) {
                        // update the column of that piece on the board
                        figures.setCol(col);
                    }
                }
            }
        }

        // if figure needs to move right
        else if (direction.equals("RIGHT")) {
            // if it is a valid move
            if (isValidMove(toMove, direction)) {
                // Create a new list of Integer
                List<Integer> toSort = new ArrayList<>();
                // Iterate through the list of current figures on the board
                for (Figures fig : this.getFigures()) {
                    // if another figure on the board has the same row and greater column
                    if (toMove.getRow() == fig.getRow() && toMove.getCol() < fig.getCol()) {
                        // add that column to the list of Integer
                        toSort.add(fig.getCol());
                    }
                }
                // sort the list
                Collections.sort(toSort);
                // Initialize a variable by storing a value that is one less than the smallest value in the list toSort
                int col = toSort.get(0) - 1;
                // Iterate through the list of figures on the board
                for (Figures figures : this.getFigures()) {
                    // if one of the figures on the board equals the piece to move
                    if (figures.equals(toMove)) {
                        // update the column of that piece on the board
                        figures.setCol(col);
                    }
                }
            }
        }
    }

    /**
     * returns true if a piece can move in a certain direction, false otherwise
     * @param toMove: the figure piece to move
     * @param direction: the direction to move toward
     * @return: true if a piece can move in the given direction, false otherwise
     */
    public boolean isValidMove(Figures toMove, String direction) {
        boolean result = false;
        List<Integer> toSort = new ArrayList<>();

        // if desired direction is up
        if (direction.equals("UP")) {
            // iterate through the list of figures on the board
            for (Figures fig : this.getFigures()) {
                // if another figure on the board has the same column and smaller row return true
                if (!(toMove.equals(fig)) && toMove.getCol() == fig.getCol() && toMove.getRow() > fig.getRow()) {
                    result = true;
                    break;
                }
            }
        }

        // if desired direction is down
        else if (direction.equals("DOWN")) {
            // iterate through the list of figures on the board
            for (Figures fig : this.getFigures()) {
                // if another figure on the board has the same column and greater row return true
                if (!(toMove.equals(fig)) && toMove.getCol() == fig.getCol() && toMove.getRow() < fig.getRow()) {
                    result = true;
                    break;
                }
            }
        }

        // if desired direction is left
        else if (direction.equals("LEFT")) {
            // iterate through the list of figures on the board
            for (Figures fig : this.getFigures()) {
                // if another figure on the board has the same row and smaller column return true
                if (!(toMove.equals(fig)) && toMove.getCol() > fig.getCol() && toMove.getRow() == fig.getRow()) {
                    result = true;
                    break;
                }
            }
        }

        // if desired direction is right
        else if (direction.equals("RIGHT")) {
            // iterate through the list of figures on the board
            for (Figures fig : this.getFigures()) {
                // if another figure on the board has the same row and greater column return true
                if (!(toMove.equals(fig)) && toMove.getCol() < fig.getCol()  && toMove.getRow() == fig.getRow()) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * returns the list of figures in the current config
     * @return: the list of figures in the current config
     */
    public List<Figures> getFigures() {
        return this.figures;
    }

    /**
     * returns the number of rows on the board
     * @return: the board's number of rows
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * returns the number of columns on the board
     * @return: the board's number of columns
     */
    public int getNumColumns() {
        return numColumns;
    }

    /**
     * returns the goalRow
     * @return: the goal row
     */
    public int getGoalRow() {
        return goalRow;
    }

    /**
     * returns the goal column
     * @return: the goal column
     */
    public int getGoalColumn() {
        return goalColumn;
    }

    /**
     * returns true if the explorer has reached the lunar lander(goal row, goal column)
     * @return: boolean value determining if the goal has been reached
     */
    @Override
    public boolean isSolution() {
        boolean result = false;
        for (Figures figure : figures) {
            if (figure.getName().equals("E") && figure.getRow() == goalRow && figure.getCol() == goalColumn) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * determine if current config is equal to object passed in
     * @param other: the object to compare the current config to
     * @return: true if current config equals other, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof LunarLandingConfig) {
            LunarLandingConfig o = (LunarLandingConfig) other;
            result = this.figures.equals(o.figures);
        }
        return result;
    }

    /**
     * return the hashcode for current config
     * @return: the hashcode for the curent config
     */
    @Override
    public int hashCode() {
        return this.figures.hashCode();
    }

    /**
     * returns a string representation of the current LunarLandingConfig
     * @return: string representation of the current LunarLandingConfig
     */
    @Override
    public String toString() {
        String str = "\t ";
        for (int i = 0; i < numColumns; i++) {
            str += i + "\t";
        }
        str += "\n___________________________\n";

        for (int i = 0; i < numRows; i++) {
            str+= i + "\t|";
            for (int j = 0; j < numColumns; j++) {
                String name = "-";
                if(i == goalRow && j == goalColumn){
                    name = "!";
                }
                for (Figures fig : this.figures) {
                    if (fig.getRow() == i && fig.getCol() == j) {
                        if (fig.getRow() == goalRow && fig.getCol() == goalColumn) {
                            name = "!" + fig.getName();
                        }
                        else {
                            name = fig.getName();
                        }
                    }
                }
                str+= name;
                str+= "\t";
            }
            str+="\n";
        }

        return str;
    }
}

package puzzles.tipover.model;

import solver.Configuration;
import util.Coordinates;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * This class will make a new TipOverConfig and will create the list of neighbors that will be passed into the solver
 * This class will also make a hashcode, an equals method, and a toString method to print out the grid
 * This class also determines if the current grid is the solution by seeing if the tipper is on the goal crate
 * @author Giovanni Coppola
 * November 2021
 */
public class TipOverConfig implements Configuration {
    /**
     * Private state variables for the tip over puzzle
     * This includes the number of rows, the number of columns, the location of the tipper, the location of the goal
     *      crate, and the current grid state. All of this data will be filled in the constructor and/or copy
     *      constructor
     */
    private int numberOfRows;
    private int numberOfColumns;
    private Coordinates tipper;
    private Coordinates goalCrate;
    private int[][] gameBoard;

    /**
     * Constructor for TipOver
     * @param numberOfRows - the number of rows in the grid
     * @param numberOfColumns - the number of columns in the grid
     * @param gameBoard - the beginning grid state
     * @param tipper - the coordinates of the tipper
     * @param goalCrate - the coordinates of the goal crate
     */
    public TipOverConfig(int numberOfRows, int numberOfColumns, int[][] gameBoard, Coordinates tipper,
                   Coordinates goalCrate) {
        this.numberOfRows = numberOfRows;
        this.numberOfColumns = numberOfColumns;
        this.gameBoard = gameBoard;
        this.tipper = tipper;
        this.goalCrate = goalCrate;
    }

    /**
     * Copy constructor for the TipOver class
     * @param other - an instance of tipover that will be copied from
     */
    public TipOverConfig(TipOverConfig other) {
        this.numberOfRows = other.numberOfRows;
        this.numberOfColumns = other.numberOfColumns;
        this.gameBoard = new int[numberOfRows][];
        for (int index = 0; index < other.gameBoard.length; index++) {
            this.gameBoard[index] = Arrays.copyOf(other.gameBoard[index], other.gameBoard[index].length);
        }
        this.tipper = other.tipper;
        this.goalCrate = other.goalCrate;
    }

    // Accessor methods for the config that will be used by the model
    public int[][] getGameBoard() {
        return gameBoard;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public Coordinates getTipper() {
        return tipper;
    }

    public Coordinates getGoalCrate() {
        return goalCrate;
    }

    /**
     * This is the method that will be called by the model to move the user in a given direction
     * @param direction
     * @return
     */
    public String moveTipper(String direction) {
        int row;
        int col;
        int currentTowerHeight = 0;

        if (direction.equals("north")) {
            if (this.tipper.row() > 0) {
                if (this.gameBoard[this.tipper.row()][this.tipper.col()] != 0) {
                    if (this.gameBoard[this.tipper.row() - 1][this.tipper.col()] != 0) {
                        // If the user is on a tower and to the north of them is a tower, move them onto it
                        this.tipper = new Coordinates(this.tipper.row() - 1, this.tipper.col());
                        return "";
                    } else {
                        if (this.gameBoard[this.tipper.row()][this.tipper.col()] > 1) {
                            // Get the neighbor of the tipper in the north direction
                            if (this.tipper.row() - 1 >= 0 && this.gameBoard[this.tipper.row() - 1][this.tipper.col()] == 0) {
                                row = this.tipper.row();
                                col = this.tipper.col();
                                currentTowerHeight = this.gameBoard[this.tipper.row()][this.tipper.col()];
                                // If the north is valid, then tip over the tower by moving through and setting each index to a 1 (crate)
                                if (isValidPath(1) && row - currentTowerHeight >= 0) {
                                    // Set the original location of the tower to a 0
                                    this.gameBoard[row][col] = 0;
                                    row -= 1;
                                    this.tipper = new Coordinates(row, col);
                                    for (int index = 0; index < currentTowerHeight; index++) {
                                        this.gameBoard[row][col] = 1;
                                        row -= 1;
                                    }
                                }
                            }
                            return "A tower has been tipped over.";
                        } else if (this.gameBoard[this.tipper.row()][this.tipper.col()] == 1) {
                            // Non-proper crate message
                            return "No crate or tower there.";
                        }
                    }
                }
            } else {
                // Bounds message
                return "Move goes off the board.";
            }
        }

        if (direction.equals("south")) {
            if (this.tipper.row() < this.numberOfRows) {
                if (this.gameBoard[this.tipper.row()][this.tipper.col()] != 0) {
                    if (this.gameBoard[this.tipper.row() + 1][this.tipper.col()] != 0) {
                        // If the user is on a tower and to the south of them is a tower, move them onto it
                        this.tipper = new Coordinates(this.tipper.row() + 1, this.tipper.col());
                        return "";
                    } else {
                        if (this.gameBoard[this.tipper.row()][this.tipper.col()] > 1) {
                            // Get the neighbor of the tipper in the north direction
                            if (this.tipper.row() + 1 <= this.numberOfRows && this.gameBoard[this.tipper.row() + 1][this.tipper.col()] == 0) {
                                row = this.tipper.row();
                                col = this.tipper.col();
                                currentTowerHeight = this.gameBoard[this.tipper.row()][this.tipper.col()];
                                // If the south is valid, then tip over the tower by moving through and setting each index to a 1 (crate)
                                if (isValidPath(2) && row + currentTowerHeight <= this.numberOfRows) {
                                    // Set the original location of the tower to a 0
                                    this.gameBoard[row][col] = 0;
                                    row += 1;
                                    this.tipper = new Coordinates(row, col);
                                    for (int index = 0; index < currentTowerHeight; index++) {
                                        this.gameBoard[row][col] = 1;
                                        row += 1;
                                    }
                                }
                            }
                            return "A tower has been tipped over.";
                        } else if (this.gameBoard[this.tipper.row()][this.tipper.col()] == 1) {
                            // Non-proper crate message
                            return "No crate or tower there.";
                        }
                    }
                }
            } else {
                // Bounds message
                return "Move goes off the board.";
            }
        }

        if (direction.equals("east")) {
            if (this.tipper.col() < this.numberOfColumns) {
                if (this.gameBoard[this.tipper.row()][this.tipper.col()] != 0) {
                    if (this.gameBoard[this.tipper.row()][this.tipper.col() + 1] != 0) {
                        // If the user is on a tower and to the east of them is a tower, move them onto it
                        this.tipper = new Coordinates(this.tipper.row(), this.tipper.col() + 1);
                        return "";
                    } else {
                        if (this.gameBoard[this.tipper.row()][this.tipper.col()] > 1) {
                            // Get the neighbor of the tipper in the north direction
                            if (this.tipper.col() <= this.numberOfColumns && this.gameBoard[this.tipper.row()][this.tipper.col() + 1] == 0) {
                                row = this.tipper.row();
                                col = this.tipper.col();
                                currentTowerHeight = this.gameBoard[this.tipper.row()][this.tipper.col()];
                                // If the north is valid, then tip over the tower by moving through and setting each index to a 1 (crate)
                                if (isValidPath(3) && col + currentTowerHeight <= this.numberOfColumns) {
                                    // Set the original location of the tower to a 0
                                    this.gameBoard[row][col] = 0;
                                    col += 1;
                                    this.tipper = new Coordinates(row, col);
                                    for (int index = 0; index < currentTowerHeight; index++) {
                                        this.gameBoard[row][col] = 1;
                                        col += 1;
                                    }
                                }
                            }
                            return "A tower has been tipped over.";
                        } else if (this.gameBoard[this.tipper.row()][this.tipper.col()] == 1) {
                            // Non-proper crate message
                            return "No crate or tower there.";
                        }
                    }
                }
            } else {
                // Bounds message
                return "Move goes off the board.";
            }
        }

        if (direction.equals("west")) {
            if (this.tipper.row() < this.numberOfRows) {
                if (this.gameBoard[this.tipper.row()][this.tipper.col()] != 0) {
                    if (this.gameBoard[this.tipper.row()][this.tipper.col() - 1] != 0) {
                        // If the user is on a tower and to the west of them is a tower, move them onto it
                        this.tipper = new Coordinates(this.tipper.row(), this.tipper.col() - 1);
                        return "";
                    } else {
                        if (this.gameBoard[this.tipper.row()][this.tipper.col()] > 1) {
                            // Get the neighbor of the tipper in the north direction
                            if (this.tipper.col() - 1 <= this.numberOfRows && this.gameBoard[this.tipper.row()][this.tipper.col() - 1] == 0) {
                                row = this.tipper.row();
                                col = this.tipper.col();
                                currentTowerHeight = this.gameBoard[this.tipper.row()][this.tipper.col()];
                                // If the south is valid, then tip over the tower by moving through and setting each index to a 1 (crate)
                                if (isValidPath(4) && col - currentTowerHeight >= 0) {
                                    // Set the original location of the tower to a 0
                                    this.gameBoard[row][col] = 0;
                                    col -= 1;
                                    this.tipper = new Coordinates(row, col);
                                    for (int index = 0; index < currentTowerHeight; index++) {
                                        this.gameBoard[row][col] = 1;
                                        col -= 1;
                                    }
                                }
                            }
                            return "A tower has been tipped over.";
                        } else if (this.gameBoard[this.tipper.row()][this.tipper.col()] == 1) {
                            // Non-proper crate message
                            return "No crate or tower there.";
                        }
                    }
                }
            } else {
                // Bounds message
                return "Move goes off the board.";
            }
        }

        return null;
    }

    /**
     * Check to see if the direction that the user wants to move in, is valid for tipping over the current tower
     *
     * @param direction - the direction that will be checked to make sure it is a valid path
     * @return - if the path is valid or not
     */
    public boolean isValidPath(int direction) {
        boolean isPathClear;
        int row;
        int col;
        int currentTowerHeight;

        switch (direction) {
            // North
            case 1 -> {
                isPathClear = true;
                row = this.tipper.row();
                col = this.tipper.col();
                currentTowerHeight = this.gameBoard[this.tipper.row()][this.tipper.col()];

                // Check if the path is clear by first checking it is within the bounds of the board
                if (row - currentTowerHeight >= 0) {
                    for (int index = 0; index < currentTowerHeight; index++) {
                        row -= 1;
                        // If it is in bounds, then loop through and make sure each index is a 0
                        // Make sure the path is false if a non-zero integer is found (a crate is in the way)
                        if (this.gameBoard[row][col] != 0) {
                            isPathClear = false;
                        }
                    }
                }
            }
            // South
            case 2 -> {
                isPathClear = true;
                row = this.tipper.row();
                col = this.tipper.col();
                currentTowerHeight = this.gameBoard[this.tipper.row()][this.tipper.col()];

                // Check if the path is clear by first checking it is within the bounds of the board
                if (row + currentTowerHeight < this.numberOfRows) {
                    for (int index = 0; index < currentTowerHeight; index++) {
                        row += 1;
                        // If it is in bounds, then loop through and make sure each index is a 0
                        // Make sure the path is false if a non-zero integer is found (a crate is in the way)
                        if (this.gameBoard[row][col] != 0) {
                            isPathClear = false;
                        }
                    }
                }
            }
            // East
            case 3 -> {
                isPathClear = true;
                row = this.tipper.row();
                col = this.tipper.col();
                currentTowerHeight = this.gameBoard[this.tipper.row()][this.tipper.col()];

                // Check if the path is clear by first checking it is within the bounds of the board
                if (col + currentTowerHeight < this.numberOfColumns) {
                    for (int index = 0; index < currentTowerHeight; index++) {
                        // If it is in bounds, then loop through and make sure each index is a 0
                        // Make sure the path is false if a non-zero integer is found (a crate is in the way)
                        col += 1;
                        if (this.gameBoard[row][col] != 0) {
                            isPathClear = false;
                        }
                    }
                }
            }
            // West
            case 4 -> {
                isPathClear = true;
                row = this.tipper.row();
                col = this.tipper.col();
                currentTowerHeight = this.gameBoard[this.tipper.row()][this.tipper.col()];

                // Check if the path is clear by first checking it is within the bounds of the board
                if (col - currentTowerHeight >= 0) {
                    for (int index = 0; index < currentTowerHeight; index++) {
                        // If it is in bounds, then loop through and make sure each index is a 0
                        // Make sure the path is false if a non-zero integer is found (a crate is in the way)
                        col -= 1;
                        if (this.gameBoard[row][col] != 0) {
                            isPathClear = false;
                        }
                    }
                }
            }
            default -> isPathClear = false;
        }
        return isPathClear;
    }

    @Override
    public boolean isSolution() {
        return this.tipper.row() == this.goalCrate.row() &&
                this.tipper.col() == this.goalCrate.col();
    }

    /**
     * Method to get all the valid neighbors in all directions of the tipper
     *
     * @return - a list of configurations, being the neighbors
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new LinkedList<>();

        // Variables that will be used for checking if the path is clear, storing the row and col, and the tower height
        boolean isPathClear;
        int row;
        int col;
        int currentTowerHeight;

        // Create a copy of the current config
        TipOverConfig tipOverConfig = new TipOverConfig(this);

        // Get the neighbor of the tipper in the north direction
        if (tipOverConfig.tipper.row()-1 >= 0 && tipOverConfig.gameBoard[tipOverConfig.tipper.row() - 1][tipOverConfig.tipper.col()] == 0) {
            // If the current game board is a tower with height greater than one (not a crate)
            if (tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()] > 1){
                isPathClear = true;
                row = tipOverConfig.tipper.row();
                col = tipOverConfig.tipper.col();
                currentTowerHeight = tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()];

                // Check if the path is clear by first checking it is within the bounds of the board
                if (row-currentTowerHeight >= 0) {
                    for (int index = 0; index < currentTowerHeight; index++) {
                        row -= 1;
                        // If it is in bounds, then loop through and make sure each index is a 0
                        // Make sure the path is false if a non-zero integer is found (a crate is in the way)
                        if (tipOverConfig.gameBoard[row][col] != 0) {
                            isPathClear = false;
                        }
                    }
                }

                row = tipOverConfig.tipper.row();
                col = tipOverConfig.tipper.col();
                // If the north is valid, then tip over the tower by moving through and setting each index to a 1 (crate)
                if (isPathClear && row-currentTowerHeight >= 0) {
                    // Set the original location of the tower to a 0
                    tipOverConfig.gameBoard[row][col] = 0;
                    row -= 1;
                    tipOverConfig.tipper = new Coordinates(row, col);
                    for (int index = 0; index < currentTowerHeight; index++) {
                        tipOverConfig.gameBoard[row][col] = 1;
                        row -= 1;
                    }
                }
            }
        } else if (tipOverConfig.tipper.row()-1 >= 0 && tipOverConfig.gameBoard[tipOverConfig.tipper.row() - 1][tipOverConfig.tipper.col()] != 0) {
            // Otherwise, if it is not zero, then move the user to the north if it is valid
            tipOverConfig.tipper = new Coordinates(tipOverConfig.tipper.row() - 1, tipOverConfig.tipper.col());
        }

        // Add the current config to the neighbors list
        neighbors.add(new TipOverConfig(tipOverConfig));

        tipOverConfig = new TipOverConfig(this);

        // Get the neighbor of the tipper in the south direction
        if (tipOverConfig.tipper.row()+1 < tipOverConfig.numberOfRows && tipOverConfig.gameBoard[tipOverConfig.tipper.row()+1][tipOverConfig.tipper.col()] == 0) {
            // If the current game board is a tower with height greater than one (not a crate)
            if (tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()] > 1){
                isPathClear = true;
                row = tipOverConfig.tipper.row();
                col = tipOverConfig.tipper.col();
                currentTowerHeight = tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()];

                // Check if the path is clear by first checking it is within the bounds of the board
                if (row+currentTowerHeight < tipOverConfig.numberOfRows) {
                    for (int index = 0; index < currentTowerHeight; index++) {
                        row += 1;
                        // If it is in bounds, then loop through and make sure each index is a 0
                        // Make sure the path is false if a non-zero integer is found (a crate is in the way)
                        if (tipOverConfig.gameBoard[row][col] != 0) {
                            isPathClear = false;
                        }
                    }
                }

                row = tipOverConfig.tipper.row();
                col = tipOverConfig.tipper.col();
                // If the north is valid, then tip over the tower by moving through and setting each index to a 1 (crate)
                if (isPathClear && row+currentTowerHeight < tipOverConfig.numberOfRows) {
                    // Set the original location of the tower to a 0
                    tipOverConfig.gameBoard[row][col] = 0;
                    row += 1;
                    tipOverConfig.tipper = new Coordinates(row, col);
                    for (int index = 0; index < currentTowerHeight; index++) {
                        tipOverConfig.gameBoard[row][col] = 1;
                        row += 1;
                    }
                }
            }
        } else if (tipOverConfig.tipper.row()+1 < tipOverConfig.numberOfRows && tipOverConfig.gameBoard[tipOverConfig.tipper.row()+1][tipOverConfig.tipper.col()] != 0) {
            // Otherwise, if it is not zero, then move the user to the south if it is valid
            tipOverConfig.tipper = new Coordinates(tipOverConfig.tipper.row() + 1, tipOverConfig.tipper.col());
        }

        // Add the current config to the neighbors list
        neighbors.add(new TipOverConfig(tipOverConfig));

        tipOverConfig = new TipOverConfig(this);

        // Get the neighbor of the tipper in the west direction
        if (tipOverConfig.tipper.col()-1 >= 0 && tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()-1] == 0) {
            // If the current game board is a tower with height greater than one (not a crate)
            if (tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()] > 1) {
                isPathClear = true;
                row = tipOverConfig.tipper.row();
                col = tipOverConfig.tipper.col();
                currentTowerHeight = tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()];

                // Check if the path is clear by first checking it is within the bounds of the board
                if (col-currentTowerHeight >= 0) {
                    for (int index = 0; index < currentTowerHeight; index++) {
                        // If it is in bounds, then loop through and make sure each index is a 0
                        // Make sure the path is false if a non-zero integer is found (a crate is in the way)
                        col -= 1;
                        if (tipOverConfig.gameBoard[row][col] != 0) {
                            isPathClear = false;
                        }
                    }
                }

                row = tipOverConfig.tipper.row();
                col = tipOverConfig.tipper.col();
                // If the north is valid, then tip over the tower by moving through and setting each index to a 1 (crate)
                if (isPathClear && col-currentTowerHeight >= 0) {
                    // Set the original location of the tower to a 0
                    tipOverConfig.gameBoard[row][col] = 0;
                    col -= 1;
                    tipOverConfig.tipper = new Coordinates(row, col);
                    for (int index = 0; index < currentTowerHeight; index++) {
                        tipOverConfig.gameBoard[row][col] = 1;
                        col -= 1;
                    }
                }
            }
        } else if (tipOverConfig.tipper.col()-1 >= 0 && tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()-1] != 0) {
            // Otherwise, if it is not zero, then move the user to the west if it is valid
            tipOverConfig.tipper = new Coordinates(tipOverConfig.tipper.row(), tipOverConfig.tipper.col()-1);
        }

        // Add the current config to the neighbors list
        neighbors.add(new TipOverConfig(tipOverConfig));

        tipOverConfig = new TipOverConfig(this);

        // Get the neighbor of the tipper in the east direction
        if (tipOverConfig.tipper.col()+1 < tipOverConfig.numberOfColumns && tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()+1] == 0) {
            // If the current game board is a tower with height greater than one (not a crate)
            if (tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()] > 1) {
                isPathClear = true;
                row = tipOverConfig.tipper.row();
                col = tipOverConfig.tipper.col();
                currentTowerHeight = tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()];

                // Check if the path is clear by first checking it is within the bounds of the board
                if (col+currentTowerHeight < tipOverConfig.numberOfColumns) {
                    for (int index = 0; index < currentTowerHeight; index++) {
                        // If it is in bounds, then loop through and make sure each index is a 0
                        // Make sure the path is false if a non-zero integer is found (a crate is in the way)
                        col += 1;
                        if (tipOverConfig.gameBoard[row][col] != 0) {
                            isPathClear = false;
                        }
                    }
                }

                row = tipOverConfig.tipper.row();
                col = tipOverConfig.tipper.col();
                // If the north is valid, then tip over the tower by moving through and setting each index to a 1 (crate)
                if (isPathClear && col+currentTowerHeight < tipOverConfig.numberOfColumns) {
                    // Set the original location of the tower to a 0
                    tipOverConfig.gameBoard[row][col] = 0;
                    col += 1;
                    tipOverConfig.tipper = new Coordinates(row, col);
                    for (int index = 0; index < currentTowerHeight; index++) {
                        tipOverConfig.gameBoard[row][col] = 1;
                        col += 1;
                    }
                }
            }
        } else if (tipOverConfig.tipper.col()+1 < tipOverConfig.numberOfColumns && tipOverConfig.gameBoard[tipOverConfig.tipper.row()][tipOverConfig.tipper.col()+1] != 0) {
            // Otherwise, if it is not zero, then move the user to the east if it is valid
            tipOverConfig.tipper = new Coordinates(tipOverConfig.tipper.row(), tipOverConfig.tipper.col()+1);
        }

        // Add the current config to the neighbors list
        neighbors.add(new TipOverConfig(tipOverConfig));

        return neighbors;
    }

    @Override
    public String toString() {
        String message = "";
        for (int rowNum = 0; rowNum < this.numberOfRows; rowNum++) {
            if (rowNum == 0) {
                message += "   ";
                for (int index = 0; index < this.numberOfColumns; index++) {
                    message += "  " + index;
                }
                message += "\n   " + "_".repeat((this.numberOfColumns)*3) + "\n";
                message += "0 |";
            } else {
                message += rowNum + " |";
            }
            for (int colNum = 0; colNum < this.numberOfColumns; colNum++) {
                if (this.gameBoard[rowNum][colNum] == 0) {
                    message += "  _";
                } else if (this.tipper.row() == rowNum && this.tipper.col() == colNum) {
                    message += " *" + this.gameBoard[rowNum][colNum];
                } else if (this.goalCrate.row() == rowNum && this.goalCrate.col() == colNum) {
                    message += " !" + this.gameBoard[rowNum][colNum];
                } else {
                    message += "  " + this.gameBoard[rowNum][colNum];
                }
            }
            message += "\n";
        }

        return message;
    }

    @Override
    public int hashCode() {
        return this.numberOfRows + this.numberOfColumns + this.goalCrate.hashCode() + Arrays.deepHashCode(this.gameBoard)
                + this.tipper.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof TipOverConfig) {
            TipOverConfig newTipOver = (TipOverConfig) other;
            result = this.numberOfRows == newTipOver.numberOfRows &&
                    this.numberOfColumns == newTipOver.numberOfColumns &&
                    this.goalCrate.equals(newTipOver.goalCrate) &&
                    this.tipper.equals(newTipOver.tipper) &&
                    Arrays.deepEquals(this.gameBoard, newTipOver.gameBoard);
        }
        return result;
    }
}

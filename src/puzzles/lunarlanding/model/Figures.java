package puzzles.lunarlanding.model;

/**
 * This class represents a figure on the board
 */
public class Figures {
    private final String name; // the name of the figure
    private int col; // the column in which the figure is
    private int row; // the row in which the figure is

    /**
     * This constructor creates a new figure
     * @param name: the name of the figure
     * @param row: the row on the board where the figure is
     * @param col: the column on the board where the figure is
     */
    public Figures (String name, int row, int col) {
        this.name = name;
        this.col = col;
        this.row = row;
    }

    /**
     * returns the name of the figure
     * @return: the name of the figure
     */
    public String getName() {
        return this.name;
    }

    /**
     * updates the column attribute of the figure
     * @param col: the new value to update the column to
     */
    public void setCol(int col) {
        this.col = col;
    }

    /**
     * returns the column attribute of the figure
     * @return: the column of the figure
     */
    public int getCol() {
        return this.col;
    }

    /**
     * updates the row attribute of the figure
     * @param row: the new value to update the row to
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * returns the row attribute of the figure
     * @return: the row of the figure
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns true if two figures are equal
     * @param other: the object to compare the current figure to
     * @return: true if other and this figure are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Figures) {
            Figures o = (Figures) other;
            result = this.name.equals(o.name) && this.row == o.row && this.col == o.col;
        }
        return result;
    }

    /**
     * return the string representation of the figure
     * @return: the string representation of the figure
     */
    @Override
    public String toString() {
        return this.name + ", " + this.row + ", " + this.col;
    }
}

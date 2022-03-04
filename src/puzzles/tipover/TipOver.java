package puzzles.tipover;

import puzzles.tipover.model.TipOverConfig;
import solver.Configuration;
import solver.Solver;
import util.Coordinates;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This class will make a new instance of the TipOverConfig and will call solve on it and print out the solved grid
 *      step by step
 *
 * @author Giovanni Coppola
 * November 2021
 */
public class TipOver {
    public static void main( String[] args ) throws FileNotFoundException {
        if (args.length < 1) {
            System.out.println("Usage: java tipover file name ...");
        } else {
            try {
                // Set the input file and scanner
                File inputFile = new File(args[0]);
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
                Configuration tipOver = new TipOverConfig(numOfRows, numOfCols, gameBoardArray, tipperPosition, goalPosition);
                Solver solve = new Solver(tipOver);
                List<Configuration> solved = solve.solver();

                // Print if the solution was found or not. If it was found, print it by step
                if (solved.isEmpty()) {
                    System.out.println("No Solution");
                } else {
                    for (int index = 0; index < solved.size(); index++) {
                        System.out.println("Step " + index + ": \n" + solved.get(index).toString());
                    }
                }

            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

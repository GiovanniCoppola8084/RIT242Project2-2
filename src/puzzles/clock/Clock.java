package puzzles.clock;

import solver.Configuration;
import solver.Solver;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Main class for the "clock" puzzle.
 *
 * @author Giovanni Coppola
 */
public class Clock implements Configuration {
    /**
     * Private member variables for the clocks
     */
    private int hoursOnClock;
    private int startTime;
    private int finalTime;

    /**
     * Create a new clock
     *
     * @param hoursOnClock - max hours on the clock
     * @param startTime - the time at which the clock will start at
     * @param finalTime - the time that the clock needs to reach
     */
    public Clock(int hoursOnClock, int startTime, int finalTime) {
        this.hoursOnClock = hoursOnClock;
        this.startTime = startTime;
        this.finalTime = finalTime;
    }

    /**
     * Run an instance of the clock puzzle.
     * @param args [0]: number of hours on the clock;
     *             [1]: starting time on the clock;
     *             [2]: goal time to which the clock should be set.
     */
    public static void main( String[] args ) {
        if ( args.length != 3 ) {
            System.out.println( "Usage: java Clock hours start end" );
        }
        else {
            // Create a new instance of the clock with the parameters from the command line argument
            Configuration node = new Clock(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));
            // Create a new solver and create the list of configurations
            Solver solve = new Solver(node);
            List<Configuration> solved = solve.solver();

            // Output the header for the hours and print the results of the BFS algorithm
            System.out.println("Hours: " + args[0] + ", Start: " + args[1] + ", End: " + args[2]);
            System.out.println("Total configs: " + solve.getTotalConfigs());
            System.out.println("Unique configs: " + solve.getUniqueConfigs());
            if (solved.isEmpty()) {
                // Print no solution if the list returned is empty
                System.out.println("No solution");
            } else {
                for (int index = 0; index < solved.size(); index++) {
                    // Sort through and print all configurations if the list is not empty
                    System.out.println("Step " + (index) + ": " + solved.get(index));
                }
            }
        }
    }

    /**
     * Compare the start time and final time to see if they are the same
     *
     * @return - the comparison of the start and finish
     */
    @Override
    public boolean isSolution() {
        return this.startTime == this.finalTime;
    }

    /**
     * Get the neighbors for the current clock configuration
     *
     * @return - the collection of neighbors found
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new LinkedList<>();
        if (this.startTime > 1 && this.startTime < this.hoursOnClock) {
            neighbors.add(new Clock(this.hoursOnClock, this.startTime - 1, this.finalTime));
            neighbors.add(new Clock(this.hoursOnClock, this.startTime + 1, this.finalTime));
        } else {
            if (this.startTime == 1) {
                neighbors.add(new Clock(this.hoursOnClock, this.hoursOnClock, this.finalTime));
                neighbors.add(new Clock(this.hoursOnClock, this.startTime + 1, this.finalTime));
            } else {
                neighbors.add(new Clock(this.hoursOnClock, this.startTime - 1, this.finalTime));
                neighbors.add(new Clock(this.hoursOnClock, 1, this.finalTime));
            }
        }
        return neighbors;
    }

    @Override
    public String toString() {
        return "" + this.startTime;
    }

    @Override
    public int hashCode() {
        return this.hoursOnClock + this.startTime + this.finalTime;
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Clock) {
            Clock newClock = (Clock) other;
            result = this.finalTime == newClock.finalTime;
        }
        return result;
    }
}

package puzzles.water;

import solver.Configuration;
import solver.Solver;

import java.util.*;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Giovanni Coppola
 */
public class Water implements Configuration {
    /**
     * Private members for the water class
     */
    private int amountOfWater;
    private List<Integer> capacityOfNBuckets;
    private List<Integer> amountInBuckets;

    /**
     * Create a new instance of the water class
     *
     * @param amountOfWater - the amount of water that needs to be in one bucket at the end
     * @param capacityOfNBuckets - the capacity that each bucket will hold
     */
    public Water(int amountOfWater, List<Integer> capacityOfNBuckets) {
        this.amountOfWater = amountOfWater;
        this.capacityOfNBuckets = capacityOfNBuckets;
        this.amountInBuckets = new LinkedList<>();
        // Set each current amount of water in the buckets to 0
        for (int index = 0; index < capacityOfNBuckets.size(); index++) {
            this.amountInBuckets.add(0);
        }
    }

    /**
     * Copy function for the water class that will be used in the getNeighbors method
     *
     * @param other - an instance of Water
     */
    public Water(Water other) {
        // Create a copy of water
        this.amountOfWater = other.amountOfWater;
        this.capacityOfNBuckets = new LinkedList<>(other.capacityOfNBuckets);
        this.amountInBuckets = new LinkedList<>(other.amountInBuckets);
    }

    /**
     * Run an instance of the water buckets puzzle.
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main( String[] args ) {
        if ( args.length < 2 ) {
            System.out.println(( "Usage: java Water amount bucket1 bucket2 ..." ));
        }
        else {
            // Loop through each of the command line arguments and add the bucket sizes
            List<Integer> buckets = new LinkedList<>();
            for (int index = 1; index < args.length; index++) {
                buckets.add(Integer.parseInt(args[index]));
            }
            // Create a new instance of water and solver and call the solve method
            Configuration node = new Water(Integer.parseInt(args[0]), buckets);
            Solver solve = new Solver(node);
            List<Configuration> solved = solve.solver();

            // Print out the header for the water and the results of the BFS algorithm
            System.out.println("Amount: " + args[0] + ", Buckets: " + buckets);
            System.out.println("Total configs: " + solve.getTotalConfigs());
            System.out.println("Unique configs: " + solve.getUniqueConfigs());
            if (solved.isEmpty()) {
                // Print out no solution if the solved list is empty
                System.out.println("No solution");
            } else {
                for (int index = 0; index < solved.size(); index++) {
                    // Otherwise, print out each of the steps taken in the BFS algorithm
                    System.out.println("Step " + index + ": " + solved.get(index).toString());
                }
            }
        }
    }

    /**
     * Method to find if the current bucket list is the solution or not
     *
     * @return - the value if the current list contains the final amount or not
     */
    @Override
    public boolean isSolution() {
        return this.amountInBuckets.contains(this.amountOfWater);
    }

    /**
     * Method to get the neighbors of the current bucket list
     *
     * @return - the collection of neighbors found
     */
    @Override
    public Collection<Configuration> getNeighbors() {

        // Create a new neighbors list
        List<Configuration> neighbors = new LinkedList<>();

        // Integer that will be used as a variable to store how much will be poured into another bucket
        int amountToPour = 0;

        //Create a copy of the current water configuration
        Water waterConfig = new Water(this);

        // For loop to loop through and fill each bucket and make a new neighbor from that
        for (int index = 0; index < waterConfig.amountInBuckets.size(); index++) {
            waterConfig.amountInBuckets.set(index, waterConfig.capacityOfNBuckets.get(index));
            if (!neighbors.contains(waterConfig)) {
                neighbors.add(new Water(waterConfig));
            }
            waterConfig = new Water(this);
        }

        // Nested for loop to go through each combination to figure out if one bucket needs to be poured into another
        for (int index = 0; index < waterConfig.amountInBuckets.size(); index++) {
            for (int value = 0; value < waterConfig.amountInBuckets.size(); value++) {
                if (index != value) {
                    // Calculate the amount that will be poured from one bucket to another
                    amountToPour = Math.min(waterConfig.amountInBuckets.get(index), (waterConfig.capacityOfNBuckets.get(value) - waterConfig.amountInBuckets.get(value)));
                    //amountToPour = waterConfig.capacityOfNBuckets.get(value) - waterConfig.amountInBuckets.get(value);
                    // Add the amount to the smaller bucket and subtract from the larger one
                    waterConfig.amountInBuckets.set(value, waterConfig.amountInBuckets.get(value) + amountToPour);
                    waterConfig.amountInBuckets.set(index, waterConfig.amountInBuckets.get(index) - amountToPour);
                    if (!neighbors.contains(waterConfig)) {
                        neighbors.add(new Water(waterConfig));
                    }
                    waterConfig = new Water(this);
                }
            }
        }

        // For loop that will loop through and empty out all the buckets
        for (int index = 0; index < waterConfig.amountInBuckets.size(); index++) {
            waterConfig.amountInBuckets.set(index, 0);
            if (!neighbors.contains(waterConfig)) {
                neighbors.add(new Water(waterConfig));
            }
            waterConfig = new Water(this);
        }

        return neighbors;
    }

    @Override
    public String toString() {
        return "" + this.amountInBuckets;
    }

    @Override
    public int hashCode() {
        return this.amountOfWater + this.capacityOfNBuckets.hashCode() + this.amountInBuckets.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Water) {
            Water newClock = (Water) other;
            result = this.amountOfWater == newClock.amountOfWater &&
            this.capacityOfNBuckets.equals(newClock.capacityOfNBuckets) &&
            this.amountInBuckets.equals(newClock.amountInBuckets);
        }
        return result;
    }
}

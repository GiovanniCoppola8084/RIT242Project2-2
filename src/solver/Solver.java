package solver;

import javax.security.auth.login.ConfigurationSpi;
import java.util.*;

/**
 * This class contains a universal algorithm to find a path from a starting
 * configuration to a solution, if one exists
 *
 * @author Giovanni Coppola
 */
public class Solver {
    /**
     * Private members for the solver class
     */
    private Configuration startNode;
    private int totalConfigs = 0;
    private int uniqueConfigs = 0;

    /**
     * Create a new instance of the solver
     *
     * @param startNode
     */
    public Solver (Configuration startNode) {
        this.startNode = startNode;
    }

    /**
     * Solver method that will conduct the BFS algorithm to find all the predecessors until the final node is reached
     *
     * @return - the list of configurations in the path order to the solution
     */
    public List<Configuration> solver() {
        Configuration finalNode = null;

        // Create the queue and start by adding the start node
        List<Configuration> queue = new LinkedList<>();
        queue.add(startNode);

        // Create a hashmap that will represent the predecessors
        Map<Configuration, Configuration> predecessors = new HashMap<>();
        predecessors.put(startNode, startNode);
        totalConfigs++;

        // Loop through the queue until it is empty
        while (!queue.isEmpty()) {
            // Make the current the first element in queue and pop it off of the queue
            Configuration current = queue.remove(0);
            uniqueConfigs++;
            if (current.isSolution()) {
                // Break the loop if the current node is the solution
                finalNode = current;
                uniqueConfigs++;
                break;
            }

            // Loop through each neighbor of the configuration and add them to predecessors if they are not already there
            for (Configuration nbr : current.getNeighbors()) {
                if (!predecessors.containsKey(nbr)) {
                    predecessors.put(nbr, current);
                    queue.add(nbr);
                }
                totalConfigs++;
            }
        }
        // Create the path and return it to solver
        List<Configuration> solved = new ArrayList<>();
        solved = constructPath(predecessors, finalNode);
        return solved;
    }

    /**
     * Method to contruct the path from the start node to the solution
     *
     * @param predecessors - list of predecessors that was calculated in solver
     * @param finalNode - the final node that is the solution
     * @return - the final path from start to finish
     */
    public List<Configuration> constructPath(Map<Configuration, Configuration> predecessors, Configuration finalNode) {
        List<Configuration> path = new LinkedList<>();

        // If the final node is in the predecessor list
        if (predecessors.containsKey(finalNode)) {
            //int currConfig = startNode.getFinal();
            Configuration currConfig = finalNode;
            // Loop through and add the configurations to the path as long as they don't equal the start node
            while (currConfig != startNode) {
                path.add(0, currConfig);
                currConfig = predecessors.get(currConfig);
            }
            path.add(0, startNode);
        }
        return path;
    }

    public int getTotalConfigs() {
        return totalConfigs;
    }

    public int getUniqueConfigs() {
        return uniqueConfigs;
    }
}

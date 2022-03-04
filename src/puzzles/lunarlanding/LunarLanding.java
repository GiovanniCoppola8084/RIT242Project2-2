package puzzles.lunarlanding;

import puzzles.lunarlanding.gui.LunarLandingGUI;
import puzzles.lunarlanding.model.LunarLandingConfig;
import solver.Configuration;
import solver.Solver;

import java.util.*;

import java.io.FileNotFoundException;

/**
 * DESCRIPTION
 * @author YOUR NAME HERE
 * November 2021
 */
public class LunarLanding {

    /*
     * code to read the file name from the command line and
     * run the solver on the puzzle
     */

    public static void main( String[] args ) throws FileNotFoundException {

        System.out.println(args[0]);
        LunarLandingConfig luna = new LunarLandingConfig(args[0]);
        Solver s = new Solver(luna);
        List <Configuration> lst = new ArrayList<>(s.solver());
        if (lst.size() > 0) {
            for (Configuration c : lst) {
                System.out.println(c + "\n");
            }
        }
        else {
            System.out.println("No Solution Found");
        }


    }
}

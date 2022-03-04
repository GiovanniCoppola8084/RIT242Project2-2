package puzzles.lunarlanding.ptui;

import puzzles.lunarlanding.model.LunarLandingModel;
import util.Observer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.*;

/**
 * This class uses the LunarLandingModel to create a PTUI.
 * @author Romant Bhattarai
 * November 2021
 */
public class LunarLandingPTUI implements Observer<LunarLandingModel, Object> {

    // state variable for the PTUI
    private LunarLandingModel model;

    /**
     * creates a new LunarLandingPTUI
     * @param args: the command line argument
     * @throws FileNotFoundException
     */
    public LunarLandingPTUI (String [] args) throws FileNotFoundException {
        List<String> file = new ArrayList<>();
        file.add(args[0]);
        this.model = new LunarLandingModel(file.get(0));
        initializeView();
    }

    /**
     * Method to run the PTUI
     * @throws FileNotFoundException
     */
    public void launch() throws FileNotFoundException {
        Scanner scan = new Scanner(System.in);
        for (; ; ) {
            System.out.print("> ");
            String line = scan.nextLine();
            String []action = line.split("\\s");
            if (action.length > 0) {
                switch (action[0]) {
                    case "choose":
                        this.model.choose(Integer.parseInt(action[1]), Integer.parseInt(action[2]));
                        break;
                    case "go":
                        switch (action[1]) {
                            case "east":
                                this.model.go("RIGHT");
                            case "west":
                                this.model.go("LEFT");
                            case "south":
                                this.model.go("DOWN");
                            case "north":
                                this.model.go("UP");

                        }
                        break;
                    case "show":
                        this.model.show();
                        break;
                    case "hint":
                        this.model.hint();
                        break;
                    case "reload":
                        this.model.reload();
                        break;
                    case "load":
                        this.model.load(action[1]);
                        break;
                    case "quit":
                       quit();
                       break;
                    default:
                        help();
                        break;
                }
            }
        }
    }

    /**
     * prints a list of commands that the user can enter
     */
    public void help() {
        System.out.println("Show all commands");
        System.out.println("go---east/west/north/south");
        System.out.println("choose---COORDINATES(row, column)");
        System.out.println("load (filename): load a new game");
        System.out.println("reload filename: reload current game");
        System.out.println("hint: make the next step");
        System.out.println("show: Display the board");
        System.out.println("quit");
    }

    public void initializeView() {
        this.model.addObserver(this);
        update(model, "");
    }

    /**
     * displays the string representation of the current model
     */
    public void displayBoard() {
        System.out.println(model.getBoard());
    }

    /**
     * end the game
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * displays the board
     * @param o: the model
     * @param arg: the arguments to print
     */
    @Override
    public void update(LunarLandingModel o, Object arg) {
        displayBoard();
        System.out.println(arg);
    }

    public static void main( String[] args ) throws FileNotFoundException {
        LunarLandingPTUI lP = new LunarLandingPTUI(args);
        lP.launch();
    }

}

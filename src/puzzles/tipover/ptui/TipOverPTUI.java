package puzzles.tipover.ptui;

import puzzles.tipover.model.TipOverModel;
import util.Observer;

import java.util.Scanner;

/**
 * This is the class that will use the TipOverModel in order to make a usable PTUI to play the tipOver game.
 * The commands that are allowed are load, reload, move, hint, show, help, and quit
 *
 * @author Giovanni Coppola
 * November 2021
 */
public class TipOverPTUI implements Observer<TipOverModel, Object> {

    // Private model state
    private TipOverModel model;

    /**
     * Constructor for the PTUI. This will create a new model and initialize the view
     *
     * @param args - command line arguments
     */
    public TipOverPTUI(String[] args) {
        this.model = new TipOverModel(args);
        initializeView();
    }

    /**
     * This is the method that will run the PTUI based on what the user inputs
     */
    private void run() {
        Scanner in = new Scanner(System.in);
        for (; ; ) {
            System.out.print("> ");
            String line = in.nextLine();
            String[] words = line.split("\\s+");
            if (words.length > 0) {
                // Switch statement that calls the commands based on what the user put
                switch (words[0]) {
                    case "quit":
                        quit();
                        break;
                    case "move":
                        switch (words[1]) {
                            // If the user wanted to move, then call moveTipper with the respective direction
                            case "north" -> this.model.moveTipper("north");
                            case "south" -> this.model.moveTipper("south");
                            case "east" -> this.model.moveTipper("east");
                            case "west" -> this.model.moveTipper("west");
                            default -> {
                                // If a legal direction was not included, then let the user know what is a legal direction
                                System.out.println("Legal directions are");
                                System.out.println("[NORTH, EAST, SOUTH, WEST]");
                            }
                        }
                        break;
                    case "reload":
                        this.model.reload();
                        break;
                    case "load":
                        this.model.load(words[1]);
                        break;
                    case "show":
                        this.model.show();
                        break;
                    case "hint":
                        this.model.hint();
                        break;
                    default:
                        // If none of the other commands were inputted, then default to calling the PTUI only method, help
                        help();
                        break;
                }
            }
        }
    }

    private void initializeView() {
        this.model.addObserver(this);
        update(model, "");
    }

    /**
     * This method will print out a list of legal commands that the user can call
     */
    private void help() {
        System.out.println("Legal commands are...");
        System.out.println("\t\t> help : Show all commands.");
        System.out.println("\t\t> move {north|south|east|west}: Go in given direction, possible tipping a tower. (1 argument)");
        System.out.println("\t\t> reload filename: Load the most recent file again.");
        System.out.println("\t\t> load {board-file-name}: Load a new game board file. (1 argument)");
        System.out.println("\t\t> hint Make the next move for me.");
        System.out.println("\t\t> show Display the board.");
        System.out.println("\t\t> quit");
    }

    /**
     * This displays the board by simply calling the toString on the model (config)
     */
    private void displayBoard() {
        System.out.println(model.getGridString());
    }

    /**
     * Update works by displaying the board and then printing out what the user did
     * @param o - the model
     * @param arg - the arguments used to print
     */
    @Override
    public void update(TipOverModel o, Object arg) {
        displayBoard();
        System.out.println(arg);
    }

    private void quit() {
        System.exit(0);
    }

    public static void main(String[] args) {
        // Create and run an instance of the PTUI
        TipOverPTUI ptui = new TipOverPTUI(args);
        ptui.run();
    }
}

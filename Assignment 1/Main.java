import java.io.*;
import java.util.*;

/**
 * This is the main class which is the entry point for the library system application
 * It handles file input and output operations and coordinates the processing of library data with items and users files
 */

public class Main {

    /**
     * The main method which executes the programme
     *
     *
     * @param args Command line arguments:
     *             [0] - Path of items' data file
     *             [1] - Path of users' data file
     *             [2] - Path of commands' file
     *             [3] - Path of output file
     */
    public static void main(String[] args) {
        // Read file paths from command line
        String itemsFile = args[0];
        String usersFile = args[1];
        String commandsFile = args[2];
        String outputFile = args[3];

        // Redirect System.out to capture all output
        try (PrintStream fileOut = new PrintStream(new FileOutputStream(outputFile))) {
            // Save the original System.out
            PrintStream originalOut = System.out;

            // Redirect System.out to file
            System.setOut(fileOut);

            // Load data and process commands
            List<Item> items = ReadItems.readItemsFromFile(itemsFile);
            List<User> users = ReadUsers.readUsersFromFile(usersFile);

            CommandProcessor processor = new CommandProcessor();
            for (Item item : items) {
                processor.addItem(item);
            }
            for (User user : users) {
                processor.addUser(user);
            }

            processor.processCommands(commandsFile);

            // Restore original System.out
            System.setOut(originalOut);

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
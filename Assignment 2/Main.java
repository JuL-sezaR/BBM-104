import java.io.*;
import java.util.*;

/**
 * This is the main class, connecting every other class together
 * Loading animals, persons and foods using CommandProcessor's methods
 * Processes the commands from the input file and adds it on to the output
 * The writes the final output to the specified file
 */

public class Main {

    /**
     * This is the main method of the program
     * It works with the specification of files needed in the terminal(animals.txt, persons.txt, foods.txt, commands.txt, output.txt)
     *
     * @param args command line arguments for input and output files
     *
     */

    public static void main(String[] args) {

        /*
         * Loads animal data from the specified file.
         * Each line in the file should contain comma-separated values representing:
         * <ul>
         *     <li>Animal type</li>
         *     <li>Name</li>
         *     <li>Age</li>
         * </ul>
         * The method creates Animal objects using CommandProcessor and adds them to it.
         * It also appends information about each added animal to the output.
         *
         * @param fileName the path to the animals data file
         * @param processor the CommandProcessor instance to add animals to
         * @param output the StringBuilder accumulating output messages
         * @throws IOException if an I/O error occurs reading the file
         */

        String animalsFile = args[0];
        String personsFile = args[1];
        String foodsFile = args[2];
        String commandsFile = args[3];
        String outputFile = args[4];

        try {
            CommandProcessor processor = new CommandProcessor();

            StringBuilder output = new StringBuilder();

            output.append("***********************************\n")
                    .append("***Initializing Animal information***\n");
            loadAnimals(animalsFile, processor, output);

            output.append("***********************************\n")
                    .append("***Initializing Visitor and Personnel information***\n");
            loadPersons(personsFile, processor, output);

            output.append("***********************************\n")
                    .append("***Initializing Food Stock***\n");
            loadFoods(foodsFile, processor, output);


            processCommands(commandsFile, processor, output);

            writeOutput(outputFile, output.toString());

        } catch (IOException e) {
            System.out.println("Error processing command: " + e.getMessage());
        }
    }

    private static void loadAnimals(String fileName, CommandProcessor processor, StringBuilder output) throws IOException {
        /*
          Loads animals data from the specified file
          Creates animal objects using CommandProcessor and adds them to it
         */
        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    String[] tokens = line.split(",");
                    String type = tokens[0];
                    String name = tokens[1];
                    int age = Integer.parseInt(tokens[2]);

                    Animal animal = CommandProcessor.createAnimal(type, name, age);
                    processor.addAnimal(animal);

                    output.append("Added new " + animal.getType() + " with name " + animal.getName() + " aged " + animal.getAge() + ".\n");

                }
            }
        }
    }

    private static void loadPersons(String fileName, CommandProcessor processor, StringBuilder output) throws IOException {
        /*
        Loads persons from the specified file
        Creates person objects using CommandProcessor and adds them to it
         */
        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    String[] tokens = line.split(",");
                    String type = tokens[0];
                    String name = tokens[1];
                    int id = Integer.parseInt(tokens[2]);

                    Person person = CommandProcessor.createPerson(type, name, id);
                    processor.addPerson(person);

                    output.append("Added new " + person.getType() + " with id " + person.getId() + " and name " + person.getName() + ".\n");
                }
            }
        }
    }

    private static void loadFoods(String fileName, CommandProcessor processor, StringBuilder output) throws IOException {
        /*
        Loads food stock from the specified file
        Creates food objects using CommandProcessor and adds them to it
         */
        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    String[] tokens = line.split(",");
                    String type = tokens[0];
                    double amount = Double.parseDouble(tokens[1]);

                    Foods foods = CommandProcessor.createFood(type, amount);
                    processor.addFood(foods);

                    output.append("There are ")
                            .append(String.format("%.3f", foods.getAmount()))
                            .append(" kg of ")
                            .append(foods.getType())
                            .append(" in stock\n");
                }
            }
        }
    }

    private static void processCommands(String fileName, CommandProcessor processor, StringBuilder output) throws IOException {
        /*
        General method for processing commands, which reads the commands from the specified file
        with the help of CommandProcessor
         */
        try (Scanner scanner = new Scanner(new FileReader(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()) {
                    output.append("***********************************\n")
                           .append("***Processing new Command***\n");
                    try {
                        processor.processCommand(line, output);
                    } catch (ZooException e) {
                        output.append(e.getMessage()).append("\n");
                    }
                }
            }
        }
    }

    private static void writeOutput(String outputFile, String output) throws IOException {
        /*
        Writes the final output to the specified output file
         */
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))){
            writer.write(output);
        }
    }
}

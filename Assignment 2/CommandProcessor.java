import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class manages animals, people and foods
 * and processes commands like feeding animals, animal visitation and listing food stock
 * It has methods to create and add animals, persons and foods
 * Also handles command execution with error handling
 */

public class CommandProcessor {
    /*
    This class is generally for command execution, creation of all needed objects and processing of all commands
     */
    private ArrayList<Animal> animals = new ArrayList<>();
    private ArrayList<Person> people = new ArrayList<>();
    private ArrayList<Foods> foods = new ArrayList<>();

    public void processCommand(String command, StringBuilder output) throws ZooException {

        String[] tokens = command.split(",");
        String action = tokens[0];

        switch (action.toLowerCase()) {
            case "feed animal":
                feed(tokens, output);
                break;
            case "animal visitation":
                visit(tokens, output);
                break;
            case "list food stock":
                listFood(output);
                break;
            default:
                output.append("Unknown command: " + command)
                      .append("\n");
                break;
        }
    }

    private void feed(String[] tokens, StringBuilder output) throws ZooException {
        /*
        Handles the feed animal command with validation of person's ID and animal's name and checking food stock
         */
        String personIdStr = tokens[1];
        String animalName = tokens[2];
        double numOfMeals;

        try {
            numOfMeals = Double.parseDouble(tokens[3]);
        } catch (NumberFormatException e) {
            String fullComand = String.join(",", tokens);
            throw new InvalidCommandException("Error processing command: " + fullComand + "\nError: " + e.getMessage());
        }

        Person person = findPersonByID(personIdStr);
        Animal animal = findAnimalByName(animalName);

        if (person == null) {
            throw new InvalidPersonID("Error: There are no visitors or personnel with the id " + personIdStr);
        }

        if (animal == null) {
            throw new InvalidAnimal("Error: Animal not found: " + animalName);
        }

        if (person instanceof Personnel) {
            output.append(person.getName() + " attempts to feed " + animal.getName() + ".\n");
        }

        if (person instanceof Visitor) {
            output.append(person.getName() + " tried to feed " + animal.getName() + ".\n");
        }

        if (!(person instanceof Personnel)) {
            throw new VisitorsCantFeed("Error: Visitors do not have the authority to feed animals.");
        }

        double requiredFood = Math.round(animal.eat() * numOfMeals * 1000.0) / 1000.0;

        String foodType = animal.getFoodType();
        String[] foodTypes = foodType.split(" and ");


        for (String type : foodTypes) {
            Foods food = findFoodByType(type);
            if (food == null) {
                throw new NotEnoughStock("Error: No food stock available for " + type.trim());
            } else if (food.getAmount() < requiredFood) {
                throw new NotEnoughFood("Error: Not enough " + food.getType());
            }
        }

        for (String type : foodTypes) {
            Foods food = findFoodByType(type.trim());
            food.setAmount(food.getAmount() - requiredFood);
        }

        // Print feeding message
        switch (animal.getType()) {
            case "Lion":
                output.append(animal.getName() + " has been given " + String.format("%.3f", requiredFood) + " kgs of meat\n");
                break;
            case "Penguin":
                output.append(animal.getName() + " has been given " + String.format("%.3f", requiredFood) + " kgs of various kinds of fish\n");
                break;
            case "Elephant":
                output.append(animal.getName() + " has been given " + String.format("%.3f", requiredFood) + " kgs assorted fruits and hay\n");
                break;
            case "Chimpanzee":
                output.append(animal.getName() + " has been given " + String.format("%.3f", requiredFood) + " kgs of meat and " + String.format("%.3f", requiredFood) + " kgs of leaves\n");
                break;
            default:
                break;
        }
    }

    private Person findPersonByID(String personIdStr) {
        /*
        This method is for finding the person with the specified ID, if it does not exist
        a custom exception will be thrown
         */
        int personId = Integer.parseInt(personIdStr); // Convert String to int
        for (Person person : people) {
            if (person.getId() == personId) { // Comparing them in int
                return person;
            }
        }
        return null;
    }

    private Animal findAnimalByName(String animalName) {
        /*
        This method is for finding the animal with the specified name, if it does not exist
        a custom exception will be thrown
         */
        for (Animal animal : animals) {
            if (animal.getName().equals(animalName)) {
                return animal;
            }
        }
        return null;
    }

    private Foods findFoodByType(String type) {
        /*
        This method is to find food by the specific type, because every animal eats a different
        type of food
         */
        for (Foods food : foods) {
            if (food.getType().equalsIgnoreCase(type)) {
                return food;
            }
        }
        return null;
    }

    private void visit(String[] tokens, StringBuilder output) throws ZooException {
        /*
        This method is basically for the visit command, it handles both the exceptions in which
        there might not be the specified animal or person. Also prints the different interactions
        changing from an animal to other if a personnel visits them
         */
        String personIdStr = tokens[1];
        String animalName = tokens[2];

        Person person = findPersonByID(personIdStr);
        Animal animal = findAnimalByName(animalName);

        if (person == null) {
            throw new InvalidPersonID("Error: There are no visitors or personnel with the id " + personIdStr);
        }

        if (person instanceof Personnel) {
            output.append(person.getName() + " attempts to clean " + animalName+ "'s habitat.\n");
        }

        if (animal == null) {
            throw new InvalidAnimal("Error: There are no animals with the name " + animalName);
        }

        if (!(person instanceof Personnel)) {
            output.append(person.getName() + " tried to register for a visit to " + animal.getName() + ".\n");
            output.append(person.getName() + " successfully visited " + animal.getName() + ".\n");
        }

        if(person instanceof Personnel) {
            output.append(person.getName() + " started cleaning " + animal.getName() +"'s habitat.\n");

            switch(animal.getType()){
                case "Lion":
                    output.append("Cleaning " + animal.getName() + "’s habitat: Removing bones and refreshing sand.\n");
                    break;
                case "Elephant":
                    output.append("Cleaning " + animal.getName() + "’s habitat: Washing the water area.\n");
                    break;
                case "Penguin":
                    output.append("Cleaning " + animal.getName() + "’s habitat: Replenishing ice and scrubbing walls.\n");
                    break;
                case "Chimpanzee":
                    output.append("Cleaning " + animal.getName() + "’s habitat: Sweeping the enclosure and replacing branches.\n");
                    break;
                default:
                    break;
            }
        }

    }

    private void listFood(StringBuilder output) {

        /*
        This method is for the listing food stock command, it works with List and lists the current
        available food stock with the done actions and in the order it has to be
         */
        output.append("Listing available Food Stock:\n");

        // Define the desired order
        List<String> order = Arrays.asList("Plant", "Fish", "Meat");

        for (String type : order) {
            Foods food = findFoodByType(type);
            if (food != null) {
                output.append(food.getType()).append(": ")
                        .append(String.format("%.3f", food.getAmount())).append(" kgs\n");
            }
        }
    }

    // Add methods for adding the created objects into the List
    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public void addPerson(Person person) {
        people.add(person);
    }
    public void addFood(Foods food) {
        foods.add(food);
    }

    // Create methods for creating the objects specified in the given input files
    public static Animal createAnimal(String type, String name, int age) {
        switch(type){
            case "Lion":
                return new Lion(name, age);
            case "Elephant":
                return new Elephant(name, age);
            case "Penguin":
                return new Penguin(name, age);
            case "Chimpanzee":
                return new Chimpanzee(name, age);
            default:
                break;
        }
        return null;
    }
    public static Person createPerson(String type, String name, int id) {
        switch(type){
            case "Personnel":
                return new Personnel(name, id);
            case "Visitor":
                return new Visitor(name, id);
            default:
                break;
        }
        return null;
    }
    public static Foods createFood(String type, double amount) {
        switch(type){
            case "Meat":
                return new Meat(amount);
            case "Fish":
                return new Fish(amount);
            case "Plant":
                return new Plant(amount);
            default:
                break;
        }
        return null;
    }
}

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * The CommandProcessor class handles all operations including borrowing, returning, paying the penalty and displaying users/items.
 * It maintains collections of users and items and processes command files
 */
public class CommandProcessor {
    private Map<String, User> users = new HashMap<>();
    private Map<String, Item> items = new HashMap<>();

    /*
    Processes a file containing library commands line by line.
     */

    public void processCommands(String commandsFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(commandsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                processCommand(line);
            }
        } catch (IOException e) {
            System.out.println("Error while reading commands: " + e.getMessage());
        }
    }

    /*
    Parses and executes a single command from the commands file
     */

    private void processCommand(String command) {
        String[] tokens = command.split(",");
        String action = tokens[0];

        if (action.equals("borrow")) {
            borrowItem(tokens);
        } else if (action.equals("return")) {
            returnItem(tokens);
        } else if (action.equals("pay")) {
            payPenalty(tokens);
        } else if (action.equals("displayUsers")) {
            displayUsers();
        } else if (action.equals("displayItems")) {
            displayItems();
        }
    }

    /*
    Handles the borrowing of an item by a user with validation checks.
     */

    private void borrowItem(String[] tokens) {
        String userId = tokens[1];
        String itemId = tokens[2];
        String borrowDateStr = tokens[3];

        User user = users.get(userId);
        Item item = items.get(itemId);

        // Validate borrowing conditions
        if (!item.isAvailable()) {
            System.out.println(user.getUserName() + " cannot borrow " + item.getTitle() + ", it is not available!");
            return;
        }

        if (user.getPenalty() >= 6.0) {
            System.out.println(user.getUserName() + " cannot borrow " + item.getTitle() + ", you must first pay the penalty amount! 6$");
            return;
        }

        if (user.getBorrowedItems().size() >= user.getMaxItems()) {
            System.out.println(user.getUserName() + " cannot borrow " + item.getTitle() + ", since the borrow limit has been reached");
            return;
        }

        // Checking item type restrictions
        if (item.getType().equals("reference") && user instanceof Student) {
            System.out.println(user.getUserName() + " cannot borrow reference item!");
            return;
        }

        if (item.getType().equals("rare") && user instanceof Guest) {
            System.out.println(user.getUserName() + " cannot borrow rare item!");
            return;
        }

        if (item.getType().equals("limited") && user instanceof Guest) {
            System.out.println(user.getUserName() + " cannot borrow limited item!");
            return;
        }

        // Calculating and applying penalty if the item is not returned at the time they should've
        LocalDate borrowDate = LocalDate.parse(borrowDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate currentDate = LocalDate.now();
        long daysBorrowed = ChronoUnit.DAYS.between(borrowDate, currentDate) + 1;
        int maxDaysAllowed = user.getMaxDaysAllowed();
        long daysLate = Math.max(daysBorrowed - maxDaysAllowed, 0);

        if (daysLate > 0) {
            double penalty = 2.0;
            user.addPenalty(penalty);
            item.returnItem();
        }

        // Completing the borrowing process
        item.borrow(borrowDateStr, user.getUserName());
        user.borrowItem(item);
        System.out.println(user.getUserName() + " successfully borrowed! " + item.getTitle());
    }

    /*
    Handles the returning of an item by a user.
     */

    private void returnItem(String[] tokens) {
        String userId = tokens[1];
        String itemId = tokens[2];

        User user = users.get(userId);
        Item item = items.get(itemId);

        user.returnItem(item);
        item.returnItem();
        System.out.println(user.getUserName() + " successfully returned " + item.getTitle());
    }

    /*
    Processes a penalty payment by a user.
     */

    private void payPenalty(String[] tokens) {
        String userId = tokens[1];
        User user = users.get(userId);

        double paidAmount = 6.0;
        user.reducePenalty(paidAmount);
        System.out.println(user.getUserName() + " has paid penalty");
    }

    /*
    Displays all users' information sorted by their ID.
     */

    private void displayUsers() {
        System.out.println("\n");
        List<User> copy = new ArrayList<>(users.values());
        copy.sort(Comparator.comparing(User::getId));
        for (User user : copy) {
            user.displayUserDetails();
        }
    }

    /*
    Displays all items' information sorted by their ID.
     */

    private void displayItems() {
        System.out.println();
        List<Item> copy = new ArrayList<>(items.values());
        copy.sort(Comparator.comparing(Item::getId));
        for (Item item : copy) {
            item.displayItemDetails();
        }
    }

    /*
    Adds a user to the system.
     */

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    /*
    Adds an item to the system
     */

    public void addItem(Item item) {
        items.put(item.getId(), item);
    }
}

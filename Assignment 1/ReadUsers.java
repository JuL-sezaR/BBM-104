import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is for reading user information from files provided and creates User objects.
 */
public class ReadUsers {

    /**
     * Reads user data from text file and creates corresponding User objects.
     *
     * @param filePath Path of the text file
     * @return List of User objects created from the data provided
     * @throws IOException If there is an error reading the file
     *
     * @see Guest
     * @see Academic
     * @see Student
     *
     * Each line represents one user with comma seperated attributes:
     * - For Guests: G,name,id,phone,occupations
     * - For Academic Members: A,name,id,phone,department,faculty,title
     * - For Students: S,name,id,phone,departement,faculty,grade
     */
    public static List<User> readUsersFromFile(String filePath) throws IOException {
        List<User> users = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");

                String classType = parts[0];
                String userName = parts[1];
                String id = parts[2];
                String phoneNumber = parts[3];

                if (classType.equals("G")) { // If User = Guest
                    String occupation = parts[4];
                    users.add(new Guest(userName, id, phoneNumber, occupation));
                }
                else if (classType.equals("A")) { // If User = Academic Member
                    String department = parts[4];
                    String faculty = parts[5];
                    String title = parts[6];
                    users.add(new Academic(userName, id, phoneNumber, department, faculty, title));
                }
                else if (classType.equals("S")) { // If User = Student
                    String department = parts[4];
                    String faculty = parts[5];
                    String grade = parts[6];
                    users.add(new Student(userName, id, phoneNumber, department, faculty, grade));
                }
            }
        }
        return users;
    }
}

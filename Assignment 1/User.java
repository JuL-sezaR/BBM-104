/*
In this class I also used inheritance for better working of the user classes
 */
import java.util.ArrayList;
import java.util.List;

/**
 * Here I used abstract base class to represent a library user.
 * Implemented common user functionality and defined abstract methods for user-specific behaviour.
 * Inherited for different user types (Student, Academic Member and Guest).
 */
public abstract class User {
    private String userName;
    private String id;
    private String phoneNumber;
    private int maxItems;
    private double penalty;
    private List<Item> borrowedItems;

    /*
    Constructs a new User with basic information found on all Users.
     */

    public User(String userName, String id, String phoneNumber, int maxItems) {
        this.userName = userName;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.maxItems = maxItems;
        this.penalty = 0.0;
        this.borrowedItems = new ArrayList<>();
    }

    // Getter Methods
    public String getUserName() {
        return userName;
    }
    public String getId() {
        return id;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public int getMaxItems() {
        return maxItems;
    }
    public double getPenalty() {
        return penalty;
    }
    public List<Item> getBorrowedItems() {
        return borrowedItems;
    }
    // Penalty Management
    public void addPenalty(double amount) {
        this.penalty += amount;
    }
    public void reducePenalty(double amount) {
        this.penalty = Math.max(this.penalty - amount, 0);
    }
    // Methods for the borrowed and returned items
    public void borrowItem(Item item) {
        borrowedItems.add(item);
    }
    public void returnItem(Item item) {
        borrowedItems.remove(item);
    }
    public abstract int getMaxDaysAllowed();
    public abstract void displayUserDetails();
}

/*
Represents a Student inherited from User superclass.
 */
class Student extends User {
    private static final int maxDaysAllowed = 30;
    private String department;
    private String faculty;
    private String grade;

    /**
     * Creates a new Student User.
     *
     * @param userName Full name of the student
     * @param id Student ID
     * @param phoneNumber Student's Phone number
     * @param department Student's Academic Department
     * @param faculty Student's Faculty
     * @param grade Student's year of study
     */

    public Student( String userName, String id, String phoneNumber, String department, String faculty, String grade) {
        super(userName, id, phoneNumber, 5);
        this.department = department;
        this.faculty = faculty;
        this.grade = grade;

    }
    // Getter Methods
    public String getDepartment() {
        return department;
    }
    public String getFaculty() {
        return faculty;
    }
    public String getGrade() {
        return grade;
    }

    @Override
    public int getMaxDaysAllowed() {
        return maxDaysAllowed;
    }

    @Override
    public void displayUserDetails() {
        System.out.println("------ User Details for "+ getId() +" ------");
        System.out.println("Name: " + getUserName() + " Phone: " + getPhoneNumber());
        System.out.println("Faculty: " + getFaculty() + " Department: " + getDepartment() + " Grade: " + getGrade()+"th");
        System.out.println();
    }
}

/*
Represents an Academic Member inherited from User superclass
 */
class Academic extends User {
    private String department;
    private String faculty;
    private String title;
    private static final int maxDaysAllowed = 15;

    /**
     * Creates a new Academic user.
     *
     * @param userName Full name
     * @param id Academic Member's ID
     * @param phoneNumber Academic Member's phone number
     * @param department Academic Member's department
     * @param faculty Academic Member's faculty
     * @param title Academic Member's title
     */

    public Academic(String userName, String id, String phoneNumber, String department, String faculty, String title) {
        super(userName, id, phoneNumber, 3);
        this.department = department;
        this.faculty = faculty;
        this.title = title;
    }

    // Getter Methods
    public String getDepartment() {
        return department;
    }
    public String getFaculty() {
        return faculty;
    }
    public String getTitle() {
        return title;
    }

    @Override
    public int getMaxDaysAllowed() {
        return maxDaysAllowed;
    }

    @Override
    public void displayUserDetails() {
        System.out.println("------ User Information for "+ getId() +" ------");
        System.out.println("Name: " + getTitle() +" "+ getUserName() + " Phone: " + getPhoneNumber());
        System.out.println("Faculty: " + getFaculty() + " Department: " + getDepartment());
        System.out.println();
    }
}

/*
Represents a Guest user inherited from User superclass
 */
class Guest extends User {
    private String occupation;
    private static final int maxDaysAllowed = 7;

    /**
     * Creates a Guest user.
     *
     * @param userName Full name
     * @param id Guest's ID
     * @param phoneNumber Guest's phone number
     * @param occupation Guest's occupation
     */

    public Guest(String userName, String id, String phoneNumber, String occupation) {
        super(userName, id, phoneNumber, 1);
        this.occupation = occupation;
    }

    // Getter Methods
    public String getOccupation() {
        return occupation;
    }
    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }
    @Override
    public int getMaxDaysAllowed() {
        return maxDaysAllowed;
    }

    @Override
    public void displayUserDetails() {
        System.out.println("------ User Information for "+ getId() +" ------");
        System.out.println("Name: " + getUserName() + " Phone: " + getPhoneNumber());
        System.out.println("Occupation: " + getOccupation());
        System.out.println();
    }
}
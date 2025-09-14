import java.util.*;

/**
 * This class is representing the Person class, which is an abstract class overriding getDetails() method
 * It has two subclasses, Student and AcademicMember
 * Student class also implements the interface IGrade for better grading system
 */

public abstract class Person {
    private final int id;
    private final String name;
    private final String email;
    private final String department;

    public Person(int id, String name, String email, String department) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.department = department;
    }

    public abstract String getDetails();

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getDepartment() {
        return department;
    }

}

class Student extends Person implements IGrade {

    private final List<Course> enrolledCourses;
    private final Map<String, String> grades;

    public Student(int id, String name, String email, String department) {
        super(id, name, email, department);
        this.enrolledCourses = new ArrayList<>();
        this.grades = new HashMap<>();
    }

    public List<Course> getEnrolledCourses() {
        return Collections.unmodifiableList(enrolledCourses);
    }

    public List<Course> getSortedEnrolledCourses() {
        List<Course> sorted = new ArrayList<>(enrolledCourses);
        sorted.sort(Comparator.comparing(Course::getCode));
        return sorted;
    }

    public List<Course> getSortedCompletedCourses() {
        List<Course> completed = new ArrayList<>();
        for (Course course : enrolledCourses) {
            String grade = grades.get(course.getCode());
            if (grade != null && !grade.equals("N/A")) {
                completed.add(course);
            }
        }
        completed.sort(Comparator.comparing(Course::getCode));
        return completed;
    }

    @Override
    public String getDetails() {
        return "Student ID: " + getId() +
                "\nName: " + getName() +
                "\nEmail: " + getEmail() +
                "\nMajor: " + getDepartment() +
                "\nStatus: Active\n";
    }

    @Override
    public void assignGrade(String studentID, String grade) {
        this.grades.put(studentID, grade);
    }

    @Override
    public Map<String, String> getGrades() {
        return grades;
    }

    public boolean enrollInCourse(Course course) {
        if (course == null) {
            System.out.println("Error: Cannot enroll in null course.");
            return false;
        }

        if (enrolledCourses.contains(course)) {
            System.out.println("Error: Already enrolled in course " + course.getCode());
            return false;
        }

        boolean success = enrolledCourses.add(course);
        if (success) {
            course.enrollStudent(this); // Bidirectional update (optional but good practice)
        }

        return success;
    }

}

class AcademicMember extends Person {
    private final List<Course> teachingCourses;

    public AcademicMember(int id, String name, String email, String department) {
        super(id, name, email, department);
        this.teachingCourses = new ArrayList<>();
    }

    public void assignToCourse(Course course) {
        teachingCourses.add(course);
    }
    public void removeFromCourse(Course course) {
        teachingCourses.remove(course);
    }

    @Override
    public String getDetails() {
        return "Faculty ID: " + getId() +
                "\nName: " + getName() +
                "\nEmail: " + getEmail() +
                "\nDepartment: " + getDepartment() + "\n";

    }
}

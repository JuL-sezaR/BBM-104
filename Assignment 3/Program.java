import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents the Programs which will be created
 * Each program has required courses
 * Handles exceptions in cases like course is not found, etc.
 */

public class Program {
    private final String code;
    private final String name;
    private final String description;
    private final String department;
    private final String degreeLevel;
    private final int totalCredits;
    private final List<Course> requiredCourses;

    public Program(String code, String name, String description, String department, String degreeLevel, int totalCredits) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.department = department;
        this.degreeLevel = degreeLevel;
        this.totalCredits = totalCredits;
        this.requiredCourses = new ArrayList<>();
    }

    public void addRequiredCourse(Course course){
        if (course != null && !requiredCourses.contains(course)){
            requiredCourses.add(course);
        }
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDepartment() {
        return department;
    }

    public String getDegreeLevel() {
        return degreeLevel;
    }

    public int getTotalCredits() {
        return totalCredits;
    }

    public List<Course> getRequiredCourses() {
        return Collections.unmodifiableList(requiredCourses);
    }

    public String getProgramDetails() {
        StringBuilder sb = new StringBuilder();
        sb.append("Program Code: ").append(code).append("\n");
        sb.append("Name: ").append(name).append("\n");
        sb.append("Department: ").append(department).append("\n");
        sb.append("Degree Level: ").append(degreeLevel).append("\n");
        sb.append("Required Credits: ").append(totalCredits).append("\n");

        List<String> courseCodes = new ArrayList<>();
        for (Course course : requiredCourses) {
            courseCodes.add(course.toString());
        }

        sb.append("Courses: {").append(String.join(",", courseCodes)).append("\n");
        return sb.toString();
    }

}

import java.util.*;

/**
 * This class represents the Courses
 * Implements the IGrade interface for better grade management
 */

public class Course implements IGrade {
    private final String code;
    private final String name;
    private String description;
    private final int creditHours;
    private final String semester;
    private AcademicMember instructor;
    private final Map<String, String> grades;
    private final List<Student> enrolledStudents;
    private final String department;
    private final String programCode;

    /**
     * Constructs a new Course with the specified parameters.
     *
     * @param code the unique identifier for the course
     * @param name the name of the course
     * @param department the department offering the course
     * @param creditHours the number of credit hours the course is worth
     * @param semester the semester when the course is offered
     * @param programCode the program code this course belongs to
     */
    public Course(String code, String name, String department, int creditHours, String semester, String programCode) {
        this.code = code;
        this.name = name;
        this.department = department;
        this.creditHours = creditHours;
        this.semester = semester;
        this.programCode = programCode;
        this.enrolledStudents = new ArrayList<>();
        this.grades = new HashMap<>();
    }

    /**
     * Enrolls a student in this course.
     *
     * @param student the student to enroll
     * @return true if enrollment was successful, false if student is null or already enrolled
     */
    public boolean enrollStudent(Student student) {
        if (student == null) {
            return false;
        }

        if (enrolledStudents.contains(student)) {
            return false;
        }

        enrolledStudents.add(student);
        return true;
    }

    /**
     * Assigns an instructor to teach this course.
     *
     * @param academic the academic member to assign as instructor
     * @return true if assignment was successful, false if academic is null
     */
    public boolean assignInstructor(AcademicMember academic) {
        if (academic == null) {
            System.out.println("Error: Academic Member not found.");
            return false;
        }

        this.instructor = academic;
        academic.assignToCourse(this);

        return true;
    }

    // Getters and overriders

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getCreditHours() {
        return creditHours;
    }
    public String getSemester() {
        return semester;
    }
    public AcademicMember getInstructor() {
        return instructor;
    }
    public String getDepartment() {
        return department;
    }

    @Override
    public String toString() {
        return this.code;
    }

    @Override
    public void assignGrade(String studentID, String grade) {
        grades.put(studentID, grade);
    }

    @Override
    public Map<String, String> getGrades() {
        return Collections.unmodifiableMap(grades);
    }


    public String getGrade(String studentID) {
        return grades.get(studentID);
    }

    public List<Student> getEnrolledStudents() {
        return Collections.unmodifiableList(enrolledStudents);
    }

    public String getInfo() {
        String sb = "Course Code: " + code + "\n" +
                "Name: " + name + "\n" +
                "Department: " + department + "\n" +
                "Credits: " + creditHours + "\n" +
                "Semester: " + semester + "\n\n";
        return sb;
    }

}
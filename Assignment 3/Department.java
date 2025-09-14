import java.util.Collections;
import java.util.List;

/**
 * This class represents Department objects
 * Which you can assign a head to them
 */

public class Department {
    private final String code;
    private final String name;
    private final String description;
    private AcademicMember head;
    private List<Program> programs;
    private List<Course> courses;
    private List<AcademicMember> facultyMembers;
    private List<Student> students;

    public Department(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.head = null;
    }

    public void setHead(AcademicMember head) {
        if (head != null) {
            this.head = head;
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

    public AcademicMember getHead() {
        return head;
    }

    public List<Program> getPrograms() {
        return Collections.unmodifiableList(programs);
    }

    public List<Course> getCourses() {
        return Collections.unmodifiableList(courses);
    }

    public List<AcademicMember> getFacultyMembers() {
        return Collections.unmodifiableList(facultyMembers);
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public void addProgram(Program program) {
        programs.add(program);
    }

    public void addCourse(Course course) {
        courses.add(course);
    }

    public void addFacultyMember(AcademicMember facultyMember) {
        facultyMembers.add(facultyMember);
    }
    public void addStudent(Student student) {
        students.add(student);
    }

    public String getDetails() {
        return "Department Code: " + code +
                "\nName: " + name +
                "\nHead: " + (head != null ? head.getName() : "Not assigned") + "\n";
    }
}

import java.util.Map;

/**
 * This is the interface which I defined for better and easier grading system
 * It lets the program get grades easier
 * Implemented in Student and Course classes
 */

public interface IGrade {
    void assignGrade(String studentID, String grade);
    Map<String, String> getGrades();
}

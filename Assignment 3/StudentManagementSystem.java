import java.util.*;

/**
 * This class represents the management system of this program
 * Manages students, academic members, courses, departments, etc.
 */

public class StudentManagementSystem {
    private final List<Student> students;
    private final List<AcademicMember> facultyMembers;
    private final List<Course> courses;
    private final List<Department> departments;
    private final List<Program> programs;

    /**
     * Constructs a new StudentManagementSystem with empty collections for all entities.
     */
    public StudentManagementSystem() {
        this.students = new ArrayList<>();
        this.facultyMembers = new ArrayList<>();
        this.courses = new ArrayList<>();
        this.departments = new ArrayList<>();
        this.programs = new ArrayList<>();
    }

    /**
     * Adds a student to the system.
     *
     * @param student the student to add
     */
    public void addStudent(Student student) {
        students.add(student);
    }

    /**
     * Gets an unmodifiable list of all students in the system.
     *
     * @return list of all students
     */
    public List<Student> getAllStudents() {
        return Collections.unmodifiableList(students);
    }

    /**
     * Adds a faculty member to the system.
     *
     * @param academicMember the academic member to add
     */
    public void addAcademicMember(AcademicMember academicMember) {
        facultyMembers.add(academicMember);
    }

    /**
     * Gets an unmodifiable list of all faculty members in the system.
     *
     * @return list of all faculty members
     */
    public List<AcademicMember> getAllFaculty() {
        return Collections.unmodifiableList(facultyMembers);
    }

    /**
     * Adds a course to the system.
     *
     * @param course the course to add
     */
    public void addCourse(Course course) {
        courses.add(course);
    }

    /**
     * Gets an unmodifiable list of all courses in the system.
     *
     * @return list of all courses
     */
    public List<Course> getAllCourses() {
        return Collections.unmodifiableList(courses);
    }

    /**
     * Adds a department to the system.
     *
     * @param department the department to add
     */
    public void addDepartment(Department department) {
        departments.add(department);
    }

    /**
     * Gets an unmodifiable list of all departments in the system.
     *
     * @return list of all departments
     */
    public List<Department> getAllDepartments() {
        return Collections.unmodifiableList(departments);
    }

    /**
     * Adds a program to the system.
     *
     * @param program the program to add
     */
    public void addProgram(Program program) {
        programs.add(program);
    }

    /**
     * Gets an unmodifiable list of all programs in the system.
     *
     * @return list of all programs
     */
    public List<Program> getAllPrograms() {
        return Collections.unmodifiableList(programs);
    }

    /**
     * Finds a student by their ID.
     *
     * @param studentID the ID of the student to find
     * @return the Student object if found, null otherwise
     */
    public Student findStudentByID(String studentID){
        int studentIDInt = Integer.parseInt(studentID);
        for (Student student : students) {
            if (student.getId() == studentIDInt) {
                return student;
            }
        }
        return null;
    }

    /**
     * Finds an academic member by their ID.
     *
     * @param academicMemberID the ID of the academic member to find
     * @return the AcademicMember object if found, null otherwise
     */
    public AcademicMember findAcademicMemberByID(String academicMemberID){
        int academicMemberIDInt = Integer.parseInt(academicMemberID);
        for (AcademicMember academicMember : facultyMembers) {
            if (academicMember.getId() == academicMemberIDInt) {
                return academicMember;
            }
        }
        return null;
    }

    /**
     * Finds a course by its code.
     *
     * @param code the course code to search for
     * @return the Course object if found, null otherwise
     */
    public Course findCourseByCode(String code) {
        for (Course course : courses) {
            if (course.getCode().equals(code)) {
                return course;
            }
        }
        return null;
    }

    /**
     * Finds a program by its code.
     *
     * @param programCode the program code to search for
     * @return the Program object if found, null otherwise
     */
    public Program findProgramByCode(String programCode){
        for (Program program : programs) {
            if (program.getCode().equals(programCode)) {
                return program;
            }
        }
        return null;
    }

    /**
     * Enrolls a student in a course.
     *
     * @param studentID the ID of the student to enroll
     * @param courseCode the code of the course to enroll in
     */
    public void enrollStudent(String studentID, String courseCode) {
        Student student = findStudentByID(studentID);
        Course course = findCourseByCode(courseCode);

        if (student == null || course == null) {
            return;
        }

        boolean isEnrolled = course.enrollStudent(student);
        if (!isEnrolled) {
            return;
        }

        student.enrollInCourse(course);
    }

    /**
     * Assigns an academic member to teach a course.
     *
     * @param academicID the ID of the academic member
     * @param courseCode the code of the course to assign
     */
    public void assignAcademicToCourse(String academicID, String courseCode) {
        AcademicMember academic = findAcademicMemberByID(academicID);
        Course course = findCourseByCode(courseCode);

        if (academic == null || course == null) {
            return;
        }

        boolean isAssigned = course.assignInstructor(academic);
        if (!isAssigned) {
            return;
        }

        academic.assignToCourse(course);
    }

    /**
     * Automatically assigns courses to programs based on course codes.
     * Courses are assigned to programs whose code matches the beginning of the course code.
     */
    public void assignCoursesToPrograms() {
        for (Program program : getAllPrograms()) {
            String programCode = program.getCode();
            for (Course course : getAllCourses()) {
                if (course.getCode().startsWith(programCode)) {
                    program.addRequiredCourse(course);
                }
            }
        }
    }

    /**
     * Assigns a grade to a student for a specific course.
     *
     * @param letterGrade the grade to assign (must be a valid letter grade)
     * @param studentID the ID of the student
     * @param courseCode the code of the course
     */
    public void assignGrade(String letterGrade, String studentID, String courseCode) {
        Course course = findCourseByCode(courseCode);
        Student student = findStudentByID(studentID);

        if (course == null) {
            System.err.println("Error: Course with code " + courseCode + " not found.");
            return;
        }

        if (student == null) {
            System.err.println("Error: Student with ID " + studentID + " not found.");
            return;
        }

        if (!LetterGrade.isValid(letterGrade)) {
            System.err.println("Error: Invalid grade '" + letterGrade + "'.");
            return;
        }

        course.assignGrade(studentID, letterGrade);
    }

    public String generateCourseReport(String courseCode) throws StudentManagerExceptions {
        Course course = findCourseByCode(courseCode);
        if (course == null) {
            throw new NonExistentCourseException("Course " + courseCode + " not found.");
        }

        Map<String, String> gradesMap = course.getGrades();
        List<Student> enrolledStudents = course.getEnrolledStudents();
        AcademicMember instructor = course.getInstructor();

        int totalStudents = enrolledStudents.size();
        double totalPoints = 0.0;
        int gradedCount = 0;

        Map<String, Integer> gradeDistribution = new HashMap<>();
        StringBuilder studentList = new StringBuilder();

        // Build list of enrolled students
        for (Student student : enrolledStudents) {
            String studentID = String.valueOf(student.getId());
            String grade = gradesMap.getOrDefault(studentID, "N/A");

            studentList.append("- ").append(student.getName())
                    .append(" (ID: ").append(studentID).append(")\n");

            if (!grade.equals("N/A")) {
                LetterGrade letterGrade = LetterGrade.fromString(grade);
                if (letterGrade != null) {
                    totalPoints += letterGrade.getValue();
                    gradedCount++;
                    gradeDistribution.put(grade, gradeDistribution.getOrDefault(grade, 0) + 1);
                }
            }
        }

        double averageGrade = (gradedCount > 0) ? totalPoints / gradedCount : 0.0;

        // Build final report string
        StringBuilder report = new StringBuilder();
        report.append("Course Code: ").append(course.getCode()).append("\n");
        report.append("Name: ").append(course.getName()).append("\n");
        report.append("Department: ").append(course.getDepartment()).append("\n");
        report.append("Credits: ").append(course.getCreditHours()).append("\n");
        report.append("Semester: ").append(course.getSemester()).append("\n\n");
        report.append("Instructor: ")
                .append(instructor != null ? instructor.getName() : "Not assigned")
                .append("\n\n");

        report.append("Enrolled Students:\n");
        if (!enrolledStudents.isEmpty()) {
            report.append(studentList);
        } else {
            report.append("No students enrolled\n");
        }

        report.append("\nGrade Distribution:\n");
        if (!gradeDistribution.isEmpty()) {
            for (Map.Entry<String, Integer> entry : gradeDistribution.entrySet()) {
                report.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
            }
        } else {
            report.append("None\n");
        }

        report.append(String.format("Average Grade: %.2f%n", averageGrade));
        report.append("\n----------------------------------------\n");

        return report.toString();
    }

    public String generateStudentReport(String studentID) throws StudentManagerExceptions {
        Student student = findStudentByID(studentID);
        if (student == null) {
            throw new NonExistentStudentException("Student Not Found with ID " + studentID);
        }

        List<Course> enrolledCourses = student.getEnrolledCourses();
        Map<String, String> gradesMap = new HashMap<>();

        double totalPoints = 0.0;
        int totalCredits = 0;

        // Build grade map and calculate GPA
        for (Course course : student.getSortedEnrolledCourses()) {
            String gradeStr = course.getGrade(studentID);

            if (gradeStr == null || gradeStr.equals("N/A")) {
                continue; // skip ungraded courses
            }

            LetterGrade letterGrade = LetterGrade.fromString(gradeStr);
            if (letterGrade == null) {
                throw new InvalidLetterGradeException("Invalid letter grade: " + gradeStr);
            }

            int creditHours = course.getCreditHours();
            totalPoints += letterGrade.getValue() * creditHours;
            totalCredits += creditHours;
            gradesMap.put(course.getCode(), gradeStr);
        }

        double gpa = (totalCredits > 0) ? totalPoints / totalCredits : 0.0;

        // Build report string
        StringBuilder report = new StringBuilder();
        report.append("Student ID: ").append(studentID).append("\n");
        report.append("Name: ").append(student.getName()).append("\n");
        report.append("Email: ").append(student.getEmail()).append("\n");
        report.append("Major: ").append(student.getDepartment()).append("\n");
        report.append("Status: Active\n");

        report.append("\nEnrolled Courses:\n");
        if (!enrolledCourses.isEmpty()) {
            for (Course course : student.getSortedEnrolledCourses()) {
                report.append("- ").append(course.getName()).append(" (").append(course.getCode()).append(")\n");
            }
        } else {
            report.append("None\n");
        }

        report.append("\nCompleted Courses:\n");
        if (!gradesMap.isEmpty()) {
            for (Map.Entry<String, String> entry : gradesMap.entrySet()) {
                String courseCode = entry.getKey();
                String grade = entry.getValue();
                Course course = findCourseByCode(courseCode);
                String courseName = course != null ? course.getName() : "Unknown";
                report.append("- ").append(courseName).append(" (").append(courseCode).append("): ").append(grade).append("\n");
            }
        } else {
            report.append("None\n");
        }

        report.append(String.format("\nGPA: %.2f%n", gpa));
        report.append("\n----------------------------------------\n");

        return report.toString();
    }

}

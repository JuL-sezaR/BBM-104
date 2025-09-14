import java.io.*;

/**
 * This is the main class of this program, which easily handles everything needed
 * Handles file i/o operations and coordinates the system's workflow
 */

public class Main {
    /**
     * The main entry point for the application.
     *
     * @param args Command line arguments containing file paths in this order:
     *             0: persons file, 1: departments file, 2: programs file,
     *             3: courses file, 4: assignments file, 5: grades file,
     *             6: output file
     * @throws StudentManagerExceptions if any system operation fails
     * @throws IOException if any file operation fails
     */
    public static void main(String[] args) throws StudentManagerExceptions, IOException {
        String personsFile = args[0];
        String departmentsFile = args[1];
        String programsFile = args[2];
        String coursesFile = args[3];
        String assignmentsFile = args[4];
        String gradesFile = args[5];
        String outputFile = args[6];

        StudentManagementSystem manager = new StudentManagementSystem();
        try{
            loadPersons(manager, personsFile);
            loadDepartments(manager, departmentsFile);
            loadPrograms(manager, programsFile);
            loadCourses(manager, coursesFile);
            loadAssignments(manager, assignmentsFile);
            loadGrades(manager, gradesFile);

            manager.assignCoursesToPrograms();

            writeReportsToFile(manager, outputFile);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Logs a system message to the internal log buffer.
     *
     * @param message the message to log
     */
    private static final StringBuilder systemLog = new StringBuilder();

    private static void log(String message) {
        systemLog.append(message).append("\n");
    }

    private static String getLog() {
        return systemLog.toString();
    }

    /**
     * Writes all system reports to the specified output file.
     * Includes academic members, students, departments, programs, courses,
     * course reports, and student reports.
     *
     * @param manager the StudentManagementSystem instance containing all data
     * @param filename the path of the output file
     * @throws IOException if file writing fails
     */
    private static void writeReportsToFile(StudentManagementSystem manager, String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {

            writer.write(getLog());

            writer.write("----------------------------------------\n");
            writer.write("            Academic Members\n");
            writer.write("----------------------------------------\n");

            for (AcademicMember faculty : manager.getAllFaculty()) {
                writer.write(faculty.getDetails() + "\n");
            }

            writer.write("----------------------------------------\n\n");

            // --- Students ---
            writer.write("----------------------------------------\n");
            writer.write("                STUDENTS\n");
            writer.write("----------------------------------------\n");

            for (Student student : manager.getAllStudents()) {
                writer.write(student.getDetails() + "\n");
            }

            writer.write("----------------------------------------\n\n");

            // --- Departments ---
            writer.write("----------------------------------------\n");
            writer.write("              DEPARTMENTS\n");
            writer.write("----------------------------------------\n");

            for (Department department : manager.getAllDepartments()) {
                writer.write(department.getDetails() + "\n");
            }

            writer.write("----------------------------------------\n\n");

            // --- Programs ---
            writer.write("----------------------------------------\n");
            writer.write("                PROGRAMS\n");
            writer.write("----------------------------------------\n");

            for (Program program : manager.getAllPrograms()) {
                writer.write(program.getProgramDetails() + "\n");
            }

            writer.write("----------------------------------------\n\n");

            // --- Courses ---
            writer.write("----------------------------------------\n");
            writer.write("                COURSES\n");
            writer.write("----------------------------------------\n");

            for (Course course : manager.getAllCourses()) {
                String courseInfo = course.getInfo();
                writer.write(courseInfo);
            }

            writer.write("----------------------------------------\n\n");

            writer.write("----------------------------------------\n");
            writer.write("             COURSE REPORTS\n");
            writer.write("----------------------------------------\n");

            for (Course course : manager.getAllCourses()) {
                try {
                    String report = manager.generateCourseReport(course.getCode());
                    writer.write(report + "\n");
                } catch (StudentManagerExceptions e) {
                    System.err.println("Failed to generate report for course " + course.getCode() + ": " + e.getMessage());
                }
            }


            // --- Student Reports --- (removed duplicate section)
            writer.write("----------------------------------------\n");
            writer.write("             STUDENT REPORTS\n");
            writer.write("----------------------------------------\n");

            for (Student student : manager.getAllStudents()) {
                try {
                    String report = manager.generateStudentReport(String.valueOf(student.getId()));
                    writer.write(report + "\n");
                } catch (StudentManagerExceptions e) {
                    System.err.println("Error generating report for student " + student.getId() + ": " + e.getMessage());
                }
            }
        }
    }

    /**
     * Loads person data (students and faculty) from a file into the system.
     *
     * @param manager the StudentManagementSystem to populate
     * @param filename the path of the persons data file
     * @throws IOException if file reading fails
     * @throws StudentManagerExceptions if data validation fails
     */
    private static void loadPersons(StudentManagementSystem manager, String filename) throws IOException, StudentManagerExceptions {

        log("Reading Person Information");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String type = tokens[0];
                int id = Integer.parseInt(tokens[1]);
                String name = tokens[2];
                String email = tokens[3];
                String department = tokens[4];

                if (type.equals("S")){
                    Student student = new Student(id, name, email, department);
                    manager.addStudent(student);
                } else if (type.equals("F")){
                    AcademicMember faculty = new AcademicMember(id, name, email, department);
                    manager.addAcademicMember(faculty);
                } else throw new InvalidPersonException("Invalid Person Type");
            }

        }catch (StudentManagerExceptions e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Loads department data from a file into the system.
     *
     * @param manager the StudentManagementSystem to populate
     * @param filename the path of the departments data file
     * @throws IOException if file reading fails
     * @throws StudentManagerExceptions if data validation fails
     */
    private static void loadDepartments(StudentManagementSystem manager, String filename) throws IOException, StudentManagerExceptions {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

            log("Reading Departments ");

            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                String code = tokens[0];
                String name = tokens[1];
                String description = tokens[2];
                String headID = tokens[3];

                // Create department
                Department department = new Department(code, name, description);

                // Find academic member by ID
                AcademicMember head = manager.findAcademicMemberByID(headID);

                if (head != null) {
                    department.setHead(head);
                } else throw new NonExistentAcademicException("Academic Member Not Found with ID " + headID);

                manager.addDepartment(department);
            }
        }catch (StudentManagerExceptions e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads program data from a file into the system.
     *
     * @param manager the StudentManagementSystem to populate
     * @param filename the path of the programs data file
     * @throws IOException if file reading fails
     */
    private static void loadPrograms(StudentManagementSystem manager, String filename) throws IOException {

        log("Reading Programs");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                String code = tokens[0];
                String name = tokens[1];
                String description = tokens[2];
                String department = tokens[3];
                String degreeLevel = tokens[4];
                int totalCredits = Integer.parseInt(tokens[5]);

                Program program = new Program(code, name, description, department, degreeLevel, totalCredits);
                manager.addProgram(program);

            }
        }
    }

    /**
     * Loads course data from a file into the system.
     *
     * @param manager the StudentManagementSystem to populate
     * @param filename the path of the courses data file
     * @throws IOException if file reading fails
     * @throws StudentManagerExceptions if data validation fails
     */
    private static void loadCourses(StudentManagementSystem manager, String filename) throws IOException {

        log("Reading Courses");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                String code = tokens[0];
                String name = tokens[1];
                String department = tokens[2];
                int credits = Integer.parseInt(tokens[3]);
                String semester = tokens[4];
                String programCode = tokens[5];

                Program program = manager.findProgramByCode(programCode);

                if (program == null) {
                    throw new NonExistentProgramException("Program " + programCode + " not found.");
                }

                Course course = new Course(code, name, department, credits, semester, programCode);
                manager.addCourse(course);

            }
        } catch (StudentManagerExceptions e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads assignment data (course enrollments and instructor assignments) from a file into the system.
     *
     * @param manager the StudentManagementSystem to populate
     * @param filename the path of the assignments data file
     * @throws IOException if file reading fails
     * @throws StudentManagerExceptions if data validation fails
     */
    private static void loadAssignments(StudentManagementSystem manager, String filename) throws IOException, StudentManagerExceptions {

        log("Reading Course Assignments");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                String personType = tokens[0];
                String id = tokens[1];
                String courseCode = tokens[2];

                if (personType.equals("F")){

                    AcademicMember faculty = manager.findAcademicMemberByID(id);
                    Course course = manager.findCourseByCode(courseCode);

                    if (faculty == null) {
                        throw new NonExistentAcademicException("Academic Member Not Found with ID " + id);
                    }
                    if (course == null) {
                        throw new NonExistentCourseException("Course " + courseCode + " Not Found");
                    }

                    manager.assignAcademicToCourse(id, courseCode);
                } else if (personType.equals("S")){

                    Student student = manager.findStudentByID(id);
                    Course course = manager.findCourseByCode(courseCode);

                    if (student == null) {
                        throw new NonExistentStudentException("Student Not Found with ID " + id);
                    }
                    if (course == null) {
                        throw new NonExistentCourseException("Course " + courseCode + " Not Found");
                    }
                    manager.enrollStudent(id, courseCode);
                }

            }
        }
    }

    /**
     * Loads grade data from a file into the system.
     *
     * @param manager the StudentManagementSystem to populate
     * @param filename the path of the grades data file
     * @throws IOException if file reading fails
     * @throws StudentManagerExceptions if data validation fails
     */
    private static void loadGrades(StudentManagementSystem manager, String filename) throws IOException, StudentManagerExceptions {

        log("Reading Grades");

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");

                String letterGrade = tokens[0];
                String studentID = tokens[1];
                String courseCode = tokens[2];

                Course course = manager.findCourseByCode(courseCode);
                Student student = manager.findStudentByID(studentID);


                if (course == null) {
                    throw new NonExistentCourseException("Course " + courseCode + " Not Found");
                }
                if (student == null) {
                    throw new NonExistentStudentException("Student " + studentID + " Not Found");
                }
                if (!LetterGrade.isValid(letterGrade)){
                    throw new InvalidLetterGradeException("The grade " + letterGrade + " is not valid");
                }
                manager.assignGrade(letterGrade, studentID, courseCode);

            }
        }
    }


}

/**
 * This is the custom Exceptions I defined for this program
 * Which covers every possible error as specified in the pdf
 * Every Exception is quite self-explanatory
 */

public class StudentManagerExceptions extends Exception {
    String message;
    public StudentManagerExceptions(String message) {
        super(message);
    }
}

class InvalidPersonException extends StudentManagerExceptions {
    public InvalidPersonException(String message) {
        super(message);
    }
}

class NonExistentStudentException extends StudentManagerExceptions {
    public NonExistentStudentException(String message) {
        super(message);
    }
}

class NonExistentAcademicException extends StudentManagerExceptions {
    public NonExistentAcademicException(String message) {
        super(message);
    }
}

class NonExistentDepartmentException extends StudentManagerExceptions {
    public NonExistentDepartmentException(String message) {
        super(message);
    }
}

class NonExistentProgramException extends StudentManagerExceptions {
    public NonExistentProgramException(String message) {
        super(message);
    }
}

class NonExistentCourseException extends StudentManagerExceptions {
    public NonExistentCourseException(String message) {
        super(message);
    }
}

class InvalidLetterGradeException extends StudentManagerExceptions {
    public InvalidLetterGradeException(String message) {
        super(message);
    }
}
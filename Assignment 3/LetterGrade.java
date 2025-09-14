/**
 * Represents the letter grade system used for academic grading.
 * Each grade has a corresponding numeric value used for GPA calculations.
 */
public enum LetterGrade {
    A1(4.00),
    A2(3.50),
    B1(3.00),
    B2(2.50),
    C1(2.00),
    C2(1.50),
    D1(1.00),
    D2(0.50),
    F3(0.00);

    private final double value;

    /**
     * Constructs a LetterGrade with the specified numeric value.
     *
     * @param value the numeric value associated with the grade
     */
    LetterGrade(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static boolean isValid(String grade) {
        try {
            valueOf(grade);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static LetterGrade fromString(String grade) {
        try {
            return valueOf(grade);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}

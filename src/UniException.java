// Utility class for handling custom exceptions in the university system.
public class UniException {

    // Static method to throw a custom exception when a course is full.
    public static void throwCourseFullException(String courseTitle) throws CourseFullException {
        throw new CourseFullException("Cannot register for " + courseTitle + ": Course is full.");  // Throw a CourseFullException with a message
    }

    // Static method to throw a generic exception for invalid login attempts.
    public static void throwInvalidLoginException() throws Exception {
        throw new Exception("Invalid credentials. Please try again.");  // Throw a general Exception with a message
    }
}
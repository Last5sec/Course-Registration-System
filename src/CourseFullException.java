
// Custom exception class to handle scenarios where a course is full.
public class CourseFullException extends Exception {
    // Constructor that takes a message and passes it to the Exception superclass.
    public CourseFullException(String message) {
        super(message);  // Call the parent class constructor with the custom message
    }
}
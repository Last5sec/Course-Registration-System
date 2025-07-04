// Generic class to represent feedback for a course, where T can be any type (e.g., String, Integer, etc.).
class Feedback<T> {
    private Student student;  // Student who gave the feedback
    private Course course;    // Course the feedback is associated with
    private T feedback;       // Generic feedback, could be textual, numeric, etc.

    // Constructor to initialize a feedback object with a student, course, and feedback of any type.
    public Feedback(Student student, Course course, T feedback) {
        this.student = student;
        this.course = course;
        this.feedback = feedback;
    }

    // Method to display the feedback, regardless of its type.
    public void displayFeedback() {
        System.out.println(" - Feedback: " + feedback);
    }
}
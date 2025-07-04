import java.util.List;

// TA (Teaching Assistant) class extends Student. TAs have additional responsibilities, such as managing grades.
class TA extends Student {
    private List<Course> assignedCourses; // Courses that the TA is assigned to assist with

    // Constructor to initialize a TA object with username, password, contact info, and assigned courses.
    public TA(String username, String password, String contactInfo, List<Course> assignedCourses) {
        super(username, password, contactInfo);  // Call the constructor of the parent Student class
        this.assignedCourses = assignedCourses;  // Set the list of courses the TA is assigned to
    }

    // Method to get the list of assigned courses.
    public List<Course> getAssignedCourses() {
        return assignedCourses;
    }

    // Method to allow the TA to update a student's grade for a course they are assigned to.
    public void updateStudentGrade(Student student, double newGrade, Course course) {
        // Check if the TA is assigned to the course.
        if (assignedCourses.contains(course)) {
            // Iterate through enrolled students in the course to find the student.
            for(Student s : course.enrolledStudents) {
                // If the student is found, update their grade.
                if(s == student) {
                    student.grades.put(course, newGrade);  // Update the student's grade for the course
                    System.out.println("Updated " + student.name + "'s grade to: " + newGrade + " for course: " + course.title);
                }
            }
        } else {
            // If the TA is not assigned to the course, print an error message.
            System.out.println("TA is not assigned to this course.");
        }
    }
}

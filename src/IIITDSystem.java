import java.util.*;

abstract class User {
    protected String name;
    protected String password;
    public User(String password){
        this.password = password;
        this.name = "Admin";
    }
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public boolean login(String name, String password) {
        return this.name.equals(name) && this.password.equals(password);
    }

}

class Student extends User {
    private String contactInfo;
    public Map<Course, Double> grades = new HashMap<>(); 
    public List<Course> registeredCourses = new ArrayList<>();

    public Student(String username, String password, String contactInfo) {
        super(username, password);
        this.contactInfo = contactInfo;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void viewAvailableCourses(List<Course> courses) {
        System.out.println("Available Courses:");
        for (Course course : courses) {
            if(!course.enrolledStudents.contains(this)) {
                course.displayCourse();
            }
        }
    }

    public void registerForCourse(Course course) throws CourseFullException {
        if (registeredCourses.contains(course)) {
            System.out.println("Already registered for this course.");
            return;
        }

        course.registerStudent(this);
        registeredCourses.add(course);
        System.out.println("Registered for course: " + course.title);
    }

    public void viewGrades() {
        System.out.println("Your Grades:");
        for (Course course : registeredCourses) {
            Double grade = grades.get(course);
            if (grade != null) {
                System.out.println("Course: " + course.title + ", Grade: " + grade);
            } else {
                System.out.println("Course: " + course.title + ", Grade: Not Assigned");
            }
        }
    }

    public void dropCourse(Course course) {
        if (registeredCourses.contains(course)) {
            registeredCourses.remove(course);
            course.enrolledStudents.remove(this);
            System.out.println("Dropped course: " + course.title);
        } else {
            System.out.println("You are not registered for this course.");
        }
    }

    public double calculateCGPA() {
        double totalGradePoints = 0.0;
        double totalCredits = 0.0;

        for (Course course : registeredCourses) {
            Double grade = grades.get(course);
            if (grade != null) {
                totalGradePoints += grade * course.credits;
                totalCredits += course.credits;
            }
        }

        return totalCredits > 0 ? totalGradePoints / totalCredits : 0.0;
    }

    public void submitFeedback(Course course, String feedback) {
        if (grades.containsKey(course)) {
            course.addFeedback(this, feedback);
            System.out.println("Feedback submitted for course: " + course.title);
        } else {
            System.out.println("Feedback can only be submitted for completed courses.");
        }
    }
}


class Professor extends User {
    List<Course> courses;

    public Professor(String name, String password) {
        super(name, password);
        this.courses = new ArrayList<>();
    }

    public void viewAndManageCourses() {
        System.out.println("Courses you teach:");
        for (Course course : courses) {
            course.displayCourse();
        }
    }

    public void updateCourse(Course course, String timing, int credits, int enrollmentLimit) {
        course.timing = timing;
        course.credits = credits;
        course.enrollmentLimit = enrollmentLimit;
        System.out.println("Course updated: " + course.title);
    }

    public void viewFeedback(Course course) {
        System.out.println("Feedback for course: " + course.title);
        course.viewFeedback();
    }

}

class Course {
    String courseCode;
    String title;
    String professor;
    int credits;
    String timing;
    int enrollmentLimit;
    List<Student> enrolledStudents;
    List<Feedback<?>> feedbackList;

    public Course(String courseCode, String title, String professor, int credits, String timing, int enrollmentLimit) {
        this.courseCode = courseCode;
        this.title = title;
        this.professor = professor;
        this.credits = credits;
        this.timing = timing;
        this.enrollmentLimit = enrollmentLimit;
        this.enrolledStudents = new ArrayList<>();
        this.feedbackList = new ArrayList<>();
    }

    public void displayCourse() {
        System.out.println("Course Code: " + courseCode + ", Title: " + title + ", Professor: " + professor + ", Credits: " + credits + ", Enrollment Limit: " + enrollmentLimit);
    }

    public void registerStudent(Student student) throws CourseFullException {
        if (enrolledStudents.size() >= enrollmentLimit) {
            UniException.throwCourseFullException(title);
        }
        enrolledStudents.add(student);
        System.out.println(student.name + " successfully registered for " + title);
    }

    public <T> void addFeedback(Student student, T feedbackData) {
        Feedback<T> feedbackEntry = new Feedback<>(student, this, feedbackData);
        feedbackList.add(feedbackEntry);
        System.out.println("Feedback added for course: " + title);
    }

    public void viewFeedback() {
        System.out.println("Feedback for course: " + title);
        if (feedbackList.isEmpty()) {
            System.out.println("No feedback available.");
        } else {
            for (Feedback<?> fb : feedbackList) {
                fb.displayFeedback();
            }
        }
    }
}

class Administrator extends User {

    public Administrator() {
        super("123");
    }

    public boolean login(String password) {
        return this.password.equals(password);
    }

    public void viewCourses(List<Course> courses) {
        System.out.println("Course Catalog:");
        for (Course course : courses) {
            course.displayCourse();
        }
    }

    public void addCourse(List<Course> courses, Course newCourse) {
        courses.add(newCourse);
        System.out.println("Course added to catalog: " + newCourse.title);
    }

    public void deleteCourse(List<Course> courses, Course courseToRemove) {
        courses.remove(courseToRemove);
        System.out.println("Course deleted from catalog: " + courseToRemove.title);
    }

    public void updateStudentGrade(Student student, double newGrade, Course course) {
        student.grades.put(course, newGrade);
        System.out.println("Updated " + student.name + "'s grade to: " + newGrade + " for course: " + course.title);
    }

    public void viewStudentProfile(Student student) {
        System.out.println("Student Profile:");
        System.out.println("Name: " + student.name);
        System.out.println("Contact Info: " + student.getContactInfo());
    }

    public void assignProfessorToCourse(Professor professor, Course course) {
        professor.courses.add(course);
        course.professor = professor.name;
        System.out.println("Assigned " + professor.name + " to course: " + course.title);
    }


    static class IIITDSystem {
        static  List<Course> courses = new ArrayList<>();
        static List<Student> students = new ArrayList<>();
        static List<Professor> professors = new ArrayList<>();
        static List<TA> ta = new ArrayList<>();
        static Administrator admin = new Administrator();
        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

            initializeData(students, professors, courses);

            while (true) {
                System.out.println("Welcome to the IIITD ERP");
                System.out.println("1. Sign Up");
                System.out.println("2. Login");
                System.out.println("3. Exit the Application");
                int mainChoice = scanner.nextInt();
                if (mainChoice == 3) {
                    System.out.println("Exiting the Application. Goodbye!");
                    break;
                }

                if (mainChoice == 1) {
                    signUp(scanner, students, professors);
                } else if (mainChoice == 2) {
                    login(scanner, students, professors, ta, admin, courses);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
            scanner.close();
        }

        private static void initializeData(List<Student> students, List<Professor> professors, List<Course> courses) {

            students.add(new Student("harsh", "1", "1"));
            students.add(new Student("harry", "1", "2"));

            professors.add(new Professor("ojaswa", "2"));
            professors.add(new Professor("vivek", "2"));
            professors.add(new Professor("aasim", "2"));
            professors.add(new Professor("satish", "2"));
            professors.add(new Professor("bapi", "2"));


            String[] courseTitles = {"DSA", "OS", "PDE", "HCI", "DM"};
            courses.add(new Course("CSE102", courseTitles[0], "ojaswa", 4, "IP",  30));
            courses.add(new Course("CSE111", courseTitles[1], "vivek", 4, "DC,BE", 30));
            courses.add(new Course("SSH101", courseTitles[2], "aasim", 4, "None",  30));
            courses.add(new Course("MTH401", courseTitles[3], "satish", 4, "LA", 30));
            courses.add(new Course("MTH201", courseTitles[4], "bapi", 4, "None",  30));


            for (int i = 0; i < professors.size(); i++) {
                professors.get(i).courses.add(courses.get(i));
            }

            System.out.println("Initialized data: 5 students, 5 professors, and 5 courses.");
        }

        private static void signUp(Scanner scanner, List<Student> students, List<Professor> professors) {
            System.out.println("Sign Up as:");
            System.out.println("1. Student");
            System.out.println("2. Professor");
            int signUpChoice = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter username:");
            String username = scanner.nextLine();
            System.out.println("Enter password:");
            String password = scanner.nextLine();

            if (signUpChoice == 1) {
                System.out.println("Enter contact info:");
                String contactInfo = scanner.nextLine();
                students.add(new Student(username, password, contactInfo));
                System.out.println("Student account created successfully.");
            } else if (signUpChoice == 2) {
                professors.add(new Professor(username, password));
                System.out.println("Professor account created successfully.");
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }

        private static void login(Scanner scanner, List<Student> students, List<Professor> professors, List<TA> tas, Administrator admin, List<Course> courses) {
            System.out.println("Login as:");
            System.out.println("1. Student");
            System.out.println("2. Professor");
            System.out.println("3. TA");
            System.out.println("4. Administrator");
            int loginChoice = scanner.nextInt();
            scanner.nextLine();

            try {
                if (loginChoice == 1) {
                    System.out.println("Enter username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();

                    boolean found = false;
                    for (Student student : students) {
                        if (student.login(username, password)) {
                            found = true;
                            studentMenu(scanner, student, courses);
                            return;
                        }
                    }
                    if (!found) {
                        UniException.throwInvalidLoginException();
                    }
                } else if (loginChoice == 2) {
                    System.out.println("Enter username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();

                    boolean found = false;
                    for (Professor professor : professors) {
                        if (professor.login(username, password)) {
                            found = true;
                            professorMenu(scanner, professor, courses);
                            return;
                        }
                    }
                    if (!found) {
                        UniException.throwInvalidLoginException();
                    }
                } else if (loginChoice == 3) {
                    System.out.println("Enter username:");
                    String username = scanner.nextLine();
                    System.out.println("Enter password:");
                    String password = scanner.nextLine();

                    boolean found = false;
                    for (TA ta : tas) {
                        if (ta.login(username, password)) {
                            found = true;
                            taMenu(scanner, ta, courses, students);
                            return;
                        }
                    }
                    if (!found) {
                        UniException.throwInvalidLoginException();
                    }
                } else if (loginChoice == 4) {
                    System.out.println("Enter admin password:");
                    String password = scanner.nextLine();

                    if (admin.login(password)) {
                        adminMenu(scanner, admin, courses, students, professors,tas);
                        return;
                    }
                    UniException.throwInvalidLoginException();
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        private static void studentMenu(Scanner scanner, Student student, List<Course> courses) {
            while (true) {
                System.out.println("Student Menu:");
                System.out.println("1. View Available Courses");
                System.out.println("2. Register for a Course");
                System.out.println("3. View Grades");
                System.out.println("4. Drop a Course");
                System.out.println("5. Submit Feedback");
                System.out.println("6. Calculate CGPA");
                System.out.println("7. Logout");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        student.viewAvailableCourses(courses);
                        break;
                    case 2:
                        System.out.println("Enter course title to register:");
                        scanner.nextLine();
                        String title = scanner.nextLine();
                        for (Course course : courses) {
                            if (course.title.equals(title)) {
                                try {
                                    student.registerForCourse(course);
                                } catch (CourseFullException e) {
                                    System.out.println(e.getMessage());
                                }
                                break;
                            }
                        }

                        break;

                    case 3:
                        student.viewGrades();
                        break;
                    case 4:
                        System.out.println("Enter course title to drop:");
                        scanner.nextLine();
                        String dropTitle = scanner.nextLine();
                        for (Course course : courses) {
                            if (course.title.equals(dropTitle)) {
                                student.dropCourse(course);
                                break;
                            }
                        }
                        break;
                    case 5:
                        System.out.println("Enter course title to submit feedback:");
                        scanner.nextLine();
                        String feedbackCourseTitle = scanner.nextLine();
                        for (Course course : courses) {
                            if (course.title.equals(feedbackCourseTitle)) {
                                System.out.println("Enter your numeric rating (1-5):");
                                int rating = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Enter your feedback:");
                                String feedbackText = scanner.nextLine();
                                student.submitFeedback(course, String.valueOf(rating));
                                student.submitFeedback(course, feedbackText);
                                break;
                            }
                        }
                        break;
                    case 6:
                        double cgpa = student.calculateCGPA();
                        System.out.println("Your CGPA: " + cgpa);
                        break;
                    case 7:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        private static void taMenu(Scanner scanner, TA ta, List<Course> courses, List<Student> students) {
            while (true) {
                System.out.println("TA Menu:");
                System.out.println("1. View Assigned Courses");
                System.out.println("2. Update Student Grades");
                System.out.println("3. Logout");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        System.out.println("Your Assigned Courses:");
                        for (Course course : ta.getAssignedCourses()) {
                            course.displayCourse();
                        }
                        break;
                    case 2:
                        System.out.println("Enter student name to update grade:");
                        scanner.nextLine();
                        String studentName = scanner.nextLine();
                        for (Student student : students) {
                            if (student.name.equals(studentName)) {
                                System.out.println("Enter course title:");

                                String courseTitle = scanner.nextLine();
                                for (Course course : ta.getAssignedCourses()) {
                                    if (course.title.equals(courseTitle)) {
                                        System.out.println("Enter new grade:");
                                        double newGrade = scanner.nextDouble();
                                        ta.updateStudentGrade(student, newGrade, course);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }


        private static void professorMenu(Scanner scanner, Professor professor, List<Course> courses) {
            while (true) {
                System.out.println("Professor Menu:");
                System.out.println("1. View Courses");
                System.out.println("2. Update Course");
                System.out.println("3. View Feedback for Your Courses");
                System.out.println("4. Logout");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        professor.viewAndManageCourses();
                        break;
                    case 2:
                        System.out.println("Enter course title to update:");
                        scanner.nextLine();
                        String titleToUpdate = scanner.nextLine();
                        for (Course course : courses) {
                            if (course.title.equals(titleToUpdate)) {

                                System.out.println("Enter new timing:");
                                String timing = scanner.nextLine();
                                System.out.println("Enter new credits:");
                                int credits = scanner.nextInt();
                                scanner.nextLine();
                                System.out.println("Enter new enrollment limit:");
                                int enrollmentLimit = scanner.nextInt();
                                professor.updateCourse(course, timing, credits, enrollmentLimit);
                                break;
                            }
                        }
                        break;
                    case 3:
                        System.out.println("Enter course title to view feedback:");
                        scanner.nextLine();
                        String feedbackCourseTitle = scanner.nextLine();
                        for (Course course : courses) {
                            if (course.title.equals(feedbackCourseTitle)) {
                                professor.viewFeedback(course);
                                break;
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Logging out...");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }

        private static void adminMenu(Scanner scanner, Administrator admin, List<Course> courses, List<Student> students, List<Professor> professors,List<TA> tas) {
            while (true) {
                System.out.println("Administrator Menu:");
                System.out.println("1. View Courses");
                System.out.println("2. Add Course");
                System.out.println("3. Delete Course");
                System.out.println("4. Manage Grades");
                System.out.println("5. View Student Profile");
                System.out.println("6. Assign Professor to Course");
                System.out.println("7. Logout");
                System.out.println("8. Assign TA");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        admin.viewCourses(courses);
                        break;
                    case 2:
                        System.out.println("Enter course details to add a course:");
                        scanner.nextLine();
                        System.out.println("Enter Course Code:");
                        String courseId = scanner.nextLine();
                        System.out.println("Enter Course Title:");
                        String courseTitle = scanner.nextLine();
                        System.out.println("Enter Professor's Name:");
                        String professorName = scanner.nextLine();
                        System.out.println("Enter Credits:");
                        int credits = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Enter Timing:");
                        String timing = scanner.nextLine();
                        System.out.println("Enter Enrollment Limit:");
                        int enrollmentLimit = scanner.nextInt();

                        Course newCourse = new Course(courseId, courseTitle, professorName, credits, timing, enrollmentLimit);
                        admin.addCourse(courses, newCourse);
                        break;
                    case 3:
                        System.out.println("Enter course title to delete:");
                        scanner.nextLine();
                        String titleToDelete = scanner.nextLine();
                        for (Course course : courses) {
                            if (course.title.equals(titleToDelete)) {
                                admin.deleteCourse(courses, course);
                                break;
                            }
                        }
                        break;
                    case 4:
                        System.out.println("Enter student name to update grades:");
                        scanner.nextLine();
                        String studentName = scanner.nextLine();
                        for (Student student : students) {
                            if (student.name.equals(studentName)) {
                                System.out.println("Enter new grade:");
                                double newGrade = scanner.nextDouble();
                                System.out.println("Enter course title:");
                                scanner.nextLine();
                                String courseTitleForGrade = scanner.nextLine();
                                for (Course course : courses) {
                                    if (course.title.equals(courseTitleForGrade)&&course.enrolledStudents.contains(student)) {
                                        admin.updateStudentGrade(student, newGrade, course);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    case 5:
                        System.out.println("Enter student name to view profile:");
                        scanner.nextLine();
                        String profileStudentName = scanner.nextLine();
                        for (Student student : students) {
                            if (student.name.equals(profileStudentName)) {
                                admin.viewStudentProfile(student);
                                break;
                            }
                        }
                        break;
                    case 6:
                        System.out.println("Enter professor's name:");
                        scanner.nextLine();
                        String professorToAssign = scanner.nextLine();
                        for (Professor professor : professors) {
                            if (professor.name.equals(professorToAssign)) {
                                System.out.println("Enter course title to assign professor:");
                                String courseTitleToAssign = scanner.nextLine();
                                for (Course course : courses) {
                                    if (course.title.equals(courseTitleToAssign)) {
                                        admin.assignProfessorToCourse(professor, course);
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        break;
                    case 7:
                        System.out.println("Logging out...");
                        return;
                    case 8:
                        System.out.println("Enter student name to assign as TA:");
                        scanner.nextLine();
                        String taStudentName = scanner.nextLine();
                        for (Student student : students) {
                            if (student.name.equals(taStudentName)) {
                                System.out.println("Enter the course titles assigned to the TA (comma-separated):");
                                String courseTitles = scanner.nextLine();
                                List<Course> assignedCourses = new ArrayList<>();
                                for (String title : courseTitles.split(",")) {
                                    for (Course course : courses) {
                                        if (course.title.equals(title.trim())) {
                                            assignedCourses.add(course);
                                        }
                                    }
                                }
                                TA new_ta = new TA(student.name, student.password, student.getContactInfo(), assignedCourses);
                                System.out.println(student.name + " assigned as TA for courses: ");
                                for(Course course : assignedCourses){
                                    System.out.print(course.title + " ");
                                }
                                System.out.println();
                                ta.add(new_ta);
                                break;
                            }
                        }
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
    }
}
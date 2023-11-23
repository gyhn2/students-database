import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Application extends DatabaseCredentials {

    static String url = String.format("jdbc:postgresql://%s:%d/%s", 
        host, port, database);

    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            
            if (conn != null) {

                boolean prompt = true;

                while(prompt) {

                    //welcome message
                    String message = "==========================\n"+
                        "This is an application where you can display, add, delete students from the student table.\n"+
                        "Respond to the prompt with the number associated with your desired action.\n\n"+
                
                        "1. Display all students\n"+
                        "2. Add a new student\n"+
                        "3. Update the email address for a student with the specified student ID\n"+
                        "4. Delete the student with the specified student ID\n";
                    System.out.println(message);

                    //user input
                    String choice = scanner.nextLine().trim();

                    while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")) {
                        System.out.println("Wrong input type. Respond with 1, 2, 3, or 4.");
                        choice = scanner.nextLine().trim();
                    }

                    // getAllStudents
                    if (choice.equals("1")) {

                        getAllStudents(conn);   

                    } else if (choice.equals("2")) {
                        
                        System.out.println("\nTo add a student, please provide each attribute.\n");
                        
                        //first name input
                        System.out.println("First Name:");
                        String first_name = scanner.nextLine().trim();
                        while (first_name.equals("")) {
                            System.out.println("Input must not be empty!");
                            first_name = scanner.nextLine().trim();
                        }

                        //last name input
                        System.out.println("Last Name:");
                        String last_name = scanner.nextLine().trim();
                        while (last_name.equals("")) {
                            System.out.println("Input must not be empty!");
                            last_name = scanner.nextLine().trim();
                        }

                        //email input
                        System.out.println("Email:");
                        String email = scanner.nextLine().trim();
                        while (email.equals("") || !email.contains("@")) {
                            System.out.println("Wrong input format!");
                            email = scanner.nextLine().trim();
                        }

                        //enrol date input
                        System.out.println("Enrollment Date (yyyy-MM-dd):");
                        String enrol = scanner.nextLine().trim();

                        Date enrollment_date = Date.valueOf(LocalDate.now());
                        boolean valid = false;

                        while (!valid) {
                            try {
                                enrollment_date = Date.valueOf(enrol);
                                valid = true;
                            } catch (IllegalArgumentException e) {
                                System.out.println("Invalid date!");
                                enrol = scanner.nextLine().trim();
                            }
                        }

                        // addStudent function call
                        addStudent(conn, first_name, last_name, email, enrollment_date);


                    } else if (choice.equals("3")) {

                        //updateStudentEmail
                        System.out.println("\nUpdate the email address by providing the student ID and new email address.");

                        //prompt id input
                        int student_id = 0;
                        boolean idIsInt = false;
                        while (!idIsInt) {
                            try {
                                System.out.println("Student ID:");
                                student_id = Integer.parseInt(scanner.nextLine().trim());
                                idIsInt = true;
                            } catch (NumberFormatException e) {
                                System.out.println("Not a valid ID. Must be an Integer.");
                            }
                        }

                        //prompt email input
                        System.out.println("New Email:");
                        String new_email = scanner.nextLine().trim();
                        while (new_email.equals("") || !new_email.contains("@")) {
                            System.out.println("Wrong input format!");
                            new_email = scanner.nextLine().trim();
                        }

                        updateStudentEmail(conn, student_id, new_email);

                    } else if (choice.equals("4")) {
                        System.out.println("\nDelete a student by providing the student ID:");

                        int student_id = 0;
                        boolean idIsInt = false;
                        while (!idIsInt) {
                            try {
                                student_id = Integer.parseInt(scanner.nextLine().trim());
                                idIsInt = true;
                            } catch (NumberFormatException e) {
                                System.out.println("Not a valid ID. Must be an Integer.");
                            }
                        }

                        deleteStudent(conn, student_id);

                    }

                    // Perform another functionality (or not)
                    prompt = nextAction(prompt);
                }

            }else {
                System.out.println("Database Connection Failed to Establish");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        
    }

    //1. getAllStudents
    public static void getAllStudents(Connection conn) {
        try {
            Statement st = conn.createStatement();
            String selectQuery = "SELECT * FROM students ORDER BY student_id";
            ResultSet rs = st.executeQuery(selectQuery);

            System.out.println("\nDisplaying all students:\n");

            while(rs.next()) {
                int studentId = rs.getInt("student_id");
                String fname = rs.getString("first_name");
                String lname = rs.getString("last_name");
                String email = rs.getString("email");
                String enrolDate = rs.getString("enrollment_date");
                System.out.println(
                    "* Student ID: " + Integer.toString(studentId) +
                    ", Name: " + fname + " " + lname +
                    ", Email: " + email +
                    ", Enrollment Date: " + enrolDate + "\n"
                );

            }

            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //2. addStudent
    public static void addStudent(Connection conn, String first_name, String last_name, String email, Date enrollment_date) {

        try {
            // insert statement
            String insertQuery = 
                "INSERT INTO students (first_name, last_name, email, enrollment_date)" +
                "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(insertQuery);
            ps.setString(1, first_name);
            ps.setString(2, last_name);
            ps.setString(3, email);
            ps.setDate(4, enrollment_date);
            ps.executeUpdate();
            System.out.println(
                "\n" + first_name + " " + last_name + 
                " with email " + email + " and enrolment date " + enrollment_date +
                " added as student!"
            );

            ps.close();

        } catch (SQLException e) {
            
            if (e.getSQLState().equals("23505")) {
                //Duplicate email: prompt a different email input
                System.out.println("Duplicate email! Write a different email.");

                String diff_email = scanner.nextLine().trim();
                while (diff_email.equals("") || !diff_email.contains("@")) {
                    System.out.println("Wrong input format!");
                    diff_email = scanner.nextLine().trim();
                }

                addStudent(conn, first_name, last_name, diff_email, enrollment_date);

            } else {
                e.printStackTrace();
            }
        }

    }

    //3. updateStudentEmail
    public static void updateStudentEmail(Connection conn, int student_id, String new_email) {

        try {
            //query
            String updateQuery = "UPDATE students SET email = ? WHERE student_id = ?";
            PreparedStatement ps = conn.prepareStatement(updateQuery);
            ps.setString(1, new_email);
            ps.setInt(2, student_id);
            int returning = ps.executeUpdate();

            //check if match
            if(returning == 0) {
                System.out.println("No matching student exists.");
            } else if (returning==1)
                System.out.println("The email of the student with ID "+student_id+ " is now "+ new_email);

            ps.close();

        } catch(SQLException e) {
            //Duplicate email: prompt a different email input and insert
                if (e.getSQLState().equals("23505")) {
                System.out.println("Duplicate email! Write a different email.");

                String diff_email = scanner.nextLine().trim();
                while (diff_email.equals("") || !diff_email.contains("@")) {
                    System.out.println("Wrong input format!");
                    diff_email = scanner.nextLine().trim();
                }

                updateStudentEmail(conn, student_id, diff_email);
            } else {
                e.printStackTrace();
            }
        }

    }

    //4. deleteStudent
    public static void deleteStudent(Connection conn, int student_id) {

        try {
            //query execution
            String deleteQuery = "DELETE FROM students WHERE student_id = ?";
            PreparedStatement ps = conn.prepareStatement(deleteQuery);
            ps.setInt(1, student_id);
            int returning = ps.executeUpdate();
            if (returning == 0) {
                System.out.println("No such student exists.");
            } else {
                System.out.println("Student with id "+student_id+" was deleted.");
            }
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Perform another action, or not
    public static boolean nextAction(boolean prompt) {
        System.out.println("Would you like to do perform another action? (Y\\N)");
        String next = scanner.nextLine().trim().toUpperCase();

        while (!next.equals("Y") && !next.equals("N")) {
            next = scanner.nextLine().trim().toUpperCase();
        }
        if (next.equals("Y")) {
            prompt = true;
        } else if (next.equals("N")) {
            prompt = false;
        }
        return prompt;
    }
    
}


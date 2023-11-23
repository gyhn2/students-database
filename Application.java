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
                    Scanner input = new Scanner(System.in);
                    String choice = input.nextLine().trim();

                    while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equals("4")) {
                        System.out.println("Wrong input type. Respond with 1, 2, 3, or 4.");
                        choice = input.nextLine().trim();
                    }

                    // Each functionality
                    if (choice.equals("1")) {
                        getAllStudents(conn);   
                    } else if (choice.equals("2")) {
                        addStudent(conn);
                    } else if (choice.equals("3")) {
                        System.out.println("\nUpdate");
                    } else if (choice.equals("4")) {
                        System.out.println("\nDelete");
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
            String selectQuery = "SELECT * FROM students";
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //2. addStudent
    public static void addStudent(Connection conn) {

            System.out.println("\nTo add a student, please provide each attribute.\n");
            
            Scanner sc = new Scanner(System.in);

            //first name input
            System.out.println("First Name:");
            String firstname = sc.nextLine().trim();
            while (firstname.equals("")) {
                System.out.println("Input must not be empty!");
                firstname = sc.nextLine().trim();
            }

            //last name input
            System.out.println("Last Name:");
            String lastname = sc.nextLine().trim();
            while (lastname.equals("")) {
                System.out.println("Input must not be empty!");
                lastname = sc.nextLine().trim();
            }

            //email input
            System.out.println("Email:");
            String email = sc.nextLine().trim();
            while (email.equals("") || !email.contains("@")) {
                System.out.println("Wrong input format!");
                email = sc.nextLine().trim();
            }

            //enrol date input
            System.out.println("Enrollment Date (yyyy-MM-dd):");
            String enrol = sc.nextLine().trim();
            while (!enrol.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
                System.out.println("Wrong input format. Must be yyyy-MM-dd");
                enrol = sc.nextLine().trim();
            }
            Date enroldate = Date.valueOf("2000-01-01");

            try {
                enroldate = Date.valueOf(enrol);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid date!");
                addStudent(conn);
            }

        try {
            // insert statement
            String insertQuery = 
                "INSERT INTO students (first_name, last_name, email, enrollment_date)" +
                "VALUES (?, ?, ?, ?)";

            PreparedStatement ps = conn.prepareStatement(insertQuery);
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, email);
            ps.setDate(4, enroldate);
            ps.executeUpdate();
            System.out.println(
                "\n" + firstname + " " + lastname + 
                " with email " + email + " and enrolment date " + enrol +
                " added as student!"
            );

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            if (e.getSQLState().equals("23505")) {
                System.out.println("Duplicate email!");
                addStudent(conn);
            }
            e.printStackTrace();
        }

    }

    //Perform another action, or not
    public static boolean nextAction(boolean prompt) {
        Scanner newFunc = new Scanner(System.in);
        System.out.println("Would you like to do perform another action? (Y\\N)");
        String next = newFunc.nextLine().trim().toUpperCase();

        while (!next.equals("Y") && !next.equals("N")) {
            next = newFunc.nextLine().trim().toUpperCase();
        }
        if (next.equals("Y")) {
            prompt = true;
        } else if (next.equals("N")) {
            prompt = false;
        }
        return prompt;
    }
    
}


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSetup extends DatabaseCredentials {

    static String url = String.format("jdbc:postgresql://%s:%d/%s", 
        host, port, database);

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            
            if (conn != null) {

                Statement st = conn.createStatement();

                //Create Table
                String createTable = "\n" +
                "CREATE TABLE IF NOT EXISTS students (" +
                "        student_id  SERIAL  PRIMARY KEY, "+
                "        first_name  TEXT    NOT NULL, " +
                "        last_name   TEXT    NOT NULL, " +
                "        email       TEXT    NOT NULL    UNIQUE, "+
                "        enrollment_date     DATE" +
                ")";
                int tableCreation = st.executeUpdate(createTable);

                if (tableCreation > 0) {
                    System.out.println("students table created.");
                }

                //Populate Data
                String populateData = "\n"+
                "INSERT INTO students (first_name, last_name, email, enrollment_date) VALUES " +
                "('John', 'Doe', 'john.doe@example.com', '2023-09-01'), "+
                "('Jane', 'Smith', 'jane.smith@example.com', '2023-09-01')," +
                "('Jim', 'Beam', 'jim.beam@example.com', '2023-09-02')";

                int tablePopulated = st.executeUpdate(populateData);

                if (tablePopulated > 0) {
                    System.out.println("students table populated.");
                }

                st.close();

            }else {
                System.out.println("Database Connection Failed to Establish");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        
    }
    
}

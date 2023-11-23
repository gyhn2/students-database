import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TableDrop extends DatabaseCredentials {

    static String url = String.format("jdbc:postgresql://%s:%d/%s", 
        host, port, database);

    public static void main(String[] args) {

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            
            if (conn != null) {

                Statement st = conn.createStatement();

                //Drop Table
                String dropTable = "DROP TABLE IF EXISTS students";
                int tableDropped = st.executeUpdate(dropTable);

                System.out.println("students table was dropped.");
                
                st.close();
                
            }else {
                System.out.println("Database Connection Failed to Establish.");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        
    }
    
}

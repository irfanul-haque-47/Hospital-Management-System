import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/hospital_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // or your DB username
    private static final String PASSWORD = ""; // or your DB password

    public static Connection getConnection() {
        try {
            // You technically don't need this line in JDBC 4.0+, but add it for safety:
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}

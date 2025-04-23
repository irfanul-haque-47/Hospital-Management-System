import java.sql.*;
import java.util.*;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Welcome to Hospital Management System");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        boolean loggedIn = false;
        if (choice == 1) {
            register();
            loggedIn = login();
        } else if (choice == 2) {
            loggedIn = login();
        } else {
            System.out.println("Invalid choice. Exiting.");
            return;
        }

        if (!loggedIn) {
            System.out.println("Login failed. Exiting.");
            return;
        }

        int option;
        do {
            System.out.println("\nHospital Management System Menu");
            System.out.println("1. Add Patient");
            System.out.println("2. View Patients");
            System.out.println("3. Add Doctor");
            System.out.println("4. View Doctors");
            System.out.println("5. Schedule Appointment");
            System.out.println("6. View Appointments");
            System.out.println("7. Generate Bill");
            System.out.println("8. View Bills");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> addPatient();
                case 2 -> viewPatients();
                case 3 -> addDoctor();
                case 4 -> viewDoctors();
                case 5 -> scheduleAppointment();
                case 6 -> viewAppointments();
                case 7 -> generateBill();
                case 8 -> viewBills();
                case 9 -> System.out.println("Exiting the system.");
                default -> System.out.println("Invalid choice.");
            }
        } while (option != 9);
    }

    static void register() {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine();
        System.out.print("Enter new password: ");
        String password = scanner.nextLine();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            System.out.println("Registration successful.");
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                System.out.println("Username already exists. Please choose a different username.");
            } else {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    static boolean login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("Login successful.\n");
                return true;
            } else {
                System.out.println("Incorrect username or password.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    static void addPatient() {
        try (Connection conn = DBUtil.getConnection()) {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter age: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter disease: ");
            String disease = scanner.nextLine();
            System.out.print("Enter assigned doctor ID: ");
            int doctorId = scanner.nextInt();
            scanner.nextLine();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO patients (name, age, disease, assignedDoctorId) VALUES (?, ?, ?, ?)");
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, disease);
            stmt.setInt(4, doctorId);
            stmt.executeUpdate();

            System.out.println("Patient added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewPatients() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM patients")) {
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Age: %d, Disease: %s, Doctor ID: %d%n",
                        rs.getInt("id"), rs.getString("name"), rs.getInt("age"),
                        rs.getString("disease"), rs.getInt("assignedDoctorId"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void addDoctor() {
        try (Connection conn = DBUtil.getConnection()) {
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter specialization: ");
            String specialization = scanner.nextLine();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO doctors (name, specialization) VALUES (?, ?)");
            stmt.setString(1, name);
            stmt.setString(2, specialization);
            stmt.executeUpdate();

            System.out.println("Doctor added successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewDoctors() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM doctors")) {
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Specialization: %s%n",
                        rs.getInt("id"), rs.getString("name"), rs.getString("specialization"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void scheduleAppointment() {
        try (Connection conn = DBUtil.getConnection()) {
            // Collect patient ID, doctor ID, appointment date, and time slot
            System.out.print("Enter patient ID: ");
            int patientId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter doctor ID: ");
            int doctorId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            System.out.print("Enter appointment date (yyyy-mm-dd): ");
            String dateInput = scanner.nextLine();  // Expect format: yyyy-mm-dd
    
            System.out.print("Enter time slot (e.g. 10:00 AM): ");
            String slot = scanner.nextLine();
    
            // Prepare SQL statement with both date and time
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO appointments (patientId, doctorId, appointmentDate, timeSlot) VALUES (?, ?, ?, ?)");
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setString(3, dateInput); // Date as String (in format yyyy-mm-dd)
            stmt.setString(4, slot);
            stmt.executeUpdate();
    
            System.out.println("Appointment scheduled successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    

    static void viewAppointments() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM appointments")) {
            while (rs.next()) {
                System.out.printf("Appointment ID: %d, Patient ID: %d, Doctor ID: %d, Date: %s, Slot: %s%n",
                        rs.getInt("appointmentId"), rs.getInt("patientId"),
                        rs.getInt("doctorId"), rs.getString("appointmentDate"), rs.getString("timeSlot"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    

    static void generateBill() {
        try (Connection conn = DBUtil.getConnection()) {
            System.out.print("Enter patient ID: ");
            int patientId = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter amount: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            PreparedStatement stmt = conn.prepareStatement("INSERT INTO billing (patientId, amount) VALUES (?, ?)");
            stmt.setInt(1, patientId);
            stmt.setDouble(2, amount);
            stmt.executeUpdate();

            System.out.println("Bill generated successfully.");
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static void viewBills() {
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM billing")) {
            while (rs.next()) {
                System.out.printf("Bill ID: %d, Patient ID: %d, Amount: %.2f%n",
                        rs.getInt("billId"), rs.getInt("patientId"), rs.getDouble("amount"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

package hospital_management;

import java.sql.*;
import java.util.Scanner;

public class Patient {
    private Connection conn;
    private Scanner sc;

    public Patient(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void addPatient() {
        sc.nextLine(); // consume leftover newline
        System.out.print("Enter Patient Name: ");
        String name = sc.nextLine();
        System.out.print("Enter Patient Age: ");
        int age = sc.nextInt();
        System.out.print("Enter Patient Gender (M/F/O): ");
        String gender = sc.next();

        String query = "INSERT INTO patients(NAME, GENDER, AGE) VALUES (?,?,?)";
        try (PreparedStatement p = conn.prepareStatement(query)) {
            p.setString(1, name);
            p.setString(2, gender);
            p.setInt(3, age);
            int rows = p.executeUpdate();
            System.out.println(rows > 0 ? "✅ Patient added successfully" : "❌ Failed to add Patient");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewPatients() {
        String query = "SELECT * FROM patients";
        try (PreparedStatement p = conn.prepareStatement(query); ResultSet res = p.executeQuery()) {
            System.out.println("\n===== PATIENTS LIST =====");
            while (res.next()) {
                System.out.printf("ID: %d, Name: %s, Age: %d, Gender: %s%n",
                        res.getInt("ID"), res.getString("NAME"), res.getInt("AGE"), res.getString("GENDER"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePatient() {
        System.out.print("Enter Patient ID to update: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Enter New Name: ");
        String name = sc.nextLine();
        System.out.print("Enter New Age: ");
        int age = sc.nextInt();
        System.out.print("Enter New Gender: ");
        String gender = sc.next();

        String query = "UPDATE patients SET NAME=?, AGE=?, GENDER=? WHERE ID=?";
        try (PreparedStatement p = conn.prepareStatement(query)) {
            p.setString(1, name);
            p.setInt(2, age);
            p.setString(3, gender);
            p.setInt(4, id);
            int rows = p.executeUpdate();
            System.out.println(rows > 0 ? "✅ Patient updated successfully" : "❌ Patient not found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePatient() {
        System.out.print("Enter Patient ID to delete: ");
        int id = sc.nextInt();
        String query = "DELETE FROM patients WHERE ID = ?";
        try (PreparedStatement p = conn.prepareStatement(query)) {
            p.setInt(1, id);
            int rows = p.executeUpdate();
            System.out.println(rows > 0 ? "✅ Patient deleted" : "❌ No patient found with ID " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean exists(int id) {
        String query = "SELECT * FROM patients WHERE ID = ?";
        try (PreparedStatement p = conn.prepareStatement(query)) {
            p.setInt(1, id);
            ResultSet res = p.executeQuery();
            return res.next();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

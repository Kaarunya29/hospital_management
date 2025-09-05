package hospital_management;

import java.sql.*;
import java.util.Scanner;

public class Doctor {
    private Connection conn;

    public Doctor(Connection conn) {
        this.conn = conn;
    }

    public void viewDoctors() {
        String query = "SELECT * FROM doctors";
        try (PreparedStatement p = conn.prepareStatement(query); ResultSet res = p.executeQuery()) {
            System.out.println("\n===== DOCTORS LIST =====");
            while (res.next()) {
                System.out.printf("ID: %d, Name: %s, Specialization: %s%n",
                        res.getInt("ID"), res.getString("NAME"), res.getString("SPECIALIZATION"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchDoctorBySpecialization(String spec) {
        String query = "SELECT * FROM doctors WHERE SPECIALIZATION=?";
        try (PreparedStatement p = conn.prepareStatement(query)) {
            p.setString(1, spec);
            ResultSet res = p.executeQuery();
            System.out.println("\n===== DOCTORS WITH SPECIALIZATION: " + spec + " =====");
            boolean found = false;
            while (res.next()) {
                found = true;
                System.out.printf("ID: %d, Name: %s%n", res.getInt("ID"), res.getString("NAME"));
            }
            if (!found) System.out.println(" No doctor found with this specialization.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean exists(int id) {
        String query = "SELECT * FROM doctors WHERE ID=?";
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

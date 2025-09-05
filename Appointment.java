package hospital_management;

import java.sql.*;
import java.util.Scanner;

public class Appointment {
    private Connection conn;
    private Scanner sc;

    public Appointment(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void bookAppointment(Patient patient, Doctor doctor) {
        System.out.print("Enter Patient ID: ");
        int pid = sc.nextInt();
        System.out.print("Enter Doctor ID: ");
        int did = sc.nextInt();
        System.out.print("Enter Appointment Date (YYYY-MM-DD): ");
        String appDate = sc.next();

        try {
            java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
            java.sql.Date appointmentDate = java.sql.Date.valueOf(appDate);

            if (appointmentDate.before(today)) {
                System.out.println("❌ Appointment cannot be in the past.");
                return;
            }

            if (patient.exists(pid) && doctor.exists(did)) {
                if (isDoctorAvailable(did, appDate)) {
                    String query = "INSERT INTO appointments(PATIENT_ID, DOCTOR_ID, APPOINTMENT_DATE) VALUES(?,?,?)";
                    try (PreparedStatement p = conn.prepareStatement(query)) {
                        p.setInt(1, pid);
                        p.setInt(2, did);
                        p.setString(3, appDate);
                        int rows = p.executeUpdate();
                        System.out.println(rows > 0 ? "Appointment booked!" : "Booking failed.");
                    }
                } else {
                    System.out.println("Doctor not available on this date.");
                }
            } else {
                System.out.println("Invalid doctor or patient ID.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void viewAppointments() {
        String query = "SELECT a.ID, p.NAME AS PNAME, d.NAME AS DNAME, d.SPECIALIZATION, a.APPOINTMENT_DATE " +
                       "FROM appointments a JOIN patients p ON a.PATIENT_ID=p.ID " +
                       "JOIN doctors d ON a.DOCTOR_ID=d.ID ORDER BY a.APPOINTMENT_DATE";
        try (PreparedStatement p = conn.prepareStatement(query); ResultSet res = p.executeQuery()) {
            System.out.println("\n===== APPOINTMENTS =====");
            while (res.next()) {
                System.out.printf("ID: %d, Patient: %s, Doctor: %s (%s), Date: %s%n",
                        res.getInt("ID"), res.getString("PNAME"),
                        res.getString("DNAME"), res.getString("SPECIALIZATION"),
                        res.getDate("APPOINTMENT_DATE"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelAppointment() {
        System.out.print("Enter Appointment ID to cancel: ");
        int id = sc.nextInt();
        String query = "DELETE FROM appointments WHERE ID=?";
        try (PreparedStatement p = conn.prepareStatement(query)) {
            p.setInt(1, id);
            int rows = p.executeUpdate();
            System.out.println(rows > 0 ? "✅ Appointment cancelled" : " No appointment found");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isDoctorAvailable(int did, String appDate) {
        String query = "SELECT COUNT(*) FROM appointments WHERE DOCTOR_ID = ? AND APPOINTMENT_DATE = ?";
        try (PreparedStatement p = conn.prepareStatement(query)) {
            p.setInt(1, did);
            p.setString(2, appDate);
            ResultSet res = p.executeQuery();
            if (res.next()) return res.getInt(1) == 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

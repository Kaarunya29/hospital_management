package hospital_management;

import java.sql.Connection;
import java.util.Scanner;

public class HospitalManagementSystem {
    public static void main(String[] args) {
        try (Connection conn = DatabaseUtil.getConnection();
             Scanner sc = new Scanner(System.in)) {

            Patient patient = new Patient(conn, sc);
            Doctor doctor = new Doctor(conn);
            Appointment appointment = new Appointment(conn, sc);

            while (true) {
                System.out.println("\n===== HOSPITAL MANAGEMENT SYSTEM =====");
                System.out.println("1. Add Patient");
                System.out.println("2. View Patients");
                System.out.println("3. Update Patient");
                System.out.println("4. Delete Patient");
                System.out.println("5. View Doctors");
                System.out.println("6. Search Doctor by Specialization");
                System.out.println("7. Book Appointment");
                System.out.println("8. View Appointments");
                System.out.println("9. Cancel Appointment");
                System.out.println("10. Exit");
                System.out.print("Enter choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1 -> patient.addPatient();
                    case 2 -> patient.viewPatients();
                    case 3 -> patient.updatePatient();
                    case 4 -> patient.deletePatient();
                    case 5 -> doctor.viewDoctors();
                    case 6 -> {
                        System.out.print("Enter specialization: ");
                        String spec = sc.next();
                        doctor.searchDoctorBySpecialization(spec);
                    }
                    case 7 -> appointment.bookAppointment(patient, doctor);
                    case 8 -> appointment.viewAppointments();
                    case 9 -> appointment.cancelAppointment();
                    case 10 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

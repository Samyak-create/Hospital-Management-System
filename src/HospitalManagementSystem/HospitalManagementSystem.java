package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

import static javax.print.attribute.Size2DSyntax.MM;
import static javax.swing.text.html.HTML.Tag.DD;

public class HospitalManagementSystem {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "54321";

    public static void main(String args[]) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found ");
            e.printStackTrace();


        }
        Scanner scanner = new Scanner(System.in);
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Patient patient = new Patient(connection, scanner);
            Doctor doctor = new Doctor(connection,scanner);
            while (true) {
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1.Add Patient");
                System.out.println("2.View Patient");
                System.out.println("3.View Doctors");
                System.out.println("4.Book Appointments");
                System.out.println("5.Exit");
                System.out.println("Enter your choice:");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        //add patient
                        patient.addPatient();
                        System.out.println();
                        break;

                    case 2:
                        //view patient
                        patient.viewPatients();
                        System.out.println();
                        break;

                    case 3:
                        //view doctors
                        doctor.viewDoctors();
                        System.out.println();
                        break;

                    case 4:
                        //Book Appointments
                        bookAppointments(patient, doctor, connection, scanner);
                        System.out.println();
                        break;

                    case 5:
                        return;
                    default:
                        System.out.println("Enter a Valid Choice:");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointments(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
        System.out.println("Enter Patient Id");
        int patientid = scanner.nextInt();
        System.out.println("Enter Doctor Id");
        int doctorid = scanner.nextInt();
        System.out.println("Enter  Appointment Date (YYYY-MM-DD)");
        String appointmentDate = scanner.next();
        if (patient.getPatientById(patientid) && doctor.getDoctorById(doctorid)) {
            if (checkDoctorAvailability(doctorid, appointmentDate, connection)) {
                String appointmentQuery = "INSERT INTO appointments(patient_id,doctor_id,appointment_date) VALUES (?,?,?)";
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(appointmentQuery);
                    preparedStatement.setInt(1, patientid);
                    preparedStatement.setInt(2, doctorid);
                    preparedStatement.setString(3, appointmentDate);
                    int rowaffected = preparedStatement.executeUpdate();
                    if (rowaffected > 0) {
                        System.out.println("Appointment Booked ");
                    } else {
                        System.out.println("Failed to book Appointment");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor is not availbale on this date  ");
            }

        } else {
            System.out.println("Either doctor or patient doesnt exist");
        }

    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
        //no. of rows correspoinding to query
        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setString(2, appointmentDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                if (count == 0) {
                    return true;
                } else {
                    return false;
                }


            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

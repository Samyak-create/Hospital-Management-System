package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public   Patient (Connection connection, Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;

    }
    public void addPatient(){
        System.out.println("Enter Patient Name");
        scanner.nextLine();
        String name=scanner.nextLine();

        System.out.println("Enter Patient Age");

// First, check if the input is an integer
        while (!scanner.hasNextInt()) {
            System.out.println("Invalid input! Please enter a valid age:");
            scanner.next();
        }
        int age = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Enter Paient Gender ");
        String gender =scanner.next();

        try{
            String query="INSERT INTO PATIENTS(name,age,gender )VALUES(?,?,?)";
            //TO RUN QUERY
           PreparedStatement preparedStatement= connection.prepareStatement(query);
           //TAKING INPUT
            preparedStatement.setString(1,name);
            preparedStatement.setInt(2,age);
            preparedStatement.setString(3,gender);
            int affectedRows=preparedStatement.executeUpdate();
            if(affectedRows>0){
                System.out.println("Patient Added Succesfully !! ");
            }else{
                System.out.println("Failed to add Patient ");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }

    }
        public void viewPatients(){
        String query = "SELECT * FROM PATIENTS";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
           //TO PRINT THE DATA
            ResultSet resultSet =preparedStatement.executeQuery();
            System.out.println("PATIENTS:");
            System.out.println("+------------+--------------------+-------+------------+");
            System.out.println("| Patient Id | Name               | Age   | Gender     |");
            System.out.println("+------------+--------------------+-------+------------+");
            while (resultSet.next()){
                int id =resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                System.out.printf("|%-12s|%-20s|%-7s|%-12s|\n",id,name,age,gender);
                System.out.println("+------------+--------------------+-------+------------+");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        }
        public boolean getPatientById(int id ){
        String query="SELECT *FROM PATIENTS WHERE id=?";
        try{
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            if(resultSet.next()){
                return true;
            }else{
                return false;
            }

        }catch(SQLException e){
            e.printStackTrace();
            }
           return false;
        }
}

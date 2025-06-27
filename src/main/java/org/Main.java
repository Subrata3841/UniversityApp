package org;


import org.model.Student;
import org.dao.StudentDAO;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main{
    private static final Scanner scanner = new Scanner(System.in);
    private static final org.dao.StudentDAO studentDAO = new StudentDAO();

    public static void main(String[] args){
        System.out.println("--- Simple Student Management (CRUD)---");
        int choice;
        do {
            displayMenu();
            System.out.println("Enter your choice: ");
            choice = getUserInput();
            switch (choice) {
                case 1:
                    addStudent();
                    break;
                case 2:
                    viewAllStudent();
                    break;
                case 3:
                    viewStudentById();
                    break;
                case 4:
                    updateStudent();
                    break;
                case 5:
                    deleteStudent();
                    break;
                case 0:
                    System.out.println("Exiting application. GoodBye");
                    break;
                default:
                    System.out.println("Invalid Choice. Please Try again");
            }
            System.out.println("\n----------------------------------------\n");
        }while (choice != 0);
        scanner.close();
    }
    private static void displayMenu(){
        System.out.println("1. Add New Student (Create)");
        System.out.println("2. View All Students (Read All)");
        System.out.println("3. View Student by ID (Read One");
        System.out.println("4. Update Student (Update)");
        System.out.println("5. Delete Student (Delete");
        System.out.println("0. Exit");
    }
    //---- Helper for safe integer input
    private static int getUserInput(){
        while (!scanner.hasNext()) {
            System.out.println("Invalid input. Please enter a number");
            scanner.next();
        }
        int input = scanner.nextInt();
        scanner.nextLine();  // Consume the rest of the line(newLine)
        return input;
    }
    //---- CRUD Function ------
    //CREATE
    private static void addStudent() {
        System.out.println("\n ---- Add New Student ----");
        System.out.print("Enter Student First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Student Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter the date of birth (YYYY-MM-DD): ");
        String dobString = scanner.nextLine();
        LocalDate dateOfBirth = null;

        try{
            LocalDate localDate = dateOfBirth = LocalDate.parse(dobString);

        }catch (DateTimeParseException e){
            System.out.println("Invalid date format. Please use YYYY-MM-DD. Student not added.");
            return;
        }
        Student newStudent = new Student(firstName, lastName, dateOfBirth);
        studentDAO.addStudent(newStudent);

    }
    //READ ALL
    private static void viewAllStudent() {
        System.out.println("\n----- All Students ------");
        List<Student> students = studentDAO.getAllStudents();
        if(students.isEmpty()){
            System.out.println("No students found.");
            return;
        }
        for(Student student : students){
            System.out.println(student);
        }
    }
    //READ ONE
    private static void viewStudentById() {
        System.out.println("\n ------ View Student By ID -----");
        System.out.print("Enter Student ID: ");
        int studentId = getUserInput();
        Student student = studentDAO.getStudentById(studentId);
        if(student != null){
            System.out.println("Student Found: "+ student);
        }else{
            System.out.println("Student with ID "+ studentId + " not found.");
        }
    }
    //UPDATE
    private  static void updateStudent() {
        System.out.println("\n---- Update Student -----");
        System.out.print("Enter Student ID to Update: ");
        int studentId = getUserInput();

        Student existingStudent = studentDAO.getStudentById(studentId);
        if (existingStudent == null) {
            System.out.println("Student with ID: " + studentId + " not found");
            return;
        }

        System.out.println("Current Student Details: " + existingStudent);
        System.out.print("Enter new First Name (or press Enter to keep current: " + existingStudent.getFirstName() + "): ");
        String firstName = scanner.nextLine();
        if (!firstName.isEmpty()) {
            existingStudent.setFirstName(firstName);
        }
        System.out.print("Enter new Last Name (or press ENTER to keep current: '" + existingStudent.getLastName() + "'): ");
        String lastName = scanner.nextLine();
        if (!lastName.isEmpty()) {
            existingStudent.setLastName(lastName);
        }
        System.out.println("Enter new Date of Birth (YYYY-MM-DD) (or press Enter to keep current: '" + existingStudent.getDateOfBirth() + "'): ");
        String dobString = scanner.nextLine();
        if (!dobString.isEmpty()) {
            try {
                existingStudent.setDateOfBirth(LocalDate.parse(dobString));
            } catch (DateTimeParseException e) {
                System.err.println("Invalid date format. date not found.");
            }
        }
        studentDAO.updateStudent(existingStudent);
    }
    //DELETE
    private static void deleteStudent() {
        System.out.println("\n ---- Delete Student ----");
        System.out.print("Enter Student Id to delete: ");
        int studentId = getUserInput();
        studentDAO.deleteStudent(studentId);
    }

}


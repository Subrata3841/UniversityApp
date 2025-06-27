package org.dao;

import org.model.Student;
import org.util.DBConnection;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    // CREATE Operation
    public void addStudent(Student student) {
        String SQL = "INSERT INTO Students(first_name, last_name, date_of_birth) VALUES(?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setDate(3, Date.valueOf(student.getDateOfBirth())); // Convert LocalDate to java.sql.Date

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        student.setStudentId(rs.getInt(1)); // Set the generated ID back to the student object
                        System.out.println("Student added successfully with ID: " + student.getStudentId());
                    }
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error adding student: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // READ Operation (All Students)
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String SQL = "SELECT student_id, first_name, last_name, date_of_birth FROM Students ORDER BY student_id";
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SQL);

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth").toLocalDate() // Convert java.sql.Date to LocalDate
                );
                students.add(student);
            }
        } catch (SQLException ex) {
            System.err.println("Error getting all students: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeResources(conn, stmt, rs);
        }
        return students;
    }

    // READ Operation (Single Student by ID)
    public Student getStudentById(int studentId) {
        String SQL = "SELECT student_id, first_name, last_name, date_of_birth FROM Students WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Student student = null;
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, studentId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("date_of_birth").toLocalDate()
                );
            }
        } catch (SQLException ex) {
            System.err.println("Error getting student by ID: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return student;
    }

    // UPDATE Operation
    public void updateStudent(Student student) {
        String SQL = "UPDATE Students SET first_name = ?, last_name = ?, date_of_birth = ? WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setString(1, student.getFirstName());
            pstmt.setString(2, student.getLastName());
            pstmt.setDate(3, Date.valueOf(student.getDateOfBirth()));
            pstmt.setInt(4, student.getStudentId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student updated successfully: " + student.getStudentId());
            } else {
                System.out.println("No student found with ID: " + student.getStudentId());
            }
        } catch (SQLException ex) {
            System.err.println("Error updating student: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // DELETE Operation
    public void deleteStudent(int studentId) {
        String SQL = "DELETE FROM Students WHERE student_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(SQL);
            pstmt.setInt(1, studentId);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Student deleted successfully: " + studentId);
            } else {
                System.out.println("No student found with ID: " + studentId);
            }
        } catch (SQLException ex) {
            System.err.println("Error deleting student: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
    }

    // Helper method to close resources
    private void closeResources(Connection conn, Statement stmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBConnection.closeConnection(conn);
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
            e.printStackTrace();
        }
    }
}



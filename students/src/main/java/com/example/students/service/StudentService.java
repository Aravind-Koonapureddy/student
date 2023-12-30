package com.example.students.service;

// StudentService.java
import com.example.students.model.Student;
import com.example.students.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public Student createStudent(Student student) {
        // Perform validations
        validateStudentForCreation(student);

        // Calculate Total, Average, and Result
        calculateStudentResults(student);

        return studentRepository.save(student);
    }

    public Student updateStudentMarks(Long studentId, Integer marks1, Integer marks2, Integer marks3) {
        // Retrieve student from the database
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Perform validations
        validateStudentForUpdate(marks1, marks2, marks3);

        // Update marks and recalculate Total, Average, and Result
        student.setMarks1(marks1);
        student.setMarks2(marks2);
        student.setMarks3(marks3);

        // Calculate Total, Average, and Result
        calculateStudentResults(student);

        return studentRepository.save(student);
    }

    private void validateStudentForCreation(Student student) {
        // Validations for creating a student
        if (student.getId() != null) {
            throw new IllegalArgumentException("Id should be auto-generated.");
        }
        validateName(student.getFirstName(), "First Name");
        validateName(student.getLastName(), "Last Name");
        validateDOB(student.getDob());
        validateSection(student.getSection());
        validateGender(student.getGender());
        validateTotalAverage(student.getTotal(), student.getAverage());
        validateResult(student.getResult());
    }

    private void validateStudentForUpdate(Integer marks1, Integer marks2, Integer marks3) {
        // Validations for updating student marks
        if (marks1 == null || marks2 == null || marks3 == null) {
            throw new IllegalArgumentException("Marks 1, Marks 2, and Marks 3 are mandatory.");
        }
        validateMarksRange(marks1, "Marks 1");
        validateMarksRange(marks2, "Marks 2");
        validateMarksRange(marks3, "Marks 3");

    }

    private void calculateStudentResults(Student student) {
        // Calculate Total, Average, and Result
        int total = student.getMarks1() + student.getMarks2() + student.getMarks3();
        double average = total / 3.0;
        String result = (student.getMarks1() >= 35 && student.getMarks2() >= 35 && student.getMarks3() >= 35) ? "Pass" : "Fail";

        student.setTotal(total);
        student.setAverage(average);
        student.setResult(result);
    }

    private void validateName(String name, String fieldName) {
        if (name == null || name.length() < 3) {
            throw new IllegalArgumentException(fieldName + " should have a minimum length of 3 characters.");
        }
    }

    private void validateDOB(Date dob) {
        // Validate DOB and age
        if (dob == null) {
            throw new IllegalArgumentException("Date of Birth is mandatory.");
        }

        LocalDate birthDate = dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate currentDate = LocalDate.now();

        int age = Period.between(birthDate, currentDate).getYears();

        if (age <= 15 || age > 20) {
            throw new IllegalArgumentException("Age should be greater than 15 and less than or equal to 20 years.");
        }
    }

    private void validateSection(String section) {
        if (!"A".equals(section) && !"B".equals(section) && !"C".equals(section)) {
            throw new IllegalArgumentException("Invalid section. Valid values are A, B, and C.");
        }
    }

    private void validateGender(String gender) {
        if (!"M".equals(gender) && !"F".equals(gender)) {
            throw new IllegalArgumentException("Invalid gender. Valid values are M or F.");
        }
    }

    private void validateMarksRange(Integer marks, String fieldName) {
        if (marks < 0 || marks > 100) {
            throw new IllegalArgumentException(fieldName + " should be in the range of 0 to 100.");
        }
    }

    private void validateTotalAverage(Integer total, Double average) {
        if (total != null && (total < 0 || total > 300)) { // Assuming the maximum possible total is 300 (100 for each subject)
            throw new IllegalArgumentException("Total should be in the range of 0 to 300.");
        }

        if (average != null && (average < 0 || average > 100)) {
            throw new IllegalArgumentException("Average should be in the range of 0 to 100.");
        }
    }

    private void validateResult(String result) {
        if (result != null && !("Pass".equalsIgnoreCase(result) || "Fail".equalsIgnoreCase(result))) {
            throw new IllegalArgumentException("Invalid result. Valid values are Pass or Fail.");
        }
    }
}


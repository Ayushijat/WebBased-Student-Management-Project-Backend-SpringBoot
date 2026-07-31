package com.example.main.services;


import java.util.List;

import com.example.main.dto.AuthResponse;
import com.example.main.dto.LoginRequest;
import com.example.main.dto.SignUpRequest;
import com.example.main.dto.StudentRequestDTO;
import com.example.main.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface StudentService {

    Student addStudent(StudentRequestDTO request);

    public List<Student> getAllStudents();

    public Student getStudentById(int id);

    public Student updateStudent(int id , Student stu);

    public void deleteStudent(int id);

    public List<Student> searchByName(String name);

    public List<Student> searchByEmail(String email);

    public List<Student> searchByDepartment(String department);

    public List<Student> searchByCity(String city);


    public List<Student> fetchAllStudent(String search, String status, Pageable pageable);

    String signup(SignUpRequest request);

    AuthResponse login(LoginRequest request);

    String logout();

    long getTotalStudents();

    Student getProfile(String email);

    AuthResponse refreshToken(String refreshToken);

    Student updateOwnProfile(String email, Student student);

}


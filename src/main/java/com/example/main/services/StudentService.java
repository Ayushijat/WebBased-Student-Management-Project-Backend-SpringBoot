package com.example.main.services;


import java.util.List;

import com.example.main.dto.AuthResponse;
import com.example.main.dto.LoginRequest;
import com.example.main.dto.SignUpRequest;
import com.example.main.entities.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface StudentService {

    public Student addStudent(Student stu);

    public List<Student> getAllStudents();

    public Student getStudentById(int id);

    public Student updateStudent(int id , Student stu);

    public void deleteStudent(int id);

    public List<Student> searchByName(String name);

    public List<Student> searchByEmail(String email);

    public List<Student> searchByDepartment(String department);

    public List<Student> searchByCity(String city);


    public List<Student> fetchAllStudent(String search, Pageable pageable);

    String signup(SignUpRequest request);

    AuthResponse login(LoginRequest request);

    String logout();

}


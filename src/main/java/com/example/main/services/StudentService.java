package com.example.main.services;


import java.awt.print.Pageable;
import java.util.List;

import com.example.main.entities.Student;
import org.springframework.data.domain.Page;


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


    public Page<Student> getStudent(int page, int size, String field, String direction);
}


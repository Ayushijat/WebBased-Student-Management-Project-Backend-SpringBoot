package com.example.main.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.main.entities.Student;
import com.example.main.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repo;

    @Override
    public Student addStudent(Student stu) {
        return repo.save(stu);
    }

    @Override
    public List<Student> getAllStudents() {

        return repo.findAll();
    }

    @Override
    public Student getStudentById(int id) {

        return repo.findById(id).orElse(null);
    }

    @Override
    public Student updateStudent(int id,Student stu) {

        Student stuData = repo.findById(id).orElse(null);
        if(stuData!=null) {
            stuData.setName(stu.getName());
            stuData.setRollno(stu.getRollno());
            stuData.setCity(stu.getCity());
            stuData.setCourse(stu.getCourse());
            stuData.setDepartment(stu.getDepartment());
            stuData.setEmail(stu.getEmail());
            stuData.setGender(stu.getGender());
            return repo.save(stuData);
        }else {
            throw new RuntimeException("Student not found with the id "+id);
        }
    }

    @Override
    public void deleteStudent(int id) {
        repo.deleteById(id);

    }

    @Override
    public List<Student> searchByName(String name) {
        return repo.findByName(name);
    }

    @Override
    public List<Student> searchByEmail(String email) {
        return repo.findByEmail(email);
    }

    @Override
    public List<Student> searchByDepartment(String department) {
        return repo.findByDepartment(department);
    }

    @Override
    public List<Student> searchByCity(String city) {
        return repo.findByCity(city);
    }

    @Override
    public Page<Student> getStudent(int page, int size, String field, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(field).ascending()
                : Sort.by(field).descending();

        Pageable pageable = PageRequest.of(page,size,sort);
        return repo.findAll(pageable);
    }


}


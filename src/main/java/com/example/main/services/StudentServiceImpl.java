package com.example.main.services;


import java.util.List;

import com.example.main.dto.AuthResponse;
import com.example.main.dto.LoginRequest;
import com.example.main.dto.SignUpRequest;
import com.example.main.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.main.entities.Student;
import com.example.main.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

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
            stuData.setPassword(stu.getPassword());
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
    public List<Student> fetchAllStudent(String search, Pageable pageable) {
        if(search==null || search.isEmpty()){
            return repo.findAll(pageable).getContent();
        }else{
            return repo.findAllByName(search,pageable).getContent();
        }
    }

    @Override
    public String signup(SignUpRequest request) {
        if(repo.existsByEmail(request.getEmail())){
            return "Email already Exists";
        }

        Student stu = new Student();

        stu.setName(request.getName());
        stu.setRollno(request.getRollno());
        stu.setCity(request.getCity());
        stu.setEmail(request.getEmail());
        stu.setPassword(passwordEncoder.encode(request.getPassword()));
        stu.setCourse(request.getCourse());
        stu.setDepartment(request.getDepartment());
        stu.setGender(request.getGender());

        repo.save(stu);

        return "Student Register Successfully";

    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Student student = repo.findStuByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("Invalid Email"));
        if(!passwordEncoder.matches(request.getPassword(),student.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        String token = jwtService.generateToken(student.getEmail());


        return new AuthResponse(token,"Login Successfully");
    }

    @Override
    public String logout() {
        return "Logout Successfully";
    }


}


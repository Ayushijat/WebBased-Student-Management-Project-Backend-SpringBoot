package com.example.main.services;


import java.util.List;

import com.example.main.dto.AuthResponse;
import com.example.main.dto.LoginRequest;
import com.example.main.dto.SignUpRequest;
import com.example.main.entities.Role;
import com.example.main.repository.RoleRepository;
import com.example.main.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.main.entities.Student;
import com.example.main.repository.StudentRepository;
import com.example.main.dto.StudentRequestDTO;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repo;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Override
    public Student addStudent(StudentRequestDTO request) {

        // Check duplicate Student ID
        if (repo.existsById(request.getId())) {
            throw new RuntimeException("Student ID already exists");
        }

        // Find Role
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        // Create Student Entity
        Student student = new Student();

        student.setId(request.getId());
        student.setName(request.getName());
        student.setRollno(request.getRollno());
        student.setEmail(request.getEmail());
        student.setPassword(
                passwordEncoder.encode(request.getPassword())
        );
        student.setMobileNo(request.getMobileNo());
        student.setCourse(request.getCourse());
        student.setDepartment(request.getDepartment());
        student.setCity(request.getCity());
        student.setGender(request.getGender());

        student.setStatus("Active");
        student.setRole(role);

        return repo.save(student);
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
            stuData.setStatus(stu.getStatus());
            stuData.setMobileNo(stu.getMobileNo());

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
    public List<Student> fetchAllStudent(String search,String status,  Pageable pageable) {


        return repo
                .searchAndFilter(search, status, pageable)
                .getContent();
    }

    @Override
    public long getTotalStudents() {
        return repo.count();
    }

    @Override
    public Student getProfile(String email) {

        return repo.findStuByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {

        // 1. Validate refresh token
        if (!jwtService.validateToken(refreshToken)) {

            throw new RuntimeException(
                    "Invalid or expired refresh token"
            );
        }


        // 2. Check token type
        String tokenType =
                jwtService.extractTokenType(refreshToken);

        if (!"REFRESH".equals(tokenType)) {

            throw new RuntimeException(
                    "Invalid token type"
            );
        }


        // 3. Extract email
        String email =
                jwtService.extractEmail(refreshToken);


        // 4. Find student
        Student student =
                repo.findStuByEmail(email)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User not found"
                                )
                        );


        // 5. Generate new access token
        String newAccessToken =
                jwtService.generateAccessToken(
                        student.getEmail(),
                        student.getRole().getName()
                );


        // 6. Return new access token
        return new AuthResponse(
                newAccessToken,
                refreshToken,
                "Access token refreshed successfully"
        );
    }

    @Override
    public Student updateOwnProfile(String email, Student updateData) {
        Student existingStudent = repo.findStuByEmail(email)
                .orElseThrow(
                        () -> new RuntimeException("Student Not Found")
                );
        existingStudent.setName(updateData.getName());
        existingStudent.setMobileNo(updateData.getMobileNo());
        existingStudent.setCourse(updateData.getCourse());
        existingStudent.setDepartment(updateData.getDepartment());
        existingStudent.setGender(updateData.getGender());
        existingStudent.setCity(updateData.getCity());

        return repo.save(existingStudent);
    }

    @Override
    public String signup(SignUpRequest request) {

        // Check whether an ADMIN already exists
        long adminCount = repo.countByRoleName("ADMIN");

        if (adminCount > 0) {
            return "Signup is disabled. Admin already exists.";
        }

        if (repo.existsById(request.getId())) {
            return "Admin ID already exists";
        }


        if (repo.existsByEmail(request.getEmail())) {
            return "Email already Exists";
        }

        Student stu = new Student();

        stu.setId(request.getId());
        stu.setName(request.getName());
        stu.setRollno(request.getRollno());
        stu.setCity(request.getCity());
        stu.setEmail(request.getEmail());

        stu.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        stu.setCourse(request.getCourse());
        stu.setDepartment(request.getDepartment());
        stu.setGender(request.getGender());
        stu.setMobileNo(request.getMobileNo());

        // First registered person becomes Admin
        Role adminRole = roleRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Admin role not found"));

        stu.setRole(adminRole);

        stu.setStatus("Active");

        repo.save(stu);

        return "Admin Registered Successfully";
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Student student = repo.findStuByEmail(request.getEmail())
                .orElseThrow(()->new RuntimeException("Invalid Email"));
        if(!passwordEncoder.matches(request.getPassword(),student.getPassword())){
            throw new RuntimeException("Invalid Password");
        }

        String accessToken =
                jwtService.generateAccessToken(
                        student.getEmail(),
                        student.getRole().getName()
                );

        String refreshToken =
                jwtService.generateRefreshToken(
                        student.getEmail()
                );

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Login Successfully"
        );
    }

    @Override
    public String logout() {
        return "Logout Successfully";
    }


}




package com.example.main.controllers;

import java.util.List;

import com.example.main.dto.*;
import com.example.main.services.FileStorageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.main.entities.Student;
import com.example.main.services.StudentService;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/student")
public class MyController {

    @Autowired
    private StudentService service;

    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<ApiResponse> addStudent(
            @Valid @RequestBody StudentRequestDTO request
    ) {

        Student stu = service.addStudent(request);

        ApiResponse apiResponse = new ApiResponse(
                true,
                "Student added Successfully",
                stu
        );

        return new ResponseEntity<>(
                apiResponse,
                HttpStatus.CREATED
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllStudents() {
        List<Student> student = service.getAllStudents();
        ApiResponse response = new ApiResponse(
                true,
                "Student Retrived Successfully",
                student
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable int id) {
        Student std =  service.getStudentById(id);

        if(std!=null) {
            ApiResponse response = new ApiResponse(
                    true,
                    "Student Found",
                    std
            );
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> UpdateStudent(@PathVariable int id,@Valid @RequestBody Student stu){
        Student stuData = service.updateStudent(id, stu);
        if(stuData!=null) {
            ApiResponse response = new ApiResponse(
                    true,
                    "Student Updated Successfully",
                    stuData
            );
            return ResponseEntity.ok(response);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable int id) {
        service.deleteStudent(id);
        ApiResponse response = new ApiResponse(
                true,
                "Student Deleted Successfully",
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/name/{name}")
    public List<Student> getByName(@PathVariable String name){
        return service.searchByName(name);
    }

    @GetMapping("/email/{email}")
    public List<Student> getByEmail(@PathVariable String email){
        return service.searchByEmail(email);
    }

    @GetMapping("/department/{department}")
    public List<Student> getByDepartment(@PathVariable String department){
        return service.searchByDepartment(department);
    }

    @GetMapping("city/{city}")
    public List<Student> getByCity(@PathVariable String city){
        return service.searchByCity(city);
    }


    @GetMapping("/all")
    public List<Student> getAll(
            @RequestParam(required = false,defaultValue = "1") int pageNo,
            @RequestParam(required = false,defaultValue = "5") int pageSize,
            @RequestParam(required = false,defaultValue = "id") String sortBy,
            @RequestParam(required = false,defaultValue = "ASC") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status
    ){
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("ASC")){
            sort = Sort.by(sortBy).ascending();
        }else{
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(pageNo-1,pageSize,sort);
        return service.fetchAllStudent(search,status,pageable);
    }

    @GetMapping("/count")
    public long getTotalStudents() {
        return service.getTotalStudents();
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getProfile(
            Authentication authentication
    ) {

        String email = authentication.getName();

        Student student =
                service.getProfile(email);

        ApiResponse response =
                new ApiResponse(
                        true,
                        "Profile fetched successfully",
                        student
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse> updateProfile(@RequestBody Student student, Authentication authentication){
        String email = authentication.getName();

        Student updateStudent = service.updateOwnProfile(email,student);
        ApiResponse response = new ApiResponse(
                true,
                "Profile Updated Successfully",
                updateStudent
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/profile/photo")
    public ResponseEntity<ApiResponse> uploadProfilePhoto(
            @RequestParam("file")MultipartFile file,
            Authentication authentication
            ){
        String email = authentication.getName();
        String fileName = fileStorageService.saveFile(file);
        Student student = service.uploadProfilePhoto(email,fileName);
        ApiResponse response = new ApiResponse(
                true,
                "profile photo uploads Successfully",
                student
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ApiResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication){

        String email = authentication.getName();
        String message = service.changePassword(email,request);
        ApiResponse response = new ApiResponse(
                true,
                message,
                null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request){

        service.forgotPassword((request.getEmail()));
        ApiResponse response = new ApiResponse(
                true,
                "OTP sent Successfully",
                null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponse> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
            ){
        service.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );
        ApiResponse response = new ApiResponse(
                true,
                "OTP verified successfully",
                null
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ){
        service.resetPassword(
                request.getEmail(),
                request.getOtp(),
                request.getNewPassword()
        );

        ApiResponse response = new ApiResponse(
                true,
                "Password Reset Successfully",
                null
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inactive-count")
    public ResponseEntity<Long> getInactiveStudentsCount(){
        System.out.println("Inactive Count API Called");
        return ResponseEntity.ok(service.getInactiveStudentsCount());
    }

}

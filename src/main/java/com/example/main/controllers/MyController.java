package com.example.main.controllers;

import java.util.List;

import com.example.main.dto.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.main.entities.Student;
import com.example.main.services.StudentService;

@RestController
@RequestMapping("/student")
public class MyController {

    @Autowired
    private StudentService service;

    @PostMapping
    public ResponseEntity<ApiResponse> addStudent(@Valid @RequestBody Student student) {
        Student stu = service.addStudent(student);
        ApiResponse apiResponse = new ApiResponse(
                true,
                "Student added Successfully",
                stu
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
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
            @RequestParam(required = false) String search
    ){
        Sort sort = null;
        if(sortDir.equalsIgnoreCase("ASC")){
            sort = Sort.by(sortBy).ascending();
        }else{
            sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(pageNo-1,pageSize,sort);
        return service.fetchAllStudent(search,pageable);
    }


//
//    @GetMapping("/all")
//    public Page<Student> getAll(
//            @RequestParam int page,
//            @RequestParam int size,
//            @RequestParam String field,
//            @RequestParam String direction){
//
//        return service.getStudent(page,size,field,direction);
//
//    }




}

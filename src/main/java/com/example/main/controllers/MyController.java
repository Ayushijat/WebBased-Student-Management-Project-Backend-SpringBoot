package com.example.main.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public Student addStudent(@RequestBody Student student) {
        return service.addStudent(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Student std =  service.getStudentById(id);

        if(std!=null) {
            return ResponseEntity.ok().body(std);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> UpdateStudent(@PathVariable int id,@RequestBody Student stu){
        Student stuData = service.updateStudent(id, stu);
        if(stuData!=null) {
            return ResponseEntity.ok(stuData);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id) {
        service.deleteStudent(id);
        return "Deleted Successfully";
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

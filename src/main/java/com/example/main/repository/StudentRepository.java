
package com.example.main.repository;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

    List<Student> findByName(String name);

    List<Student> findByEmail(String email);

    List<Student> findByDepartment(String department);

    List<Student> findByCity(String city);

    Page<Student> findAllByName(String search, Pageable pageable);

}


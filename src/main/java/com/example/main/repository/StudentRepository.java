
package com.example.main.repository;
import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.main.entities.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

    List<Student> findByName(String name);

    List<Student> findByEmail(String email);

    List<Student> findByDepartment(String department);

    List<Student> findByCity(String city);

    Page<Student> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Student> findStuByEmail(String email);

    boolean existsByEmail(String email);

    @Query("""
    SELECT s FROM Student s
    WHERE
    (:search IS NULL OR
     LOWER(s.name) LIKE LOWER(CONCAT('%', :search, '%')))
    AND
    (:status IS NULL OR
     :status = 'All' OR
     s.status = :status)
    """)
    Page<Student> searchAndFilter(
            @Param("search") String search,
            @Param("status") String status,
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(s)
    FROM Student s
    WHERE s.role.name = :roleName
    """)
    long countByRoleName(@Param("roleName") String roleName);




}


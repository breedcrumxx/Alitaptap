package com.example.demo.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Student;

public interface StudentRepository extends JpaRepository<Student, Integer>{

    @Query(value = "select * from student where student_id=:stdid", nativeQuery = true)
    Student findByStudentId(@Param("stdid") String stdid);

    @Query(value = "select * from student where batch_id=:id", nativeQuery = true)
    List<Student> findByBatchId(int id);
    
}

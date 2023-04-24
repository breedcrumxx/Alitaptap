package com.example.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.Student;

public interface StudentRepository extends JpaRepository<Student, Integer>{

    @Query(value = "select * from student where student_id=:stdid", nativeQuery = true)
    Student findByStudentId(@Param("stdid") String stdid);

    @Query(value = "select * from student where batch_id=:id", nativeQuery = true)
    List<Student> findByBatchId(int id);

    // @Query(value = "select student.id, student.address, student.batch_id, student.first_name, student.last_name, student.middle_name, student.student_id from student left join sub_student on student.id=sub_student.student_id where student.batch_id=:batchid OR (sub_student.status=\"include\" AND sub_student.schedule=:schedid)", nativeQuery = true)
    // List<Student> getStudentsFromThisSchedule(@Param("batchid") int batchid,@Param("schedid") int schedid);

    // @Query(value = "select student.id, student.address, student.batch_id, student.first_name, student.last_name, student.middle_name, student.student_id from student join sub_student on student.id=sub_student.student_id where sub_student.schedule=:schedid AND (sub_student.status=\"exclude\" AND sub_student.schedule=:schedid)", nativeQuery = true)
    // List<Student> getExcludedStudents(@Param("schedid") int schedid);

    // @Query(value = "Select * from student", nativeQuery = true)
    // Student getStudentByRFID(@Param("entityid") String entityid);
    
}

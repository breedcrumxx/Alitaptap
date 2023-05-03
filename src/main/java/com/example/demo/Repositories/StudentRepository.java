package com.example.demo.Repositories;

import java.util.List;
import java.util.Optional;

import org.hibernate.mapping.Constraint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.FormattedAttendance;
import com.example.demo.Models.Student;

public interface StudentRepository extends JpaRepository<Student, Integer>{

    @Query(value = "select * from student where student_id=:stdid", nativeQuery = true)
    Student findByStudentId(@Param("stdid") String stdid);

    @Query(value = "select * from student where batch_id=:id", nativeQuery = true)
    List<Student> findByBatchId(int id);

    @Query(value = "select * from student where rfid_id=:rfid", nativeQuery = true)
    Student getByRfid(@Param("rfid") int id);

    // @Query(value = "select student.id, concat(student.first_name, ', ', student.last_name) as fullname, if(status='On time', ) from student left join sub_student on student.id=sub_student.student_id where student.batch_id=:batchid OR (sub_student.status=\"include\" AND sub_student.schedule=:schedid)", nativeQuery = true)
    // List<Student> getStudentsFromThisSchedule(@Param("batchid") int batchid,@Param("schedid") int schedid);

    @Query(value = "select student.id, student.address, student.batch_id, student.first_name, student.last_name, student.middle_name, student.student_id, student.rfid_id from student left join sub_student on student.id=sub_student.student_id where student.batch_id=:batchid OR (sub_student.status=\"include\" AND sub_student.schedule=:schedid) order by student.last_name asc", nativeQuery = true)
    List<Student> getStudentsFromThisSchedule(@Param("batchid") int batchid,@Param("schedid") int schedid);

    @Query(value = "select student.id, student.address, student.batch_id, student.first_name, student.last_name, student.middle_name, student.student_id, student.rfid_id from student join sub_student on student.id=sub_student.student_id where sub_student.schedule=:schedid AND (sub_student.status=\"exclude\" AND sub_student.schedule=:schedid)", nativeQuery = true)
    List<Student> getExcludedStudents(@Param("schedid") int schedid);

    @Query(value = "select std.id as studentid, concat(std.last_name, ', ', std.first_name) as fullname, sum(case when atd.status='On time' then 1 else 0 end) as present, sum(case when atd.status='Late' then 1 else 0 end) as late from student as std join rfid as rf on std.rfid_id=rf.id left join attendance as atd on std.id=atd.student_id where atd.schedule=:schedid group by Fullname", nativeQuery = true)
    List<FormattedAttendanceInterface> getStudentRecords(@Param("schedid") int id);

    @Query(value = "select * from student order by last_name asc", nativeQuery = true)
    List<Student> getStudentsInOrder();

    // @Query(value = "select * from student where student.:constraint = :search order by :constraint :filter", nativeQuery = true)
    // List<Student> search(@Param("search") String search, @Param("constraint") String constraint,@Param("filter") String filter);

    @Query(value = "select * from student where student.first_name like %:search% order by first_name ASC", nativeQuery = true)
    List<Student> getStudentsByFirstname(String search);

    @Query(value = "select * from student where student.last_name like %:search% order by last_name ASC", nativeQuery = true)
    List<Student> getStudentsByLastname(String search);

    @Query(value = "select * from student where student.middle_name like %:search% order by middle_name ASC", nativeQuery = true)
    List<Student> getStudentsByMiddlename(String search);

    @Query(value = "select std.id , std.first_name, std.last_name, std.middle_name, std.student_id, std.batch_id, std.rfid_id, std.address, rf.rfid, rf.used_by, btc.course, btc.section from student as std join batch as btc on std.batch_id=btc.id join rfid as rf on std.rfid_id=rf.id where concat(btc.course, ' - ', btc.section) like %:search% order by last_name ASC", nativeQuery = true)
    List<Student> getStudentsByClass(String search);

    @Query(value = "select * from student where student.address like %:search% order by last_name ASC", nativeQuery = true)
    List<Student> getStudentsByAddress(String search);

    @Query(value = "select * from student where student.student_id like %:search% order by last_name ASC", nativeQuery = true)
    List<Student> getStudentsByStudentId(String search);
    
}

package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.Models.SubStudent;

public interface SubStudentRepository extends JpaRepository<SubStudent, Integer> {

    @Query(value = "delete from sub_student where student_id=:id AND include_to=:schedid", nativeQuery = true)
    void delete(@Param("id") int id, @Param("schedid") int schedid);

    @Query(value = "select * from sub_student where student_id=:id AND exclude_to=:schedid", nativeQuery = true)
    SubStudent exist(@Param("id") int id, @Param("schedid") int schedid);
    
}

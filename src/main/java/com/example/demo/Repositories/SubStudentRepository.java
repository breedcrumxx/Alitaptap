package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.Models.SubStudent;

public interface SubStudentRepository extends JpaRepository<SubStudent, Integer> {

    @Query(value = "delete from sub_student where student_id=:id AND include_to=:schedid", nativeQuery = true)
    void delete(int id, int schedid);
    
}

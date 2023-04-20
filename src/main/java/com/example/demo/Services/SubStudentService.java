package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.SubStudent;
import com.example.demo.Repositories.SubStudentRepository;

@Service
public class SubStudentService {
    
    @Autowired
    SubStudentRepository subStudentRepository;

    public SubStudent transferStudent(SubStudent sub) {
        return subStudentRepository.save(sub);
    }

    public void revert(int id, int schedid) {
        subStudentRepository.delete(id, schedid);
    }

    public boolean excluded(int id, int schedid){
        SubStudent record = subStudentRepository.exist(id, schedid);

        if(record == null){
            return false;
        }

        return true;
    }


}

package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Student;
import com.example.demo.Repositories.BatchRepository;
import com.example.demo.Repositories.ScheduleRepository;
import com.example.demo.Repositories.StudentRepository;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    BatchRepository batchRepository;

    @Autowired
    ScheduleRepository scheduleRepository;

    public boolean exist(String stdid){
        Student student = studentRepository.findByStudentId(stdid);

        if(student == null){
            return false;
        }

        return true;
    }

    public Student create(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudent(int id){
        Optional<Student> student = studentRepository.findById(id);

        if(!student.isPresent()){
            return null;
        }

        return student.get();
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public List<Student> getFromBatchId(int id) {
        return studentRepository.findByBatchId(id);
    }

    public List<Student> getStudents(int batchid,int schedid) {
        // return studentRepository.getStudentsFromThisSchedule(batchid ,schedid);
        return null;
    }

    public List<Student> getExcludedStudents(int schedid) {
        // return studentRepository.getExcludedStudents(schedid);
        return null;
    }

    // public Student getStudentByRFID(String rfid) {
    //     return studentRepository.getStudentByRFID(rfid);
    // }

}

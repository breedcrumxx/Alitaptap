package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.FormattedAttendance;
import com.example.demo.Models.RFID;
import com.example.demo.Models.Student;
import com.example.demo.Repositories.BatchRepository;
import com.example.demo.Repositories.FormattedAttendanceInterface;
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

    @Autowired
    RFIDService rfidService;

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
        return studentRepository.getStudentsInOrder();
    }

    public List<Student> getFromBatchId(int id) {
        return studentRepository.findByBatchId(id);
    }

    public List<Student> getStudents(int batchid,int schedid) {
        return studentRepository.getStudentsFromThisSchedule(batchid ,schedid);
    }

    public List<Student> getExcludedStudents(int schedid) {
        return studentRepository.getExcludedStudents(schedid);
    }

    public Student getStudentByRfid(int id) {
        return studentRepository.getByRfid(id);
    }

    public List<Student> getStudentsFromThisSchedule(int batchid, int schedid) {
        return studentRepository.getStudentsFromThisSchedule(batchid, schedid);
    }

    public List<FormattedAttendanceInterface> getStudentRecords(int schedid){
        return studentRepository.getStudentRecords(schedid);
    }

    // public Student updateStudent(Student student) {
    //     Student entity = getStudent(student.getId());

    //     int id = student.getId();
    //     String firstname = student.getFirstName();
    //     String middlename = student.getMiddleName();
    //     String lastname = student.getLastName();
    //     String address = student.getAddress();
    //     String studentid = student.getStudentId();
    //     int batch = student.getBatchId().getId();
    //     RFID rfid = student.getStudentRFID();

    //     if(entity.getStudentRFID().getRfid() != rfid.getRfid()){
    //         // change rfid
    //         RFID tempRFID = rfidService.getRfidById(entity.getStudentRFID().getId());
    //         tempRFID = rfidService.update()

    //         return null;
    //     }



    //     studentRepository.update(id, firstname, middlename, lastname, address, studentid, batch);

    //     return null;
    // }

    // public Student getStudentByRFID(String rfid) {
    //     return studentRepository.getStudentByRFID(rfid);
    // }

}

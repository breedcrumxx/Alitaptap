package com.example.demo.Controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.Attendance;
import com.example.demo.Models.Batch;
import com.example.demo.Models.FormattedAttendance;
import com.example.demo.Models.JsonResponse;
import com.example.demo.Models.RFID;
import com.example.demo.Models.Student;
import com.example.demo.Repositories.FormattedAttendanceInterface;
import com.example.demo.Repositories.StudentRepository;
import com.example.demo.Services.AttendanceService;
import com.example.demo.Services.BatchService;
import com.example.demo.Services.RFIDService;
import com.example.demo.Services.StudentService;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@RestController
@RequestMapping(path = "/api/student")
public class studentController {
    
    private static final String JsonMethod = null;

    @Autowired
    StudentService studentService;

    @Autowired
    BatchService batchService;

    @Autowired
    RFIDService rfidService;

    @Autowired
    AttendanceService attendanceService;

    @PostMapping(path = "/create-student")
    private JsonResponse insertStudent(@RequestBody Student student){
        JsonResponse response = new JsonResponse();
        boolean studentExist = studentService.exist(student.getStudentId());

        if(studentExist == true){
            response.status = "Error";
            response.message = "Student already exist.";

            return response;
        }
        
        RFID rfid = student.getStudentRFID();
        RFID created = rfidService.verifyAndCreate(rfid);

        if(created == null){
            response.status = "Error";
            response.message = "Internal server error, RFID might already be in use.";

            return response;
        }
        
        student.setStudentRFID(created);
        Student createdStudent = studentService.create(student);

        if(createdStudent == null){
            response.status = "Error";
            response.message = "Error creating a student record, internal server error.";

            return response;
        }

        response.status = "Success";
        response.message = "Successfully added a student to the record.";

        return response;
    }

    @PostMapping(path = "/update-student")
    private JsonResponse updateStudent(@RequestBody Student student){
        JsonResponse response = new JsonResponse();

        //get rfid first 
        RFID rfid = rfidService.getRfidById(student.getStudentRFID().getId());

        if(!rfid.getRfid().equals(student.getStudentRFID().getRfid())){
            //changed
            rfid = rfidService.verify(student.getStudentRFID().getRfid());

            if(rfid != null){
                response.status = "Error";
                response.status = "RFID is already in used.";

                return response;
            }
            System.out.println(4);
            rfid = rfidService.create(student.getStudentRFID());

            if(rfid == null){
                response.status = "Error";
                response.message = "Unable to process your request, internal server error.";

                return response;
            }
        }
        System.out.println(2);
        student.setStudentRFID(rfid);

        Student updated = studentService.create(student);

        if(updated == null){
            response.status = "Error";
            response.message = "Unable to process your request, internal server error.";

            return response;
        }
        System.out.println(3);
        response.status = "Success";
        response.message = "Successfully updated a student.";

        return response;
    }

    @GetMapping(path = "/get-student/{id}")
    private JsonResponse getStudent(@PathVariable int id){
        JsonResponse response = new JsonResponse();
        Student student = studentService.getStudent(id);
        
        if(student == null){
            response.status = "Error";
            response.message = "Server error, missing student record.";

            return response;
        }

        ObjectMapper map = new ObjectMapper();

        String json = "";
        try {
            json = map.writeValueAsString(student);
        } catch (JsonProcessingException e) {
            response.status = "Error";
            response.message = "Error processing your request, internal server error. (Student controller)";

            return response;
        }
        
        response.status = "Success";
        response.message = json;

        return response;
    }

    @GetMapping(path = "/get-students")
    private JsonResponse getStudents(){
        JsonResponse response = new JsonResponse();
        List<Student> students = studentService.getStudents();

        if(students.size() == 0){
            response.status = "Empty";
            response.message = "No records found.";

            return response;
        }

        // List<Batch> batches = batchService.getAllBatch();
        // List<Student> finStd = new ArrayList<>();

        // for(Student student: students){
        //     int batchId = student.getBatchId().getId();
        //     for(Batch batch: batches){
        //         if(batch.getId() == batchId){
        //             student.setBatchId(batch);
        //             System.out.println(student.toString());
        //             break;
        //         }
        //     }

        //     finStd.add(student);
        // }

        ObjectMapper map = new ObjectMapper();
        // map.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        map.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        String json = "";
        try {
            json = map.writeValueAsString(students);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            System.out.println(e.toString());
            response.status = "Error";
            response.message = "Error processing your request, internal server error. (Student controller)";

            return response;
        }

        response.status = "Success";
        response.message = json;

        return response;
    }

    @GetMapping(path = "/get-students/{id}/schedule/{batch}")
    private JsonResponse getStudentsFromThisSchedule( @PathVariable("batch") int batch, @PathVariable("id") int id){
        JsonResponse response = new JsonResponse();
        List<Student> students = studentService.getStudentsFromThisSchedule(batch ,id);

        if(students.size() == 0 || students == null){
            response.status = "Empty";
            response.message = "No students in this class schedule.";

            return response;
        }

        ObjectMapper map = new ObjectMapper();
        String json = "";

        try {
            json = map.writeValueAsString(students);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.status = "Error";
            response.message = "Error processing your request, internal server error. (Student controller)";

            return response;
        }

        response.status = "Success";
        response.message = json;
        
        return response;
    }

    @PutMapping(path = "/{stdid}/to-batch/{batchid}")
    private JsonResponse transferToBatch(@PathVariable("stdid") int stdid, @PathVariable("batchid") int batchid){
        JsonResponse response = new JsonResponse();
        // get batch
        Batch batch = batchService.getBatch(batchid);

        if(batch == null){
            response.status = "Error";
            response.message = "Seems like there is an internal server error, cannot find the class.";

            return response;
        }

        // getStudent 
        Student student = studentService.getStudent(stdid);

        if(student == null){
            response.status = "Error";
            response.message = "Server error, missing student record.";

            return response;
        }

        student.setBatchId(batch);
        Student check = studentService.create(student);

        if(check == null){
            response.status = "Error";
            response.message = "Cannot transfer the student, internal server error.";

            return response;
        }

        response.status = "Success";
        response.message = "Successfully transfered the student.";

        return response;
    }

    @GetMapping(path = "/get-students/{batchid}/batch/{schedid}/schedule")
    private JsonResponse getStudentRecords(@PathVariable("schedid") int schedid, @PathVariable("batchid") int batchid){
        JsonResponse response = new JsonResponse();
        List<FormattedAttendanceInterface> attendance = studentService.getStudentRecords(schedid);
        List<Student> students = studentService.getStudentsFromThisSchedule(batchid, schedid);
        List<Student> excluded = studentService.getExcludedStudents(schedid);

        if(students.size() == 0){
            response.status = "Success";
            response.message = "No students yet.";

            return response;
        }

        students.removeAll(excluded);
        List<FormattedAttendance> formatted = new ArrayList<>();

        for(Student student : students){
            FormattedAttendance temp = new FormattedAttendance();
            temp.setStudentId(student.getId());
            temp.setFullName(student.getLastName() + ", " + student.getFirstName());
            formatted.add(temp);
        }

        List<FormattedAttendance> finList = new ArrayList<>();

        for(FormattedAttendance att : formatted){
            int id = att.getStudentId();

            for(FormattedAttendanceInterface attInt : attendance){
                if(attInt.getStudentId() == id){
                    att.setPresent(attInt.getPresent());
                    att.setLate(attInt.getLate());
                    continue;
                }
            }

            finList.add(att);
        }

        if(attendance.size() == 0){
            finList = formatted;
        }

        ObjectMapper map = new ObjectMapper();

        String json = "";

        try {
            json = map.writeValueAsString(finList);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.status = "Success";
            response.message = "Internal server error, unable to process your request.";

            return response;
        }
        response.status = "Success";
        response.message = json;

        return response;
    }

    @GetMapping(path = "/get-detailed/{schedid}/schedule")
    private JsonResponse getDetailedAttendance(@PathVariable int schedid){
        JsonResponse response = new JsonResponse();
        List<Attendance> attendances = attendanceService.getDetailedAttendance(schedid);

        ObjectMapper map = new ObjectMapper();
        String json = "";

        try {
            json = map.writeValueAsString(attendances);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            response.status = "Error";
            response.message = "Unable to process your request, internal server error.";

            return response;
        }

        response.status = "Success";
        response.message = json;

        return response;
    }

}

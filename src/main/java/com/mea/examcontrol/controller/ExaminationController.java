package com.mea.examcontrol.controller;

import com.mea.examcontrol.model.DepartmentsModel;
import com.mea.examcontrol.model.ExamModel;
import com.mea.examcontrol.model.ExaminationModel;
import com.mea.examcontrol.model.HallWiseArrangements;
import com.mea.examcontrol.repository.departments.entity.DepartmentsEntity;
import com.mea.examcontrol.repository.examination.enitiy.*;
import com.mea.examcontrol.services.ExaminationServices;
import com.mea.examcontrol.util.CSVHelper;
import com.mea.examcontrol.util.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("exam")
public class ExaminationController {

    @Autowired
    ExaminationServices examinationServices;

    @GetMapping("/all")
    private List<ExaminationHeadEntity> getAllExams() {
        return examinationServices.getAllExams();
    }

    @GetMapping("/get/{id}")
    private ExaminationHeadEntity getExam(@PathVariable("id") Integer id) {
        return examinationServices.getExam(id);
    }

    @PostMapping(value = "/add")
    private ResponseEntity addExam(@RequestBody ExaminationModel examinationModel) {
        return examinationServices.addExam(examinationModel);
    }

    @PostMapping(value = "/upload/student/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity uploadStudents(@PathVariable("id") Integer id, @RequestPart("file") MultipartFile file) {
        String message = "";
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                examinationServices.uploadStudents(id, file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @PostMapping(value = "/upload/staff/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity uploadStaffs(@PathVariable("id") Integer id, @RequestPart("file") MultipartFile file) {
        String message = "";
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                examinationServices.uploadStaffs(id, file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @PostMapping(value = "/upload/room/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity uploadRooms(@PathVariable("id") Integer id, @RequestPart("file") MultipartFile file) {
        String message = "";
        if (CSVHelper.hasCSVFormat(file)) {
            try {
                examinationServices.uploadRooms(id, file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename();
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
    }

    @PostMapping("/generate/{id}")
    private ResponseEntity generateArrangements(@PathVariable("id") Integer id) {
        return examinationServices.generateArrangements(id);
    }

    @GetMapping("/generate/get/{id}")
    private List<ExaminationGenerated> getArrangements(@PathVariable("id") Integer id) {
        return examinationServices.getArrangements(id);
    }

    @GetMapping("/generate/hall/get/{id}")
    private List<HallWiseArrangements> getArrangementsRoom(@PathVariable("id") Integer id) {
        return examinationServices.getArrangementsRoom(id);
    }

    @GetMapping("/halls/get/{id}")
    private List<ExaminationHalls> getExamHalls(@PathVariable("id") Integer id) {
        return examinationServices.getExamHalls(id);
    }

    @GetMapping("/students/get/{id}")
    private List<ExaminationStudents> getExamStudents(@PathVariable("id") Integer id) {
        return examinationServices.getExamStudents(id);
    }

    @GetMapping("/staff/get/{id}")
    private List<ExaminationStaffs> getExamStaffs(@PathVariable("id") Integer id) {
        return examinationServices.getExamStaffs(id);
    }

    @GetMapping("/get/student/{regno}")
    private List<ExamModel> getExamHistoryByStudent(@PathVariable("regno") String regno){
        return examinationServices.getExamHistoryByStudent(regno);
    }

    @GetMapping("/get/staff/{staffid}")
    private List<ExamModel> getExamHistoryByStaff(@PathVariable("staffid") String staffid){
        return examinationServices.getExamHistoryByStaff(staffid);
    }

    @PostMapping("/notify/{id}")
    private ResponseEntity sendNotification(@PathVariable("id") Integer id) {
        return examinationServices.sendNotification(id);
    }

    @PostMapping("/clear/{id}")
    private ResponseEntity clearGenerated(@PathVariable("id") Integer id) {
        return examinationServices.clearGenerated(id);
    }

    @PostMapping("/close/{id}")
    private ResponseEntity closeExam(@PathVariable("id") Integer id) {
        return examinationServices.closeExam(id);
    }

    @PostMapping("/suspend/{id}")
    private ResponseEntity suspendExam(@PathVariable("id") Integer id) {
        return examinationServices.suspendExam(id);
    }

    @PostMapping(value = "/add/attendance")
     private ResponseEntity addAttendance(@RequestBody List<ExamModel> examModels) {
         return examinationServices.addAttendance(examModels);
     }

 
}

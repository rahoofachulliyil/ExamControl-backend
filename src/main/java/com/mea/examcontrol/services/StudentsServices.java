package com.mea.examcontrol.services;

import com.mea.examcontrol.model.StudentsModel;
import com.mea.examcontrol.repository.students.StudentsRepository;
import com.mea.examcontrol.repository.students.entity.StudentsEntity;
import com.mea.examcontrol.util.mail.Mail;
import com.mea.examcontrol.util.mail.MailRepository;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class StudentsServices {

    @Autowired
    private StudentsRepository studentsRepository;

    @Autowired
    private MailRepository mailRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    public boolean isRegNoExists(String registernumber) {
        return studentsRepository.existsByRegisternumber(registernumber);
    }

    public boolean isExistingId(int id) {
        return studentsRepository.existsById(id);
    }

    public ResponseEntity addNewStudent(StudentsModel studentsModel) {
        if (!isExistingId(studentsModel.getId())) {
            StudentsEntity studentsEntity = modelMapper.map(studentsModel, StudentsEntity.class);
            studentsEntity.setSecret(studentsModel.getRegisternumber()+"_"+studentsModel.getEmail());
            studentsRepository.saveAndFlush(studentsEntity);
            return new ResponseEntity<>("Student Created Successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Oops! unable to crate, branch already exist", HttpStatus.CONFLICT);
        }
    }

    public StudentsEntity getStudent(Integer id) {
        return studentsRepository.findById(id).get();
    }

    public List<StudentsEntity> getAllStudents() {
        return studentsRepository.findAll();
    }

    public ResponseEntity updateStatus(int id) {
        int status = studentsRepository.findById(id).get().getStatus();
        switch (status){
            case 1:
                status = 0;
                break;
            case 0:
                status =1;
                break;
        }
        studentsRepository.updateStatus(status, id);
        return new ResponseEntity<>("Status Updated", HttpStatus.OK);
    }

    public void addStudentFromCSV(CSVRecord csvRecord) {
        StudentsEntity studentsEntity = new StudentsEntity();
        studentsEntity.setFullname(csvRecord.get(0));
        studentsEntity.setDepartment(csvRecord.get(1));
        studentsEntity.setRegisternumber(csvRecord.get(2));
        studentsEntity.setSemseter(csvRecord.get(3));
        studentsEntity.setEmail(csvRecord.get(5));
        studentsEntity.setStatus(1);
        studentsEntity.setSecret(csvRecord.get(2)+"_"+csvRecord.get(5));
        studentsEntity.setCreatetimestamp(new Timestamp(new Date().getTime()));
        studentsRepository.saveAndFlush(studentsEntity);
    }

    public ResponseEntity studentLogin(StudentsModel studentsModel) {
        if(studentsRepository.existsByRegisternumber(studentsModel.getRegisternumber())){
            StudentsEntity studentsEntity = studentsRepository.getByRegisternumber(studentsModel.getRegisternumber());
            if(studentsEntity.getSecret().equals(studentsModel.getSecret())){
                StudentsModel studentsModel1 = modelMapper.map(studentsEntity, StudentsModel.class);
                return new ResponseEntity<>(studentsModel1, HttpStatus.OK);
            }else{
                return new ResponseEntity<>(studentsModel, HttpStatus.UNAUTHORIZED);
            }
        }else{
            return new ResponseEntity<>(studentsModel, HttpStatus.CONFLICT);
        }
    }

    public ResponseEntity<?> updateStudent(StudentsModel studentsModel) {
        System.out.println(studentsModel);
        StudentsEntity studentsEntity = studentsRepository.getByRegisternumber(studentsModel.getRegisternumber());
        if(studentsEntity==null) return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        if(studentsModel.getFullname() !=null){
            studentsEntity.setFullname(studentsModel.getFullname());
        }
        if(studentsModel.getAddress() !=null){
            studentsEntity.setAddress(studentsModel.getAddress());
        }
        if(studentsModel.getMobile() !=null){
            studentsEntity.setMobile(studentsModel.getMobile());
        }
        if(studentsModel.getSemseter() !=null){
            studentsEntity.setSemseter(studentsModel.getSemseter());
        }
        if(studentsModel.getSecret() !=null){
            studentsEntity.setSecret(studentsModel.getSecret());
        }

        studentsRepository.save(studentsEntity);
        studentsEntity.setSecret("");
        return new ResponseEntity<>(studentsEntity, HttpStatus.OK);
    }

    public ResponseEntity updateStudentSecret(StudentsModel studentsModel) {

        studentsRepository.updateSecret(studentsModel.getSecret(), studentsModel.getRegisternumber());
        return new ResponseEntity<>("Secret Updated", HttpStatus.OK);
    }

    public ResponseEntity generateStudentSecret(StudentsModel studentsModel) {
        StudentsEntity studentsEntity = studentsRepository.getByRegisternumber(studentsModel.getRegisternumber());
        if(studentsEntity==null) return new ResponseEntity<>("Student not found", HttpStatus.NOT_FOUND);
        String uuid = UUID.randomUUID().toString().substring(0, 6);
        studentsRepository.updateSecret(uuid, studentsModel.getRegisternumber());

        Mail mail = new Mail();
        mail.setMailTo("examcontrolautomation@gmail.com");
        mail.setMailSubject("Password generated");
        mail.setMailContent("For the registernumber : "+studentsModel.getRegisternumber()+" \n\n Password is : "+uuid);


        mailRepository.sendEmail(mail);

        return new ResponseEntity<>("Secret Generated", HttpStatus.OK);
    }

    public StudentsEntity getStudentByRegsiterNumber(String student) {
        return studentsRepository.getByRegisternumber(student);
    }
}

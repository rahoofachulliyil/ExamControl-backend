package com.mea.examcontrol.services;

import com.mea.examcontrol.model.ExamModel;
import com.mea.examcontrol.model.ExaminationModel;
import com.mea.examcontrol.model.HallWiseArrangements;
import com.mea.examcontrol.repository.examination.*;
import com.mea.examcontrol.repository.examination.enitiy.*;
import com.mea.examcontrol.repository.students.entity.StudentsEntity;
import com.mea.examcontrol.util.fcm.PushNotificationRequest;
import com.mea.examcontrol.util.fcm.PushNotificationService;
import com.mea.examcontrol.util.mail.Mail;
import com.mea.examcontrol.util.mail.MailService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ExaminationServices {

    @Autowired
    ExaminationHeadRepository examinationHeadRepository;
    @Autowired
    ExaminationStudentsRepository examinationStudentsRepository;
    @Autowired
    ExaminationStaffsRepository examinationStaffsRepository;
    @Autowired
    ExaminationHallsRepository examinationHallsRepository;
    @Autowired
    ExaminationGeneratedRepository examinationGeneratedRepository;
    @Autowired
    StudentsServices studentsServices;
    @Autowired
    StaffsServices staffsServices;
    @Autowired
    HallsServices hallsServices;
    @Autowired
    private PushNotificationService pushNotificationService;
    @Autowired
    private MailService mailService;

    @Value("${app.exam.generated.topic}")
    private String topic;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Value("${app.exam.generated.samplemail}")
    private String toMail;

    private final ModelMapper modelMapper = new ModelMapper();

    public List<ExaminationHeadEntity> getAllExams() {
        return examinationHeadRepository.findAll();
    }

    public ExaminationHeadEntity getExam(Integer id) {
        return examinationHeadRepository.findById(id).get();
    }

    public ResponseEntity addExam(ExaminationModel examinationModel) {
        if (!isExistingByName(examinationModel.getName())) {
            ExaminationHeadEntity examinationHeadEntity = new ExaminationHeadEntity();
            examinationHeadEntity.setName(examinationModel.getName());
            examinationHeadEntity.setDescription(examinationModel.getDescription());
            examinationHeadEntity.setStudentsperhall(examinationModel.getStudentsperhall());
            examinationHeadEntity.setExamdate(examinationModel.getExamdate());
            examinationHeadEntity.setGenerated(0);
            examinationHeadEntity.setStudentsupload(0);
            examinationHeadEntity.setHallsupload(0);
            examinationHeadEntity.setStaffsupload(0);
            examinationHeadEntity.setStatus("pending");
            examinationHeadEntity.setCreatetimestamp(new Timestamp(new Date().getTime()));
            examinationHeadRepository.saveAndFlush(examinationHeadEntity);
            return new ResponseEntity<>("Exam Created Successfully", HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Oops! unable to create cause exam already exist", HttpStatus.CONFLICT);
        }
    }

    public boolean isExistingByName(String name) {
        return examinationHeadRepository.existsByName(name);
    }

    public void uploadStudents(Integer id, MultipartFile file) {
        try {
            List<ExaminationStudents> examinationStudents = csvToExaminationStudentsEntity(file.getInputStream(), id);
            examinationStudentsRepository.saveAll(examinationStudents);
            examinationHeadRepository.updateStudentStatus(id);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    public List<ExaminationStudents> csvToExaminationStudentsEntity(InputStream is, Integer examid) {
        List<ExaminationStudents> examinationStudents = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                ExaminationStudents examinationStudent;
                if (!studentsServices.isRegNoExists(csvRecord.get(2))) {
                    studentsServices.addStudentFromCSV(csvRecord);
                    examinationStudent = mapStudents(examid, csvRecord);
                } else {
                    examinationStudent = mapStudents(examid, csvRecord);
                }
                examinationStudents.add(examinationStudent);
            }
            return examinationStudents;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private static ExaminationStudents mapStudents(Integer examid, CSVRecord csvRecord) {
        ExaminationStudents examinationStudent = new ExaminationStudents();
        examinationStudent.setExamid(examid);
        examinationStudent.setName(csvRecord.get(0));
        examinationStudent.setDepartment(csvRecord.get(1));
        examinationStudent.setRegisternumber(csvRecord.get(2));
        examinationStudent.setSemester(csvRecord.get(3));
        examinationStudent.setSubject(csvRecord.get(4));
        examinationStudent.setEmail(csvRecord.get(5));
        examinationStudent.setCreatetimestamp(new Timestamp(new Date().getTime()));
        return examinationStudent;
    }

    public void uploadStaffs(Integer id, MultipartFile file) {
        try {
            List<ExaminationStaffs> examinationStaffs = csvToExaminationStaffsEntity(file.getInputStream(), id);
            examinationStaffsRepository.saveAll(examinationStaffs);
            examinationHeadRepository.updateStaffsStatus(id);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    private List<ExaminationStaffs> csvToExaminationStaffsEntity(InputStream inputStream, Integer examid) {
        List<ExaminationStaffs> examinationStaffs = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                ExaminationStaffs examinationStaff;
                if (!staffsServices.isStaffIdExists(csvRecord.get(1))) {
                    staffsServices.addStaffFromCSV(csvRecord);
                    examinationStaff = mapStaffs(examid, csvRecord);
                } else {
                    examinationStaff = mapStaffs(examid, csvRecord);
                }
                examinationStaffs.add(examinationStaff);
            }
            return examinationStaffs;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private ExaminationStaffs mapStaffs(Integer examid, CSVRecord csvRecord) {
        ExaminationStaffs examinationStaffs = new ExaminationStaffs();
        examinationStaffs.setExamid(examid);
        examinationStaffs.setDepartment(csvRecord.get(2));
        examinationStaffs.setName(csvRecord.get(0));
        examinationStaffs.setStaffid(csvRecord.get(1));
        examinationStaffs.setCreatetimestamp(new Timestamp(new Date().getTime()));
        return examinationStaffs;
    }

    public void uploadRooms(Integer id, MultipartFile file) {
        try {
            List<ExaminationHalls> examinationHalls = csvToExaminationHallsEntity(file.getInputStream(), id);
            examinationHallsRepository.saveAll(examinationHalls);
            examinationHeadRepository.updateHallsStatus(id);
        } catch (IOException e) {
            throw new RuntimeException("fail to store csv data: " + e.getMessage());
        }
    }

    private List<ExaminationHalls> csvToExaminationHallsEntity(InputStream inputStream, Integer examid) {
        List<ExaminationHalls> examinationHalls = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {
            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            for (CSVRecord csvRecord : csvRecords) {
                ExaminationHalls examinationHall;
                if (!hallsServices.isHallnoExists(csvRecord.get(1))) {
                    hallsServices.addHallFromCSV(csvRecord);
                    examinationHall = mapRooms(examid, csvRecord);
                } else {
                    examinationHall = mapRooms(examid, csvRecord);
                }
                examinationHalls.add(examinationHall);
            }
            return examinationHalls;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    private ExaminationHalls mapRooms(Integer examid, CSVRecord csvRecord) {
        ExaminationHalls examinationHalls = new ExaminationHalls();
        examinationHalls.setExamid(examid);
        examinationHalls.setDepartment(csvRecord.get(0));
        examinationHalls.setHallno(csvRecord.get(1));
        examinationHalls.setTablecol(Integer.parseInt(csvRecord.get(2))*Integer.parseInt(csvRecord.get(4)));
        examinationHalls.setTablerow(Integer.parseInt(csvRecord.get(3)));
        examinationHalls.setStudentsperhall(Integer.parseInt(csvRecord.get(2)) * Integer.parseInt(csvRecord.get(3))*Integer.parseInt(csvRecord.get(4)));
        examinationHalls.setStudentperbench(Integer.parseInt(csvRecord.get(4)));
        examinationHalls.setCreatetimestamp(new Timestamp(new Date().getTime()));
        return examinationHalls;
    }

    public ResponseEntity generateArrangements(Integer id) {

        ExaminationHeadEntity examinationHeadEntity = examinationHeadRepository.getById(id);
        long totalstudents = examinationHeadEntity.getExaminationStudents().size();
        long totalseats = examinationHeadEntity.getExaminationHalls().stream().mapToInt(o -> o.getTablecol()*o.getTablerow()).sum();

        int index = 0;
        if (examinationHeadEntity.getExaminationHalls().size() == examinationHeadEntity.getExaminationStaffs().size()) {
            if (totalstudents <= totalseats) {
                Map<String, Map<String, ExaminationGenerated>> hallsmap = new HashMap<>();
                Map<String, Long> subjectWiseCount = examinationHeadEntity.getExaminationStudents().stream()
                        .collect(Collectors.groupingBy(ExaminationStudents::getSubject, Collectors.counting()));

                for (ExaminationHalls examinationHall : examinationHeadEntity.getExaminationHalls()) {
                    List<ExaminationStudents> examinationStudents = examinationStudentsRepository.findByExamid(examinationHeadEntity.getId());
                    Map<String, Long> roomStudentCount = getSubjectWiseCount(subjectWiseCount, examinationHall.getTablecol()*examinationHall.getTablerow());
                    List<ExaminationGenerated> initialList = generateInitialList(examinationHall);
                    for (Map.Entry<String, Long> entry : roomStudentCount.entrySet()) {
                        initialList = updateStudents(entry, initialList, examinationStudents);
                    }
                    initialList = addStaffsToExamHalls(initialList, examinationHeadEntity, index);
                    for (ExaminationGenerated examinationGenerated : initialList) {
                        examinationGeneratedRepository.saveAndFlush(examinationGenerated);
                    }
                    index = index + 1;
                }
            } else {
                return new ResponseEntity<>("Students count more than Seats available", HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>("Staff and Room Counts are not match", HttpStatus.BAD_REQUEST);
        }
        examinationHeadEntity.setGenerated(1);
        examinationHeadEntity.setStatus("generated");
        examinationHeadRepository.save(examinationHeadEntity);
        return new ResponseEntity<>("Arrangements successfully generated", HttpStatus.OK);
    }

    private List<ExaminationGenerated> addStaffsToExamHalls(List<ExaminationGenerated> initialList, ExaminationHeadEntity examinationHeadEntity, int index) {
        String staffid = examinationHeadEntity.getExaminationStaffs().get(index).getStaffid();
        initialList.forEach(f -> f.setStaff(staffid));
        return initialList;
    }

    private List<ExaminationGenerated> updateStudents(Map.Entry<String, Long> entry, List<ExaminationGenerated> initialList, List<ExaminationStudents> examinationStudents) {
        if (entry.getValue() != 0) {
            int prop = (int) (initialList.size() / entry.getValue());
            int index = 0;
            int futindex = 0;
            for (int i = 0; i < entry.getValue(); i++) {
                String regNo = "";
                String subject = "";
                if (initialList.get(index).getStudent() == "") {
                    for (int k = 0; k < examinationStudents.size(); k++) {
                        if (examinationStudents.get(k).getSubject().equalsIgnoreCase(entry.getKey())) {
                            if (examinationStudents.get(k).getSelected() == 0) {
                                regNo = examinationStudents.get(k).getRegisternumber();
                                subject = examinationStudents.get(k).getSubject();
                                examinationStudents.get(k).setSelected(1);
                                examinationStudentsRepository.save(examinationStudents.get(k));
                                initialList.get(index).setStudent(regNo);
                                initialList.get(index).setSubject(subject);
                                index = index + prop;
                                break;
                            }
                        }
                    }
                } else {
                    futindex = index;
                    for (int j = futindex; j < initialList.size(); j++) {
                        if (initialList.get(j).getStudent() == "") {
                            futindex = j;
                            for (int k = 0; k < examinationStudents.size(); k++) {
                                if (examinationStudents.get(k).getSubject().equalsIgnoreCase(entry.getKey())) {
                                    if (examinationStudents.get(k).getSelected() == 0) {
                                        regNo = examinationStudents.get(k).getRegisternumber();
                                        subject = examinationStudents.get(k).getSubject();
                                        examinationStudents.get(k).setSelected(1);
                                        examinationStudentsRepository.save(examinationStudents.get(k));
                                        break;
                                    }
                                }
                            }
                            initialList.get(futindex).setStudent(regNo);
                            initialList.get(futindex).setSubject(subject);
                            index = index + prop;
                            break;
                        }
                    }
                }
            }
        }
        return initialList;
    }

    private List<ExaminationGenerated> generateInitialListOld(ExaminationHalls examinationHall) {
        List<ExaminationGenerated> examinationGenerateds = new ArrayList<>();
        String prevSeat = "";
        for (int i = 0; i < examinationHall.getStudentsperhall(); i++) {
            ExaminationGenerated examinationGenerated = new ExaminationGenerated();
            examinationGenerated.setExamid(examinationHall.getExamid());
            examinationGenerated.setDepartment(examinationHall.getDepartment());
            examinationGenerated.setHall(examinationHall.getHallno());
            examinationGenerated.setStudent("");
            examinationGenerated.setAttendance(0);
//            String seatno = prevSeat == "" ? "A1" : getSeatNo(i+1, examinationHall, prevSeat);
            //  prevSeat = seatno;
            //  examinationGenerated.setSeat(seatno);
            examinationGenerated.setCreatetimestamp(new Timestamp(new Date().getTime()));
            examinationGenerateds.add(examinationGenerated);
        }
        return examinationGenerateds;
    }

    private List<ExaminationGenerated> generateInitialList(ExaminationHalls examinationHall) {
        List<ExaminationGenerated> examinationGenerateds = new ArrayList<>();
        int colseat = examinationHall.getTablerow();
        // char[] aplh = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M'};
        int alph = 65;
        int colindex = 0;
        int endseat = colseat;
        int seatindex = 1;

        for (int i = 1; i <= examinationHall.getStudentsperhall(); i++) {
            char indexchar;
            if (i <= endseat) {
                indexchar = (char) alph;
            } else {
                endseat = endseat + colseat;
                colindex = colindex + 1;
                seatindex = 1;
                indexchar = (char) ++alph;
            }

            ExaminationGenerated examinationGenerated = new ExaminationGenerated();
            examinationGenerated.setExamid(examinationHall.getExamid());
            examinationGenerated.setDepartment(examinationHall.getDepartment());
            examinationGenerated.setHall(examinationHall.getHallno());
            examinationGenerated.setStudent("");
            examinationGenerated.setAttendance(0);
            examinationGenerated.setSeat(Character.toString(indexchar) + Integer.toString(seatindex));
            examinationGenerated.setCreatetimestamp(new Timestamp(new Date().getTime()));
            examinationGenerateds.add(examinationGenerated);
            seatindex = seatindex + 1;
        }
        return examinationGenerateds;
    }


    private Map<String, Long> getSubjectWiseCount(Map<String, Long> subjectWiseCount, int sudentsperhall) {

        Map<String, Long> resutMap = new HashMap<>();
        int subjcount = subjectWiseCount.size();
        int prop = sudentsperhall / subjcount;
        int balance = sudentsperhall % subjcount;
        Optional<Map.Entry<String, Long>> maxEntry = subjectWiseCount.entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));
        String maxStudentsSubj = maxEntry.get().getKey();
        int count = subjectWiseCount.values().stream().mapToInt(d -> Math.toIntExact(d)).sum();
        if (count < sudentsperhall) {
            return subjectWiseCount;
        }
        for (Map.Entry<String, Long> entry : subjectWiseCount.entrySet()) {
            if (balance != 0 && maxStudentsSubj.equalsIgnoreCase(entry.getKey())) {
                int val = prop + balance;
                resutMap.put(entry.getKey(), (long) (entry.getValue() < val ? entry.getValue().intValue() : val));
                subjectWiseCount.put(entry.getKey(), entry.getValue() < val ? 0 : entry.getValue() - val);
            } else {
                resutMap.put(entry.getKey(), (long) (entry.getValue() < prop ? 0 : prop));
                subjectWiseCount.put(entry.getKey(), entry.getValue() < prop ? 0 : entry.getValue() - prop);
            }
        }
        Map<String, Long> result = resutMap.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        System.out.println(result.toString());

        return result;
    }

    // private ExaminationGenerated mapToExamGenerated(ExaminationHalls examinationHall, ExaminationStudents examinationStudents, int index) {
    //     ExaminationGenerated examinationGenerated = new ExaminationGenerated();
    //     examinationGenerated.setExamid(examinationHall.getExamid());
    //     examinationGenerated.setDepartment(examinationHall.getDepartment());
    //     examinationGenerated.setHall(examinationHall.getHallno());
    //     examinationGenerated.setStudent(examinationStudents.getRegisternumber());
    //     examinationGenerated.setAttendance(0);
    //     examinationGenerated.setSeat(index + "");
    //     examinationGenerated.setCreatetimestamp(new Timestamp(new Date().getTime()));
    //     return examinationGenerated;
    // }

    public List<ExaminationGenerated> getArrangements(Integer id) {
        return examinationGeneratedRepository.getByExamid(id);
    }

    public List<ExaminationHalls> getExamHalls(Integer id) {
        return examinationHallsRepository.getByExamid(id);
    }

    public List<ExaminationStudents> getExamStudents(Integer id) {
        return examinationStudentsRepository.findByExamid(id);
    }

    public List<ExaminationStaffs> getExamStaffs(Integer id) {
        return examinationStaffsRepository.findByExamid(id);
    }

    public List<HallWiseArrangements> getArrangementsRoom(Integer id) {

        List<ExaminationGenerated> examinationGenerateds = examinationGeneratedRepository.getByExamid(id);
        Map<String, List<ExaminationGenerated>> generatedMap = new HashMap<>();
        for (ExaminationGenerated generated : examinationGenerateds) {
            if (generatedMap.containsKey(generated.getHall())) {
                generatedMap.get(generated.getHall()).add(generated);
            } else {
                List<ExaminationGenerated> exam = new ArrayList<>();
                exam.add(generated);
                generatedMap.put(generated.getHall(), exam);
            }
        }
        String examdate = examinationHeadRepository.getExamDateById(id);
        String date = examdate.replace("T", " ");
        String finalExamdate = date.split("\\.")[0];

        List<HallWiseArrangements> hallWiseArrangements = new ArrayList<>();

        generatedMap.forEach((k, v) -> {
            HallWiseArrangements hallWise = new HallWiseArrangements();
            hallWise.setHall(k);
            hallWise.setExamid(v.get(0).getExamid());
            hallWise.setHallsEntity(v.get(0).getHalls());
            hallWise.setStaffsEntity(staffsServices.findByStaffid(v.get(0).getStaff()));
            hallWise.setExamdate(finalExamdate);
            hallWise.setExaminationGenerated(v);
            hallWiseArrangements.add(hallWise);
        });
        return hallWiseArrangements;
    }

    public List<ExamModel> getExamHistoryByStudent(String regno) {

        List<Object[]> examinationGenerateds = examinationGeneratedRepository.getExamHistoryByStudent(regno);
        List<ExamModel> examModels = new ArrayList<>();
        for (Object[] obj : examinationGenerateds) {
            ExamModel examModel = new ExamModel();
            examModel.setExamid(Integer.parseInt(obj[0].toString()));
            examModel.setDepartment(obj[1].toString());
            examModel.setHall(obj[2].toString());
            examModel.setStaff(obj[3].toString());
            examModel.setStudent(obj[4].toString());
            examModel.setSeat(obj[5].toString());
            examModel.setAttendance(Integer.parseInt(obj[6].toString()));
            examModel.setExamname(obj[7].toString());
            examModel.setDescription(obj[8].toString());
            examModel.setExamdate(obj[9].toString());
            examModel.setIsgenerated(Integer.parseInt(obj[10].toString()));
            examModel.setStatus(obj[11].toString());
            examModels.add(examModel);
        }
        return examModels;
    }

    public List<ExamModel> getExamHistoryByStaff(String staffid) {
        List<Object[]> examinationGenerateds = examinationGeneratedRepository.getExamHistoryByStaff(staffid);
        List<ExamModel> examModels = new ArrayList<>();
        for (Object[] obj : examinationGenerateds) {
            ExamModel examModel = new ExamModel();
            examModel.setExamid(Integer.parseInt(obj[0].toString()));
            examModel.setDepartment(obj[1].toString());
            examModel.setHall(obj[2].toString());
            examModel.setStaff(obj[3].toString());
            examModel.setStudent(obj[4].toString());
            examModel.setSeat(obj[5].toString());
            examModel.setAttendance(Integer.parseInt(obj[6].toString()));
            examModel.setExamname(obj[7].toString());
            examModel.setDescription(obj[8].toString());
            examModel.setExamdate(obj[9].toString());
            examModel.setIsgenerated(Integer.parseInt(obj[10].toString()));
            examModel.setStatus(obj[11].toString());
            examModels.add(examModel);
        }
        return examModels;
    }

    public ResponseEntity sendNotification(Integer id) {
        ExaminationHeadEntity examinationHeadEntity = examinationHeadRepository.getById(id);
        if (examinationHeadEntity.getGenerated() == 1) {
            examinationHeadEntity.setStatus("ongoing");
            examinationHeadRepository.save(examinationHeadEntity);
                try {
                    PushNotificationRequest pushNotificationRequest = new PushNotificationRequest();
                    pushNotificationRequest.setTitle(examinationHeadEntity.getName());
                    pushNotificationRequest.setTopic(topic);
                    List<String> msgs = examinationHeadEntity.getExaminationGenerateds().stream()
                            .map(ExaminationGenerated::getStudent)
                            .collect(Collectors.toList());
                    pushNotificationRequest.setMessage(msgs);
                    pushNotificationService.sendPushNotificationToTopic(pushNotificationRequest);
                    Thread.sleep(1000);
                } catch (Exception ignored) {

                }
                
                Mail mail = new Mail();
                mail.setMailSubject(examinationHeadEntity.getName() + " Seating Arrangement Generated");
                for (ExaminationGenerated examinationGenerated : examinationHeadEntity.getExaminationGenerateds()) {
                    StudentsEntity studentsEntity = studentsServices.getStudentByRegsiterNumber(examinationGenerated.getStudent());
                    if(studentsEntity.getEmail() != null && studentsEntity.getEmail() != ""){
                        mail.setMailFrom(fromMail);
                        mail.setMailTo(toMail);
                        mail.setMailContent("<div>Your Examination Arrangement has Generated </div>\n" +
                                "<div>Class Room: <span>" + examinationGenerated.getHall() +"</span>\n</div>" +
                                "<div>Department: <span>" + examinationGenerated.getDepartment()+"</span></div>\n" +
                                "<div>Seat No: <span>"+ examinationGenerated.getSeat()+"</span></div>\n");
                        mailService.sendEmail(mail);
                    }
                    break;
                }

            // Runnable r2 = () -> {
            //     try {
            //         Mail mail = new Mail();
            //         mail.setMailSubject(examinationHeadEntity.getName() + " Seating Arrangement Generated");
            //         for (ExaminationGenerated examinationGenerated : examinationHeadEntity.getExaminationGenerateds()) {
            //             StudentsEntity studentsEntity = studentsServices.getStudentByRegsiterNumber(examinationGenerated.getStudent());
            //             if(studentsEntity.getEmail() != null && studentsEntity.getEmail() != ""){
            //                 mail.setMailFrom(fromMail);
            //                 mail.setMailTo(toMail);
            //                 mail.setMailContent("Your Examination Arrangement has Generated \n" +
            //                         "Class Room: " + examinationGenerated.getHall() +"\n" +
            //                         "Department: " + examinationGenerated.getDepartment()+"\n" +
            //                         "Seat No: "+ examinationGenerated.getSeat()+"\n");
            //                 mailService.sendEmail(mail);
            //             }
            //         }
            //         Thread.sleep(1000);
            //     } catch (Exception ignored) {

            //     }
            // };

            // ExecutorService service = Executors.newFixedThreadPool(1);

            // service.submit(r2);

            // service.shutdown();
            // try {
            //     service.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
            // } catch (InterruptedException e) {
            //     e.printStackTrace();
            // }

            return new ResponseEntity<>("Notification process triggered", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }

    }

    public ResponseEntity clearGenerated(Integer id) {
        try {
            examinationGeneratedRepository.deleteByExamid(id);
            examinationStudentsRepository.deleteByExamid(id);
            examinationHallsRepository.deleteByExamid(id);
            examinationStaffsRepository.deleteByExamid(id);
            examinationHeadRepository.clearStatus(id);
            return new ResponseEntity<>("Data Cleared", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity closeExam(Integer id) {
        try {
            examinationHeadRepository.closeStatus(id);
            return new ResponseEntity<>("Exam Closed", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity suspendExam(Integer id) {
        try {
            examinationHeadRepository.suspendStatus(id);
            return new ResponseEntity<>("Exam Closed", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity addAttendance(List<ExamModel> examModels) {
        try {
            for(ExamModel examModel: examModels) {
                examinationGeneratedRepository.updateAttendance(examModel.getAttendance(), examModel.getExamid(), examModel.getStudent());
            }
            return new ResponseEntity<>("Attendance Updated", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>("Attendance Update Failed", HttpStatus.BAD_REQUEST);
        }
    }
}

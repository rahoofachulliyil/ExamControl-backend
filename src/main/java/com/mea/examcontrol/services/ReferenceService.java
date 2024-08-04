package com.mea.examcontrol.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mea.examcontrol.model.ReferenceModel;
import com.mea.examcontrol.repository.reference.ReferenceRepository;
import com.mea.examcontrol.repository.reference.entity.ReferenceEntity;
import com.mea.examcontrol.util.AttachmentUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;


import javax.persistence.EntityManagerFactory;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ReferenceService {


    @Autowired
    ReferenceRepository referenceRepository;

    @Autowired
    AttachmentUtils attachmentUtils;

    @Autowired
    private EntityManagerFactory emf;


    public ResponseEntity addNewReference(String reference, MultipartFile file) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        ReferenceModel referenceModel = null;
        try {
            referenceModel = objectMapper.readValue(reference, ReferenceModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(referenceModel != null){
            ReferenceEntity referenceEntity = new ReferenceEntity();
            referenceEntity.setDescription(referenceModel.getDescription());
            referenceEntity.setDepartment(referenceModel.getDepartment());
            referenceEntity.setSemester(referenceModel.getSemester());
            referenceEntity.setSubject(referenceModel.getSubject());
            referenceEntity.setType(referenceModel.getType());
            referenceEntity.setYear(referenceModel.getYear());
            referenceEntity.setStatus(1);
            referenceEntity.setCreatetimestamp(new Timestamp(new Date().getTime()));
            String location = "files/"+referenceModel.getType()+"/"+referenceModel.getSemester();
            referenceEntity.setLocation(location + "/" + file.getOriginalFilename());
            if(attachmentUtils.saveFile(file, location)){
                referenceRepository.saveAndFlush(referenceEntity);
                return new ResponseEntity<>("File Saved Successfully", HttpStatus.ACCEPTED);
            }else{
                return new ResponseEntity<>("Oops! Failed to save file", HttpStatus.CONFLICT);
            }
        }
        return null;
    }

    public List<ReferenceEntity> getAllReferences() {
        return referenceRepository.findAll();
    }

    public List<ReferenceEntity> getReferencedByType(String type) {
        return referenceRepository.findByType(type);
    }

    public ResponseEntity<Resource> downloadFile(Integer id) {
        ReferenceEntity referenceEntity = referenceRepository.getById(id);
        Resource file = attachmentUtils.load(referenceEntity.getLocation());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }


    public ResponseEntity updateStatus(int id) {
        int status = referenceRepository.findById(id).get().getStatus();
        switch (status){
            case 1:
                status = 0;
                break;
            case 0:
                status =1;
                break;
        }
        referenceRepository.updateStatus(status, id);
        return new ResponseEntity<>("Status Updated", HttpStatus.OK);
    }

    public ResponseEntity<Resource> download(Integer id) throws IOException {
        ReferenceEntity referenceEntity = referenceRepository.getById(id);
        File file = new File(referenceEntity.getLocation());

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=img.jpg");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}


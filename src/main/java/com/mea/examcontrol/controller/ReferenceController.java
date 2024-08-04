package com.mea.examcontrol.controller;

import com.mea.examcontrol.model.BranchesModel;
import com.mea.examcontrol.model.ReferenceModel;
import com.mea.examcontrol.repository.branches.entity.BranchesEntity;
import com.mea.examcontrol.repository.reference.entity.ReferenceEntity;
import com.mea.examcontrol.services.ReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("reference")
public class ReferenceController {

    @Autowired
    private ReferenceService referenceService;

    @PostMapping(value = "/new", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    private ResponseEntity addNewReference(@RequestPart("refdata") String refdata, @RequestPart("file") MultipartFile file) throws IOException {
        return referenceService.addNewReference(refdata, file);
    }

    @GetMapping("/all")
    private List<ReferenceEntity> getAllReferences() {
        return referenceService.getAllReferences();
    }

    @GetMapping("/get/{type}/all")
    private List<ReferenceEntity> getReferencedByType(@PathVariable("type") String type) {
        return referenceService.getReferencedByType(type);
    }

//    @GetMapping("/download/{id}")
    private ResponseEntity<Resource> downloadFile(@PathVariable("id") Integer id) {
        return referenceService.downloadFile(id);
    }

    @GetMapping("/download/{id}")
    private ResponseEntity<Resource> download(@PathVariable("id") Integer id) throws IOException {
        return referenceService.download(id);
    }

    @PostMapping("/status/{id}")
    private ResponseEntity updateStatus(@PathVariable("id") int id) {
        return referenceService.updateStatus(id);
    }

}

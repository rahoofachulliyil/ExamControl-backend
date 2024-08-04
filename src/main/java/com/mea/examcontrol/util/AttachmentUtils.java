package com.mea.examcontrol.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class AttachmentUtils {

    public boolean saveFile(MultipartFile file, String location) {
        Path userDirectory = Paths.get(location);
        try {
            if (!Files.exists(userDirectory)) {
                Files.createDirectory(userDirectory);
            }
            Files.copy(file.getInputStream(), userDirectory.resolve(file.getOriginalFilename()));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Resource load(String filename) {
        Path root = Paths.get("");
        try {
            Path file = root.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException("Error:" + e.getMessage());
        }

    }


//    public static byte[] convertFileContentToBlob(File file) throws IOException {
//        byte[] fileContent = null;
//        try {
//            fileContent = FileUtils.readFileToByteArray(new File(filePath));
//        } catch (IOException e) {
//            throw new IOException("Unable to convert file to byte array. " +
//                    e.getMessage());
//        }
//        return fileContent;
//    }
}

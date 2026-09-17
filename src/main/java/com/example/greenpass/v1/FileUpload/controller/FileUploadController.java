package com.example.greenpass.v1.FileUpload.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.greenpass.dtos.ResponseObject;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "*")
public class FileUploadController {

    private static final String BASE_UPLOAD_DIR = "uploads";

    @PostMapping
    public ResponseEntity<ResponseObject> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "category", defaultValue = "general") String category,
            HttpServletRequest request) {

        if (file == null || file.isEmpty()) {
            return new ResponseEntity<>(
                    new ResponseObject(false, "Please select a file to upload", null),
                    HttpStatus.BAD_REQUEST);
        }

        // Sanitize category name (allowed: rewards, announcements, reports, general)
        String cleanCategory = category.toLowerCase().trim();
        if (!cleanCategory.equals("rewards") &&
                !cleanCategory.equals("announcements") &&
                !cleanCategory.equals("reports")) {
            cleanCategory = "general";
        }

        try {
            // Ensure category directory exists
            Path categoryPath = Paths.get(BASE_UPLOAD_DIR, cleanCategory).toAbsolutePath().normalize();
            Files.createDirectories(categoryPath);

            // Generate clean unique file name
            String originalFileName = StringUtils
                    .cleanPath(file.getOriginalFilename() != null ? file.getOriginalFilename() : "image.jpg");
            String extension = "";
            int dotIndex = originalFileName.lastIndexOf(".");
            if (dotIndex >= 0) {
                extension = originalFileName.substring(dotIndex);
            } else {
                extension = ".jpg";
            }

            String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8)
                    + extension;
            Path targetLocation = categoryPath.resolve(newFileName);

            // Copy file to target location
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // Construct relative path and full URL
            String relativeUrl = "/uploads/" + cleanCategory + "/" + newFileName;
            String fullUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(relativeUrl)
                    .toUriString();

            Map<String, String> responseData = new HashMap<>();
            responseData.put("fileName", newFileName);
            responseData.put("category", cleanCategory);
            responseData.put("fileUrl", relativeUrl);
            responseData.put("fullUrl", fullUrl);

            return new ResponseEntity<>(
                    new ResponseObject(true, "File uploaded successfully", responseData),
                    HttpStatus.OK);

        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseObject(false, "Failed to upload file: " + e.getMessage(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.example.greenpass.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.UUID;

public class FileUtils {

    private static final String BASE_UPLOAD_DIR = "uploads";

    /**
     * Extracts only the file name from a path, url, or base64 string.
     * 1. If null or empty, returns null.
     * 2. If it's a base64 string, saves it to the specified category folder and returns the generated filename.
     * 3. If it contains a path or URL (e.g. /uploads/announcements/abc.jpg or http://.../abc.jpg or src/abc.jpg), returns "abc.jpg".
     * 4. If it's already just a filename, returns the filename.
     */
    public static String extractFileName(String input, String category) {
        if (input == null) {
            return null;
        }

        String trimmed = input.trim();
        if (trimmed.isEmpty() || trimmed.equalsIgnoreCase("null") || trimmed.equals("-")) {
            return null;
        }

        // Check if it's a base64 data URI
        if (trimmed.startsWith("data:image/") && trimmed.contains(";base64,")) {
            try {
                String[] parts = trimmed.split(";base64,");
                String header = parts[0].toLowerCase();
                String extension = ".jpg";
                if (header.contains("png")) {
                    extension = ".png";
                } else if (header.contains("gif")) {
                    extension = ".gif";
                } else if (header.contains("webp")) {
                    extension = ".webp";
                }

                byte[] data = Base64.getDecoder().decode(parts[1]);

                String cleanCategory = (category != null && !category.trim().isEmpty())
                        ? category.toLowerCase().trim()
                        : "general";
                Path categoryPath = Paths.get(BASE_UPLOAD_DIR, cleanCategory).toAbsolutePath().normalize();
                Files.createDirectories(categoryPath);

                String newFileName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8)
                        + extension;
                Path targetLocation = categoryPath.resolve(newFileName);

                Files.write(targetLocation, data);
                return newFileName;
            } catch (Exception e) {
                // If decoding fails, return null
                return null;
            }
        }

        // Remove query parameters or hash if any (e.g., photo.jpg?v=1)
        int queryIndex = trimmed.indexOf("?");
        if (queryIndex != -1) {
            trimmed = trimmed.substring(0, queryIndex);
        }
        int hashIndex = trimmed.indexOf("#");
        if (hashIndex != -1) {
            trimmed = trimmed.substring(0, hashIndex);
        }

        // Normalize slashes
        trimmed = trimmed.replace("\\", "/");

        // Extract the last part after the last slash
        int lastSlash = trimmed.lastIndexOf("/");
        if (lastSlash != -1 && lastSlash < trimmed.length() - 1) {
            return trimmed.substring(lastSlash + 1);
        }

        return trimmed;
    }

    public static String extractFileName(String input) {
        return extractFileName(input, "general");
    }

    /**
     * Deletes the physical file from the disk given its filename and category.
     * Prevents deletion of default assets.
     */
    public static boolean deleteFile(String fileName, String category) {
        if (fileName == null || fileName.trim().isEmpty() || fileName.equalsIgnoreCase("null") || fileName.equals("-")) {
            return false;
        }

        String cleanFileName = extractFileName(fileName, category);
        if (cleanFileName == null || cleanFileName.isEmpty()) {
            return false;
        }

        // Never delete default fallback assets
        if (cleanFileName.equalsIgnoreCase("news1.jpg") || cleanFileName.equalsIgnoreCase("default.jpg")
                || cleanFileName.equalsIgnoreCase("image.jpg")) {
            return false;
        }

        try {
            String cleanCategory = (category != null && !category.trim().isEmpty())
                    ? category.toLowerCase().trim()
                    : "general";

            // 1. Check in category directory
            Path filePath = Paths.get(BASE_UPLOAD_DIR, cleanCategory, cleanFileName).toAbsolutePath().normalize();
            if (Files.exists(filePath)) {
                return Files.deleteIfExists(filePath);
            }

            // 2. Check in root uploads
            Path rootPath = Paths.get(BASE_UPLOAD_DIR, cleanFileName).toAbsolutePath().normalize();
            if (Files.exists(rootPath)) {
                return Files.deleteIfExists(rootPath);
            }

            // 3. Search other category directories just in case
            for (String cat : new String[] { "rewards", "announcements", "reports", "general" }) {
                Path altPath = Paths.get(BASE_UPLOAD_DIR, cat, cleanFileName).toAbsolutePath().normalize();
                if (Files.exists(altPath)) {
                    return Files.deleteIfExists(altPath);
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to delete file " + cleanFileName + ": " + e.getMessage());
        }
        return false;
    }

    public static boolean deleteFile(String fileName) {
        return deleteFile(fileName, "general");
    }
}


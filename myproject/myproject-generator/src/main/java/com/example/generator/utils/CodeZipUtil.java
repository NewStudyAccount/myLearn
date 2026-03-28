package com.example.generator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CodeZipUtil {

    private static final Logger log = LoggerFactory.getLogger(CodeZipUtil.class);

    public static void zipFiles(Map<String, String> fileContents, OutputStream outputStream) throws IOException {
        ZipOutputStream zos = new ZipOutputStream(outputStream);
        zos.setLevel(9);
        
        try {
            for (Map.Entry<String, String> entry : fileContents.entrySet()) {
                String fileName = entry.getKey();
                String content = entry.getValue();
                
                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);
                
                byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
                zos.write(bytes);
                zos.closeEntry();
            }
            zos.flush();
        } finally {
            zos.close();
        }
    }

    public static void zipDirectory(String sourceDir, OutputStream outputStream) throws IOException {
        Path sourcePath = Paths.get(sourceDir);
        if (!Files.exists(sourcePath)) {
            throw new FileNotFoundException("Source directory not found: " + sourceDir);
        }

        try (ZipOutputStream zos = new ZipOutputStream(outputStream)) {
            zos.setLevel(9);
            Files.walk(sourcePath)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        String relativePath = sourcePath.relativize(file).toString()
                                .replace("\\", "/");
                        ZipEntry zipEntry = new ZipEntry(relativePath);
                        zos.putNextEntry(zipEntry);
                        Files.copy(file, zos);
                        zos.closeEntry();
                    } catch (IOException e) {
                        log.error("Error adding file to zip: {}", file, e);
                    }
                });
        }
    }

    public static File createTempDirectory() throws IOException {
        File tempDir = Files.createTempDirectory("generator_").toFile();
        tempDir.deleteOnExit();
        return tempDir;
    }

    public static File writeToTempFile(String content, String fileName) throws IOException {
        File tempDir = createTempDirectory();
        File file = new File(tempDir, fileName);
        file.deleteOnExit();
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
        
        return file;
    }

    public static void deleteDirectory(File dir) {
        if (dir == null || !dir.exists()) {
            return;
        }
        
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        dir.delete();
    }
}
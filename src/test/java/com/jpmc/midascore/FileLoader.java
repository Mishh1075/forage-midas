package com.jpmc.midascore;

import org.springframework.stereotype.Component;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class FileLoader {
    public String[] loadStrings(String path) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(path);
            if (inputStream == null) {
                throw new RuntimeException("File not found: " + path);
            }
            String fileText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return fileText.split(System.lineSeparator());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load file: " + path, e);
        }
    }
}

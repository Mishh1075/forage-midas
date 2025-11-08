package com.jpmc.midascore;

import org.springframework.stereotype.Component;
import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class FileLoader {

    public String[] loadStrings(String path) {
        try {
            // 1. Normalize leading slash
            if (path.startsWith("/")) {
                path = path.substring(1);
            }

            // 2. Ensure it ALWAYS loads from test_data/
            if (!path.startsWith("test_data/")) {
                path = "test_data/" + path;
            }

            // 3. Load from classpath
            InputStream inputStream = 
                getClass().getClassLoader().getResourceAsStream(path);

            if (inputStream == null) {
                throw new RuntimeException("File not found in classpath: " + path);
            }

            String fileText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            return fileText.split(System.lineSeparator());

        } catch (Exception e) {
            throw new RuntimeException("Failed to load file: " + path, e);
        }
    }
}

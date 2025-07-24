package org.example.courtsystem.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

public class DocumentAnalyzer {
    private static final String OUTPUT_FILE = "analysis_results.txt";
    private static final String[] SPECIAL_WORDS = {"court", "evidence", "witness", "judge", "law"};

    public static void analyzeDocument(String resourcePath) throws IOException {
        // Loading an input file from resources
        InputStream inputStream = DocumentAnalyzer.class.getClassLoader().getResourceAsStream(resourcePath);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourcePath);
        }

        // Reading file contents
        String content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        inputStream.close();

        Map<String, Integer> wordCounts = new HashMap<>();

        // Initialize counts
        for (String word : SPECIAL_WORDS) {
            wordCounts.put(word, 0);
        }

        // Count occurrences
        for (String word : SPECIAL_WORDS) {
            int count = StringUtils.countMatches(content.toLowerCase(), word.toLowerCase());
            wordCounts.put(word, count);
        }

        // Prepare result
        StringBuilder result = new StringBuilder();
        result.append("\n=== Analysis Results ===\n");
        result.append("File: ").append(resourcePath).append("\n");
        result.append("Total words: ").append(StringUtils.countMatches(content, " ") + 1).append("\n");

        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        // Writing the result to a file in the resources folder
        Path outputPath = Paths.get("src/main/resources", OUTPUT_FILE);
        Files.write(outputPath, result.toString().getBytes(StandardCharsets.UTF_8));
    }
}
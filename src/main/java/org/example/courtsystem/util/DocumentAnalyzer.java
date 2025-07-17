package org.example.courtsystem.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DocumentAnalyzer {
    private static final String OUTPUT_FILE = "analysis_results.txt";
    private static final String[] SPECIAL_WORDS = {"court", "evidence", "witness", "judge", "law"};

    public static void analyzeDocument(String filePath) throws IOException {
        String content = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
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
        result.append("File: ").append(filePath).append("\n");
        result.append("Total words: ").append(StringUtils.countMatches(content, " ") + 1).append("\n");

        for (Map.Entry<String, Integer> entry : wordCounts.entrySet()) {
            result.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        // Append to output file
        FileUtils.writeStringToFile(
                new File(OUTPUT_FILE),
                result.toString(),
                StandardCharsets.UTF_8,
                true  // Append mode
        );
    }
}
package com.group.registration.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    private static final String REGISTRATIONS_FILE = "data/registrations.txt";

    public static List<String> readAllLines() {
        List<String> lines = new ArrayList<>();
        File file = new File(REGISTRATIONS_FILE);
        if (!file.exists()) return lines;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void writeAllLines(List<String> lines) {
        new File("data").mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REGISTRATIONS_FILE))) {
            for (String line : lines) {
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendLine(String line) {
        new File("data").mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(REGISTRATIONS_FILE, true))) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
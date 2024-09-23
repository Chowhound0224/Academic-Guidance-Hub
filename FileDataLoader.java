package com.mycompany.oodj_assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileDataLoader implements DataLoader {
    @Override
    public Map<String, String> loadMap(String courseFilePath) {
        Map<String, String> courseMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(courseFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length == 13) {
                    courseMap.put(columns[2], columns[3]); // Map CourseID to CourseName
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading course file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return courseMap;
    }

    @Override
    public void loadAssignmentsFromFile(String filePath, String username, DefaultTableModel tableModel, Map<String, String> courseMap) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 10 && columns[1].equals(username)&& checkOverDuedate(columns[5])) { // Check if username matches
                    String courseId = columns[2];
                    String courseName = courseMap.getOrDefault(courseId, "Unknown Course");

                    Object[] rowData = new Object[6];
                    rowData[0] = courseId; // Course ID
                    rowData[1] = courseName; // Course Name
                    rowData[2] = columns[4]; // Submission Status
                    rowData[3] = columns[5]; // Due Date
                    rowData[4] = outputRemainingTime(columns[5]); // Time Remaining
                    rowData[5] = columns[7]; // Last Modified

                    tableModel.addRow(rowData); // Add row to table model
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void loadresult(String userID, DefaultTableModel tableModel, Map<String, String> courseMap) {

        try (BufferedReader reader = new BufferedReader(new FileReader("StudentAssignment.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (columns.length >= 3 && columns[4].equals("Submitted")&&userID.equals(columns[1])) {
                    String courseId = columns[2];
                    String courseName = courseMap.getOrDefault(courseId, "Unknown Course");
                    if (checkBetween15Days(columns[5])){
                        if (!columns[11].equals("-") && !columns[12].equals("-")) {
                            int marks = (Integer.parseInt(columns[11]) + Integer.parseInt(columns[12])) / 2;
                            Object[] rowData = {columns[2], courseName, marks};
                            tableModel.addRow(rowData);
                        }
                    }else{
                        Object[] rowData = {columns[2], courseName, "Not Available"};
                        tableModel.addRow(rowData);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getDirectoryString() {
        Path currentPath = Paths.get("");
        return currentPath.toAbsolutePath().toString();
    }

    public static String outputRemainingTime(String dueDateStr) {
        // Define the pattern to match the due date string format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm");

        try {
            // Parse the due date string into LocalDateTime using the specified pattern
            LocalDateTime dueDateTime = LocalDateTime.parse(dueDateStr, formatter);

            // Get the current date-time
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Calculate the duration between current date-time and due date-time
            Duration duration = Duration.between(currentDateTime, dueDateTime);

            // Extract days, hours, and minutes from the duration
            long days = duration.toDays();
            long hours = duration.toHours() % 24;
            long minutes = duration.toMinutes() % 60;

            // Build the remaining time string
            StringBuilder remainingTime = new StringBuilder();
            if (days > 0) {
                remainingTime.append(days).append(" day(s) ");
            }
            if (hours > 0) {
                remainingTime.append(hours).append(" hour(s) ");
            }
            if (minutes > 0) {
                remainingTime.append(minutes).append(" minute(s)");
            }

            return remainingTime.toString();
        } catch (DateTimeParseException e) {
            // Handle parsing exception
            System.err.println("Error parsing due date: " + e.getMessage());
            return "Invalid Due Date";
        }
    }

    public static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm");
        return LocalDateTime.now().format(formatter);
    }

    public static boolean checkBetween15Days(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm");
        LocalDate currentDate = LocalDate.now();
        LocalDate inputDate = LocalDate.parse(date, formatter);

        return currentDate.isAfter(inputDate.plusDays(15));
    }
    
    public static List<String[]> readFile(String fileName) throws IOException {
        List<String[]> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(getDirectoryString() + "/" + fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.split(","));
            }
        }
        return lines;
    }

    public static void writeFile(String fileName, List<String> lines) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getDirectoryString() + "/" + fileName))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }
    
    public static boolean checkOverDuedate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm");
        LocalDate currentDate = LocalDate.now();
        LocalDate inputDate = LocalDate.parse(date, formatter);

        return inputDate.isAfter(currentDate);
    }

    @Override
    public void selectFile(String PredefinedPath, JComponent parentComponent) {
        File selectedFile;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(PredefinedPath));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnValue = fileChooser.showOpenDialog(parentComponent);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            openFile(selectedFile, parentComponent);
        }
    }

    @Override
    public void openFile(File selectedFile, JComponent parentComponent) {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(parentComponent, "Please select a file first.");
            return;
        }

        new FileViewer(selectedFile);
    }
}


    

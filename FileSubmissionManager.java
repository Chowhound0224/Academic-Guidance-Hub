package com.mycompany.oodj_assignment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

public class FileSubmissionManager implements SubmissionManager {
    private DefaultTableModel tableModel;
    private JTable assignmentList;
    private String filePath;
    private String username;

    public FileSubmissionManager(DefaultTableModel tableModel, JTable assignmentList, String filePath, String username) {
        this.tableModel = tableModel;
        this.assignmentList = assignmentList;
        this.filePath = filePath;
        this.username = username;
    }

    @Override
    public void submitAssignment(int selectedRow, String destinationDir) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select an assignment to submit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            Path destinationPath = Paths.get(destinationDir);
            Path destinationFilePath = destinationPath.resolve(selectedFile.getName());

            try {
                Files.createDirectories(destinationPath);
                Files.copy(selectedFile.toPath(), destinationFilePath, StandardCopyOption.REPLACE_EXISTING);

                String newStatus = "Submitted";
                String lastModified = FileDataLoader.getCurrentDateTime();
                tableModel.setValueAt(newStatus, selectedRow, 2);
                tableModel.setValueAt(lastModified, selectedRow, 5);
                tableModel.setValueAt(FileDataLoader.outputRemainingTime(tableModel.getValueAt(selectedRow, 3).toString()), selectedRow, 4);
                saveTableDataToFile(selectedRow, newStatus, lastModified);
                JOptionPane.showMessageDialog(null, "Assignment submitted successfully!");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error submitting assignment: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    @Override
    public void removeSubmission(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Please select a submission to remove.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String courseID = (String) tableModel.getValueAt(selectedRow, 0);
        String directoryPath = Paths.get(FileDataLoader.getDirectoryString(), username, courseID).toString();
        Path directory = Paths.get(directoryPath);

        try {
            deleteDirectory(directory.toFile());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error removing submission: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        String newStatus = "Not Submitted";
        String lastModified = FileDataLoader.getCurrentDateTime();
        tableModel.setValueAt(newStatus, selectedRow, 2);
        tableModel.setValueAt(lastModified, selectedRow, 5);

        saveTableDataToFile(selectedRow, newStatus, lastModified);
    }

    @Override
    public void editSubmission(int selectedRow) {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null,
                    "Please select an assignment to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String courseID = (String) tableModel.getValueAt(selectedRow, 0);
        String directoryPath = Paths.get(FileDataLoader.getDirectoryString(), username, courseID).toString();
        Path directory = Paths.get(directoryPath);

        if (tableModel.getValueAt(selectedRow, 2).equals("Not Submitted")) {
            JOptionPane.showMessageDialog(null,
                    "This assignment has not been submitted.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(() -> {
            DirectoryFileListApp app1 = new DirectoryFileListApp(directory.toString(), () -> {
                // Check if directory is empty and update submission status
                try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
                    String lastModified = FileDataLoader.getCurrentDateTime();
                    if (!stream.iterator().hasNext()) {
                        tableModel.setValueAt("Not Submitted", selectedRow, 2);
                        tableModel.setValueAt(lastModified, selectedRow, 5); // Update Last Modified column
                        saveTableDataToFile(selectedRow, "Not Submitted", lastModified);
                    }
                    tableModel.setValueAt(FileDataLoader.outputRemainingTime(tableModel.getValueAt(selectedRow, 3).toString()), selectedRow, 4); // Update Time Remaining column
                    tableModel.setValueAt(lastModified, selectedRow, 5); // Update Last Modified column
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                            "Error reading directory: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            app1.setVisible(true);
        });
    }


    public void saveTableDataToFile(int rowIndex, String newStatus, String lastModified) {
        try {
            System.out.println("Saving table data to file: " + filePath);
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            String studentId = username;
            String courseId = (String) tableModel.getValueAt(rowIndex, 0);
            String duedate = (String) tableModel.getValueAt(rowIndex, 3);
            
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                String[] columns = line.split(",");
                if (columns.length >= 10 && columns[1].equals(studentId) && columns[2].equals(courseId)) {
                    if (columns.length >= 10 && columns[1].equals(studentId) && columns[2].equals(courseId)) {
                    columns[4] = newStatus; // Update Submission Status
                    System.out.println("New Status: " + newStatus);
                    columns[6] = FileDataLoader.outputRemainingTime(duedate);
                    columns[7] = lastModified; // Update Last Modified
                    line = String.join(",", columns);
                    System.out.println("Updated line: " + line);
                }
                    lines.set(i, String.join(",", columns));
                    break;
                }
            }
            FileDataLoader.writeFile("StudentAssignment.txt", lines);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving table data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteDirectory(File directory) throws IOException {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file); // Recursively delete subdirectories
                    } else {
                        Files.delete(file.toPath()); // Delete file
                    }
                }
            }
            directory.delete(); // Delete the empty directory
        }
    }

    

}

package com.mycompany.oodj_assignment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import java.nio.file.*;
import java.io.*;
import java.util.*;

public class Project_Manager_Model {
    // File to be read for reading and writing data
    private static final String PROJECTS_FILE_PATH = "StudentAssignment.txt";
    private static final String USER_DATA_FILE = "userdata.txt";

    // Data and Columns needed for the "Manage Project" Table
    private String[][] dataManageProject;
    private static final int[] columnManageProject = {0, 1, 3, 4, 5, 8, 9}; 

    // Data and Columns needed for the "View Project Status" Table
    private String[][] dataViewProjectStatus;
    private static final int[] columnViewProjectStatus = {1, 3, 4};

    public Project_Manager_Model() {
        dataManageProject = readDataFromFile(columnManageProject);
        dataViewProjectStatus = readDataFromFile(columnViewProjectStatus);
    }

    private String[][] readDataFromFile(int[] neededIndices) {
        List<String[]> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(PROJECTS_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String[] neededValues = new String[neededIndices.length];
                for (int i = 0; i < neededIndices.length; i++) {
                    neededValues[i] = values[neededIndices[i]];
                }
                dataList.add(neededValues);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading the file. Please try again.");
            e.printStackTrace();
        }

        return dataList.toArray(new String[0][]);
    }

    public String[][] getDataManageProject() {
        return dataManageProject;
    }

    public String[][] getDataViewProjectStatus() {
        return dataViewProjectStatus;
    }

    public void addProject(String[] projectData) {
        // Update the data arrays
        dataManageProject = appendToDataArray(dataManageProject, projectData, columnManageProject);
        dataViewProjectStatus = appendToDataArray(dataViewProjectStatus, projectData, columnViewProjectStatus);
    }

    private String[][] appendToDataArray(String[][] originalData, String[] newEntry, int[] neededIndices) {
        List<String[]> dataList = new ArrayList<>(Arrays.asList(originalData));
        String[] neededValues = new String[neededIndices.length];
        for (int i = 0; i < neededIndices.length; i++) {
            neededValues[i] = newEntry[neededIndices[i]];
        }
        dataList.add(neededValues);
        return dataList.toArray(new String[0][]);
    }

    public void updateProject(String filteredManageProjectArray, String filteredViewProjectStatusArray, int rowIndex) {
        // Convert the filtered Manage Project data to an array
        String[] arrayManageProject = filteredManageProjectArray.split(",");

        // Convert the filtered View Project Status data to an array
        String[] arrayViewProjectStatus = filteredViewProjectStatusArray.split(",");

        // Update the data arrays
        dataManageProject[rowIndex] = arrayManageProject;
        dataViewProjectStatus[rowIndex] = arrayViewProjectStatus;
    }

    public void deleteProject(int rowIndex) {
        // Update the data arrays
        dataManageProject = removeFromDataArray(dataManageProject, rowIndex);
        dataViewProjectStatus = removeFromDataArray(dataViewProjectStatus, rowIndex);
    }

    private String[][] removeFromDataArray(String[][] originalData, int rowIndex) {
        List<String[]> dataList = new ArrayList<>(Arrays.asList(originalData));
        dataList.remove(rowIndex);
        return dataList.toArray(new String[0][]);
    }

    // **** This version worked
    public void addRowsToFile(String newEntry) {
        File file = new File(PROJECTS_FILE_PATH);
        
        // Append to file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(newEntry);
            writer.newLine();

            // Update the list for Intake Codes, Students, and Lecturers
            // updateSelectionData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while writing to the file. Please try again.");
            e.printStackTrace();
        }
    }

    public void deleteRowsInFile(List<Integer> rowIndices) {
        File file = new File(PROJECTS_FILE_PATH);
        List<String> lines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading the file. Please try again.");
            e.printStackTrace();
        }
        
        Collections.sort(rowIndices);
        for (int i = rowIndices.size() - 1; i >= 0; i--) {
            int rowIndex = rowIndices.get(i);
            if(rowIndex >= 0 && rowIndex < lines.size()) {
                lines.remove(rowIndex);
            }
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
            // Update the list for Intake Codes, Students, and Lecturers
            // updateSelectionData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while writing to the file. Please try again.");
            e.printStackTrace();
        }
    }

    public void updateRowInFile(String newEntry, int rowIndex) {
        File file = new File(PROJECTS_FILE_PATH);
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading "+ PROJECTS_FILE_PATH +". Please try again.");
            e.printStackTrace();
        }

        // Replace the line at the specified index
        if (rowIndex >= 0 && rowIndex < lines.size()) {
            String newLine = String.join(",", newEntry);

            lines.set(rowIndex, newLine);
        } else {
            throw new IllegalArgumentException("rowIndex out of range");
        }

        // Write the lines back to the file
        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.println(line);
            }
            // Update the list for Intake Codes, Students, and Lecturers
            // updateSelectionData();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while writing to "+ PROJECTS_FILE_PATH +". Please try again.");
            e.printStackTrace();
        }
    }

    public String formatProjectData(String intakeCode, String studentID, String projectType,
            LocalDate date, LocalTime time, String supervisor, String secondMarker) {
        // Combine the date and time into a LocalDateTime object
        LocalDateTime submissionDateTime = LocalDateTime.of(date, time);

        // Format the LocalDateTime object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm");

        // Create projectID based on projectType
        String projectID = "";

        if (projectType.equals("Internship")) {
            projectID = "INT";
        } else if (projectType.equals("Investigation Report")) {
            projectID = "IR";
        } else if (projectType.equals("RMCP")) {
            projectID = "RMCP";
        } else if (projectType.equals("Capstone Project - P1")) {
            projectID = "CP1";
        } else if (projectType.equals("Capstone Project - P2")) {
            projectID = "CP2";
        } else if (projectType.equals("Final Year Project")) {
            projectID = "FYP";
        }

        return String.join(",",
                intakeCode,
                studentID,
                projectID,
                projectType,
                "Not Submitted",
                submissionDateTime.format(formatter),
                "-",                                    // Time left
                "-",                                    // Last Modified date
                supervisor,
                secondMarker,
                "0",                                    // Marking Status
                "-",                                    // Marks from Supervisor
                "-");                                   // Marks from Second Marker
    }

    // Filter the necessary columns for the Manage Project table
    public String filterManageProjectArray(String newProjectData) {
        // Convert the project data to an array
        String[] data = newProjectData.split(",");

        // Filter out the values you need
        String[] filteredData = new String[] {data[0], data[1], data[3], data[4], data[5], data[8], data[9]};

        // Join the filtered values back into a string
        String filteredManageProjectArray = String.join(",", filteredData);

        return filteredManageProjectArray;
    }

    // Filter the necessary columns for the View Project Status table
    public String filterViewProjectStatusArray(String newProjectData) {
        // Convert the project data to an array
        String[] data = newProjectData.split(",");

        // Filter out the values you need
        String[] filteredData = new String[] {data[1], data[3], data[4]};

        // Join the filtered values back into a string
        String filteredViewProjectStatusArray = String.join(",", filteredData);

        return filteredViewProjectStatusArray;
    }

    public boolean studentIDExistsInFile(String studentID) {
        File file = new File(PROJECTS_FILE_PATH);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(studentID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while reading StudentAssignment.txt file. Please try again.");
            e.printStackTrace();
        }
        return false;
    }

    public void updateLecturerRole(String newSupervisorID, String newSecondMarkerID, String oldSupervisorID, String oldSecondMarkerID) {
        File file = new File(USER_DATA_FILE);
        List<String> updatedLines = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
    
                String userId = parts[0];
                String role = parts[4];
    
                // Add suffix for new supervisor
                if (userId.equals(newSupervisorID) && !role.contains("SP")) {
                    role = role.contains("SM") ? "Lecturer-SP-SM" : "Lecturer-SP";
                }
    
                // Add suffix for new second marker
                if (userId.equals(newSecondMarkerID) && !role.contains("SM")) {
                    role = role.contains("SP") ? "Lecturer-SP-SM" : "Lecturer-SM";
                }
    
                // Remove SP suffix if the lecturer is no longer a supervisor
                if (userId.equals(oldSupervisorID) && !isLecturerAssignedInOtherProjects(oldSupervisorID, "Supervisor")) {
                    role = role.replace("-SP", "");
                    role = role.equals("Lecturer-") ? "Lecturer" : role; // Clean up trailing hyphen
                }
    
                // Remove SM suffix if the lecturer is no longer a second marker
                if (userId.equals(oldSecondMarkerID) && !isLecturerAssignedInOtherProjects(oldSecondMarkerID, "Second Marker")) {
                    role = role.replace("-SM", "");
                    role = role.equals("Lecturer-") ? "Lecturer" : role; // Clean up trailing hyphen
                }
    
                // Update the parts array with the modified role
                parts[4] = role;
    
                updatedLines.add(String.join(",", parts));
                
                OODJ_Assignment.updateAllUserRoles(userId, role);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        try {
            Files.write(Paths.get(USER_DATA_FILE), updatedLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private boolean isLecturerAssignedInOtherProjects(String lecturerID, String role) {
        File file = new File(PROJECTS_FILE_PATH);
    
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (role.equals("Supervisor") && parts[8].equals(lecturerID)) {
                    return true;
                } else if (role.equals("Second Marker") && parts[9].equals(lecturerID)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }   
    
    public void updateLecturerRoleAfterDeletion(String lecturerID, String roleType) throws IOException {
        List<String> roles = readRolesFile();
        List<String> updatedRoles = new ArrayList<>();

        for (String role : roles) {
            String[] parts = role.split(",");


            if (parts[0].equals(lecturerID)) {
                String currentRole = parts[4];


                if (roleType.equals("SP") && currentRole.contains("-SP")) {

                    currentRole = currentRole.replace("-SP", "");

                }
                if (roleType.equals("SM") && currentRole.contains("-SM")) {

                    currentRole = currentRole.replace("-SM", "");
                }
                currentRole = currentRole.replace("--", "-").replaceAll("-$", "");
                parts[4] = currentRole;
            }
            updatedRoles.add(String.join(",", parts));
        }

        writeRolesFile(updatedRoles);
    }

    private List<String> readRolesFile() throws IOException {
        File file = new File(USER_DATA_FILE);
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }

        return lines;
    }

    private void writeRolesFile(List<String> roles) throws IOException {
        File file = new File(USER_DATA_FILE);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String role : roles) {
                writer.write(role);
                writer.newLine();
            }
        }
    }
}

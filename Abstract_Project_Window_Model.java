package com.mycompany.oodj_assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import java.io.IOException;

public class Abstract_Project_Window_Model {
    // Intake Code file path
    private static final String INTAKE_CODE_FILE = "intakeCode.txt";

    // User data file path
    private static final String USER_DATA_FILE = "userdata.txt";

    // File to be read for reading and writing data
    private static final String PROJECTS_FILE_PATH = "StudentAssignment.txt";

    private String[] intakeCodes;
    private List<String> studentIDs;
    private Map<String, String> supervisorsAndSecondMarkers; // key: lecturer name, value: lecturer ID

    public Abstract_Project_Window_Model() {
        studentIDs = new ArrayList<>();
        supervisorsAndSecondMarkers = new HashMap<>();
        readIntakeCodesFromFile();
        readUsersFromFile();
    }

    public void readIntakeCodesFromFile() {
        List<String> codes = new ArrayList<>(); // Initialize a list to store the intake codes

        try (BufferedReader br = new BufferedReader(new FileReader(INTAKE_CODE_FILE))){
            String line;
            while ((line = br.readLine()) != null) {
                codes.add(line);
            }
        } catch (IOException e) {
            System.err.print("Error reading the file: " + e.getMessage());
        }

        intakeCodes = codes.toArray(new String[0]);
        System.out.println(intakeCodes);
    }


    public void readUsersFromFile() {
        try {
            // Clear the lists before adding new data
            supervisorsAndSecondMarkers.clear();
            studentIDs.clear();

            List<String> lines = Files.readAllLines(Paths.get(USER_DATA_FILE));
            List<String> assignedStudents = Files.readAllLines(Paths.get(PROJECTS_FILE_PATH));

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String id = parts[0];
                    String name = parts[2];
                    String role = parts[4];
                    String status = parts[5];

                    if (role.toLowerCase().contains("lecturer") && !"lecturer-pm".equalsIgnoreCase(role) && "ACTIVE".equalsIgnoreCase(status)) {
                        supervisorsAndSecondMarkers.put(id, name);
                    } else if ("Student".equalsIgnoreCase(role) && "ACTIVE".equalsIgnoreCase(status)) {
                        studentIDs.add(id);
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + USER_DATA_FILE);
            e.printStackTrace();
        }
    }

    public String[] getIntakeCodes() {
        return intakeCodes;
    }

    public List<String> getStudentIDs() {
        return studentIDs;
    }

    public Map<String, String> getSupervisorsAndSecondMarkers() {
        return supervisorsAndSecondMarkers;
    }
}

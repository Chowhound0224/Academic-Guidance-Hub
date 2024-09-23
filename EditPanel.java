package com.mycompany.oodj_assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EditPanel extends JFrame {
    private JTextField statusField;
    private JComboBox<String> roleComboBox, schoolwiseComboBox, intakecodeCmbBox, statusComboBox;
    private JButton initPasswordButton ,saveButton;
    private UserModel user;
    private UserEditPanel parentPanel;
    private JLabel passwordLabel;

    public EditPanel(UserModel user, UserEditPanel parentPanel) {
        this.user = user;
        this.parentPanel = parentPanel;
        setTitle("Edit User Data");
        setLayout(new GridLayout(5, 3));
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        statusField = new JTextField(user.getStatus());

        if (user.getRole().contains("Lecturer")) {
            schoolwiseComboBox = new JComboBox<>(new String[]{"", "Individual", "School of Computing",
                "School of Engineering", "School of Business", "School of Life Sciences",
                "School of Media Arts & Design", "School of Accounting & Finance", "School of Hospitality", "School of General Studies"});
            schoolwiseComboBox.setSelectedItem(user.getSchoolWise());
            roleComboBox = new JComboBox<>(new String[]{"Lecturer-PM", "Lecturer-SM","Lecturer-SP-SM", "Lecturer-SP","Lecturer"});
            roleComboBox.setSelectedItem(user.getRole());
        } else if (user.getRole().contains("Student")) {
            String[] intakeCodeList = new String[0]; // Initialize as an empty array
            intakecodeCmbBox = new JComboBox<>(intakeCodeList);
            
            try (BufferedReader reader = new BufferedReader(new FileReader("intakeCode.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    intakecodeCmbBox.addItem(line.trim()); // Trim whitespace from each line before adding
                }
            } catch (IOException ex) {
                // Handle file reading error appropriately
                ex.printStackTrace(); // For now, print the stack trace
            }
        }
        
        if (user.getRole().contains("Lecturer")) {
            add(new JLabel("Schoolwise:"));
            add(schoolwiseComboBox);
            add(new JLabel());
            add(new JLabel("Role:"));
            add(roleComboBox);
            add(new JLabel());
            
        } else if (user.getRole().contains("Student")) {
            add(new JLabel("Intake Code:"));
            add(intakecodeCmbBox);
            add(new JLabel());
        }
        
        add(new JLabel("Password"));
        passwordLabel = new JLabel(user.getPassword());
        add(passwordLabel);
        
        initPasswordButton = new JButton("Initialized Password");
        initPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = user.getId();
                String prefix = id.substring(0, 2);
                String remaining = id.substring(2);
                String capitalLetter = prefix.substring(0, 1);
                String lowerLetter = prefix.substring(1, 2).toLowerCase();
                String lastFourDigits = id.substring(id.length() - 4);

                String newPassword = capitalLetter + lowerLetter + remaining + "@" + lastFourDigits;
                user.setPassword(newPassword);
                passwordLabel.setText(newPassword);
            }
        });
        add(initPasswordButton);
        
        
        add(new JLabel("Status:"));
        statusComboBox = new JComboBox<>(new String[]{"ACTIVE", "INACTIVE"});
        statusComboBox.setSelectedItem(user.getStatus());
        add(statusComboBox);
        add(new JLabel());

        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveUserData();
                OODJ_Assignment.readAllUsers();
                dispose();
            }
        });
        
        add(new JLabel()); // Placeholder for layout consistency
        add(saveButton);
        
        if (user.getRole().contains("Admin")) {
            add(new JLabel());
            add(new JLabel());
            add(new JLabel());
            add(new JLabel());
            add(new JLabel());
        }
    }

    private void saveUserData() {
        String status = (String) statusComboBox.getSelectedItem();
        String schoolwise = "";
        String intakeCode = "";
        String role = user.getRole();

        if (user.getRole().contains("Lecturer")) {
            schoolwise = (String) schoolwiseComboBox.getSelectedItem();
            role = (String) roleComboBox.getSelectedItem();
        } else if (user.getRole().contains("Student")) {
            intakeCode = (String) intakecodeCmbBox.getSelectedItem();
        } 
        
        if (schoolwise.isBlank()){
            if(intakeCode.isBlank()){
                schoolwise = "-";
            } else {
                schoolwise = intakeCode;
            }
        }
        
        String userID = user.getId();
        String password = passwordLabel.getText();
        String name = user.getName();
        String date = user.getLastChangedPasswordDate();
        
        updateFile("userdata.txt", userID ,password, name, schoolwise,role, status, date);
        updateFile("userPersonalData.txt", userID,schoolwise,name,
                "", "", "", "", "", "", "", "", "", "", "", ""); // Keeping other fields blank as they are not edited
        for (User users : OODJ_Assignment.allUsers) {
            if (users.id.equals(userID)) {
                users.setSchoolwise(schoolwise);
                users.setRole(role);
                users.setPassword(password);
                users.setDate(date);
                break;
            }
        }
        parentPanel.loadUserData();
    }

    private void updateFile(String filename, String... userData) {
        List<String> lines = new ArrayList<>();
        String userId = userData[0];
        boolean userExists = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(userId)) {
                    StringBuilder updatedLine = new StringBuilder();
                    for (int i = 0; i < userData.length; i++) {
                        if (userData[i] == null || userData[i].isEmpty()) {
                            // If the new data is empty or null, keep the existing data
                            updatedLine.append(data[i]).append(",");
                        } else {
                            // Otherwise, update with the new data
                            updatedLine.append(userData[i]).append(",");
                        }
                    }
                    lines.add(updatedLine.toString());
                    userExists = true;
                } else {
                    lines.add(line);
                }
            }
            OODJ_Assignment.readAllUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If the user does not exist, add the new user data
        if (!userExists) {
            lines.add(String.join(",", userData));
        }

        // Write the updated data back to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}

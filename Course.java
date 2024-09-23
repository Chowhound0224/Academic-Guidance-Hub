package com.mycompany.oodj_assignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.*;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;
import com.mycompany.oodj_assignment.ui.Login_Window_UI;

public class Course extends JPanel implements ActionListener, LogoutCallback {
    
    private JFrame frame;
    private JComboBox<String> programBox;
    private JComboBox<String> yearOfStudyBox;
    private JComboBox<String> yearBox;
    private JComboBox<String> monthBox;
    private JTextField courseField;
    private JButton Back_Button;
    
    public Course(LogoutCallback callback) {
        frame = new JFrame("New Intake Form");
        // Set the layout to null to use absolute positioning
        setLayout(null);
        
        // Initialize combo boxes
        String[] programs = {"","Degree", "Foundation", "Diploma", "Master", "PhD"};
        programBox = new JComboBox<>(programs);
        
        String[] yearsOfStudy = {"", "1F", "2F", "3F", "4F"};
        yearOfStudyBox = new JComboBox<>(yearsOfStudy);
        
        String[] years = {"","2024", "2025", "2026", "2027", "2028"};
        yearBox = new JComboBox<>(years);
        
        String[] months = {"","03", "06", "09", "11"};
        monthBox = new JComboBox<>(months);
        
        courseField = new JTextField();
        
        // Add components to the panel with specified bounds
        JLabel programLabel = new JLabel("Choose Program:");
        programLabel.setBounds(70, 40, 150, 25);
        add(programLabel);
        
        programBox.setBounds(220, 40, 150, 25);
        add(programBox);
        
        JLabel yearOfStudyLabel = new JLabel("Choose Year of Study:");
        yearOfStudyLabel.setBounds(70, 90, 150, 25);
        add(yearOfStudyLabel);
        
        yearOfStudyBox.setBounds(220, 90, 150, 25);
        add(yearOfStudyBox);
        
        JLabel yearLabel = new JLabel("Choose Year:");
        yearLabel.setBounds(70, 140, 150, 25);
        add(yearLabel);
        
        yearBox.setBounds(220, 140, 150, 25);
        add(yearBox);
        
        JLabel monthLabel = new JLabel("Choose Month:");
        monthLabel.setBounds(70, 190, 150, 25);
        add(monthLabel);
        
        monthBox.setBounds(220, 190, 150, 25);
        add(monthBox);
        
        JLabel courseLabel = new JLabel("Enter Course:");
        courseLabel.setBounds(70, 240, 150, 25);
        add(courseLabel);
        
        courseField.setBounds(220, 240, 150, 25);
        add(courseField);
        
        JButton saveButton = new JButton("Save Course Information");
        saveButton.setBounds(70, 290, 300, 30);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCourseInfo();
            }
        });
        add(saveButton);

        Back_Button = new JButton("◄");
        Back_Button.setBounds(10, 10, 45, 25);
        Back_Button.addActionListener((ActionListener) this);
        add(Back_Button);
        
        
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 400);
        setVisible(true);
    }
    
    private void saveCourseInfo() {
        String program = (String) programBox.getSelectedItem();
        String yearOfStudy = (String) yearOfStudyBox.getSelectedItem();
        String year = (String) yearBox.getSelectedItem();
        String month = (String) monthBox.getSelectedItem();
        String course = courseField.getText();
        
        if (program == null || yearOfStudy == null || year == null || month == null || course.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill up all fields properly.");
            return;
        }

        String courseId = generateCourseId(program, yearOfStudy, year, month, course);
        if (!checkCourse(courseId)) {
            JOptionPane.showMessageDialog(this, "Course already exists.");
            return;
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("intakeCode.txt", true))) {
                writer.write(courseId);
                writer.newLine();
                JOptionPane.showMessageDialog(this, "Course information saved successfully!");
                programBox.setSelectedItem("");
                yearOfStudyBox.setSelectedItem("");
                yearBox.setSelectedItem("");
                monthBox.setSelectedItem("");
                courseField.setText("");
                
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error saving course information.");
                e.printStackTrace();
            }
        }
    }
    
    private String generateCourseId(String program, String yearOfStudy, String year, String month, String course) {
        String programPrefix = "";
        switch (program) {
            case "Degree":
                programPrefix = "APD";
                break;
            case "Foundation":
                programPrefix = "UCFF";
                break;
            case "Diploma":
                programPrefix = "APU";
                break;
            case "Master":
                programPrefix = "APM";
                break;
            case "PhD":
                programPrefix = "APP";
                break;
        }
        
        String courseSIM = getFirstLetters(course);
        return programPrefix + yearOfStudy + year.substring(2, 4) + month + courseSIM;
    }
    
    private String getFirstLetters(String str) {
        if (str == null || str.isEmpty()) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        String[] words = str.split("\\s+");
        for (String word : words) {
            if (!word.isEmpty()) {
                result.append(Character.toUpperCase(word.charAt(0)));
            }
        }
        return result.toString();
    }

    private boolean checkCourse(String courseId) {
        try (BufferedReader reader = new BufferedReader(new FileReader("intakeCode.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(courseId)) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LogoutHandler.logout((JFrame) SwingUtilities.getWindowAncestor(this), this);
    }
    
    public void onLogout() {
        setVisible(false);
        Login_Window_UI LW_UI = new Login_Window_UI();
        Login_Window LW = new Login_Window(LW_UI);
    }
}

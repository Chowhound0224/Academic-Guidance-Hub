package com.mycompany.oodj_assignment;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.mycompany.oodj_assignment.ui.Login_Window_UI;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ViewTimetable extends JPanel implements ActionListener, LogoutCallback,DataLoader {
    
    private String username;
    
    private JButton Back_Button;
    private LogoutCallback logoutCallback;
    
    private JLabel Name_Label;
    private JLabel Lecturer_Name_Label;
    private JLabel Username_Label;
    private JLabel Lecturer_Username_Label;
    private JLabel Job_Title_Label;
    private JLabel User_Role_Label;
    private JLabel Timetable_Label;
    private JTable Table;
    private DefaultTableModel TableModel;
    
    public ViewTimetable(String Name, String Username, String User_Role, LogoutCallback callback) {
        this.username = Username;
        this.logoutCallback = callback;
        setLayout(null);
        
        Back_Button = new JButton("◄");
        Back_Button.setBounds(10, 10, 45, 25);
        Back_Button.addActionListener(this);

        Name_Label = new JLabel("Name: ");
        Name_Label.setBounds(60, 40, 165, 25);
        Lecturer_Name_Label = new JLabel(Name);
        Lecturer_Name_Label.setBounds(180, 40, 165, 25);
        Username_Label = new JLabel("Username/ UserID: ");
        Username_Label.setBounds(60, 80, 165, 25);
        Lecturer_Username_Label = new JLabel(Username);
        Lecturer_Username_Label.setBounds(180, 80, 165, 25);
        Job_Title_Label = new JLabel("Job Title: ");
        Job_Title_Label.setBounds(60, 120, 165, 25);
        User_Role_Label = new JLabel(User_Role);
        User_Role_Label.setBounds(180, 120, 165, 25);
        Timetable_Label = new JLabel("Timetable: ");
        Timetable_Label.setBounds(60, 160, 165, 25);

        String[] columnNames = {"StudentID","Student Name","Intake Code",
            "ProjectID","Project Name","Date","Begin Time","End Time"};
        TableModel = new DefaultTableModel(columnNames, 0);
        Table = new JTable(TableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7;
            }
        };
        JScrollPane scrollPane = new JScrollPane(Table);
        scrollPane.setBounds(60, 200, 900, 250);
        add(Back_Button);
        add(Name_Label);
        add(Lecturer_Name_Label);
        add(Username_Label);
        add(Lecturer_Username_Label);
        add(Job_Title_Label);
        add(User_Role_Label);
        add(Timetable_Label);
        add(scrollPane);
        reloadTable();
    }
    
    private void loadTable() {
        Map<String, String> studentNames = loadMap("userdata.txt");
        Map<String, String> projectNames = loadMap("studentAssignment.txt");
        List<String[]> timetableEntries = loadTimetableEntries(studentNames, projectNames);

        for (String[] entry : timetableEntries) {
            TableModel.addRow(entry);
        }
    }

    private List<String[]> loadTimetableEntries(Map<String, String> studentNames, Map<String, String> projectNames) {
        List<String[]> entries = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try (BufferedReader reader = new BufferedReader(new FileReader("Timetable.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 6 && parts[3].equals(username) && parts[7].equals("accepted")) {
                    LocalDate currentDate = LocalDate.now();
                    LocalDate presentationDate = LocalDate.parse(parts[4], formatter);
                    if (presentationDate.isAfter(currentDate)) {
                        String studentName = studentNames.getOrDefault(parts[0], "Unknown");
                        String projectName = projectNames.getOrDefault(parts[2], "Unknown");
                        entries.add(new String[]{parts[0], studentName, parts[1], parts[2], projectName,
                            parts[4], parts[5], parts[6], parts[7]});
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading timetable. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return entries;
    }
    
    public void reloadTable() {
        TableModel.setRowCount(0);
        loadTable();
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Back_Button) {
            LogoutHandler.logout((JFrame) SwingUtilities.getWindowAncestor(this), this);
        }
    }
    
    public void onLogout() {
        setVisible(false);
        Login_Window_UI LW_UI = new Login_Window_UI();
        Login_Window LW = new Login_Window(LW_UI);
    }

@Override
public Map<String, String> loadMap(String courseFilePath) {
    Map<String, String> Map = new HashMap<>();
    if ("userdata.txt".equals(courseFilePath)) {
        try (BufferedReader reader = new BufferedReader(new FileReader(courseFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2) {
                    Map.put(parts[0], parts[2]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading user data. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } else if ("studentAssignment.txt".equals(courseFilePath)) {;
        try (BufferedReader reader = new BufferedReader(new FileReader(courseFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2) {
                    Map.put(parts[2], parts[3]);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading student assignment. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    return Map;
}
    @Override
    public void loadAssignmentsFromFile(String filePath, String username, DefaultTableModel tableModel, Map<String, String> courseMap) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void selectFile(String PredefinedPath, JComponent parentComponent) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void openFile(File selectedFile, JComponent parentComponent) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

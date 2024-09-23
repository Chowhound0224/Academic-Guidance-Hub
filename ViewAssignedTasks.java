package com.mycompany.oodj_assignment;

import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import com.mycompany.oodj_assignment.ui.Login_Window_UI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ViewAssignedTasks extends JPanel implements ActionListener, LogoutCallback {

    private JButton Back_Button;
    private String username;
    private LogoutCallback logoutCallback;
    private JLabel TaskTableLabel;
    private JLabel TaskTypeLabel;
    private JComboBox<String> taskFilterComboBox;
    private JTable TasksTable;
    private DefaultTableModel TasksTableModel;
    private JLabel SearchCourseIDLabel;
    private JTextField SearchCourseIDField;
    private JButton SearchCourseIDButton;
    private JLabel StudentTableLabel;
    private JTable StudentTable;
    private JLabel SubmissionStatusLabel;
    private JComboBox<String> submissionFilterComboBox;
    private DefaultTableModel StudentTableModel;
    private JLabel SearchStudentIDLabel;
    private JTextField SearchStudentIDField;
    private JButton SearchStudentIDButton;
    private JLabel MarkingLabel;
    private JButton MarkingButton;
    private static final String ASSIGNMENT_FILE = "StudentAssignment.txt";
    private static final String USERDATA_FILE = "userdata.txt";
    private Map<String, String> userData;
    private Map<String, String> mark1;
    private Map<String, String> mark2;

    public ViewAssignedTasks(String Username, LogoutCallback callback) {
        this.username = Username;
        this.logoutCallback = callback;
        this.userData = readUserDataFromFile();
        setLayout(null);

        Back_Button = new JButton("◄");
        Back_Button.setBounds(10, 10, 45, 25);
        Back_Button.addActionListener(this);
        
        SearchCourseIDLabel = new JLabel("Search With ProjectID:");
        SearchCourseIDLabel.setBounds(60,400,150,25);
        
        TaskTableLabel = new JLabel("Task Table:");
        TaskTableLabel.setBounds(60, 40, 165, 25);
        
        TaskTypeLabel = new JLabel("Tasks Type:");
        TaskTypeLabel.setBounds(60, 80, 150, 25);

        String[] taskTypes = {"Both", "Supervisor", "Second Marker"};
        taskFilterComboBox = new JComboBox<>(taskTypes);
        taskFilterComboBox.setBounds(160, 80, 150, 25);
        taskFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadAndDisplayTasks();
                reloadStudentTable();
            }
        });

        SearchCourseIDField = new JTextField("ProjectID");
        SearchCourseIDField.setBounds(60, 440, 150, 25);
        SearchCourseIDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    SearchCourseIDField.setText(SearchCourseIDField.getText().toUpperCase());
                    performSearch();
                }
            }
        });

        SearchCourseIDButton = new JButton("Search");
        SearchCourseIDButton.setBounds(240, 440, 100, 25);
        SearchCourseIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchCourseIDField.setText(SearchCourseIDField.getText().toUpperCase());
                performSearch();
            }
        });

        String[] columnNames1 = {"Task Type", "Intake Code", "ProjectID", "Course Name"
                ,"Submission Date", "Supervisor ID", "Second Marker ID"};
        TasksTableModel = new DefaultTableModel(columnNames1, 0);
        TasksTable = new JTable(TasksTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };
        JScrollPane scrollPanel1 = new JScrollPane(TasksTable);
        scrollPanel1.setBounds(60, 120, 700, 250);
        TableColumn column0a = TasksTable.getColumnModel().getColumn(0);
        TableColumn column1a = TasksTable.getColumnModel().getColumn(1); 
        TableColumn column2a = TasksTable.getColumnModel().getColumn(2);
        TableColumn column4a = TasksTable.getColumnModel().getColumn(4);
        TableColumn column5a = TasksTable.getColumnModel().getColumn(5);
        TableColumn column6a = TasksTable.getColumnModel().getColumn(6);
        column0a.setPreferredWidth(40);
        column1a.setPreferredWidth(50);
        column2a.setPreferredWidth(20);
        column4a.setPreferredWidth(60);
        column5a.setPreferredWidth(30);
        column6a.setPreferredWidth(50);
        
        StudentTableLabel = new JLabel("Students' Details Table:");
        StudentTableLabel.setBounds(800, 40, 165, 25);
        
        String[] columnNames2 = {"StudentID", "Student Name", "Intake Code", "Submission Status",
            "Last Modified Date", "Supervisee Status"};
        StudentTableModel = new DefaultTableModel(columnNames2, 0);
        StudentTable = new JTable(StudentTableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };
        JScrollPane scrollPanel2 = new JScrollPane(StudentTable);
        scrollPanel2.setBounds(800, 120, 650, 250);
        TableColumn column0b = StudentTable.getColumnModel().getColumn(0);
        TableColumn column1b = StudentTable.getColumnModel().getColumn(1);
        TableColumn column2b = StudentTable.getColumnModel().getColumn(2);
        TableColumn column3b = StudentTable.getColumnModel().getColumn(3);
        column0b.setPreferredWidth(30);
        column1b.setPreferredWidth(30);
        column2b.setPreferredWidth(50);
        column3b.setPreferredWidth(40);
        
        SubmissionStatusLabel = new JLabel("Submission Status:");
        SubmissionStatusLabel.setBounds(800, 80, 150, 25);
        
        String[] SubmissionStatus = {"All", "Submitted","Pending","Not Submitted"};
        submissionFilterComboBox = new JComboBox<>(SubmissionStatus);
        submissionFilterComboBox.setBounds(1000, 80, 150, 25);
        submissionFilterComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reloadStudentTable();
            }
        });
        
        SearchStudentIDLabel = new JLabel("Search With StudentID:");
        SearchStudentIDLabel.setBounds(800, 400, 200, 25);
        
        SearchStudentIDField = new JTextField("StudentID");
        SearchStudentIDField.setBounds(800, 440, 150, 25);

        SearchStudentIDButton = new JButton("Search Student");
        SearchStudentIDButton.setBounds(980, 440, 200, 25);
        SearchStudentIDButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchStudentByID();
            }
        });
        
        MarkingLabel = new JLabel("Evaluate Student Reports:");
        MarkingLabel.setBounds(1250, 400, 200, 25);
        
        MarkingButton = new JButton("Marking");
        MarkingButton.setBounds(1250, 440, 200, 25);
        MarkingButton.addActionListener(this);
        
        add(Back_Button);
        add(TaskTableLabel);
        add(TaskTypeLabel);
        add(taskFilterComboBox);
        add(SearchCourseIDLabel);
        add(SearchCourseIDField);
        add(SearchCourseIDButton);
        add(scrollPanel1);
        add(StudentTableLabel);
        add(scrollPanel2);
        add(SubmissionStatusLabel);
        add(submissionFilterComboBox);
        add(SearchStudentIDLabel);
        add(SearchStudentIDField);
        add(SearchStudentIDButton);
        add(MarkingLabel);
        add(MarkingButton);

        loadAndDisplayTasks();

        TasksTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = TasksTable.getSelectedRow();
                    if (selectedRow != -1) {
                        String intakeCode = (String) TasksTableModel.getValueAt(selectedRow, 1);
                        String courseID = (String) TasksTableModel.getValueAt(selectedRow, 2);
                        String submissionStatus = (String) submissionFilterComboBox.getSelectedItem();
                        updateStudentTable(intakeCode, courseID, submissionStatus);
                    }
                }
            }
        });
    }

    private void loadAndDisplayTasks() {
        List<String[]> assignments = readAssignmentsFromFile();
        Set<String> uniqueAssignments = new HashSet<>();
        String selectedFilter = (String) taskFilterComboBox.getSelectedItem();

        TasksTableModel.setRowCount(0);

        for (String[] assignment : assignments) {
            String taskType = username.equals(assignment[8]) ? "Supervisor" : (username.equals(assignment[9]) ? "Second Marker" : null);
            if (taskType != null) {
                String uniqueKey = assignment[0] + assignment[2] + taskType;
                if (!uniqueAssignments.contains(uniqueKey) && 
                    (selectedFilter.equals("Both") || selectedFilter.equals(taskType))) {
                    uniqueAssignments.add(uniqueKey);
                    String[] rowData = {
                        taskType,
                        assignment[0],
                        assignment[2],
                        assignment[3],
                        assignment[5],
                        assignment[8],
                        assignment[9]
                    };
                    TasksTableModel.addRow(rowData);
                }
            }
        }
    }

    private void updateStudentTable(String intakeCode, String courseID, String submissionStatus) {
        StudentTableModel.setRowCount(0);
        List<String[]> assignments = readAssignmentsFromFile();
        Map<String, String[]> timetableData = readTimetableFromFile();
        LocalDate currentDate = LocalDate.now();

        for (String[] assignment : assignments) {
            if (assignment[0].equals(intakeCode) && assignment[2].equals(courseID) && (submissionStatus.equals("All") || submissionStatus.equals(assignment[4]))) {
                String studentID = assignment[1];
                String studentName = userData.getOrDefault(studentID, "Unknown");
                String[] timetableInfo = timetableData.get(studentID);
                String superviseeStatus = "Pending Request";

                if (timetableInfo != null) {
                    String presentationDateStr = timetableInfo[3];
                    String presentationStatus = timetableInfo[6];
                    LocalDate presentationDate = LocalDate.parse(presentationDateStr);

                    if (presentationStatus.equals("accepted")) {
                        if (presentationDate.isBefore(currentDate)) {
                            superviseeStatus = "Completed";
                        } else {
                            superviseeStatus = "Scheduled";
                        }
                    }
                }

                String[] rowData = {
                    studentID,
                    studentName,
                    assignment[0],
                    assignment[4],
                    assignment[7],
                    superviseeStatus
                };
                StudentTableModel.addRow(rowData);
            }
        }
    }

    private void performSearch() {
        String searchText = SearchCourseIDField.getText().trim();
        if (searchText.isEmpty()) {
            return;
        }

        if (searchText.matches("^\\d+$")) {
            searchStudentTableByID(searchText);
        } else {
            searchTasksTableByCourseID(searchText);
        }
    }

    private void searchTasksTableByCourseID(String courseID) {
        for (int i = 0; i < TasksTableModel.getRowCount(); i++) {
            if (TasksTableModel.getValueAt(i, 2).equals(courseID)) {
                TasksTable.setRowSelectionInterval(i, i);
                TasksTable.scrollRectToVisible(TasksTable.getCellRect(i, 0, true));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "ProjectID not found");
    }

    private void searchStudentTableByID(String studentID) {
        for (int i = 0; i < StudentTableModel.getRowCount(); i++) {
            if (StudentTableModel.getValueAt(i, 0).equals(studentID)) {
                StudentTable.setRowSelectionInterval(i, i);
                StudentTable.scrollRectToVisible(StudentTable.getCellRect(i, 0, true));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "StudentID not found");
    }

    private List<String[]> readAssignmentsFromFile() {
        List<String[]> assignments = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ASSIGNMENT_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                assignments.add(fields);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return assignments;
    }

    private Map<String, String> readUserDataFromFile() {
        Map<String, String> userData = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(USERDATA_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length > 2 && fields[4].equals("Student")) {
                    userData.put(fields[0], fields[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userData;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Back_Button) {
            LogoutHandler.logout((JFrame) SwingUtilities.getWindowAncestor(this), this);
        } else if (e.getSource() == MarkingButton) {
            int selectedRow = StudentTable.getSelectedRow();
            if (selectedRow != -1) {
                String StudentID = (String) StudentTableModel.getValueAt(selectedRow, 0);
                String submissionStatus = (String) StudentTableModel.getValueAt(selectedRow, 3);
                String dueDateStr = (String) TasksTableModel.getValueAt(TasksTable.getSelectedRow(), 4);
                String supervisorID = (String) TasksTableModel.getValueAt(TasksTable.getSelectedRow(), 5);
                String secondmarkerID = (String) TasksTableModel.getValueAt(TasksTable.getSelectedRow(), 6);
                String MarkingStatus = "";
                
                Map<String, String> mark1 = new HashMap<>();
                Map<String, String> mark2 = new HashMap<>();
                try (BufferedReader br = new BufferedReader(new FileReader(ASSIGNMENT_FILE))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] fields = line.split(",");
                        if (fields.length > 2 && fields[1].equals(StudentID)) {
                            mark1.put(fields[1], fields[11]);
                            mark2.put(fields[1], fields[12]);
                        }
                    }
                } catch (IOException f) {
                    f.printStackTrace();
                }
                
                if (supervisorID.equals(username)) {
                    MarkingStatus = mark1.getOrDefault(StudentID, "Unknown");
                } else if (secondmarkerID.equals(username)) {
                    MarkingStatus = mark2.getOrDefault(StudentID, "Unknown");
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Action!");
                }
                
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    Date dueDate = dateFormat.parse(dueDateStr);

                    if ("Submitted".equals(submissionStatus) && new Date().after(dueDate) && "-".equals(MarkingStatus)) {
                        String courseID = (String) TasksTableModel.getValueAt(TasksTable.getSelectedRow(), 2);
                        boolean isSupervisor = username.equals(supervisorID);
                        new MarkingPanel(StudentID, courseID, username, isSupervisor).setVisible(true);
                    } else {
                        if (!"Submitted".equals(submissionStatus)) {
                            JOptionPane.showMessageDialog(null, "The assignment has not been Submitted!");
                            return;
                        }
                        if (!new Date().after(dueDate)) {
                            JOptionPane.showMessageDialog(null, "The due date has not Passed!");
                            return;
                        }
                        if (!"_".equals(MarkingStatus)) {
                            JOptionPane.showMessageDialog(null, "You have already mark this assignment before!");
                            return;
                        }
                    }
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a student's assignment to mark.");
            }
        }
    }

    public void reloadStudentTable() {
        int selectedRow = TasksTable.getSelectedRow();
        if (selectedRow != -1) {
            String courseID = (String) TasksTableModel.getValueAt(selectedRow, 2);
            String intakeCode = (String) TasksTableModel.getValueAt(selectedRow, 1);
            String submissionStatus = (String) submissionFilterComboBox.getSelectedItem();
            updateStudentTable(intakeCode, courseID, submissionStatus);
        }
    }
    
    private void searchStudentByID() {
        String studentID = SearchStudentIDField.getText().trim();
        if (studentID.isEmpty()) {
            return;
        }

        for (int i = 0; i < StudentTableModel.getRowCount(); i++) {
            if (StudentTableModel.getValueAt(i, 0).equals(studentID)) {
                StudentTable.setRowSelectionInterval(i, i);
                StudentTable.scrollRectToVisible(StudentTable.getCellRect(i, 0, true));
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "StudentID not found");
    }

    private Map<String, String[]> readTimetableFromFile() {
        Map<String, String[]> timetableData = new HashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader("Timetable.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                String studentID = fields[0];
                String presentationStatus = fields[7];
                LocalDate presentationDate = LocalDate.parse(fields[4], formatter);

                if (presentationStatus.equals("accepted")) {
                    if (!timetableData.containsKey(studentID) ||
                            LocalDate.parse(timetableData.get(studentID)[3], formatter).isBefore(presentationDate)) {
                        String[] data = Arrays.copyOfRange(fields, 1, fields.length);
                        timetableData.put(studentID, data);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return timetableData;
    }

    
    public void onLogout() {
        setVisible(false);
        Login_Window_UI LW_UI = new Login_Window_UI();
        Login_Window LW = new Login_Window(LW_UI);
    }
}

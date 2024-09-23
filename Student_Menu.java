package com.mycompany.oodj_assignment;

import com.mycompany.oodj_assignment.ui.Login_Window_UI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Student_Menu extends JFrame implements ActionListener, LogoutCallback, DataLoader {
    private String username, password, name, usertype;

    private JButton Back_Button1;
    private JButton Back_Button2;
    private JButton Back_Button3;
    
    private JTable assignmentList;
    private JTable timeSlotList;
    private DefaultTableModel assignmentTableModel, timeSlotTableModel, resultTableModel;
    private JButton applyButton;
    private JButton submitButton;
    private JButton removeButton;
    private JButton editButton;
    private JButton viewButton;
    public static DefaultTableModel TimeSlot;
    public static JTable TimeSlotList;
    private JScrollPane TimeSlotSp;
    
    private Map<String, String> courseMap;

    private SubmissionManager submissionManager;
    private PresentationSlotHandler presentationSlotHandler;

    private String filePath = FileDataLoader.getDirectoryString() + "\\StudentAssignment.txt";
    
    private JPanel Change_Password;
    
    private File selectedFile;
    
    public Student_Menu(String Username, String Password, String Name, String User_Role) {
        username = Username;
        password = Password;
        name = Name;
        usertype = User_Role;

        setTitle("Student Menu");
        setSize(1500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        Change_Password = new JPanel();

        Back_Button1 = new JButton("◄");
        Back_Button1.setBounds(10, 10, 45, 25);
        Back_Button1.addActionListener(this);
        JTabbedPane tabbedPane = new JTabbedPane();
        JPanel submissionPanel = new JPanel(null);
        assignmentTableModel = new DefaultTableModel(
                new Object[]{"Course ID", "Course Name", "Submission Status", "Due Date", "Time Remaining", "Last Modified"}, 0);
        assignmentList = new JTable(assignmentTableModel);
        JScrollPane assignmentScrollPane = new JScrollPane(assignmentList);
        assignmentScrollPane.setBounds(60, 40, 1000, 400);
        submissionPanel.add(assignmentScrollPane);
        submissionPanel.add(Back_Button1);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        submitButton = new JButton("Submit");
        removeButton = new JButton("Remove");
        editButton = new JButton("Edit");
        viewButton = new JButton("View");
        buttonPanel.setBounds(60, 450, 680, 50);
        submitButton.setBounds(10, 10, 80, 30);
        removeButton.setBounds(100, 10, 80, 30);
        editButton.setBounds(190, 10, 80, 30);
        viewButton.setBounds(280,10,80,30);
        buttonPanel.add(submitButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(viewButton);
        submissionPanel.add(buttonPanel);

        submitButton.addActionListener(this);
        removeButton.addActionListener(this);
        editButton.addActionListener(this);
        viewButton.addActionListener(this);

        tabbedPane.addTab("Submissions", submissionPanel);

        JPanel presentationSlotPanel = new JPanel(new BorderLayout());
        timeSlotTableModel = new DefaultTableModel(new Object[]{"Course ID", "Course Name", "Lecture ID", "Assignment Due Date"}, 0);
        timeSlotList = new JTable(timeSlotTableModel);
        timeSlotList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane timeSlotScrollPane = new JScrollPane(timeSlotList);
        presentationSlotPanel.add(timeSlotScrollPane, BorderLayout.CENTER);

        JPanel presentationSlotViewPanel = new JPanel(null);
        presentationSlotViewPanel.setBounds(60, 40, 1000, 400);
        
        Back_Button2 = new JButton("◄");
        Back_Button2.setBounds(10, 10, 45, 25);
        Back_Button2.addActionListener(this);
        TimeSlot = new DefaultTableModel(new Object[]{"Course ID", "Course Name", "Lecturer ID", "Date", "Begin Time", "End Time", "Status"}, 0);
        TimeSlotList = new JTable(TimeSlot);
        TimeSlotList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        TimeSlotSp = new JScrollPane(TimeSlotList);
        TimeSlotSp.setBounds(60, 40, 1000, 400);
        presentationSlotViewPanel.add(TimeSlotSp);
        presentationSlotViewPanel.add(Back_Button2);

        tabbedPane.addTab("View Presentation Slots Status", presentationSlotViewPanel);

        JPanel viewResultPanel = new JPanel(null);
        viewResultPanel.setBounds(60, 40, 1000, 400);
        
        Back_Button3 = new JButton("◄");
        Back_Button3.setBounds(10, 10, 45, 25);
        Back_Button3.addActionListener(this);
        resultTableModel = new DefaultTableModel(new Object[]{"Course ID", "Course Name", "Result"}, 0);
        JTable resultTable = new JTable(resultTableModel);
        JScrollPane resultScrollPane = new JScrollPane(resultTable);
        resultScrollPane.setBounds(60,40, 1000, 400);
        viewResultPanel.add(resultScrollPane);
        viewResultPanel.add(Back_Button3);

        tabbedPane.addTab("View Results", viewResultPanel);

        DateSelector dateSelector = new DateSelector();
        presentationSlotPanel.add(dateSelector, BorderLayout.EAST);

        applyButton = new JButton("Apply");
        presentationSlotPanel.add(applyButton, BorderLayout.SOUTH);
        applyButton.addActionListener(this);
        tabbedPane.addTab("Apply Presentation Slots", presentationSlotPanel);

        add(tabbedPane, BorderLayout.CENTER);

        submissionManager = new FileSubmissionManager(assignmentTableModel, assignmentList, filePath, username);
        
        reloadData();
        tabbedPane.addTab("Change Password", Change_Password);
        tabbedPane.addChangeListener(e -> {
            int Selection = tabbedPane.getSelectedIndex();
            if (Selection == 4) {
                setVisible(false);
                Change_Password CP = new Change_Password(username, password, name, usertype);
                CP.setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            int selectedRow = assignmentList.getSelectedRow();
            submissionManager.submitAssignment(selectedRow, (username + "/") + assignmentList.getValueAt(selectedRow, 0).toString());
            reloadData();
        } else if (e.getSource() == removeButton) {
            int selectedRow = assignmentList.getSelectedRow();
            submissionManager.removeSubmission(selectedRow);
            reloadData();
        } else if (e.getSource() == editButton) {
            int selectedRow = assignmentList.getSelectedRow();
            submissionManager.editSubmission(selectedRow);
            reloadData();
        } else if (e.getSource() == applyButton) {
            int selectedRow = timeSlotList.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a row to apply.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String lecturerID = timeSlotList.getValueAt(selectedRow, 2).toString();
            String courseID = timeSlotList.getValueAt(selectedRow, 0).toString();
            String selectedDate = DateSelector.getSelectedDate();
            String DueDate = timeSlotList.getValueAt(selectedRow, 3).toString();

            if (selectedDate == null || selectedDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select a date.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm");
            LocalDateTime duedate = LocalDateTime.parse(DueDate, dateTimeFormatter);
            LocalDate dueDate = duedate.toLocalDate();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate SelectedDate = LocalDate.parse(selectedDate, formatter);
            
            TimeSlotSelectionPanel timeSlotSelectionPanel = new TimeSlotSelectionPanel(lecturerID, courseID, username, selectedDate, DueDate);
            if (timeSlotSelectionPanel.hasAvailableSlots() && SelectedDate.isAfter(dueDate) && SelectedDate.isBefore(dueDate.plusDays(15))) {
                int result = JOptionPane.showConfirmDialog(null, timeSlotSelectionPanel, "Select a time slot", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                }
            }
        }else if (e.getSource() == Back_Button1 || e.getSource() == Back_Button2 || e.getSource() == Back_Button3) {
            LogoutHandler.logout(this, this);
        } else if (e.getSource() == viewButton) {
            int selectedRow = assignmentList.getSelectedRow();
            if (selectedRow != -1) {
                String status = assignmentList.getValueAt(selectedRow, 2).toString();
                String courseID = assignmentList.getValueAt(selectedRow, 0).toString();
                if (status.equals("Submitted")) {
                    String currentDirectory = System.getProperty("user.dir");
                    String PredefinedPath = currentDirectory + File.separator + username + File.separator + courseID;
                    selectFile(PredefinedPath, (JComponent) this.getContentPane());
                } else {
                    JOptionPane.showMessageDialog(this, "The assignment not submitted!", "Invalid Action", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row!", "Invalid Action", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    }
    
    public void onLogout() {
        setVisible(false);
        Login_Window_UI LW_UI = new Login_Window_UI();
        Login_Window LW = new Login_Window(LW_UI);
    }

    @Override
    public Map<String, String> loadMap(String courseFilePath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void loadAssignmentsFromFile(String filePath, String username, DefaultTableModel tableModel, Map<String, String> courseMap) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void selectFile(String PredefinedPath, JComponent parentComponent) {
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
    
    public void reloadData() {
        assignmentTableModel.setRowCount(0);
        timeSlotTableModel.setRowCount(0);
        TimeSlot.setRowCount(0);
        resultTableModel.setRowCount(0);
        
        DataLoader dataLoader = new FileDataLoader();
        
        courseMap = dataLoader.loadMap(filePath);
        dataLoader.loadAssignmentsFromFile(filePath, username, assignmentTableModel, courseMap);
        
        presentationSlotHandler = new FilePresentationSlotHandler();
        presentationSlotHandler.showCoursePresentationList(username, timeSlotTableModel, courseMap);
        presentationSlotHandler.showStatusPresentationList(username, TimeSlot, courseMap);
        
        FileDataLoader.loadresult(username, resultTableModel, courseMap);
    }
}

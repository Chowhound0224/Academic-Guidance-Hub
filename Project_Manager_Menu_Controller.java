package com.mycompany.oodj_assignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;

public class Project_Manager_Menu_Controller {
    
    private String username, password, name, userRole;
    private Project_Manager_Menu_View view;
    private Project_Manager_Model model;

    private Abstract_Project_Window_Model abstractModel;
    private Create_Project_Window_View createProjectView;
    private Create_Project_Window_Controller controller;
    
    private LogoutCallback logoutCallback;
    
    public Project_Manager_Menu_Controller(String Username, String Password, String Name, String User_Role,
            Project_Manager_Menu_View view, Project_Manager_Model model, LogoutCallback callback) {
        this.username = Username;
        this.password = Password;
        this.name = Name;
        this.userRole = User_Role;
        this.view = view;
        this.model = model;
        this.abstractModel = new Abstract_Project_Window_Model();
        this.logoutCallback = callback;

        createButtonListener();
        editButtonListener();
        deleteButtonListener();
        BackButtonListener();
        TabChangeListener();
    }

    private void createButtonListener() {
        this.view.getCreateButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    createProjectView = new Create_Project_Window_View(abstractModel);
                    controller = new Create_Project_Window_Controller(createProjectView, model, view);
        
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
                    ex.printStackTrace();
                }
            }
        });
    }

    private void editButtonListener() {
        this.view.getEditButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedRowIndices = view.getTableManageProject().getSelectedRows();

                if (selectedRowIndices.length != 1) {
                    JOptionPane.showMessageDialog(view, "Please select ONE project to edit.");
                } else {
                    int selectedRowIndex = selectedRowIndices[0];
                    try {
                        // Fetch project data based on the selected row index
                        String intakeCode = (String) view.getTableManageProject().getValueAt(selectedRowIndex, 0);
                        String studentID = (String) view.getTableManageProject().getValueAt(selectedRowIndex, 1);
                        String projectType = (String) view.getTableManageProject().getValueAt(selectedRowIndex, 2);
                        String submissionDateTime = (String) view.getTableManageProject().getValueAt(selectedRowIndex, 4);
                        String supervisor = (String) view.getTableManageProject().getValueAt(selectedRowIndex, 5);
                        String secondMarker = (String) view.getTableManageProject().getValueAt(selectedRowIndex, 6);

                        // Parse date and time
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm");
                        LocalDateTime dateTime = LocalDateTime.parse(submissionDateTime, dateTimeFormatter);

                        // Extract date and time components
                        LocalDate date = dateTime.toLocalDate();
                        LocalTime time = dateTime.toLocalTime();

                        DefaultListModel<String> studentListModel = new DefaultListModel<>();
                        studentListModel.addElement(studentID);

                        Edit_Project_Window_View editView = new Edit_Project_Window_View(abstractModel);
                        Edit_Project_Window_Controller editController = new Edit_Project_Window_Controller(model,
                                view, editView, intakeCode, studentID, projectType, date, time, supervisor, secondMarker, selectedRowIndex);

                        editView.setIntakeCode(intakeCode);
                        editView.setProjectType(projectType);
                        // editView.setSubmissionDateTime(submissionDateTime);
                        editView.setDate(date);
                        editView.setTime(time);
                        editView.setSupervisor(supervisor);
                        editView.setSecondMarker(secondMarker);

                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    private void deleteButtonListener() {
        this.view.getDeleteButton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int[] selectedRowIndices = view.getTableManageProject().getSelectedRows();

                if (selectedRowIndices.length == 0) {
                    JOptionPane.showMessageDialog(view, "Please select at least ONE project to delete.");
                } else {
                    int confirmation = JOptionPane.showConfirmDialog(view, "Are you sure you want to delete " + selectedRowIndices.length + " projects?", "Delete Projects", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirmation == JOptionPane.YES_OPTION) {
                        try {
                            List<Integer> rowsToDelete = new ArrayList<>();
                            List<String> supervisorsToUpdate = new ArrayList<>();
                            List<String> secondMarkersToUpdate = new ArrayList<>();

                            for (int i = selectedRowIndices.length - 1; i >= 0; i--) {
                                int rowIndex = selectedRowIndices[i];
                                // Fetch supervisor and second marker IDs before deleting the project
                                String supervisorID = (String) view.getTableManageProject().getValueAt(rowIndex, 5);
                                String secondMarkerID = (String) view.getTableManageProject().getValueAt(rowIndex, 6);

                                supervisorsToUpdate.add(supervisorID);
                                secondMarkersToUpdate.add(secondMarkerID);

                                model.deleteProject(rowIndex);
                                rowsToDelete.add(rowIndex);
                            }

                            model.deleteRowsInFile(rowsToDelete);
                            view.updateTable();

                            // Debug
                            System.out.println("Supervisors to update: " + supervisorsToUpdate);
                            System.out.println("Second Markers to update: " + secondMarkersToUpdate);

                            for (String supervisorID : supervisorsToUpdate) {
                                model.updateLecturerRoleAfterDeletion(supervisorID, "SP");
                            }
                            for (String secondMarkerID : secondMarkersToUpdate) {
                                model.updateLecturerRoleAfterDeletion(secondMarkerID, "SM");
                            }

                            JOptionPane.showMessageDialog(view, "Project(s) deleted successfully!");
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(view, "Error: " + ex.getMessage());
                        }
                    }
                }
            }
        });
    }
    
    private void BackButtonListener() {
        this.view.getBackButton1().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == view.getBackButton1()) {
                    LogoutHandler.logout(view, logoutCallback);
                }
            }
        });
        this.view.getBackButton2().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == view.getBackButton2()) {
                    LogoutHandler.logout(view, logoutCallback);
                }
            }
        });
    }
    
    private void TabChangeListener() {
        view.getTabbedPane().addChangeListener(e -> {
            int Selection = view.getTabbedPane().getSelectedIndex();
            if (Selection == 2) {
                view.setVisible(false);
                Change_Password CP = new Change_Password(username, password, name, userRole);
                CP.setVisible(true);
            }
        });
    }
}

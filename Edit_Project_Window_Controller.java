package com.mycompany.oodj_assignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JList;
import javax.swing.JOptionPane;

public class Edit_Project_Window_Controller {
    private Edit_Project_Window_View editProjectView;
    private Project_Manager_Menu_View view;
    private Project_Manager_Model model;
    private int selectedRowIndex;

    public Edit_Project_Window_Controller(Project_Manager_Model model, Project_Manager_Menu_View view, Edit_Project_Window_View editProjectView, String intakeCode, String studentID, String projectType, LocalDate date, LocalTime time, String supervisor, String secondMarker, int selectedRowIndex) {
        this.editProjectView = editProjectView;
        this.model = model;
        this.view = view;
        this.selectedRowIndex = selectedRowIndex;



        // Populate the view with project data
        editProjectView.setIntakeCode(intakeCode);
        editProjectView.setStudentID(studentID);
        editProjectView.setProjectType(projectType);
        // editProjectView.setSubmissionDateTime(submissionDateTime);
        editProjectView.setDate(date);
        editProjectView.setTime(time);

        editProjectView.setSupervisor(supervisor);
        editProjectView.setSecondMarker(secondMarker);

        // Add action listener to the confirm button
        editProjectView.getButtonConfirmProject().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveProject();
            }
        });
    }

    private void saveProject() {
        try {
            // Logic to save the project details
            String intakeCode = editProjectView.getIntakeCode();
            String studentID = editProjectView.getStudentID();
            String projectType = editProjectView.getProjectType();
            LocalDate date = editProjectView.getDate();
            LocalTime time = editProjectView.getTime();

            // New Supervisor and Second Marker
            String supervisor = editProjectView.getSupervisor();
            String secondMarker = editProjectView.getSecondMarker();

            // Old Supervisor and Second Marker
            String oldSupervisor = view.getTableManageProject().getValueAt(selectedRowIndex, 5).toString();
            String oldSecondMarker = view.getTableManageProject().getValueAt(selectedRowIndex, 6).toString();

            // Validate empty selections
            String errorEmptySelectionsMessage = ValidationUtils.validateEmptyEditProject(date, time);

            // If there are any empty selections, display errors.
            if(!errorEmptySelectionsMessage.isEmpty()) {
                JOptionPane.showMessageDialog(null, errorEmptySelectionsMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate project details
            String errorProjectDetailsMessage = ValidationUtils.validateProjectDetails(date, supervisor, secondMarker);

            // If there are any errors, display them and return
            if(!errorProjectDetailsMessage.isEmpty()) {
                JOptionPane.showMessageDialog(null, errorProjectDetailsMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update the project in the model (you need to implement this method in your model)
            String newProjectData = model.formatProjectData(intakeCode, studentID, projectType, date, time, supervisor, secondMarker);

            String filteredManageProjectArray = model.filterManageProjectArray(newProjectData);
            String filteredViewProjectStatusArray = model.filterViewProjectStatusArray(newProjectData);

            model.updateProject(filteredManageProjectArray, filteredViewProjectStatusArray, selectedRowIndex);
            view.updateTable();
            model.updateRowInFile(newProjectData, selectedRowIndex);

           
            // Update lecturer role in userdata.txt
            model.updateLecturerRole(supervisor, secondMarker, oldSupervisor, oldSecondMarker);

            // Optionally close the edit window or provide feedback to the user
            JOptionPane.showMessageDialog(null, "Project updated successfully.");
            editProjectView.dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

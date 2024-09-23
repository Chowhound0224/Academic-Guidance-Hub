package com.mycompany.oodj_assignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JOptionPane;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

public class Create_Project_Window_Controller {
    private Create_Project_Window_View createProjectView;
    private Project_Manager_Model model;
    private Project_Manager_Menu_View view;

    public Create_Project_Window_Controller(Create_Project_Window_View createProjectView, Project_Manager_Model model, Project_Manager_Menu_View view) {
        this.createProjectView = createProjectView;
        this.model = model;
        this.view = view;

        addButtonListener();
    }

    private void addButtonListener() {
        this.createProjectView.getButtonConfirmProject().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addProject();
                    view.updateTable();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void addProject() {
        try {
            JComboBox<String> intakeCodeComboBox = getComboBox("comboBoxIntakeCode");
            JList<String> studentList = getList("studentList");
            JComboBox<String> projectTypeComboBox = getComboBox("comboBoxProjectType");
            DatePicker datePicker = getComponent("datePicker", DatePicker.class);
            TimePicker timePicker = getComponent("timePicker", TimePicker.class);
            JComboBox<String> supervisorComboBox = getComboBox("comboBoxSupervisor");
            JComboBox<String> secondMarkerComboBox = getComboBox("comboBoxSecondMarker");

            String intakeCode = intakeCodeComboBox.getSelectedItem().toString();
            String projectType = projectTypeComboBox.getSelectedItem().toString();
            LocalDate date = datePicker.getDate();
            LocalTime time = timePicker.getTime();
            String supervisor = supervisorComboBox.getSelectedItem().toString();
            String secondMarker = secondMarkerComboBox.getSelectedItem().toString();

            // Validate empty selections
            String errorEmptySelectionsMessage = ValidationUtils.validateEmptyCreateProject(studentList, date, time);

            // If there are any empty selections, display errors.
            if (!errorEmptySelectionsMessage.isEmpty()) {
                JOptionPane.showMessageDialog(null, errorEmptySelectionsMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate project details
            String errorProjectDetailsMessage = ValidationUtils.validateProjectDetails(date, supervisor, secondMarker);

            // If there are any errors, display them and return
            if (!errorProjectDetailsMessage.isEmpty()) {
                JOptionPane.showMessageDialog(null, errorProjectDetailsMessage, "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validate studentID exists in StudentAssignment.txt
            int projectsAdded = 0;
            List<String> existingStudentIDs = new ArrayList<>();
            for (String studentID : studentList.getSelectedValuesList()) {
                
                // In the controller
                String newEntry = model.formatProjectData(intakeCode,studentID,projectType,date,time,supervisor,secondMarker);
                String validation = newEntry;
                // Check if the StudentID already exists in the file
                if (model.studentIDExistsInFile(validation.split(",")[1])) { 
                    existingStudentIDs.add(studentID);
                    continue;
                }
                // Append to file
                model.addRowsToFile(newEntry);

                // Append to table in (model update)
                model.addProject(newEntry.split(","));

                // Update Lecturers role in userdata.txt
                model.updateLecturerRole(supervisor, secondMarker, null, null);
                createProjectView.dispose();
                
                projectsAdded++;
            }

            if (!existingStudentIDs.isEmpty()) {
                JOptionPane.showMessageDialog(createProjectView, String.join(",", existingStudentIDs) + " student(s) already exist. Please double check.");
            }

            JOptionPane.showMessageDialog(createProjectView, projectsAdded + " project(s) added from " + studentList.getSelectedValuesList().size() + " selected student(s).");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(createProjectView, "An error occurred in addProject(). Please try again.");
        }
    }

    private <T extends java.awt.Component> T getComponent(String name, Class<T> componentType) {
        java.awt.Component component = createProjectView.getComponentByName(name);

        if (component != null) {
            System.out.println("Component name: " + name + ", retrieved type: " + component.getClass().getName());
        } else {
            System.out.println("Component name: " + name + " not found.");
        }

        if (componentType.isInstance(component)) {
            return componentType.cast(component);
        } else {
            throw new IllegalArgumentException("Component is not of type " + componentType.getSimpleName());
        }
    }

    @SuppressWarnings("unchecked")
    private JList<String> getList(String name) {
        return getComponent(name, JList.class);
    }

    @SuppressWarnings("unchecked")
    private JComboBox<String> getComboBox(String name) {
        return getComponent(name, JComboBox.class);
    }
}

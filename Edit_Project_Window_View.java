package com.mycompany.oodj_assignment;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JTextField;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;

public class Edit_Project_Window_View extends Abstract_Project_Window_View {
    private JComboBox<String> comboBoxIntakeCode;
    private JComboBox<String> comboBoxProjectType;
    private JComboBox<String> comboBoxSupervisor;
    private JComboBox<String> comboBoxSecondMarker;
    private JTextField textSearchStudentID;
    private DatePicker datePicker;
    private TimePicker timePicker;

    // Just added
    private JList<String> studentList;
    private JTextField textStudentID;
    // private DefaultListModel<String> studentListModel;

    @SuppressWarnings("unchecked")
    public Edit_Project_Window_View(Abstract_Project_Window_Model model) {
        super("Edit Project", model);
        // this.studentListModel = listModel;

        // Initialize combo boxes and other fields
        comboBoxIntakeCode = (JComboBox<String>) getComponentByName("comboBoxIntakeCode");
        comboBoxProjectType = (JComboBox<String>) getComponentByName("comboBoxProjectType");
        comboBoxSupervisor = (JComboBox<String>) getComponentByName("comboBoxSupervisor");
        comboBoxSecondMarker = (JComboBox<String>) getComponentByName("comboBoxSecondMarker");
        studentList = (JList<String>) getComponentByName("studentList");
        textSearchStudentID = (JTextField) getComponentByName("textSearchStudentID");
        datePicker = (DatePicker) getComponentByName("datePicker");
        timePicker = (TimePicker) getComponentByName("timePicker");

        setupConfirmProjectButton();

        // Set up the Student ID text field to be non-editable
        textStudentID = new JTextField();
        textStudentID.setBounds(300, 85, 200, 25);
        textStudentID.setName("textStudentID");
        textStudentID = textSearchStudentID;
        // Make the text field non-editable
        textStudentID.setEditable(false);  
        add(textStudentID);
        
        setVisible(true);
    }

    @Override
    protected void setupConfirmProjectButton() {
        buttonConfirmProject.setText("Save");
    }

    public void setIntakeCode(String intakeCode) {
        comboBoxIntakeCode.setSelectedItem(intakeCode);
    }

    public void setStudentID(String studentID) {
        textSearchStudentID.setText(studentID);
    }

    public void setProjectType(String projectType) {
        comboBoxProjectType.setSelectedItem(projectType);
    }

    public void setDate(LocalDate date) {
        datePicker.setDate(date);
    }

    public void setTime(LocalTime time) {
        timePicker.setTime(time);
    }

    public void setSupervisor(String supervisor) {
        comboBoxSupervisor.setSelectedItem(supervisor);
    }

    public void setSecondMarker(String secondMarker) {
        comboBoxSecondMarker.setSelectedItem(secondMarker);
    }

    public JButton getButtonConfirmProject() {
        return buttonConfirmProject;
    }

    public String getIntakeCode() {
        return (String) comboBoxIntakeCode.getSelectedItem();
    }

    public String getStudentID() {
        return textStudentID.getText();
    }

    public String getProjectType() {
        return (String) comboBoxProjectType.getSelectedItem();
    }

    public LocalDate getDate() {
        return datePicker.getDate();
    }

    public LocalTime getTime() {
        return timePicker.getTime();
    }

    public LocalDateTime getSubmissionDateTime() {
        return LocalDateTime.of(datePicker.getDate(), timePicker.getTime());
    }

    public String getSupervisor() {
        return (String) comboBoxSupervisor.getSelectedItem();
    }

    public String getSecondMarker() {
        return (String) comboBoxSecondMarker.getSelectedItem();
    }
}

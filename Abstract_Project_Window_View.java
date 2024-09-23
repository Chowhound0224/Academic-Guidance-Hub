package com.mycompany.oodj_assignment;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;


public abstract class Abstract_Project_Window_View extends JFrame{
    // Create a model instance
    protected Abstract_Project_Window_Model abstractProjectModel;
    // 
    private final String[] PROJECT_TYPES = {"Internship", "Investigation Report", "RMCP", "Capstone Project - P1", "Capstone Project - P2", "Final Year Project"}; 

    // Create a DatePicker instance
    protected DatePicker datePicker;

    // Create a TimePicker instance
    protected TimePicker timePicker;

    // Create Confirm Project Button
    protected JButton buttonConfirmProject;
    // protected JLabel labelSearchStudentID;

    public Abstract_Project_Window_View(String title, Abstract_Project_Window_Model abstractProjectModel) {        
        /*
         * Design for Window
         */
        super(title);
        setSize(600, 700);
        setLayout(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set the model
        this.abstractProjectModel = abstractProjectModel;


        /*
        * Design for Labels
        */
        // Label for Intake Code
        JLabel labelIntakeCode = new JLabel("Intake Code:");
        labelIntakeCode.setBounds(20, 10, 100, 25);
        labelIntakeCode.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Label for StudentID
        JLabel labelStudentID = new JLabel("Student ID:");
        labelStudentID.setBounds(20, 70, 100, 25);
        labelStudentID.setFont(new Font("Arial", Font.PLAIN, 16));
        
        
        
        // Label for searchStudentID
        JLabel labelSearchStudentID = new JLabel("Search Student ID:");
        labelSearchStudentID.setBounds(295, 70, 150, 25);
        labelSearchStudentID.setFont(new Font("Arial", Font.PLAIN, 12));
        
        // Label for Project Type
        JLabel labelProjectType = new JLabel("Project Type:");
        labelProjectType.setBounds(20, 320, 100, 25);
        labelProjectType.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Label for Submission Date and Time
        JLabel labelSubmissionDateAndTime = new JLabel("Submission Date and Time:");
        labelSubmissionDateAndTime.setBounds(20, 380, 200, 25);
        labelSubmissionDateAndTime.setFont(new Font("Arial", Font.PLAIN, 16));
        
        // Label for Supervisor
        JLabel labelSupervisor = new JLabel("Supervisor:");
        labelSupervisor.setBounds(20, 490, 100, 25);
        labelSupervisor.setFont(new Font("Arial", Font.PLAIN, 16));

        // Label for Second Marker
        JLabel labelSecondMarker = new JLabel("Second Marker:");
        labelSecondMarker.setBounds(20, 550, 150, 25);
        labelSecondMarker.setFont(new Font("Arial", Font.PLAIN, 16));

        // Label for Supevisor's name
        JLabel labelSupervisorName = new JLabel(abstractProjectModel.getSupervisorsAndSecondMarkers().values().toArray()[0].toString());
        labelSupervisorName.setBounds(295, 510, 250, 25);
        labelSupervisorName.setFont(new Font("Arial", Font.PLAIN, 12));

        // Label for Second Marker's name
        JLabel labelSecondMarkerName = new JLabel(abstractProjectModel.getSupervisorsAndSecondMarkers().values().toArray()[0].toString());
        labelSecondMarkerName.setBounds(295, 570, 250, 25);
        labelSecondMarkerName.setFont(new Font("Arial", Font.PLAIN, 12));
        
        
        
        /*
        * Design for ComboBox
        */
        // ComboBox for Intake Code
        String[] intakeCodes = abstractProjectModel.getIntakeCodes();
        JComboBox<String> comboBoxIntakeCode = new JComboBox<String>(intakeCodes);
        comboBoxIntakeCode.setBounds(295, 10, 210, 25);
        comboBoxIntakeCode.setFont(new Font("Arial", Font.PLAIN, 16));
        comboBoxIntakeCode.setName("comboBoxIntakeCode");
        
        // ComboBox for Project Type
        JComboBox<String> comboBoxProjectType = new JComboBox<String>(PROJECT_TYPES);
        comboBoxProjectType.setBounds(295, 320, 210, 25);
        comboBoxProjectType.setFont(new Font("Arial", Font.PLAIN, 16));
        comboBoxProjectType.setName("comboBoxProjectType");
        
        // ComboBox for Supervisor
        String[] supervisors = abstractProjectModel.getSupervisorsAndSecondMarkers().keySet().toArray(new String[0]);
        JComboBox<String> comboBoxSupervisor = new JComboBox<String>(supervisors);
        comboBoxSupervisor.setBounds(295, 490, 210, 25);
        comboBoxSupervisor.setFont(new Font("Arial", Font.PLAIN, 16));
        comboBoxSupervisor.setName("comboBoxSupervisor");
        
        // ComboBox for Second Marker
        String[] secondMarkers = abstractProjectModel.getSupervisorsAndSecondMarkers().keySet().toArray(new String[0]);
        JComboBox<String> comboBoxSecondMarker = new JComboBox<String>(secondMarkers);
        comboBoxSecondMarker.setBounds(295, 550, 210, 25);
        comboBoxSecondMarker.setFont(new Font("Arial", Font.PLAIN, 16));
        comboBoxSecondMarker.setName("comboBoxSecondMarker");
        
        // Add display supervisor's name when comboBoxSupervisor selected
        comboBoxSupervisor.addActionListener(e -> {
            String selectedSupervisor = (String) comboBoxSupervisor.getSelectedItem();
            if (selectedSupervisor != null) {
                labelSupervisorName.setText(abstractProjectModel.getSupervisorsAndSecondMarkers().get(selectedSupervisor).toString());
            }
        });

        // Add display second marker's name when comboBoxSecondMarker selected
        comboBoxSecondMarker.addActionListener(e -> {
            String selectedSecondMarker = (String) comboBoxSecondMarker.getSelectedItem();
            if (selectedSecondMarker != null) {
                labelSecondMarkerName.setText(abstractProjectModel.getSupervisorsAndSecondMarkers().get(selectedSecondMarker).toString());
            }
        });

        // Design for Confirm Project button
        buttonConfirmProject = new JButton();
        buttonConfirmProject.setBounds(400, 600, 100, 25);
        buttonConfirmProject.setFont(new Font("Arial", Font.PLAIN, 16));
        
        
        /*
        * Design for DatePicker and TimePicker
        */
        // Create a DatePicker instance
        datePicker = new DatePicker();
        datePicker.setBounds(300, 380, 250, 30);
        datePicker.setFont(new Font("Arial", Font.PLAIN, 16));
        datePicker.setName("datePicker");
        
        // Create a TimePicker instance
        timePicker = new TimePicker();
        timePicker.setBounds(300, 415,247, 30);
        timePicker.setFont(new Font("Arial", Font.PLAIN, 16));
        timePicker.setName("timePicker");
        
        
        /*
        * Design for TextFields
        */
        // Create the search bar for the student ID
        JTextField textSearchStudentID = new JTextField();
        textSearchStudentID.setBounds(300, 95, 200, 25);
        textSearchStudentID.setName("textSearchStudentID");
        
        
        add(labelIntakeCode);
        add(labelStudentID);
        add(labelSearchStudentID);
        add(labelProjectType);
        add(labelSubmissionDateAndTime);
        add(labelSupervisor);
        add(labelSupervisorName);
        add(labelSecondMarker);
        add(labelSecondMarkerName);
        add(comboBoxIntakeCode);
        add(comboBoxProjectType);
        add(comboBoxSupervisor);
        add(comboBoxSecondMarker);
        // add(scrollPane);
        // add(buttonSelectAll);
        add(buttonConfirmProject);
        add(textSearchStudentID);
        add(datePicker);
        add(timePicker);

        setVisible(true);
    }

    protected abstract void setupConfirmProjectButton();
    // protected abstract void setupStudentIDLabel();


    // ### New Code
    public Component getComponentByName(String name) {
        // Debugging statement to ensure method is being called
        System.out.println("Searching for component with name: " + name);

        return getComponentRecursive(getContentPane(), name);
    }

    // Recursive method to search for a component by name in a container and its children
    // ***Solution -> Fetched component: null for name: (component name)
    // ***Problem -> (component) might have been nested within another container, and the original method didn't traverse through all nested components to find it.
    private Component getComponentRecursive(Container container, String name) {
        for (Component component : container.getComponents()) {
            if (name.equals(component.getName())) {
                return component;
            }
            if (component instanceof Container) {
                Component nestedComponent = getComponentRecursive((Container) component, name);
                if (nestedComponent != null) {
                    return nestedComponent;
                }
            }
        }
        return null;
    }
}


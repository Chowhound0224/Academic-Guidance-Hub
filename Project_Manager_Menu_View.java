package com.mycompany.oodj_assignment;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class Project_Manager_Menu_View extends JFrame {
    private Project_Manager_Model model;
    private String[][] dataManageProject;
    private String[][] dataViewProjectStatus;

    private Create_Project_Window_View createProjectView;
    private JTabbedPane tabbedPane;

    private JButton buttonCreate;
    private JButton buttonEdit;
    private JButton buttonDelete;
    private JLabel labelListExistingProjectsBy = new JLabel("List Existing Projects by");
    private JLabel labelSearchStudentID = new JLabel("Search Student ID:");
    private String[] sortingOptions = {"All", "Internship", "Investigation Report", "RMCP", "Capstone project - P1", "Capstone project - P2", "FYP"};
    private String[] tableColumnNames_MP = {"IntakeCode", "StudentID", "Project Type", "Submission Status", "Submission Date", "Supervisor", "Second Marker"};
    private int[] tableColumnWidths_MP = {150, 150, 150, 150, 200, 150, 150};
    private String[] tableColumnNames_VPS = {"StudentID", "Project Type", "Submission Status"};
    private int[] tableColumnWidths_VPS = {150, 150, 150};

    private JButton Back_Button1, Back_Button2;
    
    private JTable tableManageProject;
    private JTable tableViewProjectStatus;

    private JPanel Change_Password;

    public Project_Manager_Menu_View(Project_Manager_Model model) {
        this.model = model;
        fetchData();
        createAndShowGUI();
    }

    private void fetchData() {
        try {
            this.dataManageProject = model.getDataManageProject();
            this.dataViewProjectStatus = model.getDataViewProjectStatus();
            this.tableManageProject = createTable(dataManageProject, tableColumnNames_MP, false);
            this.tableViewProjectStatus = createTable(dataViewProjectStatus, tableColumnNames_VPS, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createAndShowGUI() {
        // ###################### Design for "Manage Project" tab ######################
        JLabel labelListExistingProjectsBy_MP = createLabelListExistingProjectsBy(labelListExistingProjectsBy.getText(), 60, 10, 170, 25, new Font("Arial", Font.PLAIN, 16));
        // JComboBox<String> comboBoxListExistingProjectsBy_MP = createComboBox(185, 12, 210, 25, new Font("Arial", Font.PLAIN, 16));
        JComboBox<String> comboBoxListExistingProjectsBy_MP = createComboBox(tableManageProject, dataManageProject, 240, 12, 210, 25, 2);
        JLabel labelSearchStudentID_MP = createLabelListExistingProjectsBy(labelSearchStudentID.getText(), 840, 10, 110, 25, new Font("Arial", Font.PLAIN, 12));

        setTableProperties(tableManageProject, true, false, false, false, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setTableFonts(tableManageProject, new Font("Arial", Font.BOLD, 18), new Font("Arial", Font.PLAIN, 16));
        setColumnHeightAndWidths(tableManageProject, 20, tableColumnWidths_MP);

        JScrollPane scrollPane_MP = new JScrollPane(tableManageProject);
        scrollPane_MP.setBounds(60, 40, 1000, 400);

        buttonCreate = new JButton("Create");
        buttonCreate.setBounds(60, 480, 100, 25);
        buttonCreate.setFont(new Font("Arial", Font.PLAIN, 16));

        buttonEdit = new JButton("Edit");
        buttonEdit.setBounds(180, 480, 100, 25);
        buttonEdit.setFont(new Font("Arial", Font.PLAIN, 16));

        buttonDelete = new JButton("Delete");
        buttonDelete.setBounds(950, 480, 100, 25);
        buttonDelete.setFont(new Font("Arial", Font.PLAIN, 16));

        JTextField textFieldSearchStudentID_MP = createTextFieldSearch(tableManageProject, dataManageProject, 950, 10, 90, 25, 1);

        JPanel panelManageProject = new JPanel();
        panelManageProject.setLayout(null);

        Back_Button1 = new JButton("◄");
        Back_Button1.setBounds(10, 10, 45, 25);

        panelManageProject.add(labelListExistingProjectsBy_MP);
        panelManageProject.add(comboBoxListExistingProjectsBy_MP);
        panelManageProject.add(scrollPane_MP);
        panelManageProject.add(buttonCreate);
        panelManageProject.add(buttonEdit);
        panelManageProject.add(buttonDelete);
        panelManageProject.add(textFieldSearchStudentID_MP);
        panelManageProject.add(labelSearchStudentID_MP);
        panelManageProject.add(Back_Button1);

        // ###################### Design for "View Project Status" tab ######################
        JLabel labelListExistingProjectsBy_VPS = createLabelListExistingProjectsBy(labelListExistingProjectsBy.getText(), 60, 10, 170, 25, new Font("Arial", Font.PLAIN, 16));
        // JComboBox<String> comboBoxListExistingProjectsBy_VPS = createComboBox(185, 12, 210, 25, new Font("Arial", Font.PLAIN, 16));
        JComboBox<String> comboBoxListExistingProjectsBy_VPS = createComboBox(tableViewProjectStatus, dataViewProjectStatus, 240, 12, 210, 25, 1);
        JLabel labelSearchStudentID_VPS = createLabelSearchStudentID(labelSearchStudentID.getText(), 60, 55, 110, 25, new Font("Arial", Font.PLAIN, 12));
        JTextField textFieldSearchStudentID_VPS = createTextFieldSearch(tableViewProjectStatus, dataViewProjectStatus, 170, 55, 90, 25, 0);

        setTableProperties(tableViewProjectStatus, true, false, false, false, ListSelectionModel.SINGLE_SELECTION);
        setTableFonts(tableViewProjectStatus, new Font("Arial", Font.BOLD, 18), new Font("Arial", Font.PLAIN, 16));
        setColumnHeightAndWidths(tableViewProjectStatus, 20, tableColumnWidths_VPS);

        JScrollPane scrollPane_VPS = new JScrollPane(tableViewProjectStatus);
        scrollPane_VPS.setBounds(60, 80, 1000, 400);

        Back_Button2 = new JButton("◄");
        Back_Button2.setBounds(10, 10, 45, 25);

        JPanel panelViewProjectStatus = new JPanel();
        panelViewProjectStatus.setLayout(null);
        panelViewProjectStatus.add(labelListExistingProjectsBy_VPS);
        panelViewProjectStatus.add(comboBoxListExistingProjectsBy_VPS);
        panelViewProjectStatus.add(labelSearchStudentID_VPS);
        panelViewProjectStatus.add(textFieldSearchStudentID_VPS);
        panelViewProjectStatus.add(scrollPane_VPS);
        panelViewProjectStatus.add(Back_Button2);

        // ###################### Design for the "window" ######################
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Manage Project", panelManageProject);
        tabbedPane.addTab("View Project Status", panelViewProjectStatus);
        tabbedPane.addTab("Change Password", Change_Password);
        add(tabbedPane);
        setSize(1500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Project Manager Window");
        setVisible(true);
    }

    private JLabel createLabelListExistingProjectsBy(String text, int x, int y, int width, int height, Font font) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        return label;
    }

    private JLabel createLabelSearchStudentID(String text, int x, int y, int width, int height, Font font) {
        JLabel label = new JLabel(text);
        label.setBounds(x, y, width, height);
        label.setFont(font);
        return label;
    }

    private JComboBox<String> createComboBox(JTable table, String[][] data, int x, int y, int width, int height, int comboBoxColumnIndex) {
        JComboBox<String> comboBox = new JComboBox<>(sortingOptions);
        comboBox.setBounds(x, y, width, height);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateListByComboBox(table, data, comboBox, comboBoxColumnIndex);
            }
        });
        return comboBox;
    }

    private JTextField createTextFieldSearch(JTable table, String[][] data, int x, int y, int width, int height, int searchColumnIndex) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, width, height);
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateList(table, data, textField, searchColumnIndex);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateList(table, data, textField, searchColumnIndex);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateList(table, data, textField, searchColumnIndex);
            }
        });
        return textField;
    }

    private void updateList(JTable table, String[][] data, JTextField textField, int searchColumnIndex) {
        String searchText = textField.getText();
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        int studentIdColumn = searchColumnIndex;

        model.setRowCount(0);

        for (String[] row : data) {
            String studentId = row[studentIdColumn];

            if (studentId.contains(searchText)) {
                model.addRow(row);
            }
        }
    }

    private void updateListByComboBox(JTable table, String[][] data, JComboBox comboBox, int searchColumnIndex) {
        String sortProjectType = comboBox.getSelectedItem().toString();

        DefaultTableModel model = (DefaultTableModel) table.getModel();
        int rowCount = model.getRowCount();
        int projectTypeColumn = searchColumnIndex;

        model.setRowCount(0);

        for (String[] row : data) {
            String projectType = row[projectTypeColumn];

            if (sortProjectType.equals("All") || projectType.contains(sortProjectType)) {
                model.addRow(row);
            }
        }
    }

    public void setTableProperties(JTable table, boolean rowSelectionAllowed, boolean columnSelectionAllowed, boolean reorderingAllowed, boolean resizingAllowed, int selectionMode) {
        table.setName("Manage Project");
        table.setRowSelectionAllowed(rowSelectionAllowed);
        table.setColumnSelectionAllowed(columnSelectionAllowed);
        table.setSelectionMode(selectionMode);
        table.getTableHeader().setReorderingAllowed(reorderingAllowed);
        table.getTableHeader().setResizingAllowed(resizingAllowed);
    }

    public DefaultTableModel createModel(String[][] data, String[] tableColumnNames, boolean cellsEditable) {
        return new DefaultTableModel(data, tableColumnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return cellsEditable;
            }
        };
    }

    public void setTableFonts(JTable table, Font headerFont, Font tableFont) {
        table.getTableHeader().setFont(headerFont);
        table.setFont(tableFont);
    }

    public void setColumnHeightAndWidths(JTable table, int rowHeight, int[] tableColumnWidths) {
        TableColumnModel columnModel = table.getColumnModel();
        table.setRowHeight(rowHeight);
        for (int i = 0; i < tableColumnWidths.length; i++) {
            if (i < columnModel.getColumnCount()) {
                columnModel.getColumn(i).setPreferredWidth(tableColumnWidths[i]);
            }
        }
    }

    public JTable createTable(String[][] data, String[] tableColumnNames, boolean cellsEditable) {
        DefaultTableModel model = createModel(data, tableColumnNames, cellsEditable);
        return new JTable(model);
    }

    public JButton getCreateButton() {
        return buttonCreate;
    }

    public JButton getEditButton() {
        return buttonEdit;
    }

    public JButton getDeleteButton() {
        return buttonDelete;
    }

    public JTable getTableManageProject() {
        return tableManageProject;
    }

    public JTable getTableViewProjectStatus() {
        return tableViewProjectStatus;
    }

    public JButton getBackButton1() {
        return Back_Button1;
    }
    
    public JButton getBackButton2() {
        return Back_Button2;
    }
    
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public void updateTable() {
        try {
            // Update data for "Manage Project" table
            this.dataManageProject = model.getDataManageProject();
            DefaultTableModel manageProjectTableModel = (DefaultTableModel) tableManageProject.getModel();
            manageProjectTableModel.setDataVector(dataManageProject, tableColumnNames_MP);
    
            // Update data for "View Project Status" table
            this.dataViewProjectStatus = model.getDataViewProjectStatus();
            DefaultTableModel viewProjectStatusTableModel = (DefaultTableModel) tableViewProjectStatus.getModel();
            viewProjectStatusTableModel.setDataVector(dataViewProjectStatus, tableColumnNames_VPS);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating the table: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}

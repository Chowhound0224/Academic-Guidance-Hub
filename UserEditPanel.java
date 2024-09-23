package com.mycompany.oodj_assignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import com.mycompany.oodj_assignment.ui.Login_Window_UI;

public class UserEditPanel extends JPanel implements ActionListener, LogoutCallback {
    private JTable studentTable, lecturerTable, adminTable;
    private DefaultTableModel studentModel, lecturerModel, adminModel;
    private List<UserModel> students = new ArrayList<>();
    private List<UserModel> lecturers = new ArrayList<>();
    private List<UserModel> admins = new ArrayList<>();
    private JButton editButton;
    private JButton Back_Button;
    private JComboBox<String> tableComboBox;
    private JScrollPane studentScrollPane, lecturerScrollPane, adminScrollPane;
    private LogoutCallback logoutCallback;

    public UserEditPanel(LogoutCallback callback) {
        this.logoutCallback = callback;
        setLayout(null);
        setupTables();
        loadUserData();
        setupEditButton();
        setupBackButton();
        setupComboBox();
    }

    private void setupTables() {
        studentModel = new DefaultTableModel(new Object[]{"ID", "Password", "Name","Intake Code", "Status", "Role"}, 0);
        studentTable = new JTable(studentModel);
        studentScrollPane = new JScrollPane(studentTable);
        studentScrollPane.setBounds(60, 50, 800, 500);
        add(studentScrollPane);

        lecturerModel = new DefaultTableModel(new Object[]{"ID", "Password","Name", "School", "Status", "Role"}, 0);
        lecturerTable = new JTable(lecturerModel);
        lecturerScrollPane = new JScrollPane(lecturerTable);
        lecturerScrollPane.setBounds(60, 50, 800, 500);
        add(lecturerScrollPane);

        adminModel = new DefaultTableModel(new Object[]{"ID", "Password","Name", "Status", "Role"}, 0);
        adminTable = new JTable(adminModel);
        adminScrollPane = new JScrollPane(adminTable);
        adminScrollPane.setBounds(60, 50, 800, 500);
        add(adminScrollPane);

        showTable("Admin");
    }

    public void loadUserData() {
        students.clear();
        lecturers.clear();
        admins.clear();
        adminModel.setRowCount(0);
        studentModel.setRowCount(0);
        lecturerModel.setRowCount(0);

        try (BufferedReader reader = new BufferedReader(new FileReader("userdata.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length != 7) { 
                    System.err.println("Invalid data format: " + line);
                    continue;
                }
                UserModel user = new UserModel();
                user.setId(data[0]);
                user.setPassword(data[1]);
                user.setName(data[2]);
                user.setRole(data[4]);
                user.setStatus(data[5]);

                switch (user.getRole()) {
                    case "Admin":
                        admins.add(user);
                        adminModel.addRow(new Object[]{user.getId(), user.getPassword(),
                            user.getName(), user.getStatus(), user.getRole()});
                        break;
                    case "Student":
                        user.setIntakeCode(data[3]);
                        students.add(user);
                        studentModel.addRow(new Object[]{user.getId(), user.getPassword(),
                            user.getName(), user.getIntakeCode(), user.getStatus(), user.getRole()});
                        break;
                    default:
                        user.setSchoolWise(data[3]);
                        lecturers.add(user);
                        lecturerModel.addRow(new Object[]{user.getId(), user.getPassword(),
                            user.getName(), user.getSchoolWise(), user.getStatus(), user.getRole()});
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupEditButton() {
        editButton = new JButton("Edit");
        editButton.setBounds(700, 10, 100, 25);
        editButton.addActionListener(this);
        add(editButton);
    }
    private void setupBackButton() {
        Back_Button = new JButton("◄");
        Back_Button.setBounds(10, 10, 45, 25);
        Back_Button.addActionListener(this);
        add(Back_Button);
    }

    private void setupComboBox() {
        String[] tableList = {"Admin", "Student", "Lecturer"};
        tableComboBox = new JComboBox<>(tableList);
        tableComboBox.setBounds(600, 10, 100, 25);
        tableComboBox.addActionListener(this);
        add(tableComboBox);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == editButton) {
            handleEditAction();
        } else if (e.getSource() == tableComboBox) {
            String selectedTable = (String) tableComboBox.getSelectedItem();
            showTable(selectedTable);
        } else if (e.getSource() == Back_Button) {
            LogoutHandler.logout((JFrame) SwingUtilities.getWindowAncestor(this), this);
        }
    }

    private void handleEditAction() {
        JTable selectedTable = getSelectedTable();
        int selectedRow = selectedTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
            return;
        }
        UserModel user = getUserFromTable(selectedTable, selectedRow);
        if (user != null) {
            new EditPanel(user, this).setVisible(true);
        }
    }

    private JTable getSelectedTable() {
        String selectedTable = (String) tableComboBox.getSelectedItem();
        switch (selectedTable) {
            case "Student":
                return studentTable;
            case "Lecturer":
                return lecturerTable;
            case "Admin":
                return adminTable;
            default:
                return null;
        }
    }

    private void showTable(String userType) {
        studentScrollPane.setVisible("Student".equals(userType));
        lecturerScrollPane.setVisible("Lecturer".equals(userType));
        adminScrollPane.setVisible("Admin".equals(userType));
    }

    private UserModel getUserFromTable(JTable table, int row) {
        if (table == studentTable) {
            return students.get(row);
        } else if (table == lecturerTable) {
            return lecturers.get(row);
        } else if (table == adminTable) {
            return admins.get(row);
        } else {
            return null;
        }
    }
    
    public void onLogout() {
        setVisible(false);
        Login_Window_UI LW_UI = new Login_Window_UI();
        Login_Window LW = new Login_Window(LW_UI);
    }
}



package com.mycompany.oodj_assignment;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import com.mycompany.oodj_assignment.ui.Login_Window_UI;

public class ManagePresentationRequests extends JPanel implements ActionListener, LogoutCallback,DataLoader{

    private JButton Back_Button;
    private String username;
    private LogoutCallback logoutCallback;
    private JTable RequestsTable;
    private DefaultTableModel tableModel;

    public ManagePresentationRequests(String username, LogoutCallback callback) {
        this.username = username;
        this.logoutCallback = callback;
        setLayout(null);

        Back_Button = new JButton("◄");
        Back_Button.setBounds(10, 10, 45, 25);
        Back_Button.addActionListener(this);
        add(Back_Button);

        String[] columnNames = {"StudentID", "Student Name", "Intake Code",
            "ProjectID","LecturerID", "Date", "Start Time", "End Time", "Manage"};
        tableModel = new DefaultTableModel(columnNames, 0);
        RequestsTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8;
            }
        };

        RequestsTable.getColumn("Manage").setCellRenderer(new ButtonRenderer());
        RequestsTable.getColumn("Manage").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(RequestsTable);
        scrollPane.setBounds(100, 40, 900, 400);
        add(scrollPane);
        setColumnWidth(8, 150);
        loadData();
    }

    private void setColumnWidth(int columnIndex, int width) {
        TableColumn column = RequestsTable.getColumnModel().getColumn(columnIndex);
        column.setPreferredWidth(width);
    }

    private void loadData() {
        Map<String, String> studentNames = loadMap("userdata.txt");
        List<String[]> timetableEntries = loadTimetableEntries(studentNames);

        for (String[] entry : timetableEntries) {
            tableModel.addRow(entry);
        }
    }

    public void reloadData() {
        tableModel.setRowCount(0);
        loadData();
    }


    private List<String[]> loadTimetableEntries(Map<String, String> studentNames) {
        List<String[]> entries = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Timetable.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 6 && parts[3].equals(username) && (parts[7].equals("pending") || parts[7].equals("accepted"))) {
                    String studentName = studentNames.getOrDefault(parts[0], "Unknown");
                    entries.add(new String[]{parts[0], studentName, parts[1], parts[2],
                        parts[3], parts[4], parts[5], parts[6], parts[7]});
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading timetable. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return entries;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Back_Button) {
            LogoutHandler.logout((JFrame) SwingUtilities.getWindowAncestor(this), this);
        }
    }

    @Override
    public Map<String, String> loadMap(String courseFilePath) {
    Map<String, String> Map = new HashMap<>();
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

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton acceptButton = new JButton("      ✔     ️");
        private JButton rejectButton = new JButton("     ❌      ");
        private JButton manageButton = new JButton("  Manage ");
        private JButton cancelButton = new JButton(" Cancel  ");

        public ButtonRenderer() {
            setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            add(Box.createHorizontalGlue());

            add(acceptButton);
            add(rejectButton);
            add(manageButton);
            add(cancelButton);

            acceptButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            rejectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            manageButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            manageButton.setVisible(false);
            cancelButton.setVisible(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String status = (String) table.getModel().getValueAt(row, 8);
            if (status.equals("pending")) {
                acceptButton.setVisible(true);
                rejectButton.setVisible(true);
                manageButton.setVisible(false);
                cancelButton.setVisible(false);
            } else if (status.equals("accepted")) {
                acceptButton.setVisible(false);
                rejectButton.setVisible(false);
                manageButton.setVisible(true);
                cancelButton.setVisible(true);
            }
            return this;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private JPanel panel = new JPanel();
        private JButton acceptButton = new JButton("      ✔     ️");
        private JButton rejectButton = new JButton("     ❌      ");
        private JButton manageButton = new JButton("  Manage ");
        private JButton cancelButton = new JButton(" Cancel  ");
        private int row;

        public ButtonEditor(JCheckBox checkBox) {
            panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
            panel.add(Box.createHorizontalGlue());
            panel.add(acceptButton);
            panel.add(rejectButton);
            panel.add(manageButton);
            panel.add(cancelButton);

            acceptButton.setActionCommand("ACCEPT");
            rejectButton.setActionCommand("REJECT");
            manageButton.setActionCommand("MANAGE");
            cancelButton.setActionCommand("CANCEL");

            acceptButton.addActionListener(this);
            rejectButton.addActionListener(this);
            manageButton.addActionListener(this);
            cancelButton.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            String status = (String) table.getValueAt(row, 8);

            if (status.equals("pending")) {
                acceptButton.setVisible(true);
                rejectButton.setVisible(true);
                manageButton.setVisible(false);
                cancelButton.setVisible(false);
            } else if (status.equals("accepted")) {
                acceptButton.setVisible(false);
                rejectButton.setVisible(false);
                manageButton.setVisible(true);
                cancelButton.setVisible(true);
            }

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String action = e.getActionCommand();
            if ("ACCEPT".equals(action) || "REJECT".equals(action)) {
                int confirmAcceptReject = JOptionPane.showConfirmDialog(panel,
                        "Are you sure you want to " + (action.equals("ACCEPT") ? "ACCEPT" : "REJECT") + " this Presentation Request?",
                        (action.equals("ACCEPT") ? "Accept" : "Reject") + " Confirmation",
                        JOptionPane.YES_NO_OPTION);

                if (confirmAcceptReject == JOptionPane.YES_OPTION) {
                    String studentID = (String) tableModel.getValueAt(row, 0);
                    String intakeCode = (String) tableModel.getValueAt(row, 2);
                    String date = (String) tableModel.getValueAt(row, 5);
                    String startTime = (String) tableModel.getValueAt(row, 6);
                    String status = action.equals("ACCEPT") ? "accepted" : "rejected";
                    updateTimetableFile(studentID, intakeCode, date, startTime, status);

                    reloadData();
                    fireEditingStopped();
                } else {
                    fireEditingCanceled();
                }
            } else if ("MANAGE".equals(action)) {
                String studentID = (String) tableModel.getValueAt(row, 0);
                String intakeCode = (String) tableModel.getValueAt(row, 2);
                String date = (String) tableModel.getValueAt(row, 5);
                String startTime = (String) tableModel.getValueAt(row, 6);
                String endTime = (String) tableModel.getValueAt(row, 7);
                ShowDialog(studentID,intakeCode,date,startTime,endTime);
            } else if ("CANCEL".equals(action)) {
                int confirmCancel = JOptionPane.showConfirmDialog(panel, "Do you want to CANCEL this presentation?",
                        "Cancel Presentation", JOptionPane.YES_NO_OPTION);
                if (confirmCancel == JOptionPane.YES_OPTION) {
                    String studentID = (String) tableModel.getValueAt(row, 0);
                    String intakeCode = (String) tableModel.getValueAt(row, 2);
                    String date = (String) tableModel.getValueAt(row, 5);
                    String startTime = (String) tableModel.getValueAt(row, 6);
                    String status = "cancelled";
                    updateTimetableFile(studentID, intakeCode, date, startTime, status);
                    reloadData();
                    fireEditingStopped();
                } else {
                    fireEditingCanceled();
                }
            }
            reloadData();
        }
        
        private void updateTimetableFile(String studentID, String intakeCode, String date, String startTime, String status) {
            File inputFile = new File("Timetable.txt");
            File tempFile = new File("TempTimetable.txt");

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length > 6 && parts[0].equals(studentID) && parts[1].equals(intakeCode)
                            && parts[4].equals(date) && parts[5].equals(startTime)) {
                        parts[7] = status;
                        line = String.join(",", parts);
                    }
                    writer.write(line);
                    writer.newLine();
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(panel, "Error updating timetable. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
                JOptionPane.showMessageDialog(panel, "Error updating timetable file. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void ShowDialog(String StudentID, String IntakeCode, String beginDate, String beginTime, String endTime) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Manage Presentation", true);
        ManagePresentation managePresentation = new ManagePresentation(StudentID, IntakeCode, beginDate, beginTime, endTime);
        dialog.add(managePresentation);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    public void onLogout() {
        setVisible(false);
        Login_Window_UI LW_UI = new Login_Window_UI();
        Login_Window LW = new Login_Window(LW_UI);
    }
}

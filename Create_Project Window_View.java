package com.mycompany.oodj_assignment;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Create_Project_Window_View extends Abstract_Project_Window_View {

    private DefaultListModel<String> listModel;
    private JList<String> studentList;
    private JTextField textSearchStudentID;
    private JComboBox<String> comboBoxIntakeCode;

    @SuppressWarnings("unchecked")
    public Create_Project_Window_View(Abstract_Project_Window_Model model) {
        super("Add New Project", model);
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setVisible(true);
        setupConfirmProjectButton();

        // Label for studentIDSelectionTips
        JLabel labelStudentIDSelectionTips = new JLabel("Select multiple students by holding Ctrl/Cmd or Shift");
        labelStudentIDSelectionTips.setBounds(303, 255, 300, 25);
        labelStudentIDSelectionTips.setFont(new Font("Arial", Font.PLAIN, 12));

        // List and ScrollPane
        listModel = new DefaultListModel<>();
        studentList = new JList<>(listModel);
        studentList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        studentList.setName("studentList");

        JScrollPane scrollPane = new JScrollPane(studentList);
        scrollPane.setBounds(303, 145, 200, 110);

        // Select All Button
        JButton buttonSelectAll = new JButton("Select All");
        buttonSelectAll.setBounds(295, 120, 100, 25);
        buttonSelectAll.addActionListener(e -> studentList.setSelectionInterval(0, listModel.getSize() - 1));

        // Get existing text field and combo box
        textSearchStudentID = (JTextField) getComponentByName("textSearchStudentID");
        comboBoxIntakeCode = (JComboBox<String>) getComponentByName("comboBoxIntakeCode");

        if (textSearchStudentID == null || comboBoxIntakeCode == null) {
            JOptionPane.showMessageDialog(this, "Required components not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Add listeners to text field and combo box
        textSearchStudentID.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateList();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateList();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateList();
            }
        });

        comboBoxIntakeCode.addActionListener(e -> updateList());

        add(labelStudentIDSelectionTips);
        add(scrollPane);
        add(buttonSelectAll);
    }

    @Override
    protected void setupConfirmProjectButton() {
        buttonConfirmProject.setText("Add");
    }

    public JButton getButtonConfirmProject() {
        return buttonConfirmProject;
    }

    private void updateList() {
        String searchText = textSearchStudentID.getText().trim();
        String selectedIntakeCode = comboBoxIntakeCode.getSelectedItem() != null ? comboBoxIntakeCode.getSelectedItem().toString().trim() : "";

        SwingUtilities.invokeLater(() -> {
            listModel.clear();
            try (BufferedReader reader = new BufferedReader(new FileReader("userdata.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 6) {
                        // Skip invalid lines
                        continue;
                    }

                    String studentID = parts[0].trim();
                    String intakeCode = parts[3].trim();
                    String role = parts[4].trim();
                    String status = parts[5].trim();

                    if (role.toLowerCase().contains("student") && "ACTIVE".equalsIgnoreCase(status)) {
                        boolean matchesSearch = studentID.contains(searchText);
                        boolean matchesIntake = selectedIntakeCode.isEmpty() || intakeCode.equals(selectedIntakeCode);

                        if (matchesSearch && matchesIntake) {
                            listModel.addElement(studentID);
                        }
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error in updateList method");
                e.printStackTrace();
            }
        });
    }

    public java.awt.Component getComponentByName(String name) {
        return findComponentByName(getContentPane(), name);
    }

    private java.awt.Component findComponentByName(java.awt.Container container, String name) {
        for (java.awt.Component comp : container.getComponents()) {
            if (name.equals(comp.getName())) {
                return comp;
            } else if (comp instanceof java.awt.Container) {
                java.awt.Component result = findComponentByName((java.awt.Container) comp, name);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}

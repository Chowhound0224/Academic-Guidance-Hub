package com.mycompany.oodj_assignment;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class MarkingPanel extends JFrame implements ActionListener, DataLoader {
    private JButton saveButton;
    private JButton SelectFileButton;
    private String studentID;
    private String courseID;
    private String lecturerID;
    private boolean isSupervisor;
    private File selectedFile;
    
    private JLabel FileLabel;
    private JTextField FilePathField;
    private JLabel MarkLabel;
    private JTextField MarkField;
    
    private String PredefinedPath;

    public MarkingPanel(String studentID, String courseID, String lecturerID, boolean isSupervisor) {
        this.studentID = studentID;
        this.courseID = courseID;
        this.lecturerID = lecturerID;
        this.isSupervisor = isSupervisor;
        
        String currentDirectory = System.getProperty("user.dir");
        PredefinedPath = currentDirectory + File.separator + studentID + File.separator + courseID;

        setTitle("Marking Panel");
        setLayout(null);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        FileLabel = new JLabel("Select File:");
        FileLabel.setBounds(60,40,150,25);
        add(FileLabel);
        
        FilePathField = new JTextField();
        FilePathField.setBounds(240,40,300,25);
        FilePathField.setEditable(false);
        add(FilePathField);

        SelectFileButton = new JButton("🔽");
        SelectFileButton.setBounds(600,40,45,25);
        SelectFileButton.addActionListener(this);
        add(SelectFileButton);
        
        MarkLabel = new JLabel("Mark:");
        MarkLabel.setBounds(60,100,150,30);
        add(MarkLabel);
        
        MarkField = new JTextField();
        MarkField.setBounds(240,100,150,30);
        add(MarkField);

        saveButton = new JButton("Save");
        saveButton.setBounds(400,200,100,30);
        saveButton.addActionListener(this);
        add(saveButton);
        
        setVisible(true);
    }
  
    private void saveMarkingData() {
        String marks = MarkField.getText().trim();
        //Verification
        if (marks.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Marks cannot be empty.");
            return;
        }
        try {
            float mark = Float.parseFloat(marks);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Marks can only be numeric value.");
            return;
        }
        if (Float.parseFloat(marks) > 100 || Float.parseFloat(marks) < 0) {
            JOptionPane.showMessageDialog(this, "Marks must within the range of 0 to 100");
            return;
        }

        try {
            updateAssignmentFile(marks);
            JOptionPane.showMessageDialog(this, "Marking data saved successfully.");
            dispose();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving marking data.");
        }
    }

    private void updateAssignmentFile(String marks) throws IOException {
        Path filePath = Paths.get("StudentAssignment.txt");
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("The assignment file does not exist.");
        }
        java.util.List<String> lines = Files.readAllLines(filePath);
        boolean found = false;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] parts = line.split(",");
            if (parts[1].equals(studentID) && parts[2].equals(courseID)) {
                if (isSupervisor) {
                    parts[11] = marks;
                } else {
                    parts[12] = marks;
                }
                if (!parts[11].equals("-") && !parts[12].equals("-")) {
                    parts[10] = "2";
                } else if ((!parts[11].equals("-") && parts[12].equals("-")) ||
                        (parts[11].equals("-") && !parts[12].equals("-"))) {
                    parts[10] = "1";
                } else {
                    parts[10] = "0";
                }
                lines.set(i, String.join(",", parts));
                found = true;
                break;
            }
        }

        if (!found) {
            throw new IOException("The specified student and course record was not found.");
        }
        FileDataLoader.writeFile("StudentAssignment.txt", lines);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton) {
            int SaveOption = JOptionPane.showConfirmDialog(this, "Do you want to Save the marks",
                        "Save Confirmation", JOptionPane.YES_NO_OPTION);
            if (SaveOption == JOptionPane.YES_OPTION){
                saveMarkingData();
            }
        } else if (e.getSource() == SelectFileButton) {
            selectFile(PredefinedPath,(JComponent) this.getContentPane());
        }
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
            FilePathField.setText(selectedFile.getAbsolutePath());
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
}

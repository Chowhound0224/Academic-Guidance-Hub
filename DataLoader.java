package com.mycompany.oodj_assignment;

import java.io.File;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

public interface DataLoader {
    Map<String, String> loadMap(String courseFilePath);
    void loadAssignmentsFromFile(String filePath, String username, DefaultTableModel tableModel, Map<String, String> courseMap);
    
    void selectFile(String PredefinedPath, JComponent parentComponent);
    
    void openFile(File selectedFile, JComponent parentComponent);
}

package com.mycompany.oodj_assignment;

import java.util.Map;

import javax.swing.table.DefaultTableModel;
public interface PresentationSlotHandler {
    void showCoursePresentationList(String userID, DefaultTableModel tableModel,Map<String, String> courseMap);
    void showStatusPresentationList(String userID, DefaultTableModel tableModel,Map<String, String> courseMap);
    
}

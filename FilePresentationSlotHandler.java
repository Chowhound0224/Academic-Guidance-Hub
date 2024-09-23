package com.mycompany.oodj_assignment;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class FilePresentationSlotHandler implements PresentationSlotHandler {
    
    @Override
    public void showCoursePresentationList(String userID, DefaultTableModel tableModel, Map<String, String> courseMap) {
        String filePath = "StudentAssignment.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (userID.equals(columns[1]) && columns.length >= 3 && "Submitted".equals(columns[4])) {
                    String courseId = columns[2];
                    String courseName = columns[3];
                    Object[] rowData = {courseId, courseName, columns[8], columns[5]};
                    tableModel.addRow(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showStatusPresentationList(String userID, DefaultTableModel tableModel, Map<String, String> courseMap) {
        String filePath = "Timetable.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] columns = line.split(",");
                if (userID.equals(columns[0]) && columns.length >= 7) {
                    String courseId = columns[2];
                    String courseName =  courseMap.getOrDefault(courseId, "Unknown Course");
                    String lecturerID = columns[3];
                    String date = columns[4];
                    String beginTime = columns[5];
                    String endTime = columns[6];
                    String presentationStatus = columns[7];
                    Object[] rowData = {courseId, courseName, lecturerID, date, beginTime, endTime, presentationStatus};
                    tableModel.addRow(rowData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

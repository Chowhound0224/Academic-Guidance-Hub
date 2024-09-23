package com.mycompany.oodj_assignment;

public interface SubmissionManager {
    void submitAssignment(int selectedRow, String filePath);
    void removeSubmission(int selectedRow);
    void editSubmission(int selectedRow);
    void saveTableDataToFile(int selectedRow, String newStatus, String lastModified);
}

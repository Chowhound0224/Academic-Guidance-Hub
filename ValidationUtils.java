package com.mycompany.oodj_assignment;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.swing.JList;

public class ValidationUtils {

    public static String validateEmptyCreateProject(JList<String> studentList, LocalDate date, LocalTime time) {
        StringBuilder errorMessage = new StringBuilder();
        int errorCount = 1;

        // Check if studentList is empty
        if (studentList.isSelectionEmpty()) {
            errorMessage.append(errorCount++ + ". <StudentID> is not selected.\n");
        }

        // Check if the date is null
        if (date == null) {
            errorMessage.append(errorCount++ + ". <Date> is not selected.\n");
        }

        // Check if the time is null
        if (time == null) {
            errorMessage.append(errorCount++ + ". <Time> is not selected.\n");
        }

        return errorMessage.toString();
    }

    public static String validateEmptyEditProject(LocalDate date, LocalTime time) {
        StringBuilder errorMessage = new StringBuilder();
        int errorCount = 1;

        // Check if the date is null
        if (date == null) {
            errorMessage.append(errorCount++ + ". <Date> is not selected.\n");
        }

        // Check if the time is null
        if (time == null) {
            errorMessage.append(errorCount++ + ". <Time> is not selected.\n");
        }

        return errorMessage.toString();
    }

    public static String validateProjectDetails(LocalDate date, String supervisor, String secondMarker) {
        StringBuilder errorMessage = new StringBuilder();
        int errorCount = 1;

        // Check if the date is in the past
        if (date != null && date.isBefore(LocalDate.now())) {
            errorMessage.append(errorCount++ + ". <Date>: cannot be in the past.\n");
        }

        // Check if Supervisor and Second Marker are the same
        if(supervisor.equals(secondMarker)) {
            errorMessage.append(errorCount++ + ". <Supervisor> & <Second Marker>: cannot be the same person.\n");
        }

        return errorMessage.toString();
    }
}

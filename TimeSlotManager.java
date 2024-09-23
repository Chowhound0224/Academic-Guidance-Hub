package com.mycompany.oodj_assignment;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TimeSlotManager {
    private List<TimeSlot> requestedSlots;
    private List<String> availableSlots;
    private String lecturerID;
    private String selectedDate;

    public TimeSlotManager(String lecturerID, String selectedDate) {
        this.lecturerID = lecturerID;
        this.selectedDate = selectedDate;
        this.requestedSlots = new ArrayList<>();
        this.availableSlots = new ArrayList<>();
    }

    public void loadRequestedSlots() {
        try {
            List<String[]> lines = FileDataLoader.readFile("Timetable.txt");
            for (String[] columns : lines) {
                if (columns.length >= 8 && columns[3].equals(lecturerID) && columns[4].equals(selectedDate) && columns[7].equalsIgnoreCase("accepted")) {
                    String timeRange = columns[5] + "-" + columns[6];
                    requestedSlots.add(new TimeSlot(timeRange));
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public void loadAvailableSlots() {
        try {
            List<String[]> lines = FileDataLoader.readFile("AvailableSlot.txt");
            LocalDate selectedLocalDate = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            for (String[] columns : lines) {
                if (columns.length >= 5 && columns[0].equals(lecturerID)) {
                    LocalDate beginDate = LocalDate.parse(columns[1], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate endDate = LocalDate.parse(columns[3], DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String startTime = columns[2];
                    String endTime = columns[4];
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

                    if (!selectedLocalDate.isBefore(beginDate) && !selectedLocalDate.isAfter(endDate)) {
                        LocalTime start = LocalTime.parse(startTime, timeFormatter);
                        LocalTime end = LocalTime.parse(endTime, timeFormatter);

                        while (start.isBefore(end)) {
                            LocalTime slotEnd = start.plusMinutes(15);
                            String slot = start.format(timeFormatter) + "-" + slotEnd.format(timeFormatter);
                            if (!isOverlapping(slot)) {
                                availableSlots.add(slot);
                            }
                            start = slotEnd;
                        }
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error reading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private boolean isOverlapping(String slot) {
        TimeSlot newSlot = new TimeSlot(slot);
        for (TimeSlot requestedSlot : requestedSlots) {
            if (requestedSlot.isOverlapping(newSlot)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getAvailableSlots() {
        return availableSlots;
    }
}

package com.mycompany.oodj_assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TimeSlotSelectionPanel extends JPanel {
    private JComboBox<String> timeSlotComboBox;
    private TimeSlotManager timeSlotManager;
    private String lecturerID;
    private String courseID;
    private String username;
    private String selectedDate;

    public TimeSlotSelectionPanel(String lecturerID, String courseID, String username, String selectedDate, String duedate) {
        this.lecturerID = lecturerID;
        this.courseID = courseID;
        this.username = username;
        this.selectedDate = selectedDate;

        setLayout(new BorderLayout());
        timeSlotManager = new TimeSlotManager(lecturerID, selectedDate);

        timeSlotManager.loadRequestedSlots();
        timeSlotManager.loadAvailableSlots();
        List<String> availableSlots = timeSlotManager.getAvailableSlots();

        if (!availableSlots.isEmpty()) {
            
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy H:mm");
            LocalDateTime DueDate = LocalDateTime.parse(duedate, dateTimeFormatter);
            LocalDate dueDate = DueDate.toLocalDate();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate SelectedDate = LocalDate.parse(selectedDate, formatter);
            
            if (SelectedDate.isAfter(dueDate) && SelectedDate.isBefore(dueDate.plusDays(15))) {
                timeSlotComboBox = new JComboBox<>(availableSlots.toArray(new String[0]));
                add(new JLabel("Select a time slot:"), BorderLayout.NORTH);
                add(timeSlotComboBox, BorderLayout.CENTER);

                JButton selectButton = new JButton("Select");
                add(selectButton, BorderLayout.SOUTH);

                selectButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String selectedSlot = (String) timeSlotComboBox.getSelectedItem();
                        if (selectedSlot != null) {
                            try {
                                processSelectedSlot(selectedSlot);
                            } catch (IOException ex) {
                                Logger.getLogger(TimeSlotSelectionPanel.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                });
            } else {
                JOptionPane.showMessageDialog(this, "Please select the date within the Result Published Date",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No available time slots for the selected date", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public boolean hasAvailableSlots() {
        return !timeSlotManager.getAvailableSlots().isEmpty();
    }

    private void processSelectedSlot(String selectedSlot) throws FileNotFoundException, IOException {
        Path filePath = Paths.get("Timetable.txt");
        if (!Files.exists(filePath)) {
            throw new FileNotFoundException("The assignment file does not exist.");
        }
        java.util.List<String> lines = Files.readAllLines(filePath);
        
        User user = OODJ_Assignment.allUsers.stream()
            .filter(u -> u.getId().equals(username))
            .findFirst()
            .orElse(null);
        
        if (user != null) {
            String intakeCode = user.schoolwise;
            lines.add(username + "," + intakeCode + "," + courseID + "," + lecturerID + "," + selectedDate + ","
                + selectedSlot.split("-")[0] + "," + selectedSlot.split("-")[1] + ",pending");

            try {
                FileDataLoader.writeFile("Timetable.txt", lines);
                JOptionPane.showMessageDialog(this, "Time slot successfully requested.", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error writing to file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "User not found.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

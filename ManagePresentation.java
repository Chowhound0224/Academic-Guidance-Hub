package com.mycompany.oodj_assignment;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ManagePresentation extends JPanel implements ActionListener {

    private String StudentID,IntakeCode, Date, beginTime, endTime;
    private JLabel Date_Label;
    private JDateChooser Date_Chooser;
    private JLabel Begin_Time_Label;
    private JComboBox<String> Begin_Time_ComboBox;
    private JComboBox<String> Begin_Minute_ComboBox;
    private JLabel End_Time_Label;
    private JComboBox<String> End_Time_ComboBox;
    private JComboBox<String> End_Minute_ComboBox;
    private JButton Confirm_Button;

    public ManagePresentation(String studentID, String IntakeCode, String Date, String beginTime, String endTime) {
        this.StudentID = studentID;
        this.IntakeCode = IntakeCode;
        this.Date = Date;
        this.beginTime = beginTime;
        this.endTime = endTime;
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        Date_Label = new JLabel("Begin Date:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(Date_Label, gbc);

        Date_Chooser = new JDateChooser();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date initialDate = sdf.parse(Date);
            Date_Chooser.setDate(initialDate);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        JTextFieldDateEditor dateEditor = (JTextFieldDateEditor) Date_Chooser.getDateEditor();
        dateEditor.setDateFormatString("yyyy-MM-dd");
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(Date_Chooser, gbc);

        Begin_Time_Label = new JLabel("Begin Time:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(Begin_Time_Label, gbc);

        Begin_Time_ComboBox = new JComboBox<>(new String[]{"10", "11", "13", "14", "15", "16", "17"});
        Begin_Time_ComboBox.setSelectedItem(beginTime.split(":")[0]);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(Begin_Time_ComboBox, gbc);

        Begin_Minute_ComboBox = new JComboBox<>(new String[]{"00", "15", "30", "45"});
        Begin_Minute_ComboBox.setSelectedItem(beginTime.split(":")[1]);
        gbc.gridx = 2;
        gbc.gridy = 1;
        add(Begin_Minute_ComboBox, gbc);

        End_Time_Label = new JLabel("End Time:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(End_Time_Label, gbc);

        End_Time_ComboBox = new JComboBox<>(new String[]{"10", "11", "13", "14", "15", "16", "17"});
        End_Time_ComboBox.setSelectedItem(endTime.split(":")[0]);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(End_Time_ComboBox, gbc);

        End_Minute_ComboBox = new JComboBox<>(new String[]{"00", "15", "30", "45"});
        End_Minute_ComboBox.setSelectedItem(endTime.split(":")[1]);
        gbc.gridx = 2;
        gbc.gridy = 3;
        add(End_Minute_ComboBox, gbc);

        Confirm_Button = new JButton("Confirm");
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(Confirm_Button, gbc);

        Confirm_Button.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Confirm_Button) {
            int ConfirmOption = JOptionPane.showConfirmDialog(this, "Do you want to CHANGE the Time for this presentation?",
                        "Change Presentation Time", JOptionPane.YES_NO_OPTION);
            if (ConfirmOption == JOptionPane.YES_OPTION) {
                String newDate = new SimpleDateFormat("yyyy-MM-dd").format(Date_Chooser.getDate());
                String newStartTime = Begin_Time_ComboBox.getSelectedItem().toString() + ":" +
                        Begin_Minute_ComboBox.getSelectedItem().toString();
                String newEndTime = End_Time_ComboBox.getSelectedItem().toString() + ":" +
                        End_Minute_ComboBox.getSelectedItem().toString();
                
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(Date_Chooser.getDate());
                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
                    JOptionPane.showMessageDialog(this, "The selected date is a weekend."
                            + "Please choose a weekday.", "Invalid Date", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
                    Date startTime = timeFormat.parse(newStartTime);
                    Date endTime = timeFormat.parse(newEndTime);

                    if (startTime.before(endTime)) {
                        long duration = endTime.getTime() - startTime.getTime();
                        long durationInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                        if (durationInMinutes <= 60) {
                            updateTimetableFile(StudentID, IntakeCode, newDate, newStartTime, newEndTime);
                            ((Window) SwingUtilities.getWindowAncestor(this)).dispose();
                            
                        } else {
                            JOptionPane.showMessageDialog(this, "The duration between start and end"
                                    + "time cannot exceed 1 hour. Please correct the times.", "Invalid Duration", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Start time must be before end time. "
                                + "Please correct the times.", "Invalid Time", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(this, "Error parsing time. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void updateTimetableFile(String studentID, String intakeCode, String date, String startTime, String endTime) {
        File inputFile = new File("Timetable.txt");
        File tempFile = new File("TempTimetable.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 6 && parts[0].equals(studentID) && parts[1].equals(intakeCode) && parts[7].equals("accepted")) {
                    parts[4] = date;
                    parts[5] = startTime;
                    parts[6] = endTime;
                    line = String.join(",", parts);
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error updating timetable. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (!inputFile.delete() || !tempFile.renameTo(inputFile)) {
            JOptionPane.showMessageDialog(this, "Error updating timetable file. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

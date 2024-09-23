package com.mycompany.oodj_assignment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateSelector extends JPanel {
    private JPanel daysPanel;
    private JComboBox<String> monthComboBox;
    private JComboBox<Integer> yearComboBox;
    private static JLabel selectedDateLabel;
    private JButton[] dayButtons;

    public DateSelector() {
        setLayout(new BorderLayout());

        // Create the days panel
        daysPanel = new JPanel();
        daysPanel.setLayout(new GridLayout(5, 7)); // 5 rows, 7 columns

        // Add buttons for days 1-31
        dayButtons = new JButton[31];
        for (int day = 1; day <= 31; day++) {
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.addActionListener(new DayButtonListener());
            dayButtons[day - 1] = dayButton;
            daysPanel.add(dayButton);
        }

        // Create the month combo box
        String[] months = {
            "January", "February", "March", "April", "May", "June", 
            "July", "August", "September", "October", "November", "December"
        };
        monthComboBox = new JComboBox<>(months);
        monthComboBox.addActionListener(new MonthComboBoxListener());

        // Create the year combo box
        Integer[] years = new Integer[10];
        int startYear = 2024;
        for (int i = 0; i < years.length; i++) {
            years[i] = startYear + i;
        }
        yearComboBox = new JComboBox<>(years);
        yearComboBox.addActionListener(new YearComboBoxListener());

        // Panel for month and year selection
        JPanel monthYearPanel = new JPanel();
        monthYearPanel.add(monthComboBox);
        monthYearPanel.add(yearComboBox);

        // Label to display the selected date
        selectedDateLabel = new JLabel("Selected Date: ");
        selectedDateLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Add components to the panel
        add(monthYearPanel, BorderLayout.NORTH);
        add(daysPanel, BorderLayout.CENTER);
        add(selectedDateLabel, BorderLayout.SOUTH);

        // Initial visibility update
        updateDayButtonsVisibility((String) monthComboBox.getSelectedItem(), (Integer) yearComboBox.getSelectedItem());
    }

    // Action listener for day buttons
    public class DayButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String selectedDay = source.getText();
            String selectedMonth = (String) monthComboBox.getSelectedItem();
            Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
            selectedDateLabel.setText("Selected Date: " + selectedMonth + " " + selectedDay + ", " + selectedYear);
        }
    }

    public static String getSelectedDate() {
       String dateString = selectedDateLabel.getText();
    
        // Remove the "Selected Date: " prefix to get the actual date part
        String datePart = dateString.replace("Selected Date: ", "").trim();
    
        // Define the original date format
        SimpleDateFormat originalFormat = new SimpleDateFormat("MMMM d, yyyy");
        // Define the new date format
        SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
        String newDateString;
    
        try {
            // Parse the original date string into a Date object
            Date date = originalFormat.parse(datePart);
            // Format the Date object into the new format
            newDateString = newFormat.format(date);
            return newDateString;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Action listener for month combo box
    public class MonthComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<String> source = (JComboBox<String>) e.getSource();
            String selectedMonth = (String) source.getSelectedItem();
            Integer selectedYear = (Integer) yearComboBox.getSelectedItem();
            updateDayButtonsVisibility(selectedMonth, selectedYear);
        }
    }

    // Action listener for year combo box
    public class YearComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox<Integer> source = (JComboBox<Integer>) e.getSource();
            Integer selectedYear = (Integer) source.getSelectedItem();
            String selectedMonth = (String) monthComboBox.getSelectedItem();
            updateDayButtonsVisibility(selectedMonth, selectedYear);
        }
    }

    public void updateDayButtonsVisibility(String month, Integer year) {
        int monthIndex = monthComboBox.getSelectedIndex();
        int daysInSelectedMonth = DateUtils.getDaysInMonth(month, year);
        for (int i = 0; i < dayButtons.length; i++) {
            if (i < daysInSelectedMonth) {
                dayButtons[i].setVisible(true);
                dayButtons[i].setEnabled(DateUtils.isWeekday(year, monthIndex, i + 1));
            } else {
                dayButtons[i].setVisible(false);
            }
        }
    }
}

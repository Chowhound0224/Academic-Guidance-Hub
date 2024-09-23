package com.mycompany.oodj_assignment;

import org.jdesktop.swingx.JXMonthView;
import org.jdesktop.swingx.calendar.DateSelectionModel.SelectionMode;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.swing.table.TableCellRenderer;
import com.mycompany.oodj_assignment.ui.Login_Window_UI;

public class ManageAvailableSlot extends JPanel implements ActionListener, LogoutCallback {
    private JButton Back_Button;
    private LogoutCallback logoutCallback;

    private JLabel Begin_Date_Label;
    private JDateChooser Begin_Date_Chooser;
    private JLabel Begin_Time_Label;
    private JComboBox<String> Begin_Time_ComboBox;
    private JComboBox<String> Begin_Minute_ComboBox;
    private JLabel End_Date_Label;
    private JDateChooser End_Date_Chooser;
    private JLabel End_Time_Label;
    private JComboBox<String> End_Time_ComboBox;
    private JComboBox<String> End_Minute_ComboBox;
    private JButton Add_Slot_Button;
    private JXMonthView Month_View;
    private String username;

    private JTable slotsTable;
    private DefaultTableModel tableModel;
    
    private boolean isEditMode = false;
    private int editingRowIndex = -1;


    public ManageAvailableSlot(String username, LogoutCallback callback) {
        this.username = username;
        this.logoutCallback = callback;
        setLayout(null);

        Back_Button = new JButton("◄");
        Back_Button.setBounds(10, 10, 45, 25);
        Back_Button.addActionListener(this);

        Begin_Date_Label = new JLabel("Begin Date:");
        Begin_Date_Label.setBounds(60, 40, 165, 25);
        Begin_Date_Chooser = new JDateChooser();
        Begin_Date_Chooser.setBounds(180, 40, 165, 25);
        Begin_Time_Label = new JLabel("Begin Time:");
        Begin_Time_Label.setBounds(60, 80, 165, 25);
        Begin_Time_ComboBox = new JComboBox<>(new String[]{"10", "11", "13", "14", "15", "16", "17"});
        Begin_Time_ComboBox.setBounds(180, 80, 100, 25);

        Begin_Minute_ComboBox = new JComboBox<>(new String[]{"00", "15", "30", "45"});
        Begin_Minute_ComboBox.setBounds(290, 80, 75, 25);

        End_Date_Label = new JLabel("End Date:");
        End_Date_Label.setBounds(60, 120, 165, 25);
        End_Date_Chooser = new JDateChooser();
        End_Date_Chooser.setBounds(180, 120, 165, 25);
        End_Time_Label = new JLabel("End Time:");
        End_Time_Label.setBounds(60, 160, 165, 25);
        End_Time_ComboBox = new JComboBox<>(new String[]{"10", "11", "13", "14", "15", "16", "17"});
        End_Time_ComboBox.setBounds(180, 160, 100, 25);

        End_Minute_ComboBox = new JComboBox<>(new String[]{"00", "15", "30", "45"});
        End_Minute_ComboBox.setBounds(290, 160, 75, 25);

        Add_Slot_Button = new JButton("Add Slot");
        Add_Slot_Button.setBounds(180, 200, 100, 25);
        Add_Slot_Button.addActionListener(this);

        Month_View = new JXMonthView();
        Month_View.setBounds(60, 240, 300, 200);
        Month_View.setSelectionMode(SelectionMode.MULTIPLE_INTERVAL_SELECTION);
        Month_View.addActionListener(this);

        Border border = LineBorder.createGrayLineBorder();
        Month_View.setBorder(border);
        Month_View.setTraversable(true);

        Begin_Date_Chooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    updateMonthViewFromComboBox();
                }
            }
        });

        End_Date_Chooser.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    updateMonthViewFromComboBox();
                }
            }
        });

        add(Back_Button);
        add(Begin_Date_Label);
        add(Begin_Date_Chooser);
        add(Begin_Time_Label);
        add(Begin_Time_ComboBox);
        add(Begin_Minute_ComboBox);
        add(End_Date_Label);
        add(End_Date_Chooser);
        add(End_Time_Label);
        add(End_Time_ComboBox);
        add(End_Minute_ComboBox);
        add(Add_Slot_Button);
        add(Month_View);

        String[] columnNames = {"Username", "Begin Date", "Begin Time", "End Date", "End Time", "Settings"};
        tableModel = new DefaultTableModel(columnNames, 0);
        slotsTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        slotsTable.getColumn("Settings").setCellRenderer(new ButtonRenderer());
        slotsTable.getColumn("Settings").setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(slotsTable);
        scrollPane.setBounds(400, 40, 600, 400);
        add(scrollPane);

        loadTimetableSlots();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Back_Button) {
            LogoutHandler.logout((JFrame) SwingUtilities.getWindowAncestor(this), this);
        } else if (e.getSource() == Month_View) {
            Set<Date> selectedDates = Month_View.getSelection();
            if (selectedDates != null && !selectedDates.isEmpty()) {
                Date beginDate = selectedDates.iterator().next();
                Date endDate = selectedDates.stream().reduce((first, second) -> second).orElse(beginDate);
                Begin_Date_Chooser.setDate(beginDate);
                End_Date_Chooser.setDate(endDate);
            }   
        } else if (e.getSource() == Add_Slot_Button) {
            int Add_Slot_Option = JOptionPane.showConfirmDialog(this, "Continue Add Slot?", "Add Slot", JOptionPane.YES_NO_OPTION);
            if (Add_Slot_Option == JOptionPane.YES_OPTION) {
                addSlotToFile();
                loadTimetableSlots();
            }
        }
    }

    private void updateMonthViewFromComboBox() {
        Date beginDate = Begin_Date_Chooser.getDate();
        Date endDate = End_Date_Chooser.getDate();

        Month_View.setSelectionInterval(beginDate, endDate);
    }

    private void addSlotToFile() {
        Date beginDate = Begin_Date_Chooser.getDate();
        Date endDate = End_Date_Chooser.getDate();
        
        if (beginDate == null || endDate == null) {
            JOptionPane.showMessageDialog(this, "Please select both Begin Date and End Date!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (beginDate.after(endDate)) {
            JOptionPane.showMessageDialog(this, "Invalid slot! Begin Date cannot be later than End Date!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (beginDate.equals(endDate)) {
            if (isWeekend(beginDate)) {
                JOptionPane.showMessageDialog(this, "Invalid slot! Cannot add slot on a weekend!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        
        if (InvalidSlot(beginDate,endDate)) {
            return;
        }
        
        if (isWeekend(beginDate)) {
            beginDate = getNextMonday(beginDate);
        }

        if (isWeekend(endDate)) {
            endDate = getPreviousFriday(endDate);
        }
        
        String beginTime = Begin_Time_ComboBox.getSelectedItem().toString() + ":" + Begin_Minute_ComboBox.getSelectedItem().toString();
        String endTime = End_Time_ComboBox.getSelectedItem().toString() + ":" + End_Minute_ComboBox.getSelectedItem().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String data = String.format("%s,%s,%s,%s,%s", username, dateFormat.format(beginDate), beginTime, dateFormat.format(endDate), endTime);

        List<String> slots = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("AvailableSlot.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                slots.add(line.trim());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading timetable slots. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (isEditMode) {
            String oldData = String.format("%s,%s,%s,%s,%s",
                    username,
                    tableModel.getValueAt(editingRowIndex, 1),
                    tableModel.getValueAt(editingRowIndex, 2),
                    tableModel.getValueAt(editingRowIndex, 3),
                    tableModel.getValueAt(editingRowIndex, 4));

            slots.remove(oldData);
            slots.add(data);

            tableModel.setValueAt(new SimpleDateFormat("yyyy-MM-dd").format(beginDate), editingRowIndex, 1);
            tableModel.setValueAt(beginTime, editingRowIndex, 2);
            tableModel.setValueAt(new SimpleDateFormat("yyyy-MM-dd").format(endDate), editingRowIndex, 3);
            tableModel.setValueAt(endTime, editingRowIndex, 4);

            isEditMode = false;
            editingRowIndex = -1;

            JOptionPane.showMessageDialog(this, "Slot edited successfully!");
        } else {
            if (!slots.contains(data)) {
                slots.add(data);
                tableModel.addRow(new Object[]{username, dateFormat.format(beginDate), beginTime, dateFormat.format(endDate), endTime, "Settings"});
                JOptionPane.showMessageDialog(this, "Slot added successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Slot already exists!", "Duplicate Slot", JOptionPane.WARNING_MESSAGE);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("AvailableSlot.txt"))) {
            for (String slot : slots) {
                writer.write(slot);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving slot. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isWeekend(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }

    private Date getNextMonday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY) {
            cal.add(Calendar.DAY_OF_WEEK, +2);  
        } else {
            cal.add(Calendar.DAY_OF_WEEK, Calendar.MONDAY - cal.get(Calendar.DAY_OF_WEEK));
        }
        return cal.getTime();
    }

    private Date getPreviousFriday(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SUNDAY) {
            cal.add(Calendar.DAY_OF_WEEK, -2);  
        } else {
            cal.add(Calendar.DAY_OF_WEEK, Calendar.FRIDAY - dayOfWeek);
        }
        return cal.getTime();
    }

    private Boolean InvalidSlot(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        long differenceInMillis = Math.abs(cal2.getTimeInMillis() - cal1.getTimeInMillis());
        long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);

        if (differenceInDays == 1) {
            if ((isWeekend(cal1.getTime()) && cal2.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) ||
                (isWeekend(cal2.getTime()) && cal1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)) {
                JOptionPane.showMessageDialog(this, "Invalid slot! Cannot add slot spanning from Saturday to Sunday!", "Error", JOptionPane.ERROR_MESSAGE);
                return true;
            }
        }
        return false;
    }
    
    private void loadTimetableSlots() {
        FileCleaner.removeEmptyLines("AvailableSlot.txt");
        tableModel.setRowCount(0);

        List<String> slots = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("AvailableSlot.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length == 5 && parts[0].equals(username)) {
                    slots.add(line.trim());
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading timetable slots. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        List<String> combinedSlots = combineTimeSlots(slots);

        for (String combinedSlot : combinedSlots) {
            String[] rowData = combinedSlot.split(",");
            Object[] rowWithButton = new Object[6];
            System.arraycopy(rowData, 0, rowWithButton, 0, 5);
            rowWithButton[5] = "Settings";
            tableModel.addRow(rowWithButton);
        }

        writeSlotsToFile(combinedSlots);
    }

    private void writeSlotsToFile(List<String> updatedSlots) {
        List<String> allSlots = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("AvailableSlot.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                allSlots.add(line.trim());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading slots from file. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        List<String> otherLecturersSlots = new ArrayList<>();
        for (String slot : allSlots) {
            if (!slot.startsWith(username + ",")) {
                otherLecturersSlots.add(slot);
            }
        }

        otherLecturersSlots.addAll(updatedSlots);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("AvailableSlot.txt"))) {
            for (String slot : otherLecturersSlots) {
                writer.write(slot);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving slots to file. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removeSlotFromFile(String username, String beginDate, String beginTime, String endDate, String endTime) {
        List<String> slots = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("AvailableSlot.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                slots.add(line.trim());
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading timetable slots. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        String slotToRemove = String.format("%s,%s,%s,%s,%s", username, beginDate, beginTime, endDate, endTime);
        slots.remove(slotToRemove);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("AvailableSlot.txt"))) {
            for (String slot : slots) {
                writer.write(slot);
                writer.newLine();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error removing slot. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editSelectedSlot(int selectedRow) {
        int response = JOptionPane.showConfirmDialog(this, "Do you want to edit this slot?", "Edit Slot", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            String beginDate = (String) tableModel.getValueAt(selectedRow, 1);
            String beginTime = (String) tableModel.getValueAt(selectedRow, 2);
            String endDate = (String) tableModel.getValueAt(selectedRow, 3);
            String endTime = (String) tableModel.getValueAt(selectedRow, 4);

            Begin_Date_Chooser.setDate(parseDate(beginDate));   
            End_Date_Chooser.setDate(parseDate(endDate));
            Begin_Time_ComboBox.setSelectedItem(beginTime.split(":")[0]);
            Begin_Minute_ComboBox.setSelectedItem(beginTime.split(":")[1]);
            End_Time_ComboBox.setSelectedItem(endTime.split(":")[0]);
            End_Minute_ComboBox.setSelectedItem(endTime.split(":")[1]);

            isEditMode = true;
            editingRowIndex = selectedRow;
        }
    }

    private void deleteSelectedSlot(int selectedRow) {
        int response = JOptionPane.showConfirmDialog(this, "Do you want to delete this slot?", "Delete Slot", JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            String username = (String) tableModel.getValueAt(selectedRow, 0);
            String beginDate = (String) tableModel.getValueAt(selectedRow, 1);
            String beginTime = (String) tableModel.getValueAt(selectedRow, 2);
            String endDate = (String) tableModel.getValueAt(selectedRow, 3);
            String endTime = (String) tableModel.getValueAt(selectedRow, 4);

            removeSlotFromFile(username, beginDate, beginTime, endDate, endTime);

            tableModel.removeRow(selectedRow);
        }
    }

    private Date parseDate(String dateString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            return null;
        }
    }

    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText("⚙️");
            return this;
        }
    }

    private class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = "⚙️";
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                JPopupMenu menu = new JPopupMenu();
                JMenuItem editItem = new JMenuItem("Edit");
                JMenuItem deleteItem = new JMenuItem("Delete");

                editItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        editSelectedSlot(slotsTable.getSelectedRow());
                    }
                });

                deleteItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        deleteSelectedSlot(slotsTable.getSelectedRow());
                    }
                });

                menu.add(editItem);
                menu.add(deleteItem);
                menu.show(button, button.getWidth() / 2, button.getHeight() / 2);
            }
            isPushed = false;
            return new String(label);
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
    
    private List<String> combineTimeSlots(List<String> slots) {
        List<String> combinedSlots = new ArrayList<>();

        slots.sort((s1, s2) -> {
            String[] parts1 = s1.split(",");
            String[] parts2 = s2.split(",");
            Date beginDate1 = parseDate(parts1[1]);
            Date beginDate2 = parseDate(parts2[1]);
            String[] beginTimeParts1 = parts1[2].split(":");
            String[] beginTimeParts2 = parts2[2].split(":");
            int compareDates = beginDate1.compareTo(beginDate2);
            if (compareDates == 0) {
                int compareTimes = Integer.compare(Integer.parseInt(beginTimeParts1[0]), Integer.parseInt(beginTimeParts2[0]));
                if (compareTimes == 0) {
                    return Integer.compare(Integer.parseInt(beginTimeParts1[1]), Integer.parseInt(beginTimeParts2[1]));
                }
                return compareTimes;
            }
            return compareDates;
        });

        for (int i = 0; i < slots.size(); i++) {
            String currentSlot = slots.get(i);
            String[] currentParts = currentSlot.split(",");
            Date currentBeginDate = parseDate(currentParts[1]);
            Date currentEndDate = parseDate(currentParts[3]);
            String currentBeginTime = currentParts[2];
            String currentEndTime = currentParts[4];

            for (int j = i + 1; j < slots.size(); j++) {
                String nextSlot = slots.get(j);
                String[] nextParts = nextSlot.split(",");
                Date nextBeginDate = parseDate(nextParts[1]);
                Date nextEndDate = parseDate(nextParts[3]);
                String nextBeginTime = nextParts[2];
                String nextEndTime = nextParts[4];

                if ((nextBeginDate.equals(currentEndDate) || nextBeginDate.before(currentEndDate)) &&
                        (nextBeginTime.equals(currentEndTime) || nextBeginTime.compareTo(currentEndTime) < 0)) {
                    currentEndDate = nextEndDate;
                    currentParts[3] = nextParts[3];
                    currentEndTime = nextEndTime;
                    currentParts[4] = nextParts[4];
                    slots.remove(j);
                    j--;
                }
            }

            String combinedSlot = String.format("%s,%s,%s,%s,%s",
                    currentParts[0], currentParts[1], currentParts[2], currentParts[3], currentParts[4]);
            combinedSlots.add(combinedSlot);
        }

        return combinedSlots;
    }
    
    public void onLogout() {
        setVisible(false);
        Login_Window_UI LW_UI = new Login_Window_UI();
        Login_Window LW = new Login_Window(LW_UI);
    }
    
}

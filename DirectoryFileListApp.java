package com.mycompany.oodj_assignment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DirectoryFileListApp extends JFrame {

    private JTable fileTable;
    private CustomTableModel tableModel;
    private JButton deleteButton;
    private DirectoryFileListListener listener;

    public DirectoryFileListApp(String directoryPath, DirectoryFileListListener listener) {
        this.listener = listener;
        setTitle("Directory File List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);

        // Create custom table model with checkboxes
        String[] columns = {"Select", "File"};
        tableModel = new CustomTableModel(columns, 0);
        fileTable = new JTable(tableModel);
        fileTable.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxRenderer());

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(fileTable);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Create delete button
        deleteButton = new JButton("Delete Selected Files");
        deleteButton.addActionListener(e -> deleteSelectedFiles(directoryPath));
        getContentPane().add(deleteButton, BorderLayout.SOUTH);

        // Display files from a specific directory
        displayFilesInDirectory(directoryPath);
    }

    private void displayFilesInDirectory(String directoryPath) {
        Path dirPath = Paths.get(directoryPath);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath)) {
            for (Path filePath : stream) {
                String fileName = filePath.getFileName().toString();
                // Add file details to the table model with checkbox
                tableModel.addRow(new Object[]{false, fileName});
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading directory: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedFiles(String directoryPath) {
        List<Integer> rowsToDelete = new ArrayList<>();

        // Identify selected rows
        for (int row = 0; row < tableModel.getRowCount(); row++) {
            if ((Boolean) tableModel.getValueAt(row, 0)) {
                rowsToDelete.add(row);
            }
        }
        // Delete selected files
        try {
            for (int rowIndex : rowsToDelete) {
                String fileName = (String) tableModel.getValueAt(rowIndex, 1);
                Path filePath = Paths.get(directoryPath, fileName);
                Files.deleteIfExists(filePath);
            }
            JOptionPane.showMessageDialog(this,
                    "Selected files deleted successfully.",
                    "Deletion Successful", JOptionPane.INFORMATION_MESSAGE);
            refreshFileList(directoryPath); // Refresh the file list after deletion

            // Notify listener
            if (listener != null) {
                listener.onFilesDeleted();
            }

            setVisible(false);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error deleting files: " + e.getMessage(),
                    "Deletion Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshFileList(String directoryPath) {
        tableModel.setRowCount(0); // Clear existing rows
        displayFilesInDirectory(directoryPath); // Reload files from directory
    }

    // Custom TableModel class to include checkboxes
    private static class CustomTableModel extends DefaultTableModel {
        public CustomTableModel(Object[] columnNames, int rowCount) {
            super(columnNames, rowCount);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0) {
                return Boolean.class; // Checkbox column
            } else {
                return super.getColumnClass(columnIndex);
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column == 0; // Only allow editing the checkbox column
        }
    }

    // Custom TableCellRenderer for rendering checkboxes in the table
    private static class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
        public CheckBoxRenderer() {
            setHorizontalAlignment(JCheckBox.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setSelected((Boolean) value);
            return this;
        }
    }

    public interface DirectoryFileListListener {
        void onFilesDeleted();
    }
}

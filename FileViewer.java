package com.mycompany.oodj_assignment;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import org.apache.pdfbox.Loader;

public class FileViewer extends JFrame {
    public FileViewer(File file) {
        setTitle("File Viewer");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        if (file.getName().endsWith(".pdf")) {
            displayPDF(file, textArea);
        } else if (file.getName().endsWith(".doc")) {
            displayDoc(file, textArea);
        } else if (file.getName().endsWith(".docx")) {
            displayDocx(file, textArea);
        } else if (file.getName().endsWith(".txt")) {
            displayTextFile(file, textArea);
        } else if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
            displayImage(file);
        } else if (file.getName().endsWith(".mp4") || file.getName().endsWith(".mp3")) {
            playMedia(file);
        } else {
            JOptionPane.showMessageDialog(this, "Unsupported file type.");
        }

        setVisible(true);
    }

    private void displayPDF(File file, JTextArea textArea) {
        try (PDDocument document = Loader.loadPDF(file)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);
            textArea.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading PDF file.");
        }
    }

    private void displayDoc(File file, JTextArea textArea) {
        try (FileInputStream fis = new FileInputStream(file);
            HWPFDocument document = new HWPFDocument(fis)) {
            WordExtractor extractor = new WordExtractor(document);
            String text = extractor.getText();
            textArea.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading DOC file.");
        }
    }

    private void displayDocx(File file, JTextArea textArea) {
        try (FileInputStream fis = new FileInputStream(file);
             XWPFDocument document = new XWPFDocument(fis)) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                textArea.append(paragraph.getText() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading DOCX file.");
        }
    }

    private void displayTextFile(File file, JTextArea textArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                textArea.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading text file.");
        }
    }

    private void displayImage(File file) {
        try {
            ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());
            JLabel imageLabel = new JLabel(imageIcon);
            JScrollPane scrollPane = new JScrollPane(imageLabel);
            getContentPane().removeAll();
            add(scrollPane, BorderLayout.CENTER);
            revalidate();
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error displaying image.");
        }
    }

    private void playMedia(File file) {
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error playing media file.");
        }
    }
}

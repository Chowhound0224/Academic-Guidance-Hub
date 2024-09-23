package com.mycompany.oodj_assignment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import com.mycompany.oodj_assignment.ui.Login_Window_UI;
import java.time.format.DateTimeFormatter;

public class UserRegistrationPanel extends JPanel implements ActionListener, LogoutCallback {
    
    private UserEditPanel userEditPanel;
    private JTextField idTxtField, nameTextField, icTxtField, contactTxtField, addressTxtField, postcodeTxtField, emailTxtField;
    private static JComboBox<String> roleCmbBox, intakecodeCmbBox, ageCmbBox, genderCmbBox, raceCmbBox, religionCmbBox, nationalityCmbBox, maritalCmbBox, stateCmbBox, schoolWiseCmbBox;
    private JLabel idLabel,roleLabel, intakecodeLabel, nameLabel, ageLabel, icLabel, genderLabel, raceLabel, religionLabel, nationalityLabel, maritalLabel, contactLabel, addressLabel, postcodeLabel, stateLabel, emailLabel, schoolWiseLabel;
    private JButton registerButton;
    private static JButton Back_Button;
    private Map<String, Integer> roleCounts;
    LogoutCallback logoutCallback;
    public int countStudent, countLecturer, countAdmin;

    public UserRegistrationPanel(LogoutCallback callback, UserEditPanel userEditPanel) {
        this.logoutCallback = callback;
        this.userEditPanel = userEditPanel;
        roleCounts = new HashMap<>();
        setupPanel();
    }
    private void registerUser(User user) {
        try (FileWriter writer = new FileWriter("userPersonalData.txt", true)) {
            user.saveUserData(writer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void registerAccount(User user) {
        try (FileWriter writer = new FileWriter("userdata.txt", true)) {
            user.saveAccountData(writer,getDate15DaysBefore());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private LocalDate getDate15DaysBefore() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.minusDays(15);
    }


    private void setupPanel() {
        setLayout(null);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 500, 900);
        add(panel);


        // Add components
        idLabel = new JLabel("ID: ");
        idLabel.setBounds(90, 10, 165, 25);

        idTxtField = new JTextField(25);
        idTxtField.setBounds(180, 10, 230, 25);
        idTxtField.setEditable(false);

        roleLabel = new JLabel("Role: ");
        roleLabel.setBounds(90, 40, 165, 25);
        String[] roleList = {"", "Admin", "Student", "Lecturer"};
        roleCmbBox = new JComboBox<>(roleList);
        roleCmbBox.setBounds(180, 40, 230, 25);
        roleCmbBox.addActionListener(this);

        intakecodeLabel = new JLabel("Intake Code: ");
        intakecodeLabel.setBounds(90, 70, 165, 25);

        String[] intakeCodeList = {""};
        intakecodeCmbBox = new JComboBox<>(intakeCodeList);
        getIntakeCode();
        intakecodeCmbBox.setBounds(180, 70, 230, 25);

        nameLabel = new JLabel("Name: ");
        nameLabel.setBounds(90, 100, 165, 25);
        nameTextField = new JTextField(25);
        nameTextField.setBounds(180, 100, 230, 25);

        ageLabel = new JLabel("Age: ");
        ageLabel.setBounds(90, 130, 165, 25);
        String[] ageList = new String[67];
        ageList[0] = "";
        for (int i = 1; i <= 66; i++) {
            ageList[i] = String.valueOf(i + 14);
        }

        ageCmbBox = new JComboBox<>(ageList);
        ageCmbBox.setBounds(180, 130, 230, 25);

        icLabel = new JLabel("IC Number: ");
        icLabel.setBounds(90, 160, 165, 25);
        icTxtField = new JTextField(25);
        icTxtField.setBounds(180, 160, 230, 25);

        genderLabel = new JLabel("Gender: ");
        genderLabel.setBounds(90, 190, 165, 25);
        String[] genderList = {"", "Male", "Female", "Other"};
        genderCmbBox = new JComboBox<>(genderList);
        genderCmbBox.setBounds(180, 190, 230, 25);

        raceLabel = new JLabel("Race: ");
        raceLabel.setBounds(90, 220, 165, 25);
        String[] raceList = {"", "Malay", "Chinese", "Indian", "Others"};
        raceCmbBox = new JComboBox<>(raceList);
        raceCmbBox.setBounds(180, 220, 230, 25);

        religionLabel = new JLabel("Religion: ");
        religionLabel.setBounds(90, 250, 165, 25);
        String[] religionList = {"", "Islam", "Buddhism", "Christian", "Hinduism", "Other/Unknown", "No Religion"};
        religionCmbBox = new JComboBox<>(religionList);
        religionCmbBox.setBounds(180, 250, 230, 25);

        nationalityLabel = new JLabel("Nationality: ");
        nationalityLabel.setBounds(90, 280, 165, 25);
        String[] nationalityList = {"", "Malaysian", "Foreigner"};
        nationalityCmbBox = new JComboBox<>(nationalityList);
        nationalityCmbBox.setBounds(180, 280, 230, 25);

        maritalLabel = new JLabel("Marital Status: ");
        maritalLabel.setBounds(90, 310, 165, 25);
        String[] maritalStatus_List = {"", "Single", "Married"};
        maritalCmbBox = new JComboBox<>(maritalStatus_List);
        maritalCmbBox.setBounds(180, 310, 230, 25);

        contactLabel = new JLabel("Contact:");
        contactLabel.setBounds(90, 340, 165, 25);
        contactTxtField = new JTextField(15);
        contactTxtField.setBounds(180, 340, 230, 25);

        addressLabel = new JLabel("Address: ");
        addressLabel.setBounds(90, 370, 165, 25);
        addressTxtField = new JTextField(30);
        addressTxtField.setBounds(180, 370, 230, 25);

        postcodeLabel = new JLabel("Postcode:");
        postcodeLabel.setBounds(90, 400, 165, 25);
        postcodeTxtField = new JTextField(15);
        postcodeTxtField.setBounds(180, 400, 230, 25);

        stateLabel = new JLabel("State:");
        stateLabel.setBounds(90, 430, 165, 25);
        String[] countryList = {"", "Sarawak", "Sabah", "Johor", "Kedah", "Kelantan", "Malacca", "Negeri Sembilan",
            "Pahang", "Penang", "Perak", "Perlis", "Selangor", "Terengganu", "Kuala Lumpur", "Labuan", "Putrajaya", "Other"};
        stateCmbBox = new JComboBox<>(countryList);
        stateCmbBox.setBounds(180, 430, 230, 25);

        emailLabel = new JLabel("Email Address:");
        emailLabel.setBounds(90, 460, 165, 25);
        emailTxtField = new JTextField(30);
        emailTxtField.setBounds(180, 460, 230, 25);

        schoolWiseLabel = new JLabel("School Wise: ");
        schoolWiseLabel.setBounds(90, 490, 165, 25);
        String[] schoolWiseList = {"", "Individual", "School of Computing", "School of Engineering",
            "School of Business", "School of Life Sciences", "School of Media Arts & Design",
            "School of Accounting & Finance", "School of Hospitality", "School of General Studies"};
        schoolWiseCmbBox = new JComboBox<>(schoolWiseList);
        schoolWiseCmbBox.setBounds(180, 490, 230, 25);
        schoolWiseLabel.setVisible(false);
        schoolWiseCmbBox.setVisible(false);

        Back_Button = new JButton("◄");
        Back_Button.setBounds(10, 10, 45, 25);
        Back_Button.addActionListener(this);

        registerButton = new JButton("Register");
        registerButton.setBounds(180, 520, 165, 25);
        registerButton.addActionListener(this);
        
        panel.add(idLabel);
        panel.add(idTxtField);
        panel.add(roleLabel);
        panel.add(roleCmbBox);
        panel.add(intakecodeLabel);
        panel.add(intakecodeCmbBox);
        panel.add(nameLabel);
        panel.add(nameTextField);
        panel.add(ageLabel);
        panel.add(ageCmbBox);
        panel.add(icLabel);
        panel.add(icTxtField);
        panel.add(genderLabel);
        panel.add(genderCmbBox);
        panel.add(raceLabel);
        panel.add(raceCmbBox);
        panel.add(religionLabel);
        panel.add(religionCmbBox);
        panel.add(nationalityLabel);
        panel.add(nationalityCmbBox);
        panel.add(maritalLabel);
        panel.add(maritalCmbBox);
        panel.add(contactLabel);
        panel.add(contactTxtField);
        panel.add(addressLabel);
        panel.add(addressTxtField);
        panel.add(postcodeLabel);
        panel.add(postcodeTxtField);
        panel.add(stateLabel);
        panel.add(stateCmbBox);
        panel.add(emailLabel);
        panel.add(emailTxtField);
        panel.add(schoolWiseLabel);
        panel.add(schoolWiseCmbBox);
        panel.add(registerButton);
        panel.add(Back_Button);
        schoolWiseLabel.setVisible(false);
        schoolWiseCmbBox.setVisible(false);
        intakecodeLabel.setVisible(false);
        intakecodeCmbBox.setVisible(false);
        idTxtField.setEditable(false);
        loadUserCounts();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            String id = idTxtField.getText();
            String role = (String) roleCmbBox.getSelectedItem();
            String password = "";
            String name = nameTextField.getText();
            String age = (String) ageCmbBox.getSelectedItem();
            String ic = icTxtField.getText();
            String gender = (String) genderCmbBox.getSelectedItem();
            String race = (String) raceCmbBox.getSelectedItem();
            String religion = (String) religionCmbBox.getSelectedItem();
            String nationality = (String) nationalityCmbBox.getSelectedItem();
            String maritalStatus = (String) maritalCmbBox.getSelectedItem();
            String contact = contactTxtField.getText();
            String address = addressTxtField.getText();
            String postcode = postcodeTxtField.getText();
            String state = (String) stateCmbBox.getSelectedItem();
            String email = emailTxtField.getText();
            
            if (id.isEmpty() || name.isEmpty() || age.isBlank() || ic.isEmpty() || gender.isBlank() ||
                    race.isBlank() || religion.isBlank() || nationality.isBlank() || maritalStatus.isBlank() ||
                    contact.isEmpty() || address.isEmpty() || postcode.isEmpty() || state.isBlank() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You must fill up all details!", "Registration Reject", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            User user = null;
            
            String prefix = id.substring(0, 2);
            String remaining = id.substring(2);
            String capitalLetter = prefix.substring(0, 1);
            String lowerLetter = prefix.substring(1, 2).toLowerCase();
            String lastFourDigits = id.substring(id.length() - 4);

            password = capitalLetter + lowerLetter + remaining + "@" + lastFourDigits;
            
            
            if (role.equals("Student")) {
                String intakeCode = (String) intakecodeCmbBox.getSelectedItem();
                user = new Student(id, password, name, age, ic, gender, race, religion,
                       nationality, maritalStatus, contact, address, postcode, state, email, intakeCode, "Student","","");
            } else if (role.equals("Lecturer")) {
                String schoolWise = (String) schoolWiseCmbBox.getSelectedItem();
                user = new Lecturer(id, password, name, age, ic, gender, race, religion,
                        nationality, maritalStatus, contact, address, postcode, state, email, schoolWise, "Lecturer","","");
            } else if (role.equals("Admin")) {
                user = new Admin(id, password, name, age, ic, gender, race, religion, nationality, maritalStatus,
                        contact, address, postcode, state, email, "-","Admin","","");
            }

            if (user != null) {
                registerUser(user);
                registerAccount(user);
                userEditPanel.loadUserData();
                OODJ_Assignment.readAllUsers();
                JOptionPane.showMessageDialog(this, "User registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                idTxtField.setText("");
                roleCmbBox.setSelectedItem("");
                intakecodeCmbBox.setSelectedItem("");
                nameTextField.setText("");
                ageCmbBox.setSelectedItem("");
                icTxtField.setText("");
                genderCmbBox.setSelectedItem("");
                raceCmbBox.setSelectedItem("");
                religionCmbBox.setSelectedItem("");
                nationalityCmbBox.setSelectedItem("");
                maritalCmbBox.setSelectedItem("");
                contactTxtField.setText("");
                addressTxtField.setText("");
                postcodeTxtField.setText("");
                stateCmbBox.setSelectedItem("");
                emailTxtField.setText("");
                loadUserCounts();
                
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid role.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == roleCmbBox) {
            if (roleCmbBox.getSelectedItem().equals("Student")) {
                intakecodeCmbBox.setVisible(true);
                intakecodeLabel.setVisible(true);
                schoolWiseCmbBox.setVisible(false);
                schoolWiseLabel.setVisible(false);

            } else if (roleCmbBox.getSelectedItem().equals("Lecturer")) {
                schoolWiseCmbBox.setVisible(true);
                schoolWiseLabel.setVisible(true);
                intakecodeCmbBox.setVisible(false);
                intakecodeLabel.setVisible(false);
            } else {
                intakecodeCmbBox.setVisible(false);
                intakecodeLabel.setVisible(false);
                schoolWiseCmbBox.setVisible(false);
                schoolWiseLabel.setVisible(false);
            }
            addIdinidTxtField();
        } else if (e.getSource() == Back_Button) {
            LogoutHandler.logout((JFrame) SwingUtilities.getWindowAncestor(this), this);
        }
    }


    public static void getIntakeCode() {
        try (BufferedReader reader = new BufferedReader(new FileReader("intakeCode.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                intakecodeCmbBox.addItem(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void loadUserCounts() {
        roleCounts = new HashMap<>();
        roleCounts.put("Student", 0);
        roleCounts.put("Lecturer", 0);
        roleCounts.put("Admin", 0);
        try (BufferedReader reader = new BufferedReader(new FileReader("userdata.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Student")) {
                    roleCounts.put("Student", roleCounts.get("Student") + 1);
                } else if (line.contains("Lecturer")) {
                    roleCounts.put("Lecturer", roleCounts.get("Lecturer") + 1);
                } else if (line.contains("Admin")) {
                    roleCounts.put("Admin", roleCounts.get("Admin") + 1);
                }
            }
            countStudent = roleCounts.get("Student");
            countLecturer = roleCounts.get("Lecturer");
            countAdmin = roleCounts.get("Admin");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateID(String role, int count) {
        count += 1;
        String prefix = "";
        if (role.equals("Student")) {
            prefix = "TP";
        } else if(role.equals("Lecturer")) {
            prefix = "LC";
        } else if (role.equals("Admin")) {
            prefix = "A";
        }
        return prefix + String.format("%06d", count);
    }

    private void addIdinidTxtField(){
        String id;
        if (roleCmbBox.getSelectedItem().equals("Student")) {
            id = generateID("Student", countStudent);
        }   else if (roleCmbBox.getSelectedItem().equals("Lecturer")) {
            id = generateID("Lecturer",countLecturer);
        }   else if (roleCmbBox.getSelectedItem().equals("Admin")){
            id = generateID("Admin",countAdmin);
        }   else {
            id = "";
        }
        idTxtField.setText(id);
    }
    
    public void onLogout() {
        setVisible(false);
        Login_Window_UI LW_UI = new Login_Window_UI();
        Login_Window LW = new Login_Window(LW_UI);
    }
}
    
    


 

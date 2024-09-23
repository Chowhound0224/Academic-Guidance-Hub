package com.mycompany.oodj_assignment;

import com.mycompany.oodj_assignment.ui.Login_Window_UI;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Change_Password extends JFrame implements ActionListener, ItemListener, LogoutCallback {
    
    public String Username_To_Pass, Password, Name, UserType, CheckUsername; 
    private JButton Back_Button;
    private JPanel CP_Panel;
    private JLabel Username_Label;
    private JTextField Username;
    private JLabel Old_Password_Label;
    private JPasswordField Old_Password;
    private JLabel Password1_Label;
    private JPasswordField Password1;
    private JLabel Password2_Label;
    private JPasswordField Password2;
    private JCheckBox Show_Password;
    private JButton Confirm_Button;
    
    private Login_Window_UI LW_UI;
    
    public Change_Password(String username, String password, String name, String userRole) {
        Username_To_Pass = username;
        Password = password;
        Name = name;
        UserType = userRole;
        final int width = 400, height = 330;
        setTitle("Change Password");
        setSize(width, height);

        Back_Button = new JButton("◄");
        Back_Button.setBounds(10, 10,45, 25);
        Back_Button.addActionListener(this);
        //Forget Password
        Username_Label = new JLabel("Username: ");
        Username_Label.setBounds(86, 50, 160, 25);
        Username = new JTextField(40);
        Username.setBounds(160, 50, 165, 25);
        
        //Change Password
        Old_Password_Label = new JLabel("Old Password: ");
        Old_Password_Label.setBounds(68, 50, 160, 25);
        Old_Password = new JPasswordField(40);
        Old_Password.setBounds(160, 50, 165, 25);
        
        Password1_Label = new JLabel("New Password: ");
        Password1_Label.setBounds(60, 100, 150, 25);
        Password1 = new JPasswordField(15);
        Password1.setBounds(160, 100, 165, 25);
        Password2_Label = new JLabel("Confirm New Password: ");
        Password2_Label.setBounds(12, 150, 150, 25);
        Password2 = new JPasswordField(15);
        Password2.setBounds(160, 150, 165, 25);
        Show_Password = new JCheckBox("Show password");
        Show_Password.setBounds(160, 190, 165, 25);
        Show_Password.addItemListener(this);
        Confirm_Button = new JButton("Confirm");
        Confirm_Button.setBounds(180, 240, 80, 25);
        Confirm_Button.addActionListener(this);

        CP_Panel = new JPanel();
        CP_Panel.setLayout(null);
        CP_Panel.add(Back_Button);
        if (UserType.equals("_")) {
            CP_Panel.add(Username_Label);
            CP_Panel.add(Username);
        } else {
            CP_Panel.add(Old_Password_Label);
            CP_Panel.add(Old_Password);
        }
        CP_Panel.add(Password1_Label);
        CP_Panel.add(Password1);
        CP_Panel.add(Password2_Label);
        CP_Panel.add(Password2);
        CP_Panel.add(Show_Password);
        CP_Panel.add(Confirm_Button);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(CP_Panel);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Confirm_Button) {
            if (UserType.equals("_")) {
                CheckUsername = Username.getText();
                
            } else {
                CheckUsername = Username_To_Pass;
            }
            
            String NewPassword = new String(Password1.getPassword());
            String ConfirmNewPassword = new String(Password2.getPassword());

            if (!UserExist(CheckUsername)) {
                JOptionPane.showMessageDialog(this, "User not found.");
                return;
            }

            if (!AllowedToChangePassword(CheckUsername)) {
                JOptionPane.showMessageDialog(this, "You can only change your password once every 15 days.");
                return;
            }
            
            if (!UserType.equals("_")) {
                if (!Old_Password.getText().equals(Password)) {
                    JOptionPane.showMessageDialog(this, "Wrong Password. Please enter the correct password.");
                    return;
                }
            }
            
            if (NewPassword.isEmpty() || ConfirmNewPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Passwords cannot be empty. Please enter another password!");
                return;
            }

            if (!NewPassword.equals(ConfirmNewPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match. Please enter the same password!");
                return;
            }

            updatePassword(CheckUsername, NewPassword);
            JOptionPane.showMessageDialog(this, "Password changed successfully.");

            LocalDate currentDate = LocalDate.now();
            String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            userdataWriter.UpdateUserDataFile(CheckUsername, NewPassword, formattedDate);

        } else if (e.getSource() == Back_Button) {
            setVisible(false);  
            if (UserType.equals("_")) {
                Login_Window_UI LW_UI = new Login_Window_UI();
                Login_Window LW = new Login_Window(LW_UI);
            } else if (UserType.equals("Lecturer") || UserType.equals("Lecturer-SP") ||
                    UserType.equals("Lecturer-SM") || UserType.equals("Lecturer-SP-SM")) {
                Lecturer_Menu LM = new Lecturer_Menu(Username_To_Pass, Password, Name, UserType);
                LM.setVisible(true);
            } else if (UserType.equals("Student")) {
                Student_Menu SM = new Student_Menu(Username_To_Pass, Password, Name, UserType);
                SM.setVisible(true);
            } else if (UserType.equals("Admin")) {
                Admin_Menu AM = new Admin_Menu(Username_To_Pass, Password, Name, UserType);
                AM.setVisible(true);
            } else if (UserType.equals("Lecturer-PM")) {
                Project_Manager_Model model = new Project_Manager_Model();
                Project_Manager_Menu_View view = new Project_Manager_Menu_View(model);
                    new Project_Manager_Menu_Controller(Username_To_Pass, Password, Name, UserType,
                            view, model,this);
            }
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == Show_Password) {
            if (Show_Password.isSelected()) {
                Show_Password.setText("Hide Password");
                Old_Password.setEchoChar((char) 0);
                Password1.setEchoChar((char) 0);
                Password2.setEchoChar((char) 0);
            } else {
                Show_Password.setText("Show Password");
                Password1.setEchoChar('*');
                Password2.setEchoChar('*');
                Old_Password.setEchoChar('*');
            }
        }
    }

    private boolean UserExist(String username) {
        for (User user : OODJ_Assignment.allUsers) {
            if (user.id.equals(username)) {
                return true;
            }
        }
        return false;
    }

    private boolean AllowedToChangePassword(String username) {
        boolean userFound = false;
        for (User user : OODJ_Assignment.allUsers) {
            if (user.id.equals(username)) {
                userFound = true;
                LocalDate lastChangeDate = LocalDate.parse(user.getDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                LocalDate currentDate = LocalDate.now();
                long daysSinceLastChange = java.time.temporal.ChronoUnit.DAYS.between(lastChangeDate, currentDate);

                if (daysSinceLastChange >= 15) {
                    return true;
                } else {
                    return false;
                }
            }
        }

        if (!userFound) {
            JOptionPane.showMessageDialog(this, "User not found.");
        }
        return false;
    }

    private void updatePassword(String username, String newPassword) {
        for (User user : OODJ_Assignment.allUsers) {
            if (user.id.equals(username)) {
                user.password = newPassword;
                user.date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                break;
            }
        }
    }

    @Override
    public void onLogout() {
        setVisible(false);
        Login_Window_UI LW_UI = new Login_Window_UI();
        Login_Window LW = new Login_Window(LW_UI);
    }
}

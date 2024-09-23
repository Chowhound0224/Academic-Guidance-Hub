package com.mycompany.oodj_assignment.ui;

import com.mycompany.oodj_assignment.Login_Window;
import com.mycompany.oodj_assignment.Change_Password;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Login_Window_UI extends JFrame implements ActionListener, ItemListener, MouseListener {
    private JLabel Username_Label;
    public JTextField Username;
    private JLabel Password_Label;
    public JPasswordField Password;
    private JButton Login_Button;
    private JPanel Panel;
    private JCheckBox Show_Password;
    private JLabel Forget_Password;
    
    private static Login_Window LW;
    
    public Login_Window_UI() {
        final int width = 400, height = 330;
        setTitle("Login");
        setSize(width, height);

        Username_Label = new JLabel("Username: ");
        Username_Label.setBounds(70, 50, 160, 25);
        Username = new JTextField(40);
        Username.setBounds(140, 50, 165, 25);
        Password_Label = new JLabel("Password: ");
        Password_Label.setBounds(70, 100, 80, 25);
        Password = new JPasswordField(40);
        Password.setBounds(140, 100, 165, 25);
        Login_Button = new JButton("Login");
        Login_Button.setBounds(180, 180, 80, 25);
        Login_Button.addActionListener(this);
        Show_Password = new JCheckBox("Show password");
        Show_Password.setBounds(140, 140, 165, 25);
        Show_Password.addItemListener(this);
        Forget_Password = new JLabel("Forget Password");
        Forget_Password.setBounds(140, 220, 165, 25);
        Forget_Password.setForeground(Color.BLUE);
        Forget_Password.addMouseListener(this);

        Panel = new JPanel();
        Panel.setLayout(null);
        Panel.add(Username_Label);
        Panel.add(Username);
        Panel.add(Password_Label);
        Panel.add(Password);
        Panel.add(Show_Password);
        Panel.add(Login_Button);
        Panel.add(Forget_Password);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(Panel);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Login_Button) {
            String username = Username.getText();
            String password = new String(Password.getPassword());
            Login_Window.verification(username, password);
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == Show_Password) {
            if (Show_Password.isSelected()) {
                Show_Password.setText("Hide Password");
                Password.setEchoChar((char) 0);
            } else {
                Show_Password.setText("Show Password");
                Password.setEchoChar('*');
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == Forget_Password) {
            int option = JOptionPane.showConfirmDialog(this, "Do you want to change your Password?",
                    "Forget Password", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                setVisible(false);
                Change_Password CP = new Change_Password("", "", "", "_");
                CP.setVisible(true);
            } else {
                setVisible(true);
            }
        }
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }
    
    @Override
    public void mouseExited(MouseEvent e) {
    }
}

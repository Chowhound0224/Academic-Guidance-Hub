package com.mycompany.oodj_assignment;

import javax.swing.*;

public class Admin_Menu extends JFrame implements LogoutCallback{
    
    public String username, password, name, usertype;
    private Course course;
    private UserEditPanel editPanel;
    private UserRegistrationPanel userRegistrationPanel;
    private JPanel Change_Password;
    
    
    public Admin_Menu(String Username, String Password, String Name, String User_Role) {
        username = Username;
        password = Password;
        name = Name;
        usertype = User_Role;
        
        setTitle("User Management System");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBounds(10, 10, 950, 590);
        
        editPanel = new UserEditPanel(this);
        userRegistrationPanel = new UserRegistrationPanel(this, editPanel);
        course = new Course(this);
        
        Change_Password = new JPanel();
        tabbedPane.addChangeListener(e -> {
            int Selection = tabbedPane.getSelectedIndex();
            if (Selection == 3) {
                setVisible(false);
                Change_Password CP = new Change_Password(username, password, name, usertype);
                CP.setVisible(true);
            }
        });

        tabbedPane.add("Register User", userRegistrationPanel);
        tabbedPane.add("Edit Users", editPanel);
        tabbedPane.add("Modify Courses", course);
        tabbedPane.add("Change Password", Change_Password);
        setVisible(true);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(tabbedPane);
    }

    @Override
    public void onLogout() {
        LogoutHandler.logout(this, this);
    }
}



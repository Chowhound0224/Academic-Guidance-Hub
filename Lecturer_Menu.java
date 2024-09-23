package com.mycompany.oodj_assignment;

import javax.swing.*;

public class Lecturer_Menu extends JFrame implements LogoutCallback {

    public String username, password, name, usertype;

    private JTabbedPane tab;
    
    // View Timetable
    private ViewTimetable viewTimetable;
    
    // View Assigned Tasks
    private ViewAssignedTasks viewAssignedTasks;
    
    // Manage Available Slot
    private ManageAvailableSlot manageAvailableSlot;

    // Manage Presentation Requests
    private ManagePresentationRequests managePresentationRequests;
    
    // Change Password
    private JPanel Change_Password;
    
    public Lecturer_Menu(String Username, String Password, String Name, String User_Role) {
        username = Username;
        password = Password;
        name = Name;
        usertype = User_Role;

        setSize(1500, 600);
        setTitle("Lecturer Window");
        tab = new JTabbedPane();
        
        //View Timetable
        viewTimetable = new ViewTimetable(name, username, usertype, this);
        
        //View Assigned Tasks
        viewAssignedTasks = new ViewAssignedTasks(username, this);
        
        // Manage Available Slot
        manageAvailableSlot = new ManageAvailableSlot(username, this);

        // Manage Presentation Requests
        managePresentationRequests = new ManagePresentationRequests(username, this);
        
        // Change Password
        Change_Password = new JPanel();

        tab.addChangeListener(e -> {
            viewTimetable.reloadTable();
            int Selection = tab.getSelectedIndex();
            if (Selection == 4) {
                setVisible(false);
                Change_Password CP = new Change_Password(username, password, name, usertype);
                CP.setVisible(true);
            }
        });
        tab.add("Timetable", viewTimetable);
        tab.add("View Assigned Tasks & Assignment Evaluation", viewAssignedTasks);
        tab.add("Manage Available Slot", manageAvailableSlot);
        tab.add("Manage Presentation Requests", managePresentationRequests);
        tab.add("Change Password", Change_Password);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().add(tab);
        setVisible(true);
    }
    
    @Override
    public void onLogout() {
        LogoutHandler.logout(this, this);
    }
}

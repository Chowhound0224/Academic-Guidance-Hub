package com.mycompany.oodj_assignment;

import javax.swing.JOptionPane;
import com.mycompany.oodj_assignment.ui.Login_Window_UI;

public class Login_Window {
    private static Login_Window_UI LW_UI;

    public Login_Window(Login_Window_UI LW_UI) {
        this.LW_UI = LW_UI;
        FileCleaner.removeEmptyLines("AvailableSlot.txt");
        FileCleaner.removeEmptyLines("userdata.txt");
        FileCleaner.removeEmptyLines("StudentAssignment.txt");
        FileCleaner.removeEmptyLines("Timetable.txt");
    }

    public static void verification(String username, String password) {
        User login = null;
        boolean userFound = false;
        boolean permissionDenied = false;

        for (User user : OODJ_Assignment.allUsers) {
            if (user.getId().equals(username) && user.getPassword().equals(password)) {
                if ("ACTIVE".equals(user.getStatus())) {
                    login = user;
                    OODJ_Assignment.currentUser = user;
                    userFound = true;
                    break;
                } else {
                    permissionDenied = true;
                }
            }
        }

        if (userFound) {
            if (OODJ_Assignment.currentUser != null) {
                LW_UI.setVisible(false);
                String Role = OODJ_Assignment.currentUser.getRole();
                String Name = OODJ_Assignment.currentUser.getName();
                if (Role.equals("Admin")) {
                    Admin_Menu AdminMenu = new Admin_Menu(username, password,Name,Role);
                    AdminMenu.setVisible(true);
                } else if (Role.equals("Lecturer") || Role.equals("Lecturer-SP") ||
                        Role.equals("Lecturer-SM") || Role.equals("Lecturer-SP-SM")) {
                    Lecturer_Menu LecturerMenu = new Lecturer_Menu(username, password,Name,Role);
                    LecturerMenu.setVisible(true);
                } else if (Role.equals("Student")) {
                    Student_Menu app = new Student_Menu(username, password,Name,Role);
                    app.setVisible(true);
                } else if (Role.equals("Lecturer-PM")) {
                    Project_Manager_Model model = new Project_Manager_Model();
                    Project_Manager_Menu_View view = new Project_Manager_Menu_View(model);
                    new Project_Manager_Menu_Controller(username, password,Name,
                            Role, view, model, new LogoutCallback() {
                        @Override
                        public void onLogout() {
                            LW_UI.Username.setText("");
                            LW_UI.Password.setText("");
                            LW_UI.setVisible(true);
                        }
                    } );
                }
            }
        } else {
            if (permissionDenied) {
                JOptionPane.showMessageDialog(LW_UI, "Permission Denied!");
            } else {
                JOptionPane.showMessageDialog(LW_UI, "Login failed. Please check your credentials.");
            }
            OODJ_Assignment.currentUser = null;
        }
    }
}

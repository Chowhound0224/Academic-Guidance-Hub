package com.mycompany.oodj_assignment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;

import com.mycompany.oodj_assignment.ui.Login_Window_UI;
import java.io.FileWriter;
import java.time.LocalDate;

interface LogoutCallback {
    void onLogout();
}

public class OODJ_Assignment {
    
    public static Login_Window_UI LW_UI = new Login_Window_UI();
    public static Login_Window LW = new Login_Window(LW_UI);
    public static ArrayList<User> allUsers = new ArrayList<>();
    public static User currentUser = null;
    
    public static void readAllUsers () {
        try (BufferedReader br = new BufferedReader(new FileReader("userdata.txt"))){
            String line;
            while ((line = br.readLine()) != null) {
                String[] UserdataArray = line.split(",");
                String UserID = UserdataArray[0];
                String Password = UserdataArray[1];
                String Name = UserdataArray[2];
                String IntakeCode = UserdataArray[3];
                String UserRole = UserdataArray[4];
                String UserStatus = UserdataArray[5];
                String LastChangePasswordDate = UserdataArray[6];
                
                try (BufferedReader read = new BufferedReader(new FileReader("userPersonalData.txt"))){
                    String Line;
                    while ((Line = read.readLine()) != null) {
                        String[] UserPersonalDate = Line.split(",");
                        String ID = UserPersonalDate[0];
                        String Age = UserPersonalDate[3];
                        String ic = UserPersonalDate[4];
                        String gender = UserPersonalDate[5];
                        String race = UserPersonalDate[6];
                        String religion = UserPersonalDate[7];
                        String nasionality = UserPersonalDate[8];
                        String maritalStatus = UserPersonalDate[9];
                        String contact = UserPersonalDate[10];
                        String address = UserPersonalDate[11];
                        String postcode = UserPersonalDate[12];
                        String state = UserPersonalDate[13];
                        String email = UserPersonalDate[14];

                        if (UserID.equals(ID)) {
                            User user = new User(UserID,Password,IntakeCode,Name,Age,
                                    ic,gender,race,religion,nasionality,maritalStatus,contact,
                                    address,postcode,state,email,UserRole,LastChangePasswordDate,UserStatus) {
                            @Override
                            public void saveUserData(FileWriter writer) throws IOException {
                            }

                            @Override
                            public void saveAccountData(FileWriter writer, LocalDate date) throws IOException {
                            }
                            };
                            allUsers.add(user);
                        }
                    }
                } catch (IOException e) {
                    System.err.print("Error reading the file: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.print("Error reading the file: " + e.getMessage());
        }
    }
    
    public static void updateAllUserRoles(String userId, String role) {
        for (User user : allUsers) {
            if (user.getId().equals(userId)) {
                user.setRole(role);
                break;
            }
        }
    }
    
    public static void main(String[] args) {
        readAllUsers();
        LW_UI.setVisible(true);
    }
}


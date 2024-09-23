package com.mycompany.oodj_assignment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager extends UserRegistrationPanel{
    
    public static List<UserModel> loadUsersFromFile(String fileName, LogoutCallback callback) {
        List<UserModel> users = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                UserModel user = new UserModel();
                user.setId(data[0]);
                user.setName(data[1]);
                user.setIntakeCode(data[2]);
                user.setAge(data[3]);
                user.setIcNumber(data[4]);
                user.setGender(data[5]);
                user.setRace(data[6]);
                user.setReligion(data[7]);
                user.setNationality(data[8]);
                user.setMaritalStatus(data[9]);
                user.setContact(data[10]);
                user.setPostcode(data[11]);
                user.setState(data[12]);
                user.setAddress(data[13]);
                user.setEmail(data[14]);
                user.setSchoolWise(data[15]);
                users.add(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void saveUserToFile(String fileName, UserModel user) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(user.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateUsersToFile(String fileName, List<UserModel> users) {
        try (FileWriter writer = new FileWriter(fileName)) {
            for (UserModel user : users) {
                writer.write(user.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileManager(LogoutCallback callback, UserEditPanel userEditPanel) {
        super(callback,userEditPanel);
    }

}

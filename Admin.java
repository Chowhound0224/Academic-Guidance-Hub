package com.mycompany.oodj_assignment;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

class Admin extends User {
    public Admin(String id, String password, String name, String age, String ic, String gender,
            String race, String religion, String nationality, String maritalStatus, String contact,
            String address, String postcode, String state, String email, String schoolwise, String role,String date, String status) {
        super(id, password, name, age, ic, gender, race, religion, nationality,
                maritalStatus, contact, address, postcode, state, email, schoolwise,role, date,status);
    }

    @Override
    public void saveUserData(FileWriter writer) throws IOException {
        String contactData = id + ",-," + name + "," + age + "," + ic + "," + gender +
                "," + race + "," + religion + "," + nationality + "," + maritalStatus
                + "," + contact + "," + address + "," + postcode + "," + state + "," + email + ",\n";
        writer.write(contactData);
    }

    @Override
    public void saveAccountData(FileWriter writer, LocalDate date) throws IOException {
        String dateBefore = date.toString();
        String adminData = id + "," + password + "," + name + ",-," + role + ",ACTIVE," + dateBefore + ",\n";
        writer.write(adminData);
    }
}

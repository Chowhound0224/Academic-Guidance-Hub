package com.mycompany.oodj_assignment;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

class Lecturer extends User {
    private String schoolWise;
    private String dateBefore;

    public Lecturer(String id, String password, String name, String age, String ic, String gender,
            String race, String religion, String nationality, String maritalStatus, String contact,
            String address, String postcode, String state, String email, String schoolWise, String role, String date,String status) {
        super(id, password, name, age, ic, gender, race, religion, nationality,
                maritalStatus, contact, address, postcode, state, email, schoolWise,role, date, status);
        this.schoolWise = schoolWise;
    }
    
    @Override
    public void saveUserData(FileWriter writer) throws IOException {
        String contactData = id + "," + schoolWise + "," + name + "," +
                age + "," + ic + "," + gender + "," + race + "," + religion + "," + nationality
                + "," + maritalStatus + "," + contact + "," + address + "," + postcode + "," + state + "," + email + ",\n";
        writer.write(contactData);
    }

    @Override
    public void saveAccountData(FileWriter writer,LocalDate date) throws IOException {
        dateBefore = date.toString();
        String lecturerData = id + "," + password + "," + name + "," + schoolWise + "," + role + ",ACTIVE," + dateBefore + ",\n";
        writer.write(lecturerData);
    }
}

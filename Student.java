package com.mycompany.oodj_assignment;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

class Student extends User {
    private String intakeCode;
    private String dateBefore;

    public Student(String id, String password,String name, String age, String ic,
            String gender, String race, String religion, String nationality,
            String maritalStatus, String contact, String address, String postcode,
            String state, String email, String intakeCode,String role, String date, String status) {
        super(id, password, name, age, ic, gender, race, religion, nationality,
                maritalStatus, contact, address, postcode, state, email,intakeCode,role,date,status);
        this.intakeCode = intakeCode;
        this.name = name;
    }

    @Override
    public void saveUserData(FileWriter writer) throws IOException {
        String contactData = id + "," + intakeCode + "," + name + "," + age + "," + ic + "," +  
                gender + "," + race + "," + religion + "," + nationality + "," + maritalStatus +
                "," + contact + "," + address + "," + postcode + "," + state + "," + email + ",\n";
        writer.write(contactData);
    }

    @Override
    public void saveAccountData(FileWriter writer,LocalDate date) throws IOException {
        dateBefore = date.toString();
        String studentData = id + "," + password + "," + name + "," + intakeCode + "," + role + ",ACTIVE," + dateBefore + ",\n";
        writer.write(studentData);
    }
}

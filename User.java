package com.mycompany.oodj_assignment;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

abstract class User {
    protected String id;
    protected String password;
    protected String schoolwise;
    protected String name;
    protected String age;
    protected String ic;
    protected String gender;
    protected String race;
    protected String religion;
    protected String nationality;
    protected String maritalStatus;
    protected String contact;
    protected String address;
    protected String postcode;
    protected String state;
    protected String email;
    protected String role;
    protected String date;
    protected String status;

    public User(String id, String password, String name, String age, String ic, String gender, String race,
            String religion, String nationality, String maritalStatus, String contact, String address,
            String postcode, String state, String email, String schoolwise,String role, String date, String status) {
        this.id = id;
        this.password = password;
        this.schoolwise = schoolwise;
        this.name = name;
        this.age = age;
        this.ic = ic;
        this.gender = gender;
        this.race = race;
        this.religion = religion;
        this.nationality = nationality;
        this.maritalStatus = maritalStatus;
        this.contact = contact;
        this.address = address;
        this.postcode = postcode;
        this.state = state;
        this.email = email;
        this.role = role;
        this.date = date;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchoolwise() {
        return schoolwise;
    }

    public void setSchoolwise(String schoolwise) {
        this.schoolwise = schoolwise;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public void saveUserData(FileWriter writer) throws IOException {
        String contactData = id + "," + "-" + "," + name + "," + age + "," + ic + "," +
                gender + "," + race + "," + religion + "," + nationality + "," + maritalStatus +
                "," + contact + "," + address + "," + postcode + "," + state + "," + email + ",\n";
        writer.write(contactData);
    }

    public void saveAccountData(FileWriter writer, LocalDate date) throws IOException {
        String studentData = id + "," + password + "," + name + "," + "-" + "," + role + ",ACTIVE," + date.toString() + ",\n";
        writer.write(studentData);
    }

}

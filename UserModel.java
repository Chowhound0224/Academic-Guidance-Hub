package com.mycompany.oodj_assignment;

public class UserModel {
    private String id;
    private String password;
    private String name;
    private String role;
    private String intakeCode;
    private String age;
    private String icNumber;
    private String gender;
    private String race;
    private String religion;
    private String nationality;
    private String maritalStatus;
    private String contact;
    private String postcode;
    private String state;
    private String address;
    private String email;
    private String schoolWise;
    private String status;
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntakeCode() {
        return intakeCode;
    }

    public void setIntakeCode(String intakeCode) {
        this.intakeCode = intakeCode;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSchoolWise() {
        return schoolWise;
    }

    public void setSchoolWise(String schoolWise) {
        this.schoolWise = schoolWise;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getLastChangedPasswordDate() {
        return date;
    }

    public void setLastChangedPasswordDate(String date) {
        this.date = date;
    }

    public String[] toArray(String role) {
        if (role.equals("Student")) {
            return new String[]{id, password, name, intakeCode, date, age,
                icNumber,gender, race, religion, nationality, maritalStatus, contact, postcode, state, address, email};
        } else if (role.equals("Lecturer")||role.equals("Lecturer-SP")||role.equals("Lecturer-PM")||role.equals("Lecturer-SM")) {
            return new String[]{id, password, name, schoolWise,date,  age,
                icNumber,gender, race, religion, nationality, maritalStatus, contact, postcode, state, address, email};
        } else if (role.equals("Admin")) {
            return new String[]{id, password, name,"-", date, age, icNumber,
                gender, race, religion, nationality, maritalStatus, contact, postcode, state, address, email};
        } else {   
            return null;
        }
    }


    public String toString(String role) {
        if (role.equals("Student")) {
            return id +","+password+","+intakeCode + "," + name + "," + date
                    + "," + age + "," + icNumber + ","+gender+","+ race +
                    "," + religion + ","+nationality+","+maritalStatus+","+contact+","+address+","+postcode+","+state+","+email+"\n";
        } else if (role.equals("Lecturer")||role.equals("Lecturer-SP")||role.equals("Lecturer-PM")||role.equals("Lecturer-SM")) {
            return id +","+password+","+schoolWise + "," + name + "," + date
                    + "," + age + "," + icNumber + ","+ gender+","+ race +
                    "," + religion + ","+nationality+","+maritalStatus+","+contact+","+address+","+postcode+","+state+","+email+"\n";
        } else {
            return id +","+password+","+ "-" + "," + name + "," + age + date
                    + "," + "," + icNumber + "," + gender+","+ race + "," +
                    religion + ","+nationality+","+maritalStatus+","+contact+","+address+","+postcode+","+state+","+email+"\n";
        }
    }
    

}
       
    

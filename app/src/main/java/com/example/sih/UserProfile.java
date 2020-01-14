package com.example.sih;

public class UserProfile {
    public String userEmail;
    public String userName;
    public String dob;
    public String location;
    public String pincode;
    public String phonenumber;

    public UserProfile(){

    }

    public UserProfile(String userEmail, String userName, String dob, String location, String pincode, String phonenumber) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.dob = dob;
        this.location = location;
        this.pincode = pincode;
        this.phonenumber = phonenumber;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getdob() {
        return dob;
    }

    public void setdob(String dob) {
        this.dob = dob;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
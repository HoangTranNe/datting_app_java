package com.example.testfirebase.model;

public class Users {
    public String phone, profile;
    public String idcompat;
    public String email;
    public String password;
    public String name;
    public String gender;
    public String date;
    public String aim;
    public Integer verify;
    public String job;
    public String school;
    public String city;
    public String vipstatus;
    public String status;

    public Users(){}

    public Users(String Phone, String Name, String Idcompat, String Date, String Email, String Password, String Gender, String Aim, Integer Verify, String Job, String School, String City, String Vipstatus, String Status)
    {
        this.aim = Aim;
        this.city = City;
        this.email = Email;
        this.date = Date;
        this.gender = Gender;
        this.idcompat = Idcompat;
        this.job = Job;
        this.name = Name;
        this.password = Password;
        this.phone = Phone;
        this.school = School;
        this.status = Status;
        this.vipstatus = Vipstatus;
        this.verify = Verify;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getIdcompat() {
        return idcompat;
    }
    public void setIdcompat(String idcompat) {
        this.idcompat = idcompat;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public Integer getVerify() {
        return verify;
    }
    public void setVerify(Integer verify) {
        this.verify = verify;
    }

    public String getAim() {
        return aim;
    }
    public void setAim(String aim) {
        this.aim = aim;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getJob() {
        return job;
    }
    public void setJob(String job) {
        this.job = job;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getVipstatus() {
        return vipstatus;
    }
    public void setVipstatus(String vipstatus) {
        this.vipstatus = vipstatus;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}

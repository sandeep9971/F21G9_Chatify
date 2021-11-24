package com.example.chatify.models;



public class SignUpModel {
    String name,email,platform,phone,uid,status,online_status;
    public SignUpModel(){}
    public SignUpModel(String name, String email, String phone, String platform, String uid,String status,String online_status) {
        this.name = name;
        this.email = email;
        this.platform = platform;

        this.uid = uid;
        this.status=status;
        this.online_status=online_status;

        this.phone=phone;
    }

    public String getOnline_status() {
        return online_status;
    }

    public void setOnline_status(String online_status) {
        this.online_status = online_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }



    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

package com.example.chatify.models;



public class SignUpModel {
    String name,email,imgUrl,platform,token,phone,uid,status;
    public SignUpModel(){}
    public SignUpModel(String name, String email, String phone, String platform, String token, String uid, String imgUrl,String status) {
        this.name = name;
        this.email = email;
        this.imgUrl=imgUrl;
        this.platform = platform;
        this.token = token;
        this.uid = uid;
        this.status=status;


        this.phone=phone;
    }
    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

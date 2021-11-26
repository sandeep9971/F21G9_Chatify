package com.example.chatify.models;



public class ChatModel {
    String senderUserId,msg,senderUserImg,senderName,chatRoom,randomKey,message_status,message_type;
    long chattime;

    public ChatModel() {
    }

    public ChatModel(String msg, String senderName, String senderUserId, String senderUserImg, String chatRoom, String randomKey,String message_status,String message_type,long chattime) {


        this.senderUserId = senderUserId;
        this.msg = msg;
        this.senderUserImg = senderUserImg;
        this.chattime = chattime;
        this.senderName=senderName;
        this.chatRoom=chatRoom;
        this.randomKey=randomKey;
        this.message_status=message_status;
        this.message_type=message_type;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getMessage_status() {
        return message_status;
    }

    public void setMessage_status(String message_status) {
        this.message_status = message_status;
    }

    public String getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(String randomKey) {
        this.randomKey = randomKey;
    }

    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public long getChattime() {

        return chattime;
    }

    public void setChattime(long chattime) {
        this.chattime = chattime;
    }

    public String getsenderUserId() {
        return senderUserId;
    }

    public void setsenderUserId(String senderUserId) {
        this.senderUserId = senderUserId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getsenderUserImg() {
        return senderUserImg;
    }

    public void setsenderUserImg(String senderUserImg) {
        this.senderUserImg = senderUserImg;
    }



}

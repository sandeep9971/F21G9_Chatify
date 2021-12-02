package com.example.chatify.models;

import androidx.annotation.NonNull;



public class InboxModel implements Comparable<InboxModel> {
    String lastMsg,oppName,oppUid,oppImg,chatRoom,tokenId,typingStatus;
    long inboxtime,unread_count;

    public InboxModel() {
    }



    public InboxModel(String lastMsg, String oppImg, String oppName, String oppUid, String chatRoom,  long unread_count,long inboxtime) {
        this.lastMsg = lastMsg;
        this.oppName = oppName;
        this.oppUid = oppUid;
        this.oppImg = oppImg;
        this.chatRoom=chatRoom;
this.unread_count=unread_count;
        this.inboxtime = inboxtime;


    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    public String getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(String chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getLastMsg() {
        return lastMsg;
    }

    public void setLastMsg(String lastMsg) {
        this.lastMsg = lastMsg;
    }

    public String getTypingStatus() {
        return typingStatus;
    }

    public void setTypingStatus(String typingStatus) {
        this.typingStatus = typingStatus;
    }

    public long getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(long unread_count) {
        this.unread_count = unread_count;
    }

    public String getOppName() {
        return oppName;
    }

    public void setOppName(String oppName) {
        this.oppName = oppName;
    }

    public String getOppUid() {
        return oppUid;
    }

    public void setOppUid(String oppUid) {
        this.oppUid = oppUid;
    }

    public String getOppImg() {
        return oppImg;
    }

    public void setOppImg(String oppImg) {
        this.oppImg = oppImg;
    }

    public long getInboxtime() {
        return inboxtime;
    }

    public void setInboxtime(long inboxtime) {
        this.inboxtime = inboxtime;
    }

    @Override
    public int compareTo(@NonNull InboxModel o) {
        return Long.valueOf(this.getInboxtime()).compareTo(Long.valueOf(o.getInboxtime()));
    }
}

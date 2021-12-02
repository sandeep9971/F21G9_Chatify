package com.example.chatify;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;



public class Constant {
    public static final String MyPref="Pref";
    public static final String IsLoggedIn="islog";
    public static final String Email="email";
    public static final String Phone="phone";
    public static final String UID="uid";
    public static final String NAME="name";
    public static final String ImageUrl="imageUrl";
    public static final String CHATROOM="chatroom";
    public static final int CAMERA_REQUEST_CODE=1;
    public static final int REQUEST_CAMERA=200;
    public static final int READ_EXTERNAL_STORAGE=300;
    public static final int LoginUserMsgsend=0;
    public static final int SecondUserMsgSend=1;
    public static final String Token="token";
    public static final String Status="status";
    //Database
    public static final String DATABASE_NAME="Login";
    public static final String TABLE_USER="userTable";
    public static final String TABLE_INBOX="inboxTable" ;
    public static final String TABLE_MSG="msgTable";
    public static final int DATABASE_VERSION=1;
    //UserTable
    public  static final String USER_USERNAME="username";
    public static final String USER_USERIMAGE="userimage";
    public static final String USER_USERUID="useruid";
    public static final String USER_USERPHONE="userphone";
    public static final String USER_USERTOKEN="usertoken";
    public static final String USER_USEREMAIL="useremail";
    public static final String USER_PLATFORM="platform";
    public static final String USER_STATUS="status";
   //InboxTable
    public static final String INBOX_OPPNAME="oppuserName";
    public static final String INBOX_OPPUID="inboxOppUid";
    public static final String INBOX_OPPIMG="inboxOppimg";
    public static final String INBOX_CHATROOM="inboxChatRoom";
    public static final String INBOX_TOKENID="inboxTokenId";
    public static final String INBOX_LASTMSG="inboxLastMsg";
    public static final String INBOX_MSGTIME ="inboxmsgtime";
    public static final String  INBOX_UNREADCOUNT="inbox_unread_count";
    //MsgTable

    public static final String MSG_SENDERUSERID="msgSenderUserId";
    public static final String MSG_SENDERIMG="msgSenderImg";
    public static final String MSG_SENDERNAME="msgSenderName";
    public static final String MSG_CHATROOM="msgChatRoom";
    public static final String MSG_MESSAGE="msg";
    public static final String MSG_CHATTIME="inboxChattime";
    public static final String MSG_RANDOMKEY="randomKey";
    public static final String MSG_MSGTYPE="msgtype";


    public static boolean isConnectedToNetwork(Context thisActivity) {
        ConnectivityManager connMgr = (ConnectivityManager) thisActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnected();
    }
}

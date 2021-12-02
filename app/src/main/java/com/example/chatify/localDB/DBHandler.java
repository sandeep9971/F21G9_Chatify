package com.example.chatify.localDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import  com.example.chatify.models.ChatModel;
import  com.example.chatify.models.InboxModel;
import  com.example.chatify.models.SignUpModel;

import java.util.ArrayList;
import java.util.HashMap;

import static  com.example.chatify.Constant.DATABASE_NAME;
import static  com.example.chatify.Constant.DATABASE_VERSION;
import static  com.example.chatify.Constant.INBOX_CHATROOM;
import static  com.example.chatify.Constant.INBOX_LASTMSG;
import static  com.example.chatify.Constant.INBOX_MSGTIME;
import static  com.example.chatify.Constant.INBOX_OPPIMG;
import static  com.example.chatify.Constant.INBOX_OPPNAME;
import static  com.example.chatify.Constant.INBOX_OPPUID;
import static  com.example.chatify.Constant.INBOX_TOKENID;
import static  com.example.chatify.Constant.INBOX_UNREADCOUNT;
import static  com.example.chatify.Constant.MSG_CHATROOM;
import static  com.example.chatify.Constant.MSG_CHATTIME;
import static  com.example.chatify.Constant.MSG_MESSAGE;
import static  com.example.chatify .Constant.MSG_MSGTYPE;
import static  com.example.chatify .Constant.MSG_RANDOMKEY;
import static  com.example.chatify .Constant.MSG_SENDERIMG;
import static  com.example.chatify .Constant.MSG_SENDERNAME;
import static  com.example.chatify .Constant.MSG_SENDERUSERID;
import static  com.example.chatify .Constant.TABLE_INBOX;
import static  com.example.chatify .Constant.TABLE_MSG;
import static  com.example.chatify .Constant.TABLE_USER;
import static  com.example.chatify .Constant.USER_PLATFORM;
import static  com.example.chatify .Constant.USER_STATUS;
import static  com.example.chatify .Constant.USER_USEREMAIL;
import static  com.example.chatify .Constant.USER_USERIMAGE;
import static  com.example.chatify .Constant.USER_USERNAME;
import static  com.example.chatify .Constant.USER_USERPHONE;
import static  com.example.chatify .Constant.USER_USERTOKEN;
import static  com.example.chatify .Constant.USER_USERUID;



public class DBHandler extends SQLiteOpenHelper {
    Context context;

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUser = "Create table " + TABLE_USER + "(" + USER_USEREMAIL + " Text, " + USER_USERUID + " Text PRIMARY KEY , " +
                USER_USERNAME + " Text , " + USER_USERTOKEN + " Text , " + USER_USERPHONE + " Text , " + USER_USERIMAGE +
                " Text , " + USER_PLATFORM + " Text , " + USER_STATUS + " Text " + " );";
        db.execSQL(createUser);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INBOX);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MSG);
        onCreate(db);

    }

    public void insertUser(SignUpModel signUpModel) {
        Log.e("insert", "========");
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_USEREMAIL, signUpModel.getEmail());
        contentValues.put(USER_USERUID, signUpModel.getUid());
        contentValues.put(USER_USERNAME, signUpModel.getName());
        contentValues.put(USER_USERTOKEN, signUpModel.getToken());
        contentValues.put(USER_USERPHONE, signUpModel.getPhone());
        contentValues.put(USER_USERIMAGE, signUpModel.getImgUrl());
        contentValues.put(USER_PLATFORM, signUpModel.getPlatform());
        contentValues.put(USER_STATUS, signUpModel.getStatus());
       sqLiteDatabase.insertWithOnConflict(TABLE_USER, USER_USERUID, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void insertInbox(InboxModel inboxModel) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(INBOX_OPPUID, inboxModel.getOppUid());
        contentValues.put(INBOX_OPPNAME, inboxModel.getOppName());
        contentValues.put(INBOX_OPPIMG, inboxModel.getOppImg());
        contentValues.put(INBOX_TOKENID, inboxModel.getTokenId());
        contentValues.put(INBOX_CHATROOM, inboxModel.getChatRoom());
        contentValues.put(INBOX_LASTMSG, inboxModel.getLastMsg());
        contentValues.put(String.valueOf(INBOX_MSGTIME), inboxModel.getInboxtime());
        contentValues.put(String.valueOf(INBOX_UNREADCOUNT),inboxModel.getUnread_count());

      sqLiteDatabase.insertWithOnConflict(TABLE_INBOX, INBOX_CHATROOM, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public ArrayList<SignUpModel> Userfetch() {
        ArrayList<SignUpModel> list = new ArrayList<SignUpModel>();
        String select = "Select * from " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.getCount() != -1) {
            if (cursor.moveToFirst()) {
                do {
                    SignUpModel model = new SignUpModel();
                    model.setEmail(cursor.getString(0));
                    model.setUid(cursor.getString(1));
                    model.setName(cursor.getString(2));
                    model.setToken(cursor.getString(3));
                    model.setPhone(cursor.getString(4));
                    model.setImgUrl(cursor.getString(5));
                    model.setPlatform(cursor.getString(6));
                    model.setStatus(cursor.getString(7));

                    list.add(model);

                } while (cursor.moveToNext());
            }
        }
        return list;
    }


 public void deleteAll()
    {
        SQLiteDatabase db=this.getWritableDatabase();


        db.execSQL("Delete from " + TABLE_USER);
    }



}

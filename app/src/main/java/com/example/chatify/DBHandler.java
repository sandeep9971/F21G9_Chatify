package com.example.chatify;

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

        String createInbox = "Create table " + TABLE_INBOX + "(" + INBOX_OPPUID + " Text," + INBOX_OPPNAME + " Text , " +
                INBOX_OPPIMG + " Text , " + INBOX_TOKENID + " Text , " + INBOX_CHATROOM + " Text PRIMARY KEY, " + INBOX_LASTMSG + " Text , " + INBOX_MSGTIME + " Long , " +  INBOX_UNREADCOUNT + " Long " + "  );";
        db.execSQL(createInbox);

        String createMsg = "Create table " + TABLE_MSG + "(" + MSG_SENDERUSERID + " Text," + MSG_SENDERNAME + " Text," +
                MSG_SENDERIMG + " Text , " + MSG_CHATROOM + " Text , " + MSG_MESSAGE + " Text , " + MSG_CHATTIME + " Long  PRIMARY KEY , " + MSG_RANDOMKEY + " Text , " + MSG_MSGTYPE  + " TEXT " + ");";
        db.execSQL(createMsg);
        Log.e("tablecreate", " " + createUser + createInbox + createMsg);

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
               contentValues.put(USER_USERPHONE, signUpModel.getPhone());
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

    public void insertMsg(ChatModel chatModel,String randomkey) {
        if(!CheckIsMsgAlreadyInDBorNot(randomkey)) {
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MSG_SENDERUSERID, chatModel.getsenderUserId());
            contentValues.put(MSG_SENDERNAME, chatModel.getSenderName());
            contentValues.put(MSG_SENDERIMG, chatModel.getsenderUserImg());
            contentValues.put(MSG_CHATROOM, chatModel.getChatRoom());
            contentValues.put(MSG_MESSAGE, chatModel.getMsg());
            contentValues.put(MSG_RANDOMKEY, chatModel.getRandomKey());
            contentValues.put(String.valueOf(MSG_CHATTIME), chatModel.getChattime());
            contentValues.put(MSG_MSGTYPE,chatModel.getMessage_type());
            sqLiteDatabase.insertWithOnConflict(TABLE_MSG, MSG_CHATTIME, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public boolean CheckIsDataAlreadyInDBorNot(String chatroom) {
        try {
            SQLiteDatabase sqldb = this.getReadableDatabase();
            String Query = "Select * from " + TABLE_INBOX + " where " + INBOX_CHATROOM + " = '" + chatroom +"'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        } catch (Exception e){
            return false;
        }

    }


    public void checkInsert(InboxModel inboxModel, String chatroom){
        if (CheckIsDataAlreadyInDBorNot(chatroom)){
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues contentValues=new ContentValues();
            contentValues.put(INBOX_OPPUID, inboxModel.getOppUid());
            contentValues.put(INBOX_OPPNAME, inboxModel.getOppName());
            contentValues.put(INBOX_OPPIMG, inboxModel.getOppImg());
            contentValues.put(INBOX_TOKENID, inboxModel.getTokenId());
            contentValues.put(INBOX_CHATROOM, inboxModel.getChatRoom());
            contentValues.put(INBOX_LASTMSG, inboxModel.getLastMsg());
            contentValues.put(String.valueOf(INBOX_MSGTIME), inboxModel.getInboxtime());
            contentValues.put(String.valueOf(INBOX_UNREADCOUNT),inboxModel.getUnread_count());
            long i = db.update(TABLE_INBOX,contentValues,INBOX_CHATROOM + " = ? ",new String[]{chatroom});
            if (i!=-1){
            }

        } else {
            insertInbox(inboxModel);
        }
    }

    public ArrayList<SignUpModel> Userfetch() {
        Log.e("User fetch data" , "==========");
        ArrayList<SignUpModel> list = new ArrayList<SignUpModel>();
        String select = "Select * from " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        Log.e("Cursor Count : ", "" + cursor.getCount());
        if (cursor.getCount() != -1) {
            if (cursor.moveToFirst()) {
                do {
                    Log.e("User fetch data" , "innnn");
                    SignUpModel model = new SignUpModel();
                    model.setEmail(cursor.getString(0));
                    model.setUid(cursor.getString(1));
                    model.setName(cursor.getString(2));

                    model.setPhone(cursor.getString(4));

                    model.setPlatform(cursor.getString(6));
                    model.setStatus(cursor.getString(7));

                    list.add(model);

                    Log.e("User fetch data" , "==========" + list.size());
                } while (cursor.moveToNext());
            }
        }
        return list;
    }



    public ArrayList<InboxModel> Inboxfetch() {
        ArrayList<InboxModel> list1 = new ArrayList<InboxModel>();
        String select = "Select * from " + TABLE_INBOX;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(select, null);
        if (cursor.getCount() != -1) {
            if (cursor.moveToFirst()) {
                do {

                    InboxModel model = new InboxModel();
                    model.setOppUid(cursor.getString(0));
                    model.setOppName(cursor.getString(1));
                    model.setOppImg(cursor.getString(2));
                    model.setTokenId(cursor.getString(3));
                    model.setChatRoom(cursor.getString(4));
                    model.setLastMsg(cursor.getString(5));
                    model.setInboxtime(Long.parseLong(cursor.getString(6)));
                    model.setUnread_count(Long.parseLong(cursor.getString(7)));
                    list1.add(model);
                } while (cursor.moveToNext());
            }
        }
        return list1;
    }

    public ArrayList<ChatModel> msgFetchOld(String chatroomName) {
        ArrayList<ChatModel> list = new ArrayList<ChatModel>();
        String select = "Select * from " + TABLE_MSG + " where " + MSG_CHATROOM + " = '" + chatroomName + "' ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                ChatModel model = new ChatModel();
                model.setsenderUserId(cursor.getString(0));
                model.setSenderName(cursor.getString(1));
                model.setsenderUserImg(cursor.getString(2));
                model.setChatRoom(cursor.getString(3));
                model.setMsg(cursor.getString(4));
                model.setChattime(Long.parseLong(cursor.getString(5)));
                model.setRandomKey(cursor.getString(6));
                model.setMessage_type(cursor.getString(7));
                list.add(model);
            } while (cursor.moveToNext());
        }

        return list;


    }


    public HashMap<String, ChatModel> msgFetch(String chatroomName) {
        HashMap<String, ChatModel> hashMap = new HashMap<>();
        String select = "Select * from " + TABLE_MSG;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);
       if (cursor.moveToFirst()) {
            do {

                ChatModel model = new ChatModel();
                model.setsenderUserId(cursor.getString(0));
                model.setSenderName(cursor.getString(1));
                model.setsenderUserImg(cursor.getString(2));
                model.setChatRoom(cursor.getString(3));
                model.setMsg(cursor.getString(4));
                model.setChattime(Long.parseLong(cursor.getString(5)));
                model.setRandomKey(cursor.getString(6));
                hashMap.put(model.getRandomKey(), model);
            } while (cursor.moveToNext());
        }

        return hashMap;
    }
    public void deleteAll()
    {
        SQLiteDatabase db=this.getWritableDatabase();

        db.execSQL("Delete from " + TABLE_MSG);
        db.execSQL("Delete from " + TABLE_INBOX);
        db.execSQL("Delete from " + TABLE_USER);
    }

    public boolean CheckIsMsgAlreadyInDBorNot(String randomkey) {
        try {
            SQLiteDatabase sqldb = this.getReadableDatabase();
            String Query = "Select * from " + TABLE_MSG + " where " + MSG_RANDOMKEY + " = '" + randomkey +"'";
            Cursor cursor = sqldb.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        } catch (Exception e){
            return false;
        }

    }

}

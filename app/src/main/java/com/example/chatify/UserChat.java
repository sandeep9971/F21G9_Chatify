package com.example.chatify;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatify.localDB.DBHandler;
import com.example.chatify.models.ChatModel;
import com.example.chatify.models.MsgAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.chatify.Constant.CAMERA_REQUEST_CODE;
import static com.example.chatify.Constant.MyPref;
import static com.example.chatify.Constant.READ_EXTERNAL_STORAGE;
import static com.example.chatify.Constant.REQUEST_CAMERA;
import static com.example.chatify.Constant.isConnectedToNetwork;

public class UserChat extends AppCompatActivity {
    HashMap<String, ChatModel> hashMap = new HashMap<>();
    EditText edMsgSend;
    boolean msgTypeImg,isCamera;
    MsgAdapter msgAdapter;
    ImageView  btnMsgSend, imgBack;
    TextView userMsgName;
    String intentuserName, intentuidSecondUser, chatroomUser1, chatroomUser2, ChatRoom,
            msgType,senderUID, randomKey, message, imgIntent, senderImg, senderName, intentPhone,
            intentToken, senderphone;
    DatabaseReference refChat, createChatUid, refInbox, refInbox1, refInboxUser1, refInboxUser2, unreadCountSet, refUsers;
    SharedPreferences sharedPreferences;
    List<ChatModel> chatList;
    RecyclerView recyclerView;
    DBHandler db;
    List<String> randomkeyStore;
    ChatModel chatModel;
    long count;
    Bundle extras;
    Uri selectedImage,resultUri,downloadUrl;
    Cursor cursor;
    StorageReference  mStorageRef;
    Boolean refChatCall;


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            try {
                count = dataSnapshot.getValue(long.class);
            } catch (Exception e) {
                count = 0;
            }
            Log.e("COUNT VALUE", "128  " + count);
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            if (dataSnapshot != null) {
                chatModel = dataSnapshot.getValue(ChatModel.class);
                unreadCountSet = FirebaseDatabase.getInstance().getReference("inbox").child(senderUID).child(ChatRoom);
                unreadCountSet.child("unread_count").setValue(0);

                long dayCheck=chatModel.getChattime();

                if (!randomkeyStore.contains(chatModel.getRandomKey())) {
                    chatList.add(chatModel);

                }
                if (msgAdapter != null) {
                    msgAdapter.notifyDataSetChanged();
                    recyclerView.scrollToPosition(chatList.size() - 1);
                }
            }
        }

        @Override public void onChildChanged(DataSnapshot dataSnapshot, String s) {  }
        @Override public void onChildRemoved(DataSnapshot dataSnapshot) { }
        @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
        @Override  public void onCancelled(DatabaseError databaseError) {  }
    };
    ValueEventListener checkRoomsListen = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (dataSnapshot == null) {
                ChatRoom = chatroomUser1;
            } else {
                if (dataSnapshot.hasChild(chatroomUser1)) {
                    ChatRoom = chatroomUser1;
                } else if (dataSnapshot.hasChild(chatroomUser2)) {
                    ChatRoom = chatroomUser2;
                } else {
                    ChatRoom = chatroomUser1;
                }
            }
            refChat.removeEventListener(checkRoomsListen);
            refInbox1 = refInbox.child(intentuidSecondUser).child(ChatRoom).child("unread_count");
            refInbox1.addValueEventListener(valueEventListener);
            chatFetch();
        }
        @Override  public void onCancelled(DatabaseError databaseError) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_chat);
        initViews();
        edMsgSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    if(!TextUtils.isEmpty(ChatRoom)) {
                        FirebaseDatabase.getInstance().getReference("inbox").child(senderUID).child(ChatRoom).child("typing_status")
                                .setValue("typing...");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        intentuserName = getIntent().getExtras().getString("frgInboxAdptr_name");
        intentPhone = getIntent().getExtras().getString("recylerAdpter_phoneNo");
        if (!TextUtils.isEmpty(intentuserName)) {
            imgIntent = getIntent().getExtras().getString("frgInboxAdptr_img");
            ChatRoom = getIntent().getExtras().getString("frgInboxAdptr_chatroom");
            intentToken = getIntent().getExtras().getString("frgInboxAdptr_tokenId");
            intentuidSecondUser = getIntent().getExtras().getString("frgInboxAdptr_uid");
            userMsgName.setText(intentuserName);

            chatroomUser1 = senderUID + "_" + intentuidSecondUser;
            chatroomUser2 = intentuidSecondUser + "_" + senderUID;
            refInbox1 = refInbox.child(intentuidSecondUser).child(ChatRoom).child("unread_count");
            refInbox1.addValueEventListener(valueEventListener);
            adpterSet();
            chatFetch();
        } else {
            intentuserName = getIntent().getExtras().getString("recylerAdpter_name");
            intentuidSecondUser = getIntent().getExtras().getString("recylerAdpter_uidmsg");
            userMsgName.setText(intentuserName);
            intentPhone = getIntent().getExtras().getString("recylerAdpter_phoneNo");
            intentToken = getIntent().getExtras().getString("recylerAdpter_token");
            imgIntent = getIntent().getExtras().getString("recylerAdpter_image");

            chatroomUser1 = senderUID + "_" + intentuidSecondUser;
            chatroomUser2 = intentuidSecondUser + "_" + senderUID;
            checkRooms();
            adpterSet();
        }

        sendbutton();

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserChat.this, Inbox.class);
                startActivity(intent);
                finish();
            }
        });

    }



    private void adpterSet() {
        final LinearLayoutManager linearLayout;
        linearLayout = new LinearLayoutManager( UserChat.this);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
       for (int i = 0; i < chatList.size(); i++) {
            randomkeyStore.add(chatList.get(i).getRandomKey());
        }
        msgAdapter = new MsgAdapter( UserChat.this, chatList, imgIntent);
        recyclerView.setAdapter(msgAdapter);
        recyclerView.scrollToPosition(hashMap.size() - 1);
    }

    private void chatFetch() {
        refChat.child(ChatRoom).addChildEventListener(childEventListener);
    }

    private void sendbutton() {
        btnMsgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               message = edMsgSend.getText().toString().trim();

                Log.e("UserChat","ed"+message);
                if(msgTypeImg==true)
                {
                   msgType="1";
                }
                else
                {
                    msgType="0";
                }
                if (!TextUtils.isEmpty(message)) {
                    if (isConnectedToNetwork( UserChat.this)) {

                        edMsgSend.setText("");
                        createChatUid = refChat.child(ChatRoom);
                        randomKey = refChat.push().getKey();
                        Log.e("Userchat","Rnadom"+ randomKey);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("msg", message);
                        hashMap.put("senderName", senderName);
                        hashMap.put("senderUserId", senderUID);
                        hashMap.put("senderUserImg", senderImg);
                        hashMap.put("chatRoom", ChatRoom);
                        hashMap.put("randomKey", randomKey);
                        hashMap.put("message_status","0");
                        hashMap.put("message_type",msgType);
                        hashMap.put("chattime", ServerValue.TIMESTAMP);
                        createChatUid.child(randomKey).setValue(hashMap);
                        msgTypeImg=false;
                        inboxdata();
                    } else {
                        Toast.makeText( UserChat.this, "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.rl_parent), "Message cannot be empty", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkRooms() {
        refChat.addValueEventListener(checkRoomsListen);
    }

    private void inboxdata() {
        refInboxUser1 = refInbox.child(senderUID).child(ChatRoom);
        final HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("lastMsg", message);
        hashMap.put("oppImg", imgIntent);
        hashMap.put("oppName", intentuserName);
        hashMap.put("oppUid", intentuidSecondUser);
        hashMap.put("chatRoom", ChatRoom);
        hashMap.put("tokenId", intentToken);
        hashMap.put("inboxtime", ServerValue.TIMESTAMP);
        hashMap.put("unread_count", 0);

        refInboxUser1.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                refInboxUser2 = refInbox.child(intentuidSecondUser).child(ChatRoom);
                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("lastMsg", message);
                hashMap1.put("oppImg", senderImg);
                hashMap1.put("oppName", senderName);
                hashMap1.put("oppUid", senderUID);
                hashMap1.put("chatRoom", ChatRoom);
                hashMap1.put("tokenId", intentToken);
                hashMap1.put("inboxtime", ServerValue.TIMESTAMP);
                hashMap1.put("unread_count", ++count);
                Log.e("COUNT VALUE ", "328  " + count);
                refInboxUser2.setValue(hashMap1);
 }
        });
    }



    private void initViews() {
        chatList = new ArrayList<>();
        randomkeyStore = new ArrayList<>();
        edMsgSend = findViewById(R.id.ed_msgsend);

        userMsgName = findViewById(R.id.tvuser_msg_name);
        btnMsgSend = findViewById(R.id.btn_msg_send);
        recyclerView = findViewById(R.id.msg_recycle);
        imgBack = findViewById(R.id.imgback);

        sharedPreferences = getSharedPreferences(MyPref, MODE_PRIVATE);
        senderName = sharedPreferences.getString("name", "");
        senderImg = sharedPreferences.getString("imageUrl", "");
        intentToken = sharedPreferences.getString("token", "");
        senderphone = sharedPreferences.getString("phone", "");
        senderUID = sharedPreferences.getString("uid", "");

 mStorageRef = FirebaseStorage.getInstance().getReference();
        refChat = FirebaseDatabase.getInstance().getReference("chats");
        refInbox = FirebaseDatabase.getInstance().getReference("inbox");
        db = new DBHandler( UserChat.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        refChatCall=true;
        EventBus.getDefault().unregister(this);
        refInbox1.removeEventListener(valueEventListener);
        if (!TextUtils.isEmpty(ChatRoom)) {


            FirebaseDatabase.getInstance().getReference("inbox").child(senderUID).child(ChatRoom).child("unread_count").setValue(0);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(String s) {
        msgAdapter.notifyDataSetChanged();
        recyclerView.scrollToPosition(chatList.size() - 1);
    }

    }


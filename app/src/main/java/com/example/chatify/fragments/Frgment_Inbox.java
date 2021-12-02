package com.example.chatify.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatify.Inbox;
import com.example.chatify.R;
import com.example.chatify.UserChat;
import com.example.chatify.models.FrgmentInboxAdptr;
import com.example.chatify.models.InboxModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.chatify.Constant.MyPref;
import static com.example.chatify.Constant.isConnectedToNetwork;

public class Frgment_Inbox extends Fragment {

    View view;
    RecyclerView recyclerView;
    FrgmentInboxAdptr frgmentInboxAdptr;

    List<InboxModel> list;
    SharedPreferences sharedPreferences;
    String uid;
    Inbox inbox;
    DatabaseReference databaseReference;
    List<String> chatRoomssss;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frgment__inbox, container, false);

        inbox = (Inbox) getActivity();

        sharedPreferences = getActivity().getSharedPreferences(MyPref, MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");
        list = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerInbox);
        final LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);
        inboxData();
return view;
    }


    @Override
    public void onResume() {
        super.onResume();
 inboxData();
  }
    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);

    }

public void setAdapater()
{
    Collections.sort(list);
    frgmentInboxAdptr=new FrgmentInboxAdptr(getActivity(), list,this);
    recyclerView.setAdapter(frgmentInboxAdptr);
}
public void inboxData() {
    chatRoomssss = new ArrayList<>();

    databaseReference = FirebaseDatabase.getInstance().
            getReference().child("inbox").child(uid);
    if (isConnectedToNetwork(getActivity())) {
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                InboxModel inboxModel = dataSnapshot.getValue(InboxModel.class);
                if (inboxModel.getLastMsg() == null) {
                    return;
                }
                if (chatRoomssss.size() > 0) {
                    for (int i = 0; i < chatRoomssss.size(); i++) {
                        if (!chatRoomssss.contains(inboxModel.getChatRoom())) {
                            chatRoomssss.add(inboxModel.getChatRoom());
                            list.add(inboxModel);
                        }
                    }
                } else {
                    chatRoomssss.add(inboxModel.getChatRoom());
                    list.add(inboxModel);

                }
                EventBus.getDefault().post("Message");
                setAdapater();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                InboxModel inboxModel = dataSnapshot.getValue(InboxModel.class);
                if (inboxModel.getLastMsg() == null) {
                    return;
                }
                if (chatRoomssss.size() > 0) {
                    for (int i = 0; i < chatRoomssss.size(); i++) {
                        if (!chatRoomssss.contains(inboxModel.getChatRoom())) {
                            chatRoomssss.add(inboxModel.getChatRoom());
                            list.add(inboxModel);
                        } else {

                        }
                    }
                } else {
                    chatRoomssss.add(inboxModel.getChatRoom());
                    list.add(inboxModel);
                }
                EventBus.getDefault().post("Message");
                setAdapater();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }else {
        Toast.makeText( getActivity(), "Please Check your Internet Connection", Toast.LENGTH_SHORT).show();

    }


}


}

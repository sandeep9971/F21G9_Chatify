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

import com.example.chatify.R;
import com.example.chatify.localDB.DBHandler;
import com.example.chatify.models.SignUpModel;
import com.example.chatify.models.UserlistAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.example.chatify.Constant.MyPref;
import static com.example.chatify.Constant.isConnectedToNetwork;


public class User extends Fragment {
    RecyclerView recyclerView;
    List<SignUpModel> list, listTemp;
    DatabaseReference refUsers;
    UserlistAdapter userListAdapter;
    View view;
    DBHandler db;
    SharedPreferences sharedPreferences;


    String uid;
    SignUpModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        db = new DBHandler(getActivity());

        listTemp = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences(MyPref, MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

        recyclerView = view.findViewById(R.id.recycler);
        final LinearLayoutManager linearLayout;
        linearLayout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayout);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        list = new ArrayList<>();

        userListAdapter = new UserlistAdapter(getActivity(), list);
        recyclerView.setAdapter(userListAdapter);

        if (!isConnectedToNetwork(view.getContext())) {
            list.clear();
            list = db.Userfetch();
            Log.e("list size", list.size() + "");
            userListAdapter = new UserlistAdapter(getActivity(), list);
            recyclerView.setAdapter(userListAdapter);
        }
        else {
            refUsers = FirebaseDatabase.getInstance().getReference("users");

            refUsers.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    model = dataSnapshot.getValue(SignUpModel.class);
                    Log.e("model", model.toString());

                    list.add(model);
                    db.insertUser(model);

                    Log.e("list size", list.size() + "");
                    userListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
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


        }
    }


}

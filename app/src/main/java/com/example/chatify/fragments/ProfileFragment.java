package com.example.chatify.fragments;

import android.content.SharedPreferences;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.example.chatify.inbox;
import com.example.chatify.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.Context.MODE_PRIVATE;
import static com.example.chatify.Constant.MyPref;
public class ProfileFragment extends Fragment {

    Button btnSave;
    ImageView imgIndex, nameEdit, phoneEdit, statusEdit;
    EditText userName, phoneNumber, edStatus;
    String nameUser, userID, editUsername, phnNumber, status, statusSh;
    boolean nameedit, imgUpdate, phnedit, statusedit;


    StorageReference mStorageRef;
    DatabaseReference databaseUsers;

    inbox inbox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        inbox = (inbox) getActivity();
        imgIndex = view.findViewById(R.id.index_img);
        btnSave = view.findViewById(R.id.btn_save);
        nameEdit = view.findViewById(R.id.name_edit);
        userName = view.findViewById(R.id.username);
        phoneNumber = view.findViewById(R.id.phone);
        phoneEdit = view.findViewById(R.id.phone_edit);
        statusEdit = view.findViewById(R.id.status_edit);
        edStatus = view.findViewById(R.id.ed_status);
        initViews();
return  view;
    }

        private void initViews () {
            userName.setFocusable(false);
            btnSave.setVisibility(View.GONE);
            edStatus.setSingleLine(true);
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPref, MODE_PRIVATE);
            nameUser = sharedPreferences.getString("name", "");
            userID = sharedPreferences.getString("uid", "");
            phnNumber = sharedPreferences.getString("phone", "");
            statusSh = sharedPreferences.getString("status", "");
            userName.setText(nameUser);
            userName.setSelection(userName.getText().length());
            phoneNumber.setText(phnNumber);
            phoneNumber.setSelection(phoneNumber.getText().length());
            edStatus.setText(statusSh);
            edStatus.setSelection(edStatus.getText().length());
            databaseUsers = FirebaseDatabase.getInstance().getReference();
            mStorageRef = FirebaseStorage.getInstance().getReference();

        }
}




package com.example.chatify.fragments;

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

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatify.Inbox;
import com.example.chatify.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.example.chatify.Constant.CAMERA_REQUEST_CODE;
import static com.example.chatify.Constant.MyPref;
import static com.example.chatify.Constant.NAME;
import static com.example.chatify.Constant.Phone;
import static com.example.chatify.Constant.Status;
import static com.example.chatify.Constant.isConnectedToNetwork;

public class ProfileFragment extends Fragment {

    Button btnSave;
    ImageView imgIndex, imgEdit, nameEdit, phoneEdit, statusEdit;
    EditText userName, phoneNumber, edStatus;
    String nameUser, imgUrl, userID, editUsername, phnNumber, status, statusSh;
    TextView tvCamera, tvGallery;
    boolean nameedit, imgUpdate, phnedit, statusedit;
    Bundle extras;
    Cursor cursor;
    Uri selectedImage, resultUri, downloadUrl;
    String imgDecodableString;
    StorageReference mStorageRef;
    DatabaseReference databaseUsers;

    Inbox inbox;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        inbox = (Inbox) getActivity();
        imgIndex = view.findViewById(R.id.index_img);
        btnSave = view.findViewById(R.id.btn_save);
        nameEdit = view.findViewById(R.id.name_edit);
        userName = view.findViewById(R.id.username);
        phoneNumber = view.findViewById(R.id.phone);
        phoneEdit = view.findViewById(R.id.phone_edit);
        statusEdit = view.findViewById(R.id.status_edit);
        edStatus = view.findViewById(R.id.ed_status);
        initViews();


        nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setVisibility(View.VISIBLE);
                userName.setEnabled(true);
                userName.requestFocus();
                userName.setFocusableInTouchMode(true);
                nameedit = true;          }
        });

        phoneEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setVisibility(View.VISIBLE);
                phoneNumber.setEnabled(true);
                phoneNumber.requestFocus();
                phoneNumber.setFocusableInTouchMode(true);
                phnedit = true;
            }
        });
        statusEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setVisibility(View.VISIBLE);
                edStatus.setEnabled(true);
                edStatus.requestFocus();
                edStatus.setFocusableInTouchMode(true);
                statusedit = true;
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
                if (isConnectedToNetwork(getActivity())) {
                    if ((nameedit == true) && (imgUpdate == true) && (phnedit == true) && (statusedit == true)) {

                    }
                    if (resultUri != null) {
                        editUsername = userName.getText().toString();
                        phnNumber = phoneNumber.getText().toString();
                        status = edStatus.getText().toString();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPref, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(NAME, editUsername);
                        editor.putString(Phone, phnNumber);
                        editor.putString(Status, status);
                        editor.commit();
                        databaseUsers.child("users").child(userID).child("name").setValue(editUsername);
                        databaseUsers.child("users").child(userID).child("phone").setValue(phnNumber);
                        databaseUsers.child("users").child(userID).child("status").setValue(status);
                        nameedit = false;
                        imgUpdate = false;
                        phnedit = false;
                        statusedit = false;
                    }

                    else {
                        editUsername = userName.getText().toString();
                        phnNumber = phoneNumber.getText().toString();
                        status = edStatus.getText().toString();
                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPref, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(NAME, editUsername);
                        editor.putString(Phone, phnNumber);
                        editor.putString(Status, status);
                        editor.commit();
                        databaseUsers.child("users").child(userID).child("name").setValue(editUsername);
                        databaseUsers.child("users").child(userID).child("phone").setValue(phnNumber);
                        databaseUsers.child("users").child(userID).child("status").setValue(status);
                        Toast.makeText(getActivity(), "Data Updated", Toast.LENGTH_LONG).show();
                    }
                } else
                {
                    Toast.makeText(getActivity(),"Please Check your Internet Connection",Toast.LENGTH_LONG).show();
                }
                btnSave.setVisibility(View.GONE);

            }

        });
        return view;
    }

    private void initViews() {
        userName.setFocusable(false);
        btnSave.setVisibility(View.GONE);
        edStatus.setSingleLine(true);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPref, MODE_PRIVATE);
        nameUser = sharedPreferences.getString("name", "");
        imgUrl = sharedPreferences.getString("imageUrl", "");
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


    public void imageCropResult(Intent data,int resultCode){
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            resultUri = result.getUri();
            Log.d("gggggg", resultUri + "check");
            imgIndex.setImageURI(resultUri);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            extras = data.getExtras();

        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            CropImage.activity(selectedImage).setAspectRatio(1,1)
                    .start(getActivity());
        }
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                Log.d("gggggg", resultUri + "check");
                imgIndex.setImageURI(resultUri);
            }
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        btnSave.setVisibility(View.GONE);
        userName.setFocusable(false);
        edStatus.setFocusable(false);
        phoneNumber.setFocusable(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        btnSave.setVisibility(View.GONE);
        userName.setFocusable(false);
        edStatus.setFocusable(false);
        phoneNumber.setFocusable(false);
    }
}


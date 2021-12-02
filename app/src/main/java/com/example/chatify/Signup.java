package com.example.chatify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatify.localDB.DBHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.example.chatify.models.SignUpModel;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

import static com.example.chatify.Constant.CAMERA_REQUEST_CODE;
import static com.example.chatify.Constant.READ_EXTERNAL_STORAGE;
import static com.example.chatify.Constant.REQUEST_CAMERA;

public class Signup extends AppCompatActivity {
    public static String platform = "0";
    EditText edName, edEmail, edPasswrd, edConfrmpswd,edPhone;
    ImageView imgCamera;
    Boolean isLoad = false;
    String name, email, password, conpswd,uid,imgDecodableString,token,phone,status;
    TextView tvCamera, tvGallery;
    Cursor cursor;
    Button btn_signup;
    Uri selectedImage, downloadUrl,resultUri;
    DatabaseReference databaseUsers,user;
    File file;
    boolean isCamera;
    Bundle extras;
    Bitmap imageBitmap;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    DBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        db=new DBHandler(Signup.this);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        edName = findViewById(R.id.ed_name);
        progressBar=findViewById(R.id.progressBar2);
        edEmail = findViewById(R.id.ed_email);
        edPhone=findViewById(R.id.ed_phone);
        edPasswrd = findViewById(R.id.ed_password);
        edConfrmpswd = findViewById(R.id.ed_password2);

        btn_signup = findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add();

            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        InputMethodManager imm = (InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        return super.dispatchTouchEvent(motionEvent);
    }
    public void createAccount() {
        if (!isLoad) {
            progressBar.setVisibility(View.VISIBLE);
            isLoad = true;
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Token is", "token value " + token);
                                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                if (!TextUtils.isEmpty(uid)) {


                                }
                                user = databaseUsers.child(uid);
                                SignUpModel model = new SignUpModel(name, email,phone, platform, token,
                                        uid,String.valueOf(downloadUrl),status);
                                user.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) { finish();
                                    }
                                });
                                Toast.makeText(Signup.this, "New User Added", Toast.LENGTH_LONG).show();
                                Log.d("Verify", "createUserWithEmail:success");
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
                                isLoad =false;
                                Log.e("Verify", "createUserWithEmail:failure" + task.getException().getMessage().toString() + " ");
                                Toast.makeText(Signup.this, "Authentication failed." + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }

    private void add() {


        name = edName.getText().toString().trim();
        email = edEmail.getText().toString().trim();
        phone = edPhone.getText().toString().trim();
        password = edPasswrd.getText().toString().trim();
        conpswd = edConfrmpswd.getText().toString().trim();

      
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(Signup.this, "Please fill the name ", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(Signup.this, "Please Check the email", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password) || password.length() != 8) {
            Toast.makeText(Signup.this, " Password must be alphanumeric and contain one special Symbol", Toast.LENGTH_LONG).show();
        } else if (!password.equalsIgnoreCase(conpswd)) {
            Toast.makeText(Signup.this, "Confirm password must be same", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches() || phone.length() != 10) {
            Toast.makeText(Signup.this, "please check the Phone number", Toast.LENGTH_LONG).show();

        }

        else {
            createAccount();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            extras = data.getExtras();

        } else if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            imgDecodableString = cursor.getString(columnIndex);
            cursor.close();
            CropImage.activity(selectedImage).setAspectRatio(1,1)
                    .start(Signup.this);

        } else if (requestCode == 3 && resultCode == RESULT_OK && data != null) {
            extras = data.getExtras();
            Bitmap image = (Bitmap) extras.get("data");
            imgCamera.setImageBitmap(image);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                imgCamera.setImageURI(resultUri);

            }
        }
    }

}
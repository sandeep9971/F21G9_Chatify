package com.example.chatify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;



import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.chatify.models.SignUpModel;



public class Signup extends AppCompatActivity {
    public static String platform = "0";
    EditText edName, edEmail, edPasswrd, edConfrmpswd, edPhone;

    Boolean isLoad = false;
    String name, email, password, conpswd, uid, phone, status, onlineStatus;
    Button btn_signup;

    DatabaseReference databaseUsers, user;


    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        edName = findViewById(R.id.ed_name);
        progressBar = findViewById(R.id.progressBar2);
        edEmail = findViewById(R.id.ed_email);
        edPhone = findViewById(R.id.ed_phone);
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


    public void createAccount() {
        if (!isLoad) {
            progressBar.setVisibility(View.VISIBLE);
            isLoad = true;
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("on success", "done ");
                                uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                if (!TextUtils.isEmpty(uid)) {

                                }
                                user = databaseUsers.child(uid);
                                SignUpModel model = new SignUpModel(name, email, phone, platform,
                                        uid, status, onlineStatus);
                                user.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        finish();
                                    }
                                });

                                Toast.makeText(Signup.this, "New User Added", Toast.LENGTH_LONG).show();
                                Log.d("Verify", "createUserWithEmail:success");
                                finish();
                            } else {
                                progressBar.setVisibility(View.GONE);
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
        } else if (TextUtils.isEmpty(password) )//|| password.length() != 8) {
        {Toast.makeText(Signup.this, " Password must be alphanumeric and contain one special Symbol", Toast.LENGTH_LONG).show();
        } else if (!password.equalsIgnoreCase(conpswd)) {
            Toast.makeText(Signup.this, "Confirm password must be same", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(phone) || !Patterns.PHONE.matcher(phone).matches() || phone.length() != 10) {
            Toast.makeText(Signup.this, "please check the Phone number", Toast.LENGTH_LONG).show();

        } else {
            createAccount();
        }
    }




}
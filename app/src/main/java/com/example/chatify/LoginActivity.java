package com.example.chatify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.chatify.models.SignUpModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.chatify.Constant.Email;
import static com.example.chatify.Constant.ImageUrl;
import static com.example.chatify.Constant.IsLoggedIn;
import static com.example.chatify.Constant.MyPref;
import static com.example.chatify.Constant.NAME;
import static com.example.chatify.Constant.Phone;
import static com.example.chatify.Constant.Status;
import static com.example.chatify.Constant.Token;
import static com.example.chatify.Constant.UID;

public class LoginActivity extends AppCompatActivity {

    TextView tvSignup;
    TextView btnlogin;
 DatabaseReference databaseUsers;
    EditText edEmail, edPassword;
    String email, password;

     private FirebaseAuth mAuth;
    ProgressBar progressBar;
    boolean isLoading;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvSignup = findViewById(R.id.tv_signup);
        progressBar=findViewById(R.id.progressBar2);
        btnlogin = findViewById(R.id.btn_login);
        edEmail = findViewById(R.id.ed_email);
        edPassword = findViewById(R.id.ed_password);
        mAuth = FirebaseAuth.getInstance();
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");


        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, Signup.class);
                startActivity(intent);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAuthFirebase();
            }
        });
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        InputMethodManager imm = (InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        return super.dispatchTouchEvent(motionEvent);
    }

    public void loginAuthFirebase() {

        email = edEmail.getText().toString();
        password = edPassword.getText().toString();

        if (TextUtils.isEmpty(email) ||!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(LoginActivity.this, "Please Check the email", Toast.LENGTH_LONG).show();

        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, " Password must be alphanumeric", Toast.LENGTH_LONG).show();
        } else {
            if(!isLoading)
                progressBar.setVisibility(View.VISIBLE);
            isLoading =true;
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("signin", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                databaseUsers.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot != null) {


                                            if (!TextUtils.isEmpty(token)){
                                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).
                                                        child("token").setValue(token);
                                                Log.e("Login","Token :::::" +token);
                                                FirebaseDatabase.getInstance().getReference("users").child(uid).child("online_status").setValue("online");

                                            }
                                            SignUpModel model = dataSnapshot.getValue(SignUpModel.class);
                                            SharedPreferences sharedPreferences = getSharedPreferences(MyPref, MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(Email, email);
                                            editor.putString(UID, uid);
                                            editor.putString(NAME, model.getName());

                                            editor.putString(Phone,model.getPhone());
                                            editor.putString(Token,token);
                                            editor.putString(Status,model.getStatus());
                                            editor.putBoolean(IsLoggedIn, true);
                                            editor.commit();

                                            Intent i = new Intent(LoginActivity.this, inbox.class);
                                            startActivity(i);
                                            finish();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                progressBar.setVisibility(View.GONE);
                                isLoading = false;
                                Log.e("noo", "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed." + task.getException().getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
}
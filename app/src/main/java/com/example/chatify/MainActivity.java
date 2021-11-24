package com.example.chatify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import static com.example.chatify.Constant.IsLoggedIn;
import static com.example.chatify.Constant.MyPref;

import com.Inbox;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sharedPreferences = getSharedPreferences(MyPref, MODE_PRIVATE);
                if (!sharedPreferences.getBoolean(IsLoggedIn, false)) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    uid = sharedPreferences.getString("uid","");

                    Intent intent = new Intent(MainActivity.this, Inbox.class);
                    startActivity(intent);
                    finish();

                }
            }
        }, 3000);

    }
}
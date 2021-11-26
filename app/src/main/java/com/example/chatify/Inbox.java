package com.example.chatify;

import static com.example.chatify.Constant.MyPref;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
;

import com.example.chatify.fragments.ProfileFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class inbox extends AppCompatActivity {

    public Button logout;
    TabLayout tbInbox;
    ViewPager viewPager;
    public SharedPreferences sharedPreferences;
    DBHandler db;
    String uid, token;
ProfileFragment profileFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        setUpViews();

        sharedPreferences = getSharedPreferences(MyPref, MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");

        FirebaseDatabase.getInstance().getReference("users").child(uid).child("token").setValue(token);


        db = new DBHandler(inbox.this);
        addTabs(viewPager);
        tbInbox.setupWithViewPager(viewPager);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void setUpViews() {


        tbInbox = findViewById(R.id.tab_inbox);
        logout = findViewById(R.id.btnLOGOUT);
        viewPager = findViewById(R.id.view_pager);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sharedPreferences = getSharedPreferences(MyPref, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear().commit();
                db.deleteAll();
                Intent intent = new Intent(inbox.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });



     viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) { }
    });
}

    private void addTabs(ViewPager viewPager) {
        adptr ad = new adptr(getSupportFragmentManager());
        profileFragment = new ProfileFragment();
        ad.addTabs(profileFragment, "Profile");
        viewPager.setAdapter(ad);

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("online_status").setValue("online");

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("online_status").setValue("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("online_status").setValue("");
        Log.e("destroy", "0000");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}

class adptr extends FragmentPagerAdapter {
    List<String> str = new ArrayList();
    List<Fragment> frg = new ArrayList();

    public adptr(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return frg.get(position);
    }

    @Override
    public int getCount() {
        return frg.size();
    }

    public void addTabs(Fragment fragment, String string) {
        str.add(string);
        frg.add(fragment);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return str.get(position);
    }

}
package com.example.chatify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;

import com.example.chatify.fragments.Frgment_Inbox;
import com.example.chatify.fragments.ProfileFragment;
import com.example.chatify.fragments.User;
import com.example.chatify.localDB.DBHandler;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.chatify.Constant.MyPref;

public class Inbox extends AppCompatActivity {

    public ImageView menu;
    TabLayout tbInbox;
    ViewPager viewPager;
    public SharedPreferences sharedPreferences;
    DBHandler db;
    String uid, token;
ProfileFragment profileFragment;
    User userFragmnt;
    Frgment_Inbox frgmentInbox;
    public EditText edSearch;
    public RelativeLayout linearLayoutSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setUpViews();

        sharedPreferences = getSharedPreferences(MyPref, MODE_PRIVATE);
        uid = sharedPreferences.getString("uid", "");
   db = new DBHandler(Inbox.this);
        addTabs(viewPager);
        tbInbox.setupWithViewPager(viewPager);

    }

    private void setUpViews() {


        tbInbox = findViewById(R.id.tab_inbox);
        menu = findViewById(R.id.menu);
        viewPager = findViewById(R.id.view_pager);

        menu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                final PopupMenu popupMenu = new PopupMenu(Inbox.this, menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                                         public boolean onMenuItemClick(MenuItem item) {
                                                             switch (item.getItemId()) {
                                                                 case R.id.log_out:
                                                                     SharedPreferences.Editor editor = sharedPreferences.edit();
                                                                     editor.clear().commit();
                                                                     db.deleteAll();
                                                                     Intent intent = new Intent(Inbox.this, LoginActivity.class);
                                                                     startActivity(intent);
                                                                     finish();
                                                                     return true;
                                                             }
                                                             return true;
                                                         }
                                                     }
                );
                popupMenu.inflate(R.menu.mainmenu);
                popupMenu.show();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if (position == 2) {

                } else {

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });


    }

    private void addTabs(ViewPager viewPager) {
        adptr ad = new adptr(getSupportFragmentManager());
      profileFragment = new ProfileFragment();
        userFragmnt = new User();
        frgmentInbox = new Frgment_Inbox();
        ad.addTabs(frgmentInbox, "Inbox");
        ad.addTabs(userFragmnt, "Contacts");
   ad.addTabs(profileFragment, "Profile");
        viewPager.setAdapter(ad);

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

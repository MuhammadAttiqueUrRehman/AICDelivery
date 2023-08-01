package com.aic.aicdelivery;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import layout.fr_account;
import layout.fr_homepage;
import layout.fr_noservice;
import layout.fr_refer;
import layout.fr_vendorreviewsingle;
import layout.fr_youtubelist;

public class Main2Activity extends AppCompatActivity {
    public static String firsttime = "";
    public static LinearLayout ln;
    IntroManager intromanager;
    HMCoreData myDB;
    Fragment myFragment;
    ProgressBar progressBar2;
    public static BottomNavigationView bottomNavigationView;

    public Main2Activity() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bottom_navigation, menu);
        return;
    }

    public static void showErrorPage(String error) {
        return;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intromanager = new IntroManager(this);
        myDB = new HMCoreData(this);
        setContentView(R.layout.fragment_fr_postlogin);
        ln = (LinearLayout) findViewById(R.id.footer);
        bottomNavigationView = findViewById(R.id.footerx);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        Main2Activity.ln.setVisibility(View.VISIBLE);
        myFragment = new fr_homepage();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).commitAllowingStateLoss();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                    Fragment myFragment = new Fragment();
                    int id = menuItem.getItemId();
                    switch (id) {

                        case R.id.nav_home:
                            myFragment = new fr_homepage();
                            break;
                        case R.id.nav_videos:
                            myFragment = new fr_youtubelist();
                            break;
                        case R.id.nav_review:
                            myFragment = new fr_vendorreviewsingle();
                            break;
                        case R.id.nav_faq:
                            myFragment = new fr_refer();
                            break;
                        case R.id.nav_user:
                            Log.i("Debugging", "Administrator Key :" + intromanager.getAdmin());
                            if (intromanager.getAdmin().equals("Y")) {
                                myFragment = new fr_account();
                            } else {
                                myFragment = new fr_noservice();
                            }
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).commitAllowingStateLoss();
                    return true;
                }
            };

}

package com.aic.aicdelivery;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Handler;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class splashScreen extends Activity {
    private static int SPLASH_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        printKeyHash();
        new Handler().postDelayed(new Runnable() {
            //Showing splash screen with a timer, this will show the brand logo
            @Override
            public void run() {
                Intent i = new Intent(splashScreen.this, MainActivity.class);
                startActivity(i);
                finish();
                //close splash screen
                finish();
            }
        }, SPLASH_TIMEOUT);
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = this.getPackageManager().getPackageInfo("com.aic.knpartner", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.i("KeyHash:", e.toString());
        }
    }

}

package com.aic.aicdelivery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.json.JSONException;
import org.json.JSONObject;

import layout.MerchantSetupFragment;
import layout.fr_login;
import layout.fr_walkthrough_slide;


public class MainActivity extends AppCompatActivity implements AsyncResponse {
    public static String firsttime = "", firstOutput = "", customermobile = "", locaddress = "", deviceid = "", changeaddress = "";
    private Fragment fragment;
    private IntroManager intromanager;
    private HMCoreData myDB;
    private ProgressBar progressBar2;

    public void showFragment(Fragment fr) {
        Fragment myFragment = fr;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).commit();
    }

    //private long lastPressedTime;
    // private static final int PERIOD = 2000;

  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            switch (event.getAction()) {
                case KeyEvent.ACTION_DOWN:
                    if (event.getDownTime() - lastPressedTime < PERIOD) {
                        finish();
                    } else {
                        myDB.showToast(getApplicationContext(), "Press again to exit.");
                        lastPressedTime = event.getEventTime();
                    }
                    return true;
            }
        }
        return false;
    }*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDB = new HMCoreData(this);
        intromanager = new IntroManager(this);
        setContentView(R.layout.activity_main);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        intromanager.setNewDevice(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
        intromanager.setDevice(HMConstants.deviceplatform + "^" + HMConstants.deviceversion + "^null^" + intromanager.getLatLon() + "^" + HMConstants.screenHeight + "^" + HMConstants.screenWidth + "^" + intromanager.getNewDevice() + "^" + HMConstants.devicemodel + "^" + HMConstants.devicemanufacturer + "^");

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        if (deepLink != null && deepLink.getQueryParameter("mobile") != null && deepLink.getQueryParameter("referdevice") != null && deepLink.getQueryParameter("linktype") != null && deepLink.getQueryParameter("productcode") != null) {
                            Log.i("Debugging", "Deep link " + deepLink.toString());
                            intromanager.setReferMobile(deepLink.getQueryParameter("mobile"));
                            intromanager.setReferDevice(deepLink.getQueryParameter("referdevice"));
                            //Check if first time
                            if (intromanager.getFirstTime() && deepLink.getQueryParameter("linktype").equals("DOWNLOAD")) {
                                intromanager.setFirstTime(false);
                                addReferrer();
                            }
                        }
                    }
                });


        if (intromanager.getFirst1()) {
            Log.i("Debugging", "Came to Walk through");
            myDB = new HMCoreData(this);

            intromanager.setFirst1(false);
            fragment = new fr_walkthrough_slide();
            showFragment(fragment);

        } else {
            initVendor();
//            Log.i("Debugging", "Came to Main");
//            intromanager.setFirst2(false);
//            if (!isLocationEnabled(this)) {
//                statusCheck();
//            }
//            if (firsttime.length() == 0) {
//                initVendor();
//            } else {
//
//                Log.i("Debugging", "Came to Nexttime");
//                fragment = new fr_login();
//                showFragment(fragment);
//            }
        }

    }

    public void initVendor() {
        Log.i("Debugging", "Came to Firsttime");
        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        intromanager.setNewDevice(uuid);
        String xuuid = HMConstants.deviceplatform + "^" + HMConstants.deviceversion + "^null^" + intromanager.getLatLon() + "^" + HMConstants.screenHeight + "^" + HMConstants.screenWidth + "^" + uuid + "^" + HMConstants.devicemodel + "^" + HMConstants.devicemanufacturer + "^";
        intromanager.setDevice(xuuid);
        Log.i("Debugging", "Device 11111111111111111111111111111111 :" + intromanager.getDevice());
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("geolocation", intromanager.getLatLon());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String[] myTaskParams = {"/SSMinitializeVendor", jsonParam.toString()};
        Log.i("Debugging", jsonParam.toString());
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this, progressBar2, "SSMinitializeVendor");
            aasyncTask.delegate = (AsyncResponse) this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addReferrer() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("reference", intromanager.getReferDevice());
            jsonMain.put("mobile", intromanager.getReferMobile().trim());
            jsonMain.put("customer", intromanager.getNewDevice());
            jsonMain.put("additionalinfo", HMConstants.faqType);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(this, e.getMessage());
        }
        Log.i("Debugging", "Referral Data :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMUpdateReferrer", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this, progressBar2, "SSMUpdateReferrer");
            aasyncTask.delegate = (AsyncResponse) this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getBaseContext(), e.getMessage());
            return;
        }
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.d("debugging", output);
        JSONObject jObj = new JSONObject(output);
        if (!jObj.getString("statuscode").equals("000")) {
            myDB.showToast(this, jObj.getString("statusdesc"));
            return;
        }
        if (handle.equals("SSMinitializeVendor")) {
            intromanager.setHomeOP(output);
            intromanager.setAppSharing(jObj.getString("shorttext"));
            intromanager.setSharingText(jObj.getString("sharetext"));
            intromanager.setSharingURL(jObj.getString("sharepagelink"));
            intromanager.setSharingLink(jObj.getString("sharelink"));
            intromanager.setAppStoreURL(jObj.getString("appstorevendorandroid"));
            intromanager.setIOSStoreURL(jObj.getString("appstorevendorios"));
            intromanager.setSupportEmail(jObj.getString("jcareemail"));
            intromanager.setSupportCall(jObj.getString("jcarecall"));
            intromanager.setVisitingCharges(jObj.getString("visitingcharge"));

            intromanager.setQRCode(jObj.getString("paytmqrcode"));
            intromanager.setPaymentMobile(jObj.getString("paytmmobile"));
            intromanager.setPaymentBank(jObj.getString("paymentbank"));
            intromanager.setMobilePrefix(jObj.getString("mobileprefix"));

            if (jObj.getString("showmerchantsetup").equals("1") && !intromanager.getIsInit()) {
                firsttime = "1";
                fragment = new MerchantSetupFragment();
                showFragment(fragment);
            } else {
                firsttime = "1";
                fragment = new fr_login();
                showFragment(fragment);
            }
        }
    }

    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your device location services seems to be disabled, You need to enable location for AIC Delivery to work. Do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}


package com.aic.aicdelivery;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements AsyncResponse {
    private IntroManager introManager;
    private HMAppVariables appVariables;
    private HMCoreData myDB;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        introManager = new IntroManager(this);
        myDB = new HMCoreData(this);
        appVariables = new HMAppVariables();

        if (!introManager.getFCMKey().equals("")) {
            if (!introManager.getFCMKey().equals(refreshedToken)) {
                Log.i("Debugging", "33333333333333333333333333333333333333333333333333333333333333333333333");
                Log.i("Debugging", "Refreshed token: " + refreshedToken);
                introManager.setFCMKey(refreshedToken);
                if (introManager.getLogged()) {
                    Log.i("Debugging", "4444444444444444444444444444444444444444444444444444444444444444444444");
                    Log.i("Debugging", "Refreshed token: " + refreshedToken);
                    sendRegistrationToServer(refreshedToken);
                }
            }
        }
    }

    private void sendRegistrationToServer(String refreshedToken) {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            IntroManager intromanager;
            intromanager = new IntroManager(this);
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", introManager.getDevice());
            jsonMain.put("customer", appVariables.ucustomerid);
            jsonMain.put("certificate", appVariables.ucertificate);
            jsonMain.put("reference", refreshedToken);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(this, e.getMessage());
        }
        Log.i("Debugging", "Refresh key :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMUpdateFCMKeyVendor", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this, null, "SSMUpdateFCMKeyVendor");
            aasyncTask.delegate = (AsyncResponse) this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(this, e.getMessage());
            return;
        }
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        Log.i("Debugging", "Key Refresh Successful :" + output);

    }
}

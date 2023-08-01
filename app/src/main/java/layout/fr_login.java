package layout;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMConstants;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.providerInfo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class fr_login extends Fragment implements AsyncResponse {

    Button btnlogin, btnforgotpin, btnregister;
    ProgressBar progressBar8;
    HMCoreData myDB;
    Toolbar mtoolbar;
    TextView mTitle;
    EditText txtlogin;
    EditText txtpin;
    IntroManager intromanager;
    View v;
    providerInfo providerinfo;
    String uuid, xuuid;
    ShareActionProvider mShareActionProvider;
    String mlink;
    MenuItem actionItem;
    String mmobile;
    TextView tv_outlet_name;

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        Fragment myFragment;
        int id = items.getItemId();
        switch (id) {
            case R.id.menu_info1:
                myFragment = new fr_version_pre();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
            case R.id.menu_dataprivacy1:
                myFragment = new fr_data_privacy_pre();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
            case R.id.menu_faq:
                myFragment = new fr_faq_pre();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
        }
        return true;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.home_menu, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("Debugging","frLogin");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        printKeyHash();
        intromanager = new IntroManager(getActivity());
        providerinfo = new providerInfo(getActivity());
        FirebaseMessaging.getInstance().subscribeToTopic(HMConstants.partnerTopic);
        providerinfo.clearAllData();
        myDB = new HMCoreData(getContext());
        Log.i("Debugging", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_login, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Login");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        btnlogin = (Button) v.findViewById(R.id.btnlogin);
        btnforgotpin = (Button) v.findViewById(R.id.btnforgotpin);
        btnregister = (Button) v.findViewById(R.id.btnregister);
        progressBar8 = (ProgressBar) v.findViewById(R.id.progressBar8);
        tv_outlet_name = v.findViewById(R.id.tv_outlet_name);
        tv_outlet_name.setText(intromanager.getMerchantName());
        txtlogin = v.findViewById(R.id.txtlogin);
        txtpin = (EditText) v.findViewById(R.id.txtpin);
        Log.i("Debugging", "Login Text " + intromanager.getMobile());
      //  if (intromanager.getMobile().isEmpty())
      //      txtlogin.setText(intromanager.getMobilePrefix());
     //   else
            txtlogin.setText(intromanager.getMobile());
        FirebaseMessaging.getInstance().subscribeToTopic(HMConstants.partnerTopic);
        String token = FirebaseInstanceId.getInstance().getToken();
        if (!intromanager.getFCMKey().equals(token)) {
            intromanager.setFCMKey(token);
        }
      /*  txtlogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
               if (hasFocus) {
                   if (intromanager.getMobile().length() == 0) {
                       txtlogin.setPrefix(HMConstants.mobilePrefix);
                   }
                   } else {
                        if (txtlogin.length() < HMConstants.mobilePrefix.length() + 1)
                            txtlogin.setPrefix("");
                   }
            }
        });*/

        txtlogin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
              /*  if (hasFocus) {
                    txtlogin.setPrefix(intromanager.getMobilePrefix());
                } else {
                    if (txtlogin.length() < intromanager.getMobilePrefix().length() + 1)
                        if (intromanager.getMobile().length() > 0)
                            txtlogin.setText(intromanager.getMobile());
                        else
                            txtlogin.setPrefix("");
                }*/
            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtlogin.length() != 12) {
                    txtlogin.setError("Enter Mobile Number (9715xxxxxxxx");
                    return;
                } else if (txtpin.length() != 4) {
                    txtpin.setError("Please Enter 4 digit PIN");
                    return;
                }
                intromanager.setMobile(txtlogin.getText().toString());
                Log.i("Debugging", "Mobile Number :" + intromanager.getMobile());
                Log.i("Debugging", "Device :" + intromanager.getDevice());
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("employeeid", txtlogin.getText().toString());
                    jsonMain.put("password", txtpin.getText().toString());
                    jsonMain.put("reference", intromanager.getFCMKey());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String[] myTaskParams = {"/SSMPartnerLogin", jsonParam.toString()};
                Log.i("Debugging", jsonParam.toString());
                try {
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar8, "SSMPartnerLogin");
                    aasyncTask.delegate = (AsyncResponse) fr_login.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        btnforgotpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtlogin.length() != 12) {
                    txtlogin.setError("Enter Mobile Number(9715xxxxxxxx");
                    return;
                }
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("mobile", txtlogin.getText());
                    jsonMain.put("emailid", "");
                    jsonMain.put("employeeaccount", "1");
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    myDB.showToast(getActivity(), e.getMessage());
                }
                String[] myTaskParams = {"/SSMSendEnrolmentToken", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar8, "forgotpin");
                    aasyncTask.delegate = (AsyncResponse) fr_login.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    myDB.showToast(getActivity(), e.getMessage());
                }
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                Fragment myFragment = new fr_registertype();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });
        return v;
    }

    @Override
    public void processFinish(String output, String mview) throws HMOwnException, JSONException {
        //Write local SQL
        intromanager.setMobile(txtlogin.getText().toString());
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.d("Debugging", "response " + output);
        JSONObject jObj = new JSONObject(output);
        if (!jObj.getString("statuscode").equals("000")) {
            myDB.showToast(getActivity(), jObj.getString("statusdesc"));
            return;
        }
        if (mview.equals("forgotpin")) {
            Bundle bundle = new Bundle();
            bundle.putString("calledfrom", "pinchange");
            bundle.putString("tokenref", jObj.getString("referencenumber"));
            intromanager.setMobile(txtlogin.getText().toString());
            Fragment myFragment = new fr_validation();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        } else if (mview.equals("SSMPartnerLogin")) {
            intromanager.setLoginOP(output);
            intromanager.setLogged(true);
            intromanager.setLoginJson(jObj.toString());
            intromanager.setEmployeeID(jObj.getString("employeeid"));
            intromanager.setEmployeeName(jObj.getString("partnername"));
            intromanager.setCertificate(jObj.getString("certificate"));
            intromanager.setOutletID(jObj.getString("outletid"));
            intromanager.setCheckinStatus(jObj.getString("checkinstatus"));
            intromanager.setCompanyType(jObj.getString("companytype"));
            intromanager.setServiceImage1(jObj.getString("profileimage"));
            intromanager.setAdmin(jObj.getString("isadmin"));
            intromanager.setCategory(jObj.getString("servicecategory"));
            intromanager.setOutletName(jObj.getString("outletname"));
            Log.i("Debugging", "Service Category 1  " + jObj.getString("servicecategory"));
            Log.i("Debugging", "Service Category  2 " + intromanager.getCategory());
            Intent intent = new Intent(getActivity(), Main2Activity.class);
            startActivity(intent);
            getActivity().finish();
        }

    }


    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo("com.aic.knpartner", PackageManager.GET_SIGNATURES);
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


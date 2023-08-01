package layout;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.providerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class fr_validation extends Fragment implements AsyncResponse {
    private String strtext, strfullname, strmobile1, strmobile2, stremail, straddress, straccount, strifsccode, strgender, strgeolocation, strhandle;
    private EditText txtcode;
    private Button btnvalidate;
    private Button btnresendcode;
    private ProgressBar progressBar9;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private IntroManager intromanager;
    private providerInfo providerinfo;
    private TextView resendctr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void showResend() {
        new CountDownTimer(180000, 1000) {

            public void onTick(long millisUntilFinished) {
                resendctr.setVisibility(View.VISIBLE);
                btnresendcode.setVisibility(View.GONE);
                resendctr.setText("Re-send OTP Code in  " + millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                resendctr.setVisibility(View.GONE);
                btnresendcode.setVisibility(View.VISIBLE);
            }
        }.start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        providerinfo = new providerInfo(getActivity());
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getActivity());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_validation, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("OTP Validation");
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);
        btnvalidate = (Button) v.findViewById(R.id.btnvalidate);
        btnresendcode = (Button) v.findViewById(R.id.btnresendcode);
        txtcode = (EditText) v.findViewById(R.id.txtvalidationcode);
        progressBar9 = (ProgressBar) v.findViewById(R.id.progressBar9);
        resendctr = v.findViewById(R.id.resendctr);
        strhandle = getArguments().getString("calledfrom");
        if (getArguments().getString("calledfrom").equals("promoter")) {
            strfullname = getArguments().getString("fullname");
            strmobile1 = getArguments().getString("txtmobile1");
            strmobile2 = getArguments().getString("txtmobile2");
            stremail = getArguments().getString("txtemail");
            straddress = getArguments().getString("txtaddress");
            strgeolocation = getArguments().getString("txtgeolocation");
            straccount = getArguments().getString("txtaccount");
            strifsccode = getArguments().getString("txtifsccode");
            strgender = getArguments().getString("gender");
            strtext = getArguments().getString("tokenref");
            Log.i("Debugging", "address location 23222 " + strgeolocation);
        } else if (getArguments().getString("calledfrom").equals("provider")) {
            strtext = getArguments().getString("tokenref");
        } else if (getArguments().getString("calledfrom").equals("pinchange")) {
            //intromanager.setMobile(getArguments().getString("txtmobile1"));  Set this in the login page
            strtext = getArguments().getString("tokenref");
        }

        showResend();


        btnvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtcode.length() != 6) {
                    txtcode.setError("Enter the 6 digit validation code received on your mobile and email.");
                    return;
                }
                if (strhandle.equals("promoter")) {
                    enrolpromoter();
                } else if (strhandle.equals("provider")) {
                    enrolpartner();
                } else if (strhandle.equals("pinchange")) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonMain = new JSONObject();
                    try {
                        jsonMain.put("merchantcode", intromanager.getMerchantCode());
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("reference", strtext);
                        jsonMain.put("idnumber", txtcode.getText());
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                        myDB.showToast(getActivity(), e.getMessage());
                    }
                    String[] myTaskParams = {"/SSMValidateToken", jsonParam.toString()};
                    //  new HMDataAccess().execute(myTaskParams);
                    try {
                        Log.i("Debugging", jsonParam.toString());
                        HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "SSMValidateToken");
                        aasyncTask.delegate = (AsyncResponse) fr_validation.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                        myDB.showToast(getActivity(), e.getMessage());
                    }
                }
            }
        });

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Fragment myFragment = new fr_login();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnresendcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResend();
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("mobile", strmobile1 == null ? intromanager.getMobile() : strmobile1);
                    jsonMain.put("email", stremail);
                    jsonMain.put("employeeaccount", "1");
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    myDB.showToast(getActivity(), e.getMessage());
                }
                String[] myTaskParams = {"/SSMSendEnrolmentToken", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "SSMSendEnrolmentToken");
                    aasyncTask.delegate = (AsyncResponse) fr_validation.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    myDB.showToast(getActivity(), e.getMessage());
                }
            }
        });
        return v;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        //Write local SQL
        JSONObject myjson = new JSONObject(output);
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        Bundle bundle = new Bundle();
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        }
        if (handle.equals("SSMSendEnrolmentToken")) {
            strtext = myjson.getString("referencenumber");
            myDB.showToast(getContext(), "OTP sent to your mobile and email");
            return;
        }
        if (handle.equals("SSMValidateToken")) {
            Fragment myFragment = new fr_reset_pin();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        } else if (handle.equals("SSMPromoterRegister")) {
            bundle.putString("orderref", "");
            bundle.putString("message", "Your promoter registration request is completed successfuly.  You will receive a welcome email with your access credentails soon.");
            Fragment myFragment = new fr_confirm_request();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        } else if (handle.equals("SSMPartnerRegister")) {
            bundle.putString("orderref", "Thank you");
            bundle.putString("message", "Your service provider registration request is completed successfuly.  You will receive a welcome email with your access credentails soon.");
            Fragment myFragment = new fr_confirm_request();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }

    private void enrolpromoter() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("fullname", strfullname);
            jsonMain.put("mobile", strmobile1);
            jsonMain.put("contact", strmobile2);
            jsonMain.put("email", stremail);
            jsonMain.put("address", straddress);
            jsonMain.put("gender", strgender);
            jsonMain.put("geolocation", strgeolocation);
            jsonMain.put("bankaccount", straccount);
            jsonMain.put("ifsccode", strifsccode);
            jsonMain.put("reference", strtext);
            jsonMain.put("otp", txtcode.getText());
            jsonMain.put("fbtoken", intromanager.getFCMKey());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            myDB.showToast(getActivity(), e.getMessage());
        }
        String[] myTaskParams = {"/SSMPromoterRegister", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("Promoter Register ", jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "SSMPromoterRegister");
            aasyncTask.delegate = (AsyncResponse) fr_validation.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            myDB.showToast(getActivity(), e.getMessage());
        }
    }

    private void enrolpartner() {
        JSONArray servicejson = new JSONArray();
        JSONObject jsonDoc;
        JSONArray documentjson = new JSONArray();
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        try {
            //Service details
            for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
                jsonDoc = new JSONObject();
                jsonDoc.put("det", providerinfo.getAllServiceItems().get(i).toString());
                servicejson.put(jsonDoc);
            }

            //Aadhar
            jsonDoc = new JSONObject();
            jsonDoc.put("imagetype", "A");
            jsonDoc.put("image", providerinfo.getAadharImage());
            jsonDoc.put("idnumber", providerinfo.getAadhar());
            documentjson.put(jsonDoc);

            //Trade License
            jsonDoc = new JSONObject();
            jsonDoc.put("imagetype", "T");
            jsonDoc.put("image", providerinfo.getTradeImage());
            jsonDoc.put("idnumber", providerinfo.getTrade());
            documentjson.put(jsonDoc);

            //Driving License
            jsonDoc = new JSONObject();
            jsonDoc.put("imagetype", "D");
            jsonDoc.put("image", providerinfo.getDLImage());
            jsonDoc.put("idnumber", providerinfo.getDL());
            documentjson.put(jsonDoc);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.i("Service Array ", servicejson.toString());
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("fullname", providerinfo.getFullName());
            jsonMain.put("mobile", providerinfo.getMobile1());
            jsonMain.put("mobile2", providerinfo.getMobile2());
            jsonMain.put("email", providerinfo.getEmail());
            jsonMain.put("address", providerinfo.getAddress());
            jsonMain.put("gender", providerinfo.getGender());
            jsonMain.put("geolocation", providerinfo.getLatLon());
            jsonMain.put("bankaccount", providerinfo.getAccount());
            jsonMain.put("ifsccode", providerinfo.getIFSC());
            jsonMain.put("companytype", providerinfo.getEntity());
            jsonMain.put("serviceList", servicejson);
            jsonMain.put("reference", strtext);
            jsonMain.put("idnumber", providerinfo.getReferral());
            jsonMain.put("fromdate", providerinfo.getYear());
            jsonMain.put("sgst", providerinfo.getTeamSize());
            jsonMain.put("otp", txtcode.getText());
            jsonMain.put("qualification", providerinfo.getQualification());
            jsonMain.put("profilepicture", providerinfo.getProfileImage());
            jsonMain.put("gstnumber", providerinfo.getGSTNumber());
            jsonMain.put("contact", providerinfo.getContactName());
            jsonMain.put("radius", providerinfo.getRadius());
            jsonMain.put("fbtoken", intromanager.getFCMKey());
            jsonMain.put("documentList", documentjson);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            myDB.showToast(getActivity(), e.getMessage());
        }
        String[] myTaskParams = {"/SSMPartnerRegister", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("Debugging", jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "SSMPartnerRegister");
            aasyncTask.delegate = (AsyncResponse) fr_validation.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            myDB.showToast(getActivity(), e.getMessage());
        }
    }
}

/*
 refer = indata.idnumber
        year = indata.fromdate
        team = indata.sgst
 */
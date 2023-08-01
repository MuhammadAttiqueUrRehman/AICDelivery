package layout;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Switch;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMAppVariables;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_providerprofile4 extends Fragment implements AsyncResponse {
    private EditText txtaccount, txtifsccode, txtgst;
    private Button btnprev, btnnext;
    private ProgressBar progressBar4;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private Switch switchtc;
    private IntroManager intromanager;
    private providerInfo providerinfo;
    private Boolean ifsc = true, refer = true;

    public fr_providerprofile4() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        intromanager = new IntroManager(getActivity());
        providerinfo = new providerInfo(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            myDB.showToast(getActivity(), e.getMessage());
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_providerregister4, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Bank Details");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        btnnext = (Button) v.findViewById(R.id.btnnext);
        btnprev = (Button) v.findViewById(R.id.btnprev);
        txtgst = (EditText) v.findViewById(R.id.txtgst);
        txtaccount = (EditText) v.findViewById(R.id.txtaccount);
        txtifsccode = (EditText) v.findViewById(R.id.txtifsccode);
        switchtc = (Switch) v.findViewById(R.id.switchtc);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        //Retrieve Memory
        //Update memory
        txtgst.setText(providerinfo.getGSTNumber());
        txtaccount.setText(providerinfo.getAccount());
        txtifsccode.setText(providerinfo.getIFSC());

        Log.i("Debugging", "Account " + providerinfo.getAccount());
        Log.i("Debugging", "IFSC Code " + providerinfo.getIFSC());
        Log.i("Debugging", "Referral " + providerinfo.getReferral());

        switchtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_tandc();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        txtifsccode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*    ifsc=false;
                if (txtifsccode.length() >= 11) {
                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonMain = new JSONObject();
                    try {
                        jsonMain.put("merchantcode", HMConstants.appmerchantid);
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("reference", txtifsccode.getText().toString());
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                        myDB.showToast(getActivity(), e.getMessage());
                    }
                    String[] myTaskParams = {"/SSMIFSCCodeCheck", jsonParam.toString()};
                    //  new HMDataAccess().execute(myTaskParams);
                    try {
                        Log.i("Debugging", jsonParam.toString());
                        HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4, "SSMIFSCCodeCheck");
                        aasyncTask.delegate = (AsyncResponse) fr_providerprofile4.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                        myDB.showToast(getActivity(), e.getMessage());
                    }
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                Fragment myFragment = new fr_providerprofile3();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        //txtyear,txtteam,txtreferral,txtaadhar,txttrade,txtpan,txtdl
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                if (txtaccount.length() == 0) {
                    txtaccount.setError("Enter bank IBAN");
                    txtaccount.requestFocus();
                    return;
                }

                if (txtifsccode.length() == 0) {
                    txtifsccode.setError("Enter bank IFSC code");
                    txtifsccode.requestFocus();
                    return;
                }

            /*     if (!ifsc) {
                    txtifsccode.setError("Invalid IFSC Code");
                    txtifsccode.requestFocus();
                    return;
                }*/


                if (!switchtc.isChecked()) {
                    myDB.showToast(getContext(), "Please accept Terms & Conditions");
                    return;
                }

                //Update memory
                providerinfo.setAccount(txtaccount.getText().toString());
                providerinfo.setIFSC(txtifsccode.getText().toString());
                providerinfo.setReferral(intromanager.getReferMobile().trim());
                providerinfo.setGSTNumber(txtgst.getText().toString());

                updatepartner();

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
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);

        if (handle.equals("SSMIFSCCodeCheck")) {
            if (!statuscodekey.toString().equals("000")) {
                txtifsccode.setError("Invalid IFSC Code");
                txtifsccode.requestFocus();
                ifsc = false;
            } else {
                ifsc = true;
            }
        } else if (handle.equals("SSMSendEnrolmentToken")) {

            if (!statuscodekey.toString().equals("000")) {
                myDB.showToast(getActivity(), statusdesckey);
                return;
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("tokenref", myjson.getString("referencenumber"));
                bundle.putString("calledfrom", "provider");
                Fragment myFragment = new fr_validation();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        } else if (handle.equals("SSMupdatepartnerprofile")) {
            Bundle bundle = new Bundle();
            bundle.putString("orderref", "Thank You");
            bundle.putString("message", "Your service provider details successfully updated.");
            Fragment myFragment = new fr_confirm_request_post();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();

        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == android.R.id.home) {
            Fragment myFragment = new fr_providerprofile3();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    private void updatepartner() {
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
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("certificate", intromanager.getCertificate());
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
            jsonMain.put("idnumber", providerinfo.getReferral());
            jsonMain.put("fromdate", providerinfo.getYear());
            jsonMain.put("sgst", providerinfo.getTeamSize());
            jsonMain.put("qualification", providerinfo.getQualification());
            jsonMain.put("gstnumber", providerinfo.getGSTNumber());
            jsonMain.put("contact", providerinfo.getContactName());
            jsonMain.put("radius", providerinfo.getRadius());
            jsonMain.put("documentList", documentjson);
            jsonMain.put("profilepicture", providerinfo.getProfileImage());
            jsonMain.put("fbtoken", intromanager.getFCMKey());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            myDB.showToast(getActivity(), e.getMessage());
        }
        String[] myTaskParams = {"/SSMupdatepartnerprofile", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("Partner Register ", jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4, "SSMupdatepartnerprofile");
            aasyncTask.delegate = (AsyncResponse) fr_providerprofile4.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            myDB.showToast(getActivity(), e.getMessage());
        }
    }
}

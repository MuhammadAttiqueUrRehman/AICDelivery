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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_providerregister4 extends Fragment implements AsyncResponse {
    private EditText txtaccount, txtifsccode, txtgst;
    private Button btnprev, btnnext;
    private ProgressBar progressBar4;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private Switch switchtc;
    private IntroManager intromanager;
    private providerInfo providerinfo;
    private Boolean ifsc = false, refer = false;

    public fr_providerregister4() {
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
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
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
        txtaccount.setText(providerinfo.getAccount());
        txtifsccode.setText(providerinfo.getIFSC());
        txtgst.setText(providerinfo.getGSTNumber());

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
           /*     if(txtifsccode.length()>=11){
                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonMain = new JSONObject();
                    try {
                        jsonMain.put("merchantcode", HMConstants.appmerchantid);
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("reference", txtifsccode.getText().toString());
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                       myDB.showToast(getActivity(),e.getMessage());
                    }
                    String[] myTaskParams = {"/SSMIFSCCodeCheck", jsonParam.toString()};
                    //  new HMDataAccess().execute(myTaskParams);
                    try {
                        Log.i("Debugging",jsonParam.toString());
                        HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4, "SSMIFSCCodeCheck");
                        aasyncTask.delegate = (AsyncResponse) fr_providerregister4.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                       myDB.showToast(getActivity(),e.getMessage());
                    }
                }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        txtifsccode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*    if(txtifsccode.length()>=11){
                    JSONObject jsonParam = new JSONObject();
                    JSONObject jsonMain = new JSONObject();
                    try {
                        jsonMain.put("merchantcode", HMConstants.appmerchantid);
                        jsonMain.put("mdevice", intromanager.getDevice());
                        jsonMain.put("reference", txtifsccode.getText().toString());
                        jsonParam.put("indata", jsonMain);
                    } catch (JSONException e) {
                       myDB.showToast(getActivity(),e.getMessage());
                    }
                    String[] myTaskParams = {"/SSMIFSCCodeCheck", jsonParam.toString()};
                    //  new HMDataAccess().execute(myTaskParams);
                    try {
                        Log.i("Debugging",jsonParam.toString());
                        HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4, "SSMIFSCCodeCheck");
                        aasyncTask.delegate = (AsyncResponse) fr_providerregister4.this;
                        aasyncTask.execute(myTaskParams);
                    } catch (Exception e) {
                       myDB.showToast(getActivity(),e.getMessage());
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
                Fragment myFragment = new fr_providerregister3();
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
                    txtifsccode.setError("Enter bank name");
                    txtifsccode.requestFocus();
                    return;
                }

          /*      if (!ifsc){
                    txtifsccode.setError("Invalid IFSC Code");
                    txtifsccode.requestFocus();
                    return;
                }
*/

                if (!switchtc.isChecked()) {
                    myDB.showToast(getContext(), "Please accept Terms & Conditions");
                    return;
                }

                //Update memory
                providerinfo.setAccount(txtaccount.getText().toString());
                providerinfo.setIFSC(txtifsccode.getText().toString());
                providerinfo.setReferral(intromanager.getReferMobile().trim());
                providerinfo.setGSTNumber(txtgst.getText().toString());
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("mobile", providerinfo.getMobile1());
                    jsonMain.put("email", providerinfo.getEmail());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    myDB.showToast(getActivity(), e.getMessage());
                }
                String[] myTaskParams = {"/SSMSendEnrolmentToken", jsonParam.toString()};
                Log.i("Debugging", jsonParam.toString());
                try {
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4, "SSMSendEnrolmentToken");
                    aasyncTask.delegate = (AsyncResponse) fr_providerregister4.this;
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
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == android.R.id.home) {
            Fragment myFragment = new fr_providerregister3();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }
}

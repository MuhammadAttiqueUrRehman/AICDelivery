package layout;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Switch;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMAppVariables;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import com.aic.aicdelivery.R;
import com.aic.aicdelivery.placesActivity;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import static android.app.Activity.RESULT_OK;

public class fr_promoter extends Fragment implements AsyncResponse {
    private static final int PLACES_RETURN_RESULT = 2;
    private int PLACE_PICKER_REQUEST = 1;
    private EditText txtfullname, txtemail, address, txtaccount, txtifsccode, addressloc;
    private EditText txtmobile1, txtmobile2;
    private Button btncreateaccount;
    private ImageButton btnlocation;
    // private ImageView viewcheck;
    private RadioButton male, female;
    private ProgressBar progressBar11;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private Switch switchtc;
    private IntroManager intromanager;
    private Boolean ifsc = false;

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
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_promoter, container, false);
     /*   mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Promoter Registration");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);*/

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Promoter Registration");
        mtoolbar.setNavigationIcon(R.drawable.ico_back);


        btncreateaccount = (Button) v.findViewById(R.id.btncreateaccount);
        txtfullname = (EditText) v.findViewById(R.id.txtfullname);
        txtmobile1 = v.findViewById(R.id.txtmobile1);
        txtmobile2 = v.findViewById(R.id.txtmobile2);
        txtemail = (EditText) v.findViewById(R.id.txtemail);
        address = (EditText) v.findViewById(R.id.address);
        txtaccount = (EditText) v.findViewById(R.id.txtaccount);
        txtifsccode = (EditText) v.findViewById(R.id.txtifsccode);
        btnlocation = (ImageButton) v.findViewById(R.id.btnlocation);
        addressloc = (EditText) v.findViewById(R.id.addressloc);
        male = (RadioButton) v.findViewById(R.id.male);
        female = (RadioButton) v.findViewById(R.id.female);
        switchtc = (Switch) v.findViewById(R.id.switchtc);
        progressBar11 = (ProgressBar) v.findViewById(R.id.progressBar11);

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_registertype();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });


        switchtc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_tandc();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        txtmobile1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
         /*       if (hasFocus) {
                    txtmobile1.setPrefix(intromanager.getMobilePrefix());
                } else {
                    if (txtmobile1.length() < intromanager.getMobilePrefix().length() + 1)
                        txtmobile1.setPrefix("");
                }*/
            }
        });

        txtmobile2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
               /* if (hasFocus) {
                    txtmobile2.setPrefix(intromanager.getMobilePrefix());
                } else {
                    if (txtmobile2.length() < intromanager.getMobilePrefix().length() + 1)
                        txtmobile2.setPrefix("");
                }*/
            }
        });

       /* txtmobile1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtmobile1.setPrefix(HMConstants.mobilePrefix);
                } else {
                    if (txtmobile1.length() < HMConstants.mobilePrefix.length() + 1)
                        txtmobile1.setPrefix("");
                }
            }
        });

        txtmobile2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    txtmobile2.setPrefix(HMConstants.mobilePrefix);
                } else {
                    if (txtmobile2.length() < HMConstants.mobilePrefix.length() + 1)
                        txtmobile2.setPrefix("");
                }
            }
        });*/

        btnlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), placesActivity.class);
                startActivityForResult(intent, PLACES_RETURN_RESULT);
                /*PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent = builder.build(getActivity());
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                }*/
            }
        });

        txtifsccode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                   /* if(txtifsccode.getText().toString().trim().length()>=11){
                        JSONObject jsonParam = new JSONObject();
                        JSONObject jsonMain = new JSONObject();
                        try {
                            jsonMain.put("merchantcode", HMConstants.appmerchantid);
                            jsonMain.put("mdevice", intromanager.getDevice());
                            jsonMain.put("reference", txtifsccode.getText().toString());
                            jsonParam.put("indata", jsonMain);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            myDB.showToast(getContext(),e.getMessage());
                            return;
                        }
                        String[] myTaskParams = {"/SSMIFSCCodeCheck", jsonParam.toString()};
                        //  new HMDataAccess().execute(myTaskParams);
                        try {
                            Log.i("Debugging",jsonParam.toString());
                            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar11, "SSMIFSCCodeCheck");
                            aasyncTask.delegate = (AsyncResponse) fr_promoter.this;
                            aasyncTask.execute(myTaskParams);
                        } catch (Exception e) {
                            e.printStackTrace();
                            myDB.showToast(getContext(),e.getMessage());
                            return;
                        }
                    }*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        btncreateaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtfullname.getText().toString().trim().length() == 0) {
                    txtfullname.setError("Enter full name");
                    txtfullname.requestFocus();
                    return;
                }

                if (txtmobile1.getText().toString().trim().length() != 12) {
                    txtmobile1.setError("enter a primary mobile number(9715xxxxxxxx");
                    txtmobile1.requestFocus();
                    return;
                }

                if (txtmobile2.getText().toString().trim().length() != 12) {
                    txtmobile2.setError("enter a secondary mobile number (9715xxxxxxxx");
                    txtmobile2.requestFocus();
                    return;
                }

                if (txtemail.getText().toString().trim().length() == 0) {
                    txtemail.setError("Enter a valid email address");
                    txtemail.requestFocus();
                    return;
                }

                if (address.getText().toString().trim().length() == 0) {
                    address.setError("Enter address");
                    address.requestFocus();
                    return;
                }

                if (txtaccount.getText().toString().trim().length() == 0) {
                    txtaccount.setError("Enter bank IBAN");
                    txtaccount.requestFocus();
                    return;
                }

                if (txtifsccode.getText().toString().trim().length() == 0) {
                    txtifsccode.setError("Enter Bank Name");
                    txtifsccode.requestFocus();
                    return;
                }

          /*      if (!ifsc){
                    txtifsccode.setError("Invalid IFSC Code");
                    txtifsccode.requestFocus();
                    return;
                }*/
                if (addressloc.getText().toString().trim().length() == 0) {
                    myDB.showToast(getContext(), "Set geolocation through marker");
                    return;
                }
                if (!myDB.isValidEmailId(txtemail.getText().toString())) {
                    txtemail.setError("Invalid Email Address");
                    return;
                }
                if (!switchtc.isChecked()) {
                    myDB.showToast(getContext(), "Please accept Terms & Conditions");
                    return;
                }

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("mobile", txtmobile1.getText());
                    jsonMain.put("email", txtemail.getText());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMSendEnrolmentToken", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar11, "SSMSendEnrolmentToken");
                    aasyncTask.delegate = (AsyncResponse) fr_promoter.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                    return;
                }

            }
        });
        return v;
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        Bitmap bitmap;
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
        } else if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("fullname", String.valueOf(txtfullname.getText()));
            bundle.putString("txtmobile1", String.valueOf(txtmobile1.getText()));
            bundle.putString("txtmobile2", String.valueOf(txtmobile2.getText()));
            bundle.putString("txtemail", String.valueOf(txtemail.getText()));
            bundle.putString("txtaddress", String.valueOf(address.getText()));
            bundle.putString("txtgeolocation", String.valueOf(addressloc.getText()));
            bundle.putString("txtaccount", String.valueOf(txtaccount.getText()));
            bundle.putString("txtifsccode", String.valueOf(txtifsccode.getText()));
            bundle.putString("gender", (male.isChecked() ? "M" : "F"));
            bundle.putString("tokenref", myjson.getString("referencenumber"));
            bundle.putString("calledfrom", "promoter");
            Log.i("Debugging", "address location " + addressloc.getText());
            Fragment myFragment = new fr_validation();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == android.R.id.home) {
            Fragment myFragment = new fr_account();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    public void onActivityResult(int requestCode, int resultcode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultcode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
//                String address = String.format("%s", place.getAddress());
                String latlng = String.format("%s", place.getLatLng());
                latlng = latlng.substring(latlng.indexOf("(") + 1, latlng.length() - 1);
                addressloc.setText(latlng);

            }
            //   if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
            //      getActivity().getSupportFragmentManager().popBackStack();
            return;
        } else if (requestCode == PLACES_RETURN_RESULT) {
            if (resultcode == RESULT_OK) {
//                String address = String.format("%s", data.getStringExtra("keyLocation"));
                String latlng = String.format("%s", data.getStringExtra("keyLatLon"));
                /*latlng = latlng.substring(latlng.indexOf("(") + 1, latlng.length() - 1);*/
                addressloc.setText(latlng);
            }
        }
    }
}

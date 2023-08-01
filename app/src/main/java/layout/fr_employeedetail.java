package layout;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.placesActivity;
import com.aic.aicdelivery.spinClass2;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_employeedetail extends Fragment implements AsyncResponse {
    private static final int PLACES_RETURN_RESULT = 2;
    private int PLACE_PICKER_REQUEST = 1;
    EditText txtmobile1;
    private EditText txtfullname, txtemail, address, addressloc;
    private Spinner spnqualify;
    private Button btnnext;
    private ImageButton btnlocation;
    private RadioButton male, female, active, inactive;
    private ProgressBar progressBar11;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private IntroManager intromanager;
    private String qualcode = "", stremployee = "";

    public fr_employeedetail() {
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
        myDB = new HMCoreData(getContext());

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_employeedetail, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Employee Information");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        btnnext = (Button) v.findViewById(R.id.btnnext);
        spnqualify = (Spinner) v.findViewById(R.id.spnqualify);
        addressloc = (EditText) v.findViewById(R.id.addressloc);
        txtfullname = (EditText) v.findViewById(R.id.txtfullname);
        txtmobile1 =  v.findViewById(R.id.txtmobile1);
        txtemail = (EditText) v.findViewById(R.id.txtemail);
        address = (EditText) v.findViewById(R.id.address);
        male = (RadioButton) v.findViewById(R.id.male);
        female = (RadioButton) v.findViewById(R.id.female);
        active = (RadioButton) v.findViewById(R.id.active);
        inactive = (RadioButton) v.findViewById(R.id.inactive);
        btnlocation = (ImageButton) v.findViewById(R.id.btnlocation);
        progressBar11 = (ProgressBar) v.findViewById(R.id.progressBar11);

        txtmobile1.setText(intromanager.getMobilePrefix());

        txtmobile1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
           /*     if (hasFocus) {
                    txtmobile1.setPrefix(intromanager.getMobilePrefix());
                } else {
                    if (txtmobile1.length() < intromanager.getMobilePrefix().length() + 1)
                        txtmobile1.setPrefix("");
                }*/
            }
        });

        setData();
//        spnqualify.setVisibility(View.GONE);
        if ((getArguments() != null)) {
            stremployee = getArguments().getString("employeeaccount");
            fetchData();
            txtfullname.setEnabled(false);
            txtmobile1.setEnabled(false);
        }
        //retrieve memory

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
                    return;
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                    return;
                }*/
                //  Fragment myFragment = new firsttime_address();
                //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        if (spnqualify.getVisibility() == View.VISIBLE)
            spnqualify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    spinClass2 spin = (spinClass2) parent.getSelectedItem();
                    qualcode = spin.getId();
                    //      myDB.showToast(getActivity(),qualcode);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });


        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtfullname.length() == 0) {
                    txtfullname.setError("Enter full name");
                    return;
                }

                if (txtmobile1.length() != 12) {
                    txtmobile1.setError("Enter primary mobile number(9715xxxxxxxx");
                    return;
                }


                if (txtemail.length() == 0) {
                    txtemail.setError("Enter a valid email address");
                    return;
                }

                if (address.length() == 0) {
                    address.setError("Enter address");
                    return;
                }

                if (spnqualify.getVisibility() == View.VISIBLE)
                    if (qualcode.length() == 0) {
                        myDB.showToast(getContext(), "Select Skill");
                        return;
                    }

                if (addressloc.length() == 0) {
                    addressloc.setError("Set geolocation through marker");
                    return;
                }
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

                if ((getArguments() != null)) {
                    updateemployee();
                } else {
                    addemployee();
                }
            }
        });
        return v;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.i("Debugging", "Partner Profile  Output" + output);
        JSONObject objects;
        JSONObject myjson = new JSONObject(output);

        if (!myjson.getString("statuscode").equals("000")) {
            myDB.showToast(getActivity(), myjson.getString("statusdesc"));
            return;
        } else if (handle.equals("SSMServiceList")) {
            ArrayList<spinClass2> spinQualify = new ArrayList<>();
            JSONArray json_array_item = myjson.getJSONArray("servicelist");
            spinQualify.add(new spinClass2("Select Skill", "", ""));
            for (int i = 0; i < json_array_item.length(); i++) {
                objects = json_array_item.getJSONObject(i);
                spinQualify.add(new spinClass2(objects.getString("servicename"), objects.getString("servicecode"), ""));
            }
            ArrayAdapter<spinClass2> adapter = new ArrayAdapter<spinClass2>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinQualify);
            spnqualify.setAdapter(adapter);


        } else if (handle.equals("SSMaddpartneremployee")) {
            Bundle bundle = new Bundle();
            bundle.putString("orderref", "Thank You");
            bundle.putString("message", "New Employee details successfully updated.");
            Fragment myFragment = new fr_confirm_request_post();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        } else if (handle.equals("SSMupdatepartneremployee")) {
            Bundle bundle = new Bundle();
            bundle.putString("orderref", "Thank You");
            bundle.putString("message", "Employee details successfully updated.");
            Fragment myFragment = new fr_confirm_request_post();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        } else if (handle.equals("SSMGetPartnerEmployee")) {
            txtfullname.setText(myjson.getString("fullname"));
            txtmobile1.setText(myjson.getString("mobile"));
            txtemail.setText(myjson.getString("email"));
            address.setText(myjson.getString("address"));
            addressloc.setText(myjson.getString("geolocation"));
            if (myjson.getString("status").equals("1")) {
                active.setChecked(true);
            } else {
                inactive.setChecked(true);
            }
            if (myjson.getString("gender").equals("M")) {
                male.setChecked(true);
            } else {
                female.setChecked(true);
            }

            for (int i = 0; i < spnqualify.getCount(); i++) {
                Log.i("Skill Register ", spnqualify.getItemAtPosition(i).toString() + "   " + myjson.getString("subcategoryname"));
                if ((spnqualify.getItemAtPosition(i).toString().equals(myjson.getString("subcategoryname")))) {
                    spnqualify.setSelection(i);
                    break;
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == android.R.id.home) {
            Fragment myFragment = new fr_employeelist();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    private void setData() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonParam.put("indata", jsonMain);

        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMServiceList", jsonParam.toString()};
        Log.i("Debugging", "Service list " + jsonParam.toString());
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar11, "SSMServiceList");
            aasyncTask.delegate = (AsyncResponse) fr_employeedetail.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
    }

    public void onActivityResult(int requestCode, int resultcode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultcode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String address = String.format("%s", place.getAddress());
                String latlng = String.format("%s", place.getLatLng());
                latlng = latlng.substring(latlng.indexOf("(") + 1, latlng.length() - 1);
                addressloc.setText(latlng);
            }
            //   if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
            //      getActivity().getSupportFragmentManager().popBackStack();
        } else if (requestCode == PLACES_RETURN_RESULT) {
            if (resultcode == RESULT_OK) {
                String address = String.format("%s", data.getStringExtra("keyLocation"));
                String latlng = String.format("%s", data.getStringExtra("keyLatLon"));
                /*latlng = latlng.substring(latlng.indexOf("(") + 1, latlng.length() - 1);*/
                addressloc.setText(latlng);
            }
        }
    }

    private void addemployee() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("fullname", txtfullname.getText().toString());
            jsonMain.put("mobile", txtmobile1.getText().toString());
            jsonMain.put("email", txtemail.getText().toString());
            jsonMain.put("address", address.getText().toString());
            jsonMain.put("gender", male.isChecked() ? "M" : "F");
            jsonMain.put("status", active.isChecked() ? "1" : "0");
            jsonMain.put("geolocation", addressloc.getText().toString());
            jsonMain.put("fbtoken", intromanager.getFCMKey());
            jsonMain.put("service", qualcode);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMaddpartneremployee", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("Partner Register ", jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar11, "SSMaddpartneremployee");
            aasyncTask.delegate = (AsyncResponse) fr_employeedetail.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
    }

    private void updateemployee() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();


        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("employeeaccount", stremployee);
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("fullname", txtfullname.getText().toString());
            jsonMain.put("mobile", txtmobile1.getText().toString());
            jsonMain.put("email", txtemail.getText().toString());
            jsonMain.put("address", address.getText().toString());
            jsonMain.put("gender", male.isChecked() ? "M" : "F");
            jsonMain.put("status", active.isChecked() ? "1" : "0");
            jsonMain.put("geolocation", addressloc.getText().toString());
            jsonMain.put("fbtoken", intromanager.getFCMKey());
            jsonMain.put("service", qualcode);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMupdatepartneremployee", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            Log.i("Partner Register ", jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar11, "SSMupdatepartneremployee");
            aasyncTask.delegate = (AsyncResponse) fr_employeedetail.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
    }

    private void fetchData() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("employeeaccount", stremployee);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMGetPartnerEmployee", jsonParam.toString()};

        try {
            Log.i("Debugging", "Partner Profile Request Input" + jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar11, "SSMGetPartnerEmployee");
            aasyncTask.delegate = fr_employeedetail.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
    }
}

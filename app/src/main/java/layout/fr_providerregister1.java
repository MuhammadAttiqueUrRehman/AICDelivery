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
import android.widget.Switch;

import com.aic.aicdelivery.HMAppVariables;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.placesActivity;
import com.aic.aicdelivery.providerInfo;
import com.aic.aicdelivery.spinClass;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_providerregister1 extends Fragment {
    private int PLACE_PICKER_REQUEST = 1;
    private static final int PLACES_RETURN_RESULT = 2;
    private EditText txtfullname, txtemail, address, txtyear, txtteam, addressloc, txtcontact, txtradius;
    private EditText txtmobile1, txtmobile2;
    private Spinner spnqualify;
    private Button btnnext;
    private ImageButton btnlocation;
    private RadioButton male, female, individual, company, unspecified;
    private ProgressBar progressBar11;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private Switch switchtc;
    private IntroManager intromanager;
    private providerInfo providerinfo;
    private String qualcode = "";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public fr_providerregister1() {
        // Required empty public constructor
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
        View v = inflater.inflate(R.layout.fragment_fr_providerregister1, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("General Information");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        btnnext = (Button) v.findViewById(R.id.btnnext);
        spnqualify = (Spinner) v.findViewById(R.id.spnqualify);
        addressloc = (EditText) v.findViewById(R.id.addressloc);
        txtfullname = (EditText) v.findViewById(R.id.txtfullname);
        txtmobile1 = v.findViewById(R.id.txtmobile1);
        txtmobile2 = v.findViewById(R.id.txtmobile2);
        txtemail = (EditText) v.findViewById(R.id.txtemail);
        address = (EditText) v.findViewById(R.id.address);
        txtyear = (EditText) v.findViewById(R.id.txtyear);
        txtteam = (EditText) v.findViewById(R.id.txtteam);
        txtcontact = (EditText) v.findViewById(R.id.txtcontact);
        txtradius = (EditText) v.findViewById(R.id.txtradius);
        male = (RadioButton) v.findViewById(R.id.male);
        company = (RadioButton) v.findViewById(R.id.company);
        female = (RadioButton) v.findViewById(R.id.female);
        individual = (RadioButton) v.findViewById(R.id.individual);
        unspecified = (RadioButton) v.findViewById(R.id.unspecified);
        btnlocation = (ImageButton) v.findViewById(R.id.btnlocation);
        switchtc = (Switch) v.findViewById(R.id.switchtc);
        progressBar11 = (ProgressBar) v.findViewById(R.id.progressBar11);
        setData();

        //retrieve memory
        if (providerinfo.getFullName().length() > 0) {
            txtfullname.setText(providerinfo.getFullName());
            txtmobile1.setText(providerinfo.getMobile1());
            txtmobile2.setText(providerinfo.getMobile2());
            txtemail.setText(providerinfo.getEmail());
            address.setText(providerinfo.getAddress());
            txtyear.setText(providerinfo.getYear());
            txtteam.setText(providerinfo.getTeamSize());
            addressloc.setText(providerinfo.getLatLon());
            txtcontact.setText(providerinfo.getContactName());
            txtradius.setText(providerinfo.getRadius());
            if (providerinfo.getEntity().equals("C")) {
                company.setChecked(true);
            } else {
                individual.setChecked(true);
            }
            if (providerinfo.getGender().equals("M")) {
                male.setChecked(true);
            } else if (providerinfo.getGender().equals("F")) {
                female.setChecked(true);
            } else {
                unspecified.setChecked(true);
            }

            for (int i = 0; i < spnqualify.getCount(); i++) {
                if (spnqualify.getItemAtPosition(i).equals(providerinfo.getQualification())) {
                    spnqualify.setSelection(i);
                    Log.i("Debugging", "Spinner Qualification " + providerinfo.getQualification());
                    break;
                }
            }
        }

        txtmobile1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
             /*   if (hasFocus) {
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
                /*if (hasFocus) {
                    txtmobile2.setPrefix(intromanager.getMobilePrefix());
                } else {
                    if (txtmobile2.length() < intromanager.getMobilePrefix().length() + 1)
                        txtmobile2.setPrefix("");
                }*/
            }
        });

        /*txtmobile1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }*/
                //  Fragment myFragment = new firsttime_address();
                //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        spnqualify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinClass spin = (spinClass) parent.getSelectedItem();
                qualcode = spin.getId();
                //   myDB.showToast(getActivity(),qualcode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //txtyear,txtteam,txtreferral,txtaadhar,txttrade,txtpan,txtdl
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtfullname.getText().toString().trim().length() == 0) {
                    txtfullname.setError("Enter full name");
                    return;
                }

                if (txtcontact.getText().toString().trim().length() == 0) {
                    txtcontact.setError("Enter contact name");
                    return;
                }

                if (txtmobile1.getText().toString().trim().length() != 12) {
                    txtmobile1.setError("Enter primary mobile number");
                    return;
                }

                if (txtmobile2.getText().toString().trim().length() != 12) {
                    txtmobile2.setError("Enter alternate mobile number");
                    return;
                }

                if (txtemail.getText().toString().trim().length() == 0) {
                    txtemail.setError("Enter a valid email address");
                    return;
                }

                if (address.getText().toString().trim().length() == 0) {
                    address.setError("Enter address");
                    return;
                }

                if (qualcode.length() == 0) {
                    myDB.showToast(getContext(), "Select qualification");
                    return;
                }

                if (addressloc.getText().toString().trim().length() == 0) {
                    addressloc.setError("Set geolocation through marker");
                    return;
                }
                if (txtradius.getText().toString().trim().equals("")) {
                    txtradius.setError("Enter a valid number (0 - 30)");
                    return;
                }

                if (Integer.parseInt(txtradius.getText().toString()) < 3) {
                    txtradius.setError("The operating radius should be 3 or more Kms.");
                    return;
                }
                if (!myDB.isValidEmailId(txtemail.getText().toString())) {
                    txtemail.setError("Invalid Email Address");
                    return;
                }

                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                providerinfo.setFullName(txtfullname.getText().toString());
                providerinfo.setMobile1(txtmobile1.getText().toString());
                providerinfo.setMobile2(txtmobile2.getText().toString());
                providerinfo.setEmail(txtemail.getText().toString());
                providerinfo.setYear(txtyear.getText().toString());
                providerinfo.setTeamSize(txtteam.getText().toString());
                providerinfo.setAddress(address.getText().toString());
                providerinfo.setLatLon(addressloc.getText().toString());
                providerinfo.setContactName(txtcontact.getText().toString());
                providerinfo.setQualification(qualcode);
                providerinfo.setEntity(company.isChecked() ? "C" : "I");
                providerinfo.setRadius(txtradius.getText().toString());
                /*intromanager.setImageCount(0);*/
                if (male.isChecked()) {
                    providerinfo.setGender("M");
                } else if (female.isChecked()) {
                    providerinfo.setGender("F");
                } else {
                    providerinfo.setGender("U");
                }

                //Update memory
                Log.i("Debugging", "Full Name " + providerinfo.getFullName());
                Log.i("Debugging", "Mobile 1 " + providerinfo.getMobile1());
                Log.i("Debugging", "Mobile 2 " + providerinfo.getMobile2());
                Log.i("Debugging", "Email " + providerinfo.getEmail());
                Log.i("Debugging", "Address " + providerinfo.getAddress());
                Log.i("Debugging", "Location " + providerinfo.getLatLon());
                Log.i("Debugging", "Qualification " + providerinfo.getQualification());
                Log.i("Debugging", "Entity " + providerinfo.getEntity());
                Log.i("Debugging", "Gender " + providerinfo.getGender());


                Fragment myFragment = new fr_providerregister2();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });
        return v;
    }


    private void setData() {

        ArrayList<spinClass> spinQualify = new ArrayList<>();

        spinQualify.add(new spinClass("", "Select Qualification", 0, false));
        spinQualify.add(new spinClass("M", "Master's Degree", 0, false));
        spinQualify.add(new spinClass("E", "Engineering", 0, false));
        spinQualify.add(new spinClass("G", "Graduate", 0, false));
        spinQualify.add(new spinClass("D", "Diploma", 0, false));
        spinQualify.add(new spinClass("H", "Higher Secondary", 0, false));
        spinQualify.add(new spinClass("P", "Primary School", 0, false));

        //fill data in spinner
        ArrayAdapter<spinClass> adapter = new ArrayAdapter<spinClass>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinQualify);
        spnqualify.setAdapter(adapter);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        Log.i("Debugging", "Option Menu xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx ");
        int id = items.getItemId();
        if (id == android.R.id.home) {
            Fragment myFragment = new fr_registertype();
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
                providerinfo.setLatLon(latlng);
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
                providerinfo.setLatLon(latlng);
                addressloc.setText(latlng);
            }
        }
    }
}

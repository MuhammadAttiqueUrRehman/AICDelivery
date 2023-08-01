package layout;

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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMAppVariables;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;


public class fr_settings extends Fragment implements AsyncResponse{
    private ProgressBar progressBar9;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    IntroManager intromanager;
    private Button btnsave;
    private Switch switchcity,switchoffer;

    public fr_settings() {
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
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fr_settings, container, false);
        // Inflate the layout for this fragment
        intromanager=new IntroManager(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(),e.getMessage());
            return null;
        }

        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Settings");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        progressBar9= (ProgressBar) v.findViewById(R.id.progressBar9);
        btnsave=(Button) v.findViewById(R.id.btnhome);
        switchcity=(Switch) v.findViewById(R.id.switchcity);
        switchoffer=(Switch) v.findViewById(R.id.switchoffer);


        switchcity.setChecked(intromanager.getCityNoLimit().equals("1")?true:false);
        switchoffer.setChecked(intromanager.getPushOffers().equals("1")?true:false);


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("employeeid", intromanager.getEmployeeID());
                    jsonMain.put("certificate", intromanager.getCertificate());
                    jsonMain.put("outlet", intromanager.getOutletID());
                    jsonMain.put("citynolimit", switchcity.isChecked()?"1":"0");
                    jsonMain.put("pushoffers", switchoffer.isChecked()?"1":"0");
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(),e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMVendorSettings", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "SSMVendorSettings");
                    aasyncTask.delegate = (AsyncResponse) fr_settings.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(),e.getMessage());
                    return;
                }
            }
        });


        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        int id=items.getItemId();
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output=output.substring(1,output.length()-1);
        output=output.replace("\\", "");
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        //Write local SQL
        JSONObject myjson = new JSONObject(output);
        statuscode = myDB.statusValues(output);
        statuscodekey= (String) statuscode.get("statuscode");
        statusdesckey= (String) statuscode.get("statusdesc");
        if (!statuscodekey.toString().equals("000")) {
           myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            intromanager.setCityNoLimit(switchcity.isChecked()?"1":"0");
            intromanager.setPushOffers(switchoffer.isChecked()?"1":"0");
            Fragment myFragment = new fr_settings_confirm();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }
}

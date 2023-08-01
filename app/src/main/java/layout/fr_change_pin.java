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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class fr_change_pin extends Fragment implements AsyncResponse {
    private EditText txtcode;
    private Button btnvalidate;
    private ProgressBar progressBar15;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private TextView mTitle;
    private IntroManager intromanager;

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
        myDB = new HMCoreData(getContext());
        intromanager = new IntroManager(getActivity());
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_change_pin, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Current PIN");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        btnvalidate = (Button) v.findViewById(R.id.btnvalidate);
        txtcode = (EditText) v.findViewById(R.id.txtcurpin);
        progressBar15 = (ProgressBar) v.findViewById(R.id.progressBar15);

        btnvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtcode.length() == 0) {
                    txtcode.setError("Please enter current 4 digit PIN");
                    return;
                }
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("employeeid", intromanager.getMobile());
                    jsonMain.put("pin", txtcode.getText());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    myDB.showToast(getActivity(), e.getMessage());
                }
                String[] myTaskParams = {"/SSMvalidatePasswordVendor", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar15, "SSMvalidatePasswordVendor");
                    aasyncTask.delegate = (AsyncResponse) fr_change_pin.this;
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
        Map<String, String> statuscode;
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        String statuscodekey;
        String statusdesckey;
        //Write local SQL
        JSONObject myjson = new JSONObject(output);
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), "Current PIN Incorrect, re-enter!");
            return;
        } else {
            Fragment myFragment = new fr_reset_pin();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
       /* int id=items.getItemId();
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }*/
        Main2Activity.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        return true;
    }

}


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
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_refererdashboard extends Fragment implements AsyncResponse {
    private TextView lblreferral, lbltxn;
    private Button btnrefresh;
    private IntroManager intromanager;
    private HMCoreData myDB;
    private ProgressBar progressBar4;

    public fr_refererdashboard() {
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
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == android.R.id.home) {
            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getActivity());

        View v = inflater.inflate(R.layout.fragment_fr_refererdashboard, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Referral Dashboard");
        lblreferral = (TextView) v.findViewById(R.id.lblreferal);
        lbltxn = (TextView) v.findViewById(R.id.lbltxn);
        btnrefresh = (Button) v.findViewById(R.id.btnrefresh);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        refreshReferal();


        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshReferal();
            }
        });


        return v;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        Log.i("Debugging", output);
        Log.i("Debugging", handle);
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        if (!statuscodekey.equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            lblreferral.setText(myjson.getString("referal"));
            lbltxn.setText(myjson.getString("txn"));
        }
    }

    private void refreshReferal() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
        }
        String[] myTaskParams = {"/SSMReferalDashboardPartner", jsonParam.toString()};

        try {
            Log.i("Debugging", "Referral Dashboard Input" + jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4, "SSMReferalDashboardPartner");
            aasyncTask.delegate = (AsyncResponse) fr_refererdashboard.this;
            aasyncTask.execute(myTaskParams);
            Log.i("Debugging", "Referral Refresh" + jsonParam.toString());
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
        }
        return;
    }
}
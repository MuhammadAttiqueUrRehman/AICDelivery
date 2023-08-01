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
import android.widget.RatingBar;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_unfeedback extends Fragment implements AsyncResponse {
    private ProgressBar progressBar9;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    IntroManager intromanager;
    private EditText txtAddInfo;
    private RatingBar starrating;
    private Button btnhome;

    public fr_unfeedback() {
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
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getContext());

        View v = inflater.inflate(R.layout.fragment_fr_unfeedback, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(
                getString(R.string.app_name) + " Rating");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        txtAddInfo = (EditText) v.findViewById(R.id.txtAddinfo);
        starrating = (RatingBar) v.findViewById(R.id.ratingstar);
        btnhome = (Button) v.findViewById(R.id.btnhome);
        progressBar9 = (ProgressBar) v.findViewById(R.id.progressBar9);

        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (txtAddInfo.getText().toString().trim().equals("")) {
                    txtAddInfo.setError("Enter few comments about your experience with the app.");
                    return;
                }
                if (starrating.getRating() == 0) {
                    myDB.showToast(getContext(), "Provide Rating 1 to 5");
                    return;
                }
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("producttype", "2");
                    jsonMain.put("comments", txtAddInfo.getText());
                    jsonMain.put("outlet", intromanager.getOutletID());
                    jsonMain.put("starrating", starrating.getRating() <= 1 ? "1" : starrating.getRating());
                    jsonMain.put("customer", intromanager.getEmployeeID());
                    jsonMain.put("certificate", intromanager.getCertificate());
                    jsonMain.put("orderid", "");
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                    return;
                }
                String[] myTaskParams = {"/SSMUNRating", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar9, "SSMUNRating");
                    aasyncTask.delegate = (AsyncResponse) fr_unfeedback.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }

            }
        });

        return v;
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
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            Fragment myFragment = new fr_feedback_confirm();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }
}

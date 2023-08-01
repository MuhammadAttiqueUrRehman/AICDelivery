package layout;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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


public class fr_reset_pin extends Fragment implements AsyncResponse {
    private Button btnvalidate;
    private ProgressBar progressBar10;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private TextView mTitle;
    private EditText txtpin;
    private ImageButton btnloginclose;
    IntroManager intromanager;

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
        View v = inflater.inflate(R.layout.fragment_fr_reset_pin, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("New PIN");
        mtoolbar.setNavigationIcon(R.drawable.ico_cross);

        btnvalidate = (Button) v.findViewById(R.id.btnvalidate);
        progressBar10 = (ProgressBar) v.findViewById(R.id.progressBar10);
        txtpin = (EditText) v.findViewById(R.id.txtnewpin);

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_login();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnvalidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtpin.length() != 4) {
                    txtpin.setError("Enter a valid 4 digit PIN");
                    return;
                }

                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("reference", intromanager.getFCMKey());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("customer", intromanager.getMobile());
                    jsonMain.put("idnumber", txtpin.getText());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    myDB.showToast(getActivity(), e.getMessage());
                }
                String[] myTaskParams = {"/SSMsavePINVendor", jsonParam.toString()};
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar10, "resetpin");
                    aasyncTask.delegate = (AsyncResponse) fr_reset_pin.this;
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
        Log.i("Debugging Login Result", output);
        //Write local SQL
        JSONObject myjson = new JSONObject(output);
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
            Bundle bundle = new Bundle();
            bundle.putString("orderref", "Thank You");
            bundle.putString("message", "Your new PIN has been setup successfully, now you can login with the new PIN");
            Fragment myFragment = new fr_confirm_pinchange();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }


}

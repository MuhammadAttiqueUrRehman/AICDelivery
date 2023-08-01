package layout;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMAppVariables;
import com.aic.aicdelivery.HMConstants;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;

import org.json.JSONException;
import org.json.JSONObject;


public class fr_tandc extends Fragment implements AsyncResponse {
    private Toolbar mtoolbar;
    private TextView mTitle, aboutinfo;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private IntroManager intromanager;
    private ProgressBar progressBar13;

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
        myDB = new HMCoreData(getContext());
        intromanager = new IntroManager(getActivity());
        View v = inflater.inflate(R.layout.fragment_fr_tandc, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Terms & Conditions");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        aboutinfo = (TextView) v.findViewById(R.id.taboutinfo);
        progressBar13 = (ProgressBar) v.findViewById(R.id.progressBar13);

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("reference", HMConstants.tandc);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        Log.i("Debugging", "Booking Timeline Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMGeneralContentDisplay", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar13, "SSMGeneralContentDisplay");
            aasyncTask.delegate = (AsyncResponse) fr_tandc.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        int id = items.getItemId();
        if (id == android.R.id.home) {
//            if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)
            getActivity().getSupportFragmentManager().popBackStack();
//            Main2Activity.bottomNavigationView.setSelectedItemId(R.id.nav_home);
            return true;
        }
        if (Main2Activity.bottomNavigationView != null) {
            Main2Activity.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
        return true;
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\\\\\\"", "'");
        output = output.replace("\\", "");
        JSONObject myjson = (JSONObject) new JSONObject(output);
        String mt = myjson.getString("longtext").replace("rn", "");
        mt = mt.replace("'", "\"");
        aboutinfo.setText(Html.fromHtml(mt), TextView.BufferType.SPANNABLE);
    }
}

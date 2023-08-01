package layout;

import android.content.Intent;
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
import com.aic.aicdelivery.HMConstants;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.MainActivity;
import com.aic.aicdelivery.R;

import org.json.JSONException;
import org.json.JSONObject;


public class fr_data_privacy_pre extends Fragment implements AsyncResponse {
    private Toolbar mtoolbar;
    private TextView aboutinfo;
    private HMCoreData myDB;
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
        View v = inflater.inflate(R.layout.fragment_fr_data_privacy_pre, container,false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Privacy Policy");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        aboutinfo = (TextView) v.findViewById(R.id.taboutinfo);
        progressBar13 = (ProgressBar) v.findViewById(R.id.progressBar13);
        //aboutinfo.setText(Html.fromHtml(getString(R.string.versiontext)),TextView.BufferType.SPANNABLE);

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", HMConstants.appmerchantid);
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("reference", HMConstants.dataPrivacy);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        Log.i("Debugging","Booking Timeline Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMGeneralContentDisplay", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar13, "SSMGeneralContentDisplay");
            aasyncTask.delegate = (AsyncResponse) fr_data_privacy_pre.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        return v;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
        return true;
      /*  int id=items.getItemId();
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
        Fragment myFragment = new fr_login();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_place, myFragment).addToBackStack(null).commitAllowingStateLoss();
        return super.onOptionsItemSelected(items);*/
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        //   output = output.substring(1, output.length() - 1);
        //    output = output.replace("\\", "");
        //     Gson myjson= new Gson();
        //     Properties data = myjson.fromJson(output, Properties.class);

        //   output = output.replace("\"", "'");
        //JSONObject myjson =  new JSONObject(output);
        //     aboutinfo.setText(Html.fromHtml(data.getProperty("longtext")));

        output = output.substring(1, output.length() - 1);
        output = output.replace("\\\\\\\"", "'");
        output = output.replace("\\", "");
        JSONObject myjson =(JSONObject) new JSONObject(output);
        String mt = myjson.getString("longtext").replace("rn","");
        mt = mt.replace("'","\"");
        aboutinfo.setText(Html.fromHtml(mt),TextView.BufferType.SPANNABLE);  }
}

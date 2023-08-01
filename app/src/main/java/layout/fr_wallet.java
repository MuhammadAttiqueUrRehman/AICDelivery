package layout;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.walletAdapter;
import com.aic.aicdelivery.walletVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_wallet extends Fragment implements AsyncResponse {
    private static LinearLayout fillcart;
    private static LinearLayout emptycart;
    private walletAdapter adapter;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private ProgressBar progressBar4;
    private ArrayList<walletVO> walletList = new ArrayList<walletVO>();
    private RecyclerView recyclerView;
    private View v;
    private TextView lblamount, lblcash, lblsettlement;
    private IntroManager intromanager;

    public fr_wallet() {
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
        Log.i("Debugging", "Option Menu");
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
        intromanager = new IntroManager(getActivity());
        myDB = new HMCoreData(getActivity());

        v = inflater.inflate(R.layout.fragment_fr_wallet, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Finances");
        recyclerView = (RecyclerView) v.findViewById(R.id.accountlist);
        lblamount = (TextView) v.findViewById(R.id.lblamount);
        lblcash = (TextView) v.findViewById(R.id.lblcash);
        lblsettlement = (TextView) v.findViewById(R.id.lblsettlement);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);
        lblamount.setText(getString(R.string.Rs) + " " + "0.00");
        fetchData();
        return v;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Log.i("Debugging", "Wallet Statement Input" + output);

        walletList.clear();
        adapter = new walletAdapter(getActivity(), walletList, R.layout.wallet_card);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        JSONObject myjson = new JSONObject(output);
        String ccy = myjson.getString("currency");
        JSONArray json_array = myjson.getJSONArray("walletstatement");
        final Float totamt = Float.parseFloat(myjson.getString("balance"));
        lblamount.setText(getString(R.string.Rs) + " " + String.format("%.02f", totamt));
        lblcash.setText("Cash Received: " + getString(R.string.Rs) + " " + String.format("%.02f", totamt));
        lblsettlement.setText("Last Settlement: " + getString(R.string.Rs) + " " + String.format("%.02f", totamt));
        for (int i = 0; i < json_array.length(); i++) {
            JSONObject objects = json_array.getJSONObject(i);
            walletVO y = new walletVO(objects.getString("transactiondate"), objects.getString("narration1"), objects.getString("narration2"), getString(R.string.Rs) + " " + objects.getString("amount"));
            walletList.add(y);
        }
        adapter.notifyDataSetChanged();


        if (json_array.length() == 0) {
            fillcart.setVisibility(View.GONE);
            emptycart.setVisibility(View.VISIBLE);
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.GONE);
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
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMVendorWalletStatement", jsonParam.toString()};

        try {
            Log.i("Debugging", "Wallet Statement Input" + jsonParam.toString());
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4, "SSMVendorWalletStatement");
            aasyncTask.delegate = fr_wallet.this;
            aasyncTask.execute(myTaskParams);
            Log.i("Debugging", "Wallet Statement Input" + jsonParam.toString());
        } catch (Exception e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
            return;
        }
    }

}

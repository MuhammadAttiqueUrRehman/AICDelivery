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
import com.aic.aicdelivery.HMAppVariables;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.reviewAdapter;
import com.aic.aicdelivery.reviewVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_vendorreview extends Fragment implements AsyncResponse {
    public static LinearLayout fillcart;
    public static LinearLayout emptycart;
    private reviewAdapter adapter;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;
    private RecyclerView recyclerView;
    private ProgressBar progressBar4;
    View v;
    MenuInflater inflater;
    ArrayList<reviewVO> reviewList = new ArrayList<reviewVO>();

    public fr_vendorreview() {
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
        myDB = new HMCoreData(getContext());

        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_vendorreview, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Customer Reviews");
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        recyclerView = (RecyclerView) v.findViewById(R.id.messqagelist);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);
        fetchData();
        return v;
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
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        String ccy = appVariables.ucurrency;
        JSONObject myjson = new JSONObject(output);
        JSONArray json_array_vendor = myjson.getJSONArray("customerreview");
        //Vendor Review carousal
        adapter = new reviewAdapter(getActivity(), (ArrayList<reviewVO>) reviewList, R.layout.review_cell);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        reviewList.clear();
        for (int x = 0; x < json_array_vendor.length(); x++) {
            JSONObject object1 = json_array_vendor.getJSONObject(x);
            reviewVO a = new reviewVO(object1.getString("outletimage"), json_array_vendor.length(), object1.getString("outletcode"), object1.getString("outletname"), object1.getInt("numberstar"), object1.getInt("numberofreviews"), object1.getString("comment"), object1.getString("customername"), object1.getString("city"), object1.getString("reviewdate"), object1.getString("servicecategory"));
            reviewList.add(a);
        }
        if (myjson.length() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchData() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            IntroManager intromanager = new IntroManager(requireContext());
            jsonMain.put("mdevice", appVariables.udevicecomboined);
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("producttype", "2");
            jsonMain.put("outlet", "");
            jsonMain.put("service", "");
            jsonParam.put("indata", jsonMain);

        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMCustomerReviewList", jsonParam.toString()};
        Log.i("Debugging", "JSON Vendor" + jsonParam.toString());
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMCustomerReviewList");
            aasyncTask.delegate = (AsyncResponse) fr_vendorreview.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }

    }

}

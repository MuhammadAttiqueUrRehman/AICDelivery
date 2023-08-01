package layout;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.filterDialog;
import com.aic.aicdelivery.providerInfo;
import com.aic.aicdelivery.timeLineAdapterMissed;
import com.aic.aicdelivery.timeLineVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class fr_transitorders extends Fragment implements AsyncResponse, filterDialog.BottomSheetListener {
    private static LinearLayout fillcart;
    private static LinearLayout emptycart;
    private timeLineAdapterMissed adapter;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private TextView mTitle;
    private RecyclerView recyclerView;
    private ProgressBar progressBar4x;
    private MenuInflater inflater;
    private IntroManager intromanager;
    private providerInfo providerinfo;
    ArrayList<timeLineVO> ar = new ArrayList<timeLineVO>();
    private Menu mnu;


    public fr_transitorders() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.vendor_search_menu, menu);
        this.mnu = menu;
        return;
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
        providerinfo = new providerInfo(getActivity());
        providerinfo.clearServiceItem();

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_transitorders, container, false);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Transit Orders");
        mtoolbar.setNavigationIcon(R.drawable.ico_back);
        recyclerView = (RecyclerView) v.findViewById(R.id.bookinglist);
        progressBar4x = (ProgressBar) v.findViewById(R.id.progressBar4x);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);

        fetchData();

        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Main2Activity.bottomNavigationView.setSelectedItemId(R.id.nav_home);
            }
        });

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    Main2Activity.bottomNavigationView.setVisibility(View.GONE);
                } else {
                    Main2Activity.bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }

          *//*  @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    Main2Activity.bottomNavigationView.setVisibility(View.VISIBLE);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }*//*
        });*/
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        Fragment myFragment;

        int id = items.getItemId();
        switch (id) {
            case R.id.nav_home:
             /*    myFragment = new fr_homepage();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/
                Main2Activity.bottomNavigationView.setSelectedItemId(R.id.nav_home);
                break;
            case R.id.menu_filter:
                filterDialog bs = new filterDialog();
                bs.show(getActivity().getSupportFragmentManager(), "Filter Services");
                bs.setTargetFragment(fr_transitorders.this, 1);
                // myFragment = new filterDialog();
                // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
        }
        return super.onOptionsItemSelected(items);
    }

    private void fetchData() {
        JSONArray servicejson = new JSONArray();
        JSONObject jsonDoc;
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();

        //Service details
        for (int i = 0; i < providerinfo.getAllServiceItems().size(); i++) {
            jsonDoc = new JSONObject();
            try {
                jsonDoc.put("det", providerinfo.getAllServiceItems().get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            servicejson.put(jsonDoc);
        }
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("employeeaccount", intromanager.getAdmin().equals("Y") ? "" : intromanager.getEmployeeID());
//            jsonMain.put("employeeaccount", intromanager.getEmployeeID());
            jsonMain.put("serviceList", servicejson);
            jsonMain.put("status", 4);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
        }
        Log.i("Debugging", "New Orders Input :" + jsonParam.toString());
//        String[] myTaskParams = {"/SSMMissedOrderList", jsonParam.toString()};
        String[] myTaskParams = {"/SSMNewOrderListProduct", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4x, "SSMMissedOrderList");
            aasyncTask.delegate = (AsyncResponse) fr_transitorders.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
            return;
        }
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\\\\\\"", "'");
        output = output.replace("\\", "");

        Log.i("Debugging", "Booking Timeline Output :" + output);

        adapter = new timeLineAdapterMissed(getActivity(), ar, R.layout.timeline_card);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        JSONObject myjson = new JSONObject(output);
        JSONArray json_array_item = myjson.getJSONArray("timeline");

        ar.clear();
        for (int i = 0; i < json_array_item.length(); i++) {
            JSONObject objects = json_array_item.getJSONObject(i);

            timeLineVO y = new timeLineVO(objects.getString("orderdate"), objects.getString("customercode"), objects.getString("orderid"), objects.getString("itemname"), objects.getString("logstatus"), objects.getString("logstatusdes"), objects.getString("vendorcode"), objects.getString("finalprice"), objects.getString("vendorname"), objects.getString("maxstatus"), objects.getString("remarks"), objects.getString("materialcharges"), objects.getString("materialgst")
                    , objects.has("deliverydate") ? objects.getString("deliverydate") : "00:00"
                    , objects.getString("paymenttype"), objects.getString("numberofitems")
                    , objects.getString("vendorcode"), objects.getString("vehiclename"), objects.getString("address")
                    , objects.getString("mobilenumber"), objects.getString("geolocation"), objects.getString("deliveryleadtime"), objects.getString("orderstatus")
                    , objects.getJSONArray("itemdetail").toString(), objects.getString("giftmessage"),objects.getString("customername"));
            ar.add(y);
        }
        adapter.notifyDataSetChanged();

        if (json_array_item.length() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onButtonClicked(String info) {
        Log.i("Debugging", "Came to Button Click  " + info);
        fetchData();
        if (info.equals("Y")) {
            mnu.getItem(0).setIcon(R.drawable.ic_tune_blue);
        } else {
            mnu.getItem(0).setIcon(R.drawable.ic_tune_white);
        }
    }
}
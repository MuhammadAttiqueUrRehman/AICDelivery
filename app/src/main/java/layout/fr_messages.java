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
import com.aic.aicdelivery.messageAdapterProd;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class fr_messages extends Fragment implements AsyncResponse {
    public static LinearLayout fillcart;
    public static LinearLayout emptycart;
    private messageAdapterProd adapter;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private TextView mTitle;
    private RecyclerView recyclerView;
    private Boolean forceupdate = false;
    private ProgressBar progressBar4;
    IntroManager intromanager;
    View v;
    MenuInflater inflater;
    MaterialSearchView searchview;
    ArrayList<String> ar = new ArrayList<String>();


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
        // inflater =   ((AppCompatActivity)getActivity()).getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = (MenuItem) menu.findItem(R.id.action_search);
        searchview.setMenuItem(item);
        // searchview.showSearch(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    /*@Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Debugging Menu", "Loaded Menu");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (forceupdate) {
            forceupdate = false;
        }
        myDB = new HMCoreData(getActivity());
        intromanager = new IntroManager(getActivity());

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_messages, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("My Notifications");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        searchview = (MaterialSearchView) v.findViewById(R.id.search_view);

        recyclerView = (RecyclerView) v.findViewById(R.id.messqagelist);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);

        fetchData();

        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                if (dy > 0 )
                {
                    Main2Activity.bottomNavigationView.setVisibility(View.GONE);
                } else{
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

        searchview.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {

            @Override
            public void onSearchViewShown() {
                //  searchview.clearFocus();
            }

            @Override
            public void onSearchViewClosed() {
                try {
                    searchLocal("");
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                    return;
                }
                searchview.hideKeyboard(v);
            }
        });

        searchview.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //     if (query.length()>0){
                Log.i("DebuggingPop", "Inside Submit");
                Log.i("DebuggingPop", "Showing Query" + query);
                try {
                    searchLocal(query);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                }
                searchview.hideKeyboard(v);
                //    }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 0) {
                    //        Log.i("DebuggingPop", "Inside Empty");
                    Log.i("DebuggingPop", "Inside Change");
                    try {
                        searchLocal(newText);
                    } catch (Exception e) {
                        e.printStackTrace();
                        myDB.showToast(getContext(), e.getMessage());
                    }
                }
                return true;
            }
        });

      /*  btnRecur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forceupdate=false;
                FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
                ftr.detach(fr_messages.this).attach(fr_messages.this).commitAllowingStateLoss();
            }
        });*/

        return v;
    }


    private void searchLocal(String txt) throws HMOwnException {
        ArrayList<String> ar1 = new ArrayList<String>();

        adapter = new messageAdapterProd(getContext(), ar1, "");
        recyclerView.setAdapter(adapter);
        Log.i("DebuggingPop", "Query Length " + txt.length());

        if (txt.length() == 0) {
            for (String item : ar) {
                ar1.add(item);
            }
        } else {
            for (String item : ar) {

                if (item.toUpperCase().contains(txt.toUpperCase())) {
                    ar1.add(item);
                }
            }
        }
        Log.i("DebuggingPop", "data size " + ar1.size());
        if (ar1.size() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);
        }


        adapter.notifyDataSetChanged();
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

        adapter = new messageAdapterProd(getContext(), ar, "");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        JSONObject myjson = new JSONObject(output);
        JSONArray json_array = myjson.getJSONArray("customermessages");

        for (int i = 0; i < json_array.length(); i++) {
            JSONObject objects = json_array.getJSONObject(i);
            ar.add(String.valueOf(objects));

        }
        if (json_array.length() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        } else {
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    /*
     merchantcode = indata.merchantcode
            employeeid = indata.employeeid
            customerid = indata.outlet
            password = indata.certificate
            device = indata.mdevice
     */

    private void fetchData() {
        ar.clear();
        if (!forceupdate) {
            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("mdevice", intromanager.getDevice());
                jsonMain.put("merchantcode", intromanager.getMerchantCode());
                jsonMain.put("outlet", intromanager.getOutletID());
                jsonMain.put("employeeid", intromanager.getEmployeeID());
                jsonMain.put("certificate", intromanager.getCertificate());
                jsonParam.put("indata", jsonMain);

            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            String[] myTaskParams = {"/SSMarchiveHistoryVendor", jsonParam.toString()};
            // new HMDataAccess().execute(myTaskParams);
            Log.i("Debugging", jsonParam.toString());
            try {
                HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMarchiveHistoryVendor");
                aasyncTask.delegate = (AsyncResponse) fr_messages.this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            forceupdate = true;
        }
    }


}

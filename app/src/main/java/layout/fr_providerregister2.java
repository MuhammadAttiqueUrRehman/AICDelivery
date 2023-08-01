package layout;


import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMAppVariables;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.providerInfo;
import com.aic.aicdelivery.serviceList;
import com.aic.aicdelivery.spinClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_providerregister2 extends Fragment implements AsyncResponse{
    private Button btnnext,btnprev;
    private RecyclerView recyclerview;
    private ProgressBar progressBar4;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private IntroManager intromanager;
    private providerInfo providerinfo;
    private ArrayList<spinClass> servicelist = new ArrayList<spinClass>();
    private serviceList adapter;


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
    public boolean onOptionsItemSelected(MenuItem items){
        Log.i("Debugging", "Option Menu xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx ");
        int id=items.getItemId();
        if (id == android.R.id.home){
            Fragment myFragment= new fr_providerregister1();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }
    public fr_providerregister2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        intromanager=new IntroManager(getActivity());
        providerinfo=new providerInfo(getActivity());
        myDB = new HMCoreData(getContext());
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
           myDB.showToast(getActivity(),e.getMessage());
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_providerregister2, container,false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Select Services");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        progressBar4= (ProgressBar) v.findViewById(R.id.progressBar4);
        btnnext=(Button) v.findViewById(R.id.btnnext);
        btnprev=(Button) v.findViewById(R.id.btnprev);
        recyclerview=(RecyclerView) v.findViewById(R.id.servicelist);
        fetchData();

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //saveAddInfo();
                if (providerinfo.getAllServiceItems().size()==0){
                    myDB.showToast(getContext(),"Select atleast one Service");
                    return;
                }
                Log.i("Debugging","Service Info " +  providerinfo.getAllServiceItems());
                FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
                Fragment myFragment= new fr_providerregister3();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btnprev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Fragment myFragment= new fr_providerregister1();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                return;
            }
        });

        return v;
    }

    private void fetchData() {
            JSONObject jsonParam = new JSONObject();
            JSONObject jsonMain = new JSONObject();
            try {
                jsonMain.put("mdevice", intromanager.getDevice());
                jsonMain.put("merchantcode", intromanager.getMerchantCode());
                jsonParam.put("indata", jsonMain);

            } catch (JSONException e) {
                e.printStackTrace();
               myDB.showToast(getContext(), e.getMessage());
                return;
            }
            String[] myTaskParams = {"/SSMServiceList", jsonParam.toString()};
        Log.i("Debugging","Service ilist " +  jsonParam.toString());
            try {
                HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMServiceList");
                aasyncTask.delegate = (AsyncResponse) fr_providerregister2.this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
               myDB.showToast(getContext(), e.getMessage());
                return;
            }
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        adapter = new serviceList(getContext(),servicelist);
        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(adapter);
        servicelist.clear();

        JSONObject myjson = new JSONObject(output);
        JSONArray json_array_item = myjson.getJSONArray("servicelist");
        Boolean mc=false;
        for (int i = 0; i < json_array_item.length(); i++) {
            JSONObject objects = json_array_item.getJSONObject(i);
            for (int m = 0; m < providerinfo.getAllServiceItems().size(); m++) {
                if(providerinfo.getAllServiceItems().get(m).equals(objects.getString("servicecode"))){
                    mc=true;
                    break;
                }else{
                    mc=false;
                }
            }
            spinClass spin=new spinClass(objects.getString("servicename"),objects.getString("servicecode"),i,mc);
            servicelist.add(spin);

        }
        adapter.notifyDataSetChanged();
    }
}

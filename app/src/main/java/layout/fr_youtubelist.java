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


import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.YoutubeVideoAdapter;
import com.aic.aicdelivery.YoutubeVideoModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_youtubelist extends Fragment implements AsyncResponse {
    private static LinearLayout fillcart;
    private static LinearLayout emptycart;
    private YoutubeVideoAdapter adapter;
    private HMCoreData myDB;
    private RecyclerView recyclerView;

    IntroManager intromanager;
    private ProgressBar progressBar4;
    private View v;


    ArrayList<YoutubeVideoModel> youtubelist = new ArrayList<YoutubeVideoModel>();

    public fr_youtubelist() {
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
        Main2Activity.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        return true;
      /*  Fragment myFragment = new fr_homepage();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        myDB = new HMCoreData(getContext());
        intromanager = new IntroManager(getActivity());

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_youtubelist, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Partner Training Videos");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);


        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);
        fetchData();


       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener()
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

        return v;
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        adapter = new YoutubeVideoAdapter(getContext(), youtubelist);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        youtubelist.clear();
        JSONObject myjson = new JSONObject(output);
        JSONArray json_array = myjson.getJSONArray("youtubelist");
        JSONObject objects;
        YoutubeVideoModel youtubeVideoModel;
        for (int i = 0; i < json_array.length(); i++) {
            objects = json_array.getJSONObject(i);
            youtubeVideoModel = new YoutubeVideoModel();
            youtubeVideoModel.setVideoId(objects.getString("playid"));
            youtubeVideoModel.setTitle(objects.getString("description"));
            youtubeVideoModel.setDuration(objects.getString("duration"));
            youtubelist.add(youtubeVideoModel);

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


    private void fetchData() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("reference", "2"); //Vendor Youtube Content
            jsonParam.put("indata", jsonMain);

        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return;
        }
        String[] myTaskParams = {"/SSMYouTubeList", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMYouTubeList");
            aasyncTask.delegate = (AsyncResponse) fr_youtubelist.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
            return;
        }

    }
}

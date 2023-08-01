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
import android.widget.RatingBar;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.reviewAdapter;
import com.aic.aicdelivery.reviewVO;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_vendorreviewsingle extends Fragment implements AsyncResponse {
    public static LinearLayout fillcart;
    public static LinearLayout emptycart;
    private reviewAdapter adapter;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private TextView mTitle;
    private RecyclerView recyclerView;
    private ProgressBar progressBar4;
    View v;
    MenuInflater inflater;
    private CircleImageView listImage;
    private TextView outlet,category,avgrating,textrate1,textrate2,textrate3,textrate4,textrate5,totalreviews;
    private RatingBar ratingnum1,ratingnum2,ratingnum3,ratingnum4,ratingnum5;
    ArrayList<reviewVO> reviewList = new ArrayList<reviewVO>();
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

    public fr_vendorreviewsingle() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            myDB = new HMCoreData(getContext());
            intromanager=new IntroManager(getActivity());


        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_vendorreviewsingle, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Service Provider Review");
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        recyclerView = (RecyclerView) v.findViewById(R.id.messqagelist);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        fillcart=(LinearLayout) v.findViewById(R.id.fillcart);
        emptycart=(LinearLayout) v.findViewById(R.id.emptycart);
        listImage=(CircleImageView) v.findViewById(R.id.listImage);
        ratingnum1=(RatingBar) v.findViewById(R.id.ratingnum1);
        ratingnum2=(RatingBar) v.findViewById(R.id.ratingnum2);
        ratingnum3=(RatingBar) v.findViewById(R.id.ratingnum3);
        ratingnum4=(RatingBar) v.findViewById(R.id.ratingnum4);
        ratingnum5=(RatingBar) v.findViewById(R.id.ratingnum5);

        totalreviews=(TextView) v.findViewById(R.id.totalreviews);
        outlet=(TextView) v.findViewById(R.id.outlet);
        category=(TextView) v.findViewById(R.id.category);
        avgrating=(TextView) v.findViewById(R.id.avgrating);
        textrate1=(TextView) v.findViewById(R.id.textrate1);
        textrate2=(TextView) v.findViewById(R.id.textrate2);
        textrate3=(TextView) v.findViewById(R.id.textrate3);
        textrate4=(TextView) v.findViewById(R.id.textrate4);
        textrate5=(TextView) v.findViewById(R.id.textrate5);
        fetchData();
        return v;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        Main2Activity.bottomNavigationView.setSelectedItemId(R.id.nav_home);
      /*  Fragment myFragment = new fr_homepage();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();*/
        return true;
    }
    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        JSONObject myjson = new JSONObject(output);
        JSONArray json_array_vendor = myjson.getJSONArray("outlet");



        //Vendor Review carousal
        adapter = new reviewAdapter(getActivity(), (ArrayList<reviewVO>) reviewList,R.layout.vendorreview_cell);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        reviewList.clear();
        Log.i("Debugging", "JSON Vendor" + json_array_vendor);
        for (int i = 0; i < json_array_vendor.length(); i++) {
            int ctr=0;
            JSONObject objects = json_array_vendor.getJSONObject(i);
            outlet.setText(objects.getString("outletname"));
            category.setText(objects.getString("servicecategory"));
            avgrating.setText("Rating: " +objects.getString("averagerating") + " Stars");
            totalreviews.setText("Reviews : " + objects.getString("numberofreviews"));
            textrate1.setText(objects.getString("fivestar") + " Reviews");
            ratingnum1.setRating(5);
            textrate2.setText(objects.getString("fourstar") + " Reviews");
            ratingnum2.setRating(4);
            textrate3.setText(objects.getString("threestar") + " Reviews");
            ratingnum3.setRating(3);
            textrate4.setText(objects.getString("twostar") + " Reviews");
            ratingnum4.setRating(2);
            textrate5.setText(objects.getString("onestar") + " Reviews");
            ratingnum5.setRating(1);
            if (objects.getString("itemimage") != null) {
                Picasso.with(getActivity())
                        .load(objects.getString("itemimage"))
                        .placeholder(R.drawable.haal_meer_large)
                        .fit()
                        .noFade()
                        .into(listImage);
            }
            JSONArray json_array_customer = objects.getJSONArray("customerreviews");
            for (int x = 0; x < json_array_customer.length() ; x++) {
                JSONObject object1 = json_array_customer.getJSONObject(x);
                reviewVO a = new reviewVO(objects.getString("itemimage"), json_array_customer.length(), objects.getString("outletcode"), objects.getString("outletname"), object1.getInt("numberofstars"), objects.getInt("numberofreviews"), object1.getString("reviewcomment"), object1.getString("customername"), object1.getString("city"),object1.getString("reviewdate"),objects.getString("servicecategory"));
                if (ctr<=50) {
                    reviewList.add(a);
                    ctr++;
                } else {
                    break;
                }
            }
        }
        if (json_array_vendor.length()==0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        }else{
            fillcart.setVisibility(View.VISIBLE);
            emptycart.setVisibility(View.INVISIBLE);
        }
        adapter.notifyDataSetChanged();
    }

    private void fetchData(){
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("producttype", "2");
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("service", "");
            jsonParam.put("indata", jsonMain);

        } catch (JSONException e) {
            e.printStackTrace();
           myDB.showToast(getContext(),e.getMessage());
            return ;
        }
        String[] myTaskParams = {"/SSMProviderReviewList", jsonParam.toString()};
        Log.i("Debugging", "JSON Vendor" + jsonParam.toString());
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "SSMProviderReviewList");
            aasyncTask.delegate = (AsyncResponse) fr_vendorreviewsingle.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
           myDB.showToast(getContext(),e.getMessage());
            return ;
        }

    }

}

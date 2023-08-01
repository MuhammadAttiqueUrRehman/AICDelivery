package layout;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
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
import com.aic.aicdelivery.historyorderAdapterProd;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class fr_order_history extends Fragment implements AsyncResponse {
    private static LinearLayout fillcart;
    private static LinearLayout emptycart;
    private historyorderAdapterProd adapter;
    private HMCoreData myDB;
    private HMAppVariables appVariables = new HMAppVariables();
    private Toolbar mtoolbar;
    private TextView mTitle;
    private RecyclerView recyclerView;
    private ProgressBar progressBar4;
    private Boolean forceupdate = false;
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (forceupdate) {
            forceupdate = false;
        }
        myDB = new HMCoreData(getContext());

        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fr_order_history, container, false);
        //   btnRecur=(ImageButton) v.findViewById(R.id.btnRecurring);
        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        mTitle = (TextView) mtoolbar.findViewById(R.id.toolbar_title);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        recyclerView = (RecyclerView) v.findViewById(R.id.messqagelist);
        progressBar4 = (ProgressBar) v.findViewById(R.id.progressBar4);
        fillcart = (LinearLayout) v.findViewById(R.id.fillcart);
        emptycart = (LinearLayout) v.findViewById(R.id.emptycart);
        //  searchview =(MaterialSearchView) v.findViewById(R.id.search_view);
        mTitle.setText("Orders History");
        try {
            appVariables = myDB.getUserData();
        } catch (Exception e) {
            myDB.showToast(getContext(), e.getMessage());
            return null;
        }
        fetchData();

    /*    btnRecur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forceupdate=false;
                FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
                ftr.detach(fr_order_history.this).attach(fr_order_history.this).commitAllowingStateLoss();
            }
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
                    }
                }
                return true;
            }
        });

        return v;
    }

    private void fetchData() {
        if (!forceupdate) {
            JSONObject jsonParam = new JSONObject();
            try {
                IntroManager intromanager = new IntroManager(getActivity());
                jsonParam.put("mdevice", appVariables.udevicecomboined);
                jsonParam.put("merchantcode", intromanager.getMerchantCode());
                jsonParam.put("customerid", appVariables.ucustomerid);
                jsonParam.put("password", appVariables.ucertificate);
                jsonParam.put("ordertype", "");
                jsonParam.put("itemsearch", "");

            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            String[] myTaskParams = {"/orderhistory.aspx", jsonParam.toString()};
            //  new HMDataAccess().execute(myTaskParams);
            try {
                HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4, "orderlist");
                aasyncTask.delegate = (AsyncResponse) fr_order_history.this;
                aasyncTask.execute(myTaskParams);
            } catch (Exception e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
            forceupdate = true;
        }
    }


    private void searchLocal(String txt) throws HMOwnException {
        ArrayList<String> ar1 = new ArrayList<String>();
        String ccy = appVariables.ucurrency;

        adapter = new historyorderAdapterProd(getContext(), ar1, ccy);
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
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {

        String ccy = appVariables.ucurrency;
        adapter = new historyorderAdapterProd(getContext(), ar, ccy);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new fr_order_history.GridSpacingItemDecoration(1, dpTopx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        JSONObject myjson = new JSONObject(output);
        JSONArray json_array = myjson.getJSONArray("orderlist");

        for (int i = 0; i < json_array.length(); i++) {
            JSONObject objects = json_array.getJSONObject(i);
            ar.add(String.valueOf(objects));

        }

        if (json_array.length() == 0) {
            fillcart.setVisibility(View.INVISIBLE);
            emptycart.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;


        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spacing = spacing;
            this.spanCount = spanCount;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }

                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }

            }
        }

    }

    private int dpTopx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}

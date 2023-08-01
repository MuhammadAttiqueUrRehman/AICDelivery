package layout;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMConstants;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.MainActivity;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.detailitemVO;
import com.aic.aicdelivery.employeeAdapterSimple;
import com.aic.aicdelivery.spinClass2;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_homepage extends Fragment implements AsyncResponse, employeeAdapterSimple.ClickEvent, View.OnClickListener {
    IntroManager intromanager;
    HMCoreData myDB;
    RecyclerView recyclerView;
    View v;
    ProgressBar progressBar3;
    Switch companystatus;
    TextView neworders, completedorders, tv_onload, tv_ontransit, tv_balance;
    CardView card1, card2, card3, card4, card5;
    JSONObject jObj;
    public static ArrayList<detailitemVO> emplist;
    employeeAdapterSimple adapter;
    LinearLayout ln;
    ShareActionProvider mShareActionProvider;
    String mlink;
    MenuItem actionItem;

    public fr_homepage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem items) {
        Fragment myFragment;
        int id = items.getItemId();
        switch (id) {
            case R.id.menu_contactcustomer:
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + intromanager.getSupportCall())));
                break;
            case R.id.menu_changepin:
                myFragment = new fr_change_pin();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
            case R.id.menu_feedback:
                myFragment = new fr_unfeedback();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
            case R.id.menu_info:
                myFragment = new fr_version();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
            case R.id.menu_contactus:
                myFragment = new fr_contactus();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
            case R.id.menu_dataprivacy:
                myFragment = new fr_data_privacy();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
            case R.id.menu_tandc:
                myFragment = new fr_tandc();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                break;
            case R.id.menu_logout:
                Log.i("Debugging", "Service Being logged out");
                //Call Logout service
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("employeeid", intromanager.getEmployeeID());
                    jsonMain.put("certificate", intromanager.getCertificate());
                    jsonMain.put("outlet", intromanager.getOutletID());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                }
                String[] myTaskParams = {"/SSMPartnerLogout", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    Log.i("Debugging", jsonParam.toString());
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar3, "SSMPartnerLogout");
                    aasyncTask.delegate = (AsyncResponse) fr_homepage.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(), e.getMessage());
                }
                break;
        }

        return super.onOptionsItemSelected(items);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        requireActivity().getMenuInflater().inflate(R.menu.post_menu, menu);
        return;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Main2Activity.ln.setVisibility(View.VISIBLE);
        intromanager = new IntroManager(requireContext());
        myDB = new HMCoreData(getContext());


        v = inflater.inflate(R.layout.fragment_fr_homepage, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(HMConstants.partnerHeader);
        //((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle(intromanager.getLocation());
        // mtoolbar.setNavigationIcon(R.drawable.ic_exit);
        companystatus = (Switch) v.findViewById(R.id.companystatus);
        progressBar3 = (ProgressBar) v.findViewById(R.id.progressBar3);
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        neworders = (TextView) v.findViewById(R.id.neworders);
        completedorders = (TextView) v.findViewById(R.id.completeorders);
        tv_onload = (TextView) v.findViewById(R.id.tv_onload);
        tv_ontransit = (TextView) v.findViewById(R.id.tv_ontransit);
        tv_balance = (TextView) v.findViewById(R.id.tv_balance);
        card1 = (CardView) v.findViewById(R.id.card1);
        card2 = (CardView) v.findViewById(R.id.card2);
        card3 = (CardView) v.findViewById(R.id.card3);
        card4 = (CardView) v.findViewById(R.id.card4);
        card5 = (CardView) v.findViewById(R.id.card5);
        ln = (LinearLayout) v.findViewById(R.id.emp1);
        companystatus.setText(intromanager.getOutletName());
        companystatus.setChecked(intromanager.getCheckinStatus().equals("1") ? true : false);
        FirebaseMessaging.getInstance().subscribeToTopic(HMConstants.partnerTopic);
        String token = FirebaseInstanceId.getInstance().getToken();
        intromanager.setFCMKey(token);
        sendRegistrationToServer(token);


        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(requireContext(), e.getMessage());
        }
        Log.i("Debugging", "Item Balances of order:" + jsonParam.toString());
        String[] myTaskParams = {"/SSMOrderBalance", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(requireContext(), progressBar3, "SSMOrderBalance");
            aasyncTask.delegate = (AsyncResponse) fr_homepage.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
        }
     /*   try {
            JSONObject jobj = new JSONObject(intromanager.getLoginJson());
            neworders.setText(jobj.getString("neworders"));
            completedorders.setText(jobj.getString("completeorders"));
            tv_onload.setText(jobj.getString("loadorders"));
            tv_ontransit.setText(jobj.getString("transitorders"));
            tv_balance.setText(jobj.getString("quotations"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        //New Orders
        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_neworders();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        //Completed Orders
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_completedorders();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        //New Quotations
        card3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_loadorders();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        //missed Orders
        card4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (intromanager.getAdmin().equals("Y")) {
                Fragment myFragment = new fr_transitorders();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
//                }
            }
        });

        //balance Orders
        card5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment myFragment = new fr_balanceorders();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();

            }
        });

        companystatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                String mdata = buttonView.isChecked() ? "1" : "0";
                intromanager.setCheckinStatus(mdata);

                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("employeeid", intromanager.getEmployeeID());
                    jsonMain.put("certificate", intromanager.getCertificate());
                    jsonMain.put("outlet", intromanager.getOutletID());
                    jsonMain.put("status", intromanager.getCheckinStatus());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                }
                Log.i("Debugging", "Company Status Change :" + jsonParam.toString());
                String[] myTaskParams = {"/SSMCompanyCheckInOut", jsonParam.toString()};
                //  new HMDataAccess().execute(myTaskParams);
                try {
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar3, "SSMCompanyCheckInOut");
                    aasyncTask.delegate = (AsyncResponse) fr_homepage.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getActivity(), e.getMessage());
                    return;
                }
            }
        });
        if (!(intromanager.getAdmin().equals("Y"))) {
            ln.setVisibility(View.GONE);
            companystatus.setVisibility(View.GONE);
        }

       /* recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    Main2Activity.bottomNavigationView.setVisibility(View.GONE);
                } else {
                    Main2Activity.bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
*//*
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState)
            {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                {
                    Main2Activity.bottomNavigationView.setVisibility(View.VISIBLE);
                }

                super.onScrollStateChanged(recyclerView, newState);
            }
            *//*
        });*/

        return v;
    }

    private void getEmployees() {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(requireContext(), e.getMessage());
        }
        Log.i("Debugging", "Employee List :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMEmployeeList", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(requireContext(), progressBar3, "SSMEmployeeList");
            aasyncTask.delegate = (AsyncResponse) this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }

    private void sendRegistrationToServer(String refreshedToken) {
        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("customer", intromanager.getEmployeeID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("reference", refreshedToken);
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
        }
        Log.i("Debugging", "FCM Update key :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMUpdateFCMKeyVendor", jsonParam.toString()};
        //  new HMDataAccess().execute(myTaskParams);
        try {
            HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar3, "SSMUpdateFCMKeyVendor");
            aasyncTask.delegate = (AsyncResponse) this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getActivity(), e.getMessage());
            return;
        }
    }


    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        JSONObject jObj = new JSONObject(output);
        Log.d("debugging", output);
        if (handle.equals("SSMPartnerLogout")) {
            intromanager.setLoginOP("");
            intromanager.setLogged(false);
            intromanager.setEmployeeID("");
            intromanager.setCertificate("");
            intromanager.setOutletID("");
            intromanager.setCheckinStatus("");
            intromanager.setCompanyType("");
            intromanager.setServiceImage1("");
            intromanager.setAdmin("");
            intromanager.setCheckinStatus("");
            intromanager.setOutletName("");
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else if (!jObj.getString("statuscode").equals("000")) {
            myDB.showToast(getActivity(), jObj.getString("statusdesc"));
            return;
        } else if (handle.equals("SSMOrderBalance")) {
            Log.i("Debugging", "Order Balance Enter" + output);
            try {
                /*neworders.setText(jObj.getString("neworders"));
                completedorders.setText(jObj.getString("completeorders"));
                quotation.setText(jObj.getString("quotations"));
                missedorders.setText(jObj.getString("missedorders"));*/


                neworders.setText(jObj.getString("neworders_delivery"));
//                completedorders.setText(jObj.getString("completeorders"));
                completedorders.setText(jObj.getString("historyorder_delivery"));
                tv_onload.setText(jObj.getString("loadorder_delivery"));
                tv_ontransit.setText(jObj.getString("transitorder_delivery"));
                tv_balance.setText(jObj.getString("balanceorder_delivery"));

                getEmployees();
            } catch (JSONException e) {
                e.printStackTrace();
                myDB.showToast(getContext(), e.getMessage());
                return;
            }
        } else if (handle.equals("SSMCompanyCheckInOut")) {
            myDB.showToast(getActivity(), "Company Status Changed");

            return;
        } else if (handle.equals("SSMEmployeeList")) {
            jObj = new JSONObject(output);
            JSONArray json_array = (JSONArray) jObj.getJSONArray("employeelist");
            JSONObject objects;
            emplist = new ArrayList<detailitemVO>();
            adapter = new employeeAdapterSimple(getActivity(), (ArrayList<detailitemVO>) emplist, R.layout.employ_service_box);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(adapter);
            adapter.setClickEvent(this);
            intromanager.clearEmployee();
            intromanager.clearEmployeeActive();
            intromanager.addEmployee(new spinClass2("Select Employee", "", ""));

            for (int i = 0; i < json_array.length(); i++) {
                objects = json_array.getJSONObject(i);
                detailitemVO a = new detailitemVO(objects.getString("employeename"), objects.getString("employeecode"), objects.getString("checkinstatus"), objects.getString("userstatus"));
                emplist.add(a);
                spinClass2 spin = new spinClass2(objects.getString("employeename"), objects.getString("employeecode"), objects.getString("userstatus"));
                intromanager.addEmployee(spin);

                if (objects.getString("checkinstatus").equals("1") && objects.getString("userstatus").equals("1")) {
                    intromanager.addEmployeeActive(spin);
                }
            }
            adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void clickEventItem() {
        Log.i("Debugging", "Reloading Employees");
        getEmployees();
    }

    @Override
    public void onClick(View v) {
    }
}


package layout;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMAppVariables;
import com.aic.aicdelivery.HMConstants;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_checkout extends Fragment implements AsyncResponse {
    private ImageView imgcard,imgcash;
    private TextView totalamount;
    private ProgressBar progressBar20;
    private RelativeLayout pay3,pay4;
    private IntroManager intromanager;
    private HMCoreData myDB;
    private HMAppVariables hmAppVariables = new HMAppVariables();
    private String transactionref;
    private Toolbar mtoolbar;

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
        intromanager=new IntroManager(getActivity());
        myDB = new HMCoreData(getActivity());

        try {
            hmAppVariables = myDB.getUserData();
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(),e.getMessage());
        }

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fr_checkout, container, false);
        mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Accept Payment");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(79,7,0), PorterDuff.Mode.SRC_IN);
        totalamount = (TextView) v.findViewById(R.id.totalamount);
        pay3 = (RelativeLayout) v.findViewById(R.id.pay3);
        pay4 = (RelativeLayout) v.findViewById(R.id.pay4);
        progressBar20 = (ProgressBar) v.findViewById(R.id.progressBar20);
        transactionref = getArguments().getString("orderid");

        //final Float totamt=Float.parseFloat(intromanager.getVisitingCharges()) + (Float.parseFloat(intromanager.getVisitingCharges())*((intromanager.getSGST()/100)+(intromanager.getCGST()/100)));
        final Float totamt=Float.parseFloat(getArguments().getString("totalreceivable"));

        totalamount.setText("Total :" + getString(R.string.Rs)  + " " +  String.format("%.02f", totamt));

        pay4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(HMConstants.alertheader);
                alert.setMessage("You have selected to make payment using Card Payment. Continue?").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //call payTM QR Code Page
                                Bundle bundle = new Bundle();
                                bundle.putString("actiontype", getArguments().getString("actiontype"));
                                bundle.putString("customer", getArguments().getString("customer"));
                                bundle.putString("employeeid", intromanager.getEmployeeID());
                                bundle.putString("certificate", intromanager.getCertificate());
                                bundle.putString("vendorid", getArguments().getString("vendorid"));
                                bundle.putString("orderid", getArguments().getString("orderid"));
                                bundle.putString("totalreceivable", getArguments().getString("totalreceivable"));
                                Fragment myFragment = new fr_qrcheckout();
                                myFragment.setArguments(bundle);
                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert.show();
            }
        });

        pay3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(HMConstants.alertheader);
                alert.setMessage("You have selected to make your payment by cash. Continue?").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                JSONObject jsonParam = new JSONObject();
                                JSONObject jsonMain = new JSONObject();
                                try {
                                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                                    jsonMain.put("mdevice", intromanager.getDevice());
                                    jsonMain.put("employeeid", intromanager.getEmployeeID());
                                    jsonMain.put("certificate", intromanager.getCertificate());
                                    jsonMain.put("customer", getArguments().getString("customer"));
                                    jsonMain.put("channelref", HMConstants.appchannelref);
                                    jsonMain.put("netamount",getArguments().getString("totalreceivable"));
                                    jsonMain.put("outlet", getArguments().getString("vendorid"));
                                    jsonMain.put("orderid",transactionref);
                                    jsonMain.put("fbtoken",intromanager.getFCMKey());
                                    jsonMain.put("status",getArguments().getString("actiontype"));
                                    jsonParam.put("indata", jsonMain);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    myDB.showToast(getContext(),e.getMessage());
                                }
                                String[] myTaskParams = {"/SSMpayByCOD", jsonParam.toString()};
                                //  new HMDataAccess().execute(myTaskParams);
                                try {
                                    Log.i("Debugging", jsonParam.toString());
                                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "paycash");
                                    aasyncTask.delegate = (AsyncResponse) fr_checkout.this;
                                    aasyncTask.execute(myTaskParams);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    myDB.showToast(getContext(),e.getMessage());
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert.show();
            }
        });
        return v;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");
        Map<String, String> statuscode;
        String statuscodekey;
        String statusdesckey;
        statuscode = myDB.statusValues(output);
        statuscodekey = (String) statuscode.get("statuscode");
        statusdesckey = (String) statuscode.get("statusdesc");
        JSONObject myjson = new JSONObject(output);
        Log.i("Debugging", "Checksum Output : " + output);
        if (!statuscodekey.toString().equals("000")) {
            myDB.showToast(getActivity(), statusdesckey);
            return;
        } else {
           if (handle == "paycash") {
                Bundle bundle = new Bundle();
                bundle.putString("paymentref", statuscode.get("paymentref"));
                bundle.putString("orderref", "Order Ref :" + statuscode.get("paymentref"));
                bundle.putString("message", "Thank you for the order.  Please make payment to the service staff.");
                Fragment myFragment = new fr_checkout_confirm_payment();
                myFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        int id=items.getItemId();
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
    }
}

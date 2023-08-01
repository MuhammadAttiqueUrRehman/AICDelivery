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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMConstants;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class fr_qrcheckout extends Fragment implements AsyncResponse {
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private EditText txtreference,txtamount;
    private ProgressBar progressBar20;
    private ImageView imgsplash;
    private IntroManager intromanager;
    private Button btnaccept;
    private ScrollView accscroll;
    private TextView lblpaymobile,lblbank;

    public fr_qrcheckout() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.call_menu, menu);
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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        intromanager = new IntroManager(getActivity());
        myDB=new HMCoreData(getActivity());
        View v = inflater.inflate(R.layout.fragment_fr_qrcheckout, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Accept Card Payment");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.rgb(255,255,255), PorterDuff.Mode.SRC_IN);

        progressBar20 = (ProgressBar) v.findViewById(R.id.progressBar20);
        txtreference=(EditText) v.findViewById(R.id.txtreference);
        txtamount=(EditText) v.findViewById(R.id.txtamount);
        imgsplash=(ImageView) v.findViewById(R.id.imgsplash);
        btnaccept=(Button) v.findViewById(R.id.btnaccept);
        accscroll=(ScrollView) v.findViewById(R.id.accscroll);
        lblpaymobile=(TextView) v.findViewById(R.id.lblpaymobile);
        lblbank=(TextView) v.findViewById(R.id.lblbank);
        imgsplash=(ImageView) v.findViewById(R.id.imgsplash);
        final Float totamt=Float.parseFloat(getArguments().getString("totalreceivable"));
        txtamount.setText(getString(R.string.Rs)  + " " +  String.format("%.02f", totamt));
        lblpaymobile.setText("Beneficiary Mobile Wallet:" + " " + intromanager.getPaymentMobile());
        lblbank.setText("Beneficiary Bank: " + " " + intromanager.getPaymentBank());
        Picasso.with(getActivity())
                .load(intromanager.getQRCode())
                .placeholder(R.drawable.haal_meer_large)
                .fit()
                .centerInside()
                .noFade()
                .into(imgsplash);

        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (txtreference.length() == 0) {
                    myDB.showToast(getContext(), "Please enter payment reference from Wallet");
                    return;
                }
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(HMConstants.alertheader);
                alert.setMessage("Is the payment reference and amount OK, Are you sure to confirm the payment?").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                JSONObject jsonParam = new JSONObject();
                                JSONObject jsonMain = new JSONObject();
                                try {
                                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                                    jsonMain.put("mdevice", intromanager.getDevice());
                                    jsonMain.put("employeeid",intromanager.getEmployeeID());
                                    jsonMain.put("certificate",intromanager.getCertificate());
                                    jsonMain.put("customer", getArguments().getString("customer"));
                                    jsonMain.put("channelref", HMConstants.appchannelref);
                                    jsonMain.put("netamount", getArguments().getString("totalreceivable"));
                                    jsonMain.put("outlet", getArguments().getString("vendorid"));
                                    jsonMain.put("orderid", getArguments().getString("orderid"));
                                    jsonMain.put("paymentref", txtreference.getText());
                                    jsonMain.put("reference", getArguments().getString("orderid"));
                                    jsonMain.put("fbtoken",intromanager.getFCMKey());
                                    jsonMain.put("status", getArguments().getString("actiontype"));
                                    jsonParam.put("indata", jsonMain);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    myDB.showToast(getContext(), e.getMessage());
                                }

                                String[] myTaskParams = {"/SSMpayByPayTM", jsonParam.toString()};
                                //  new HMDataAccess().execute(myTaskParams);
                                try {
                                    Log.i("Debugging", jsonParam.toString());
                                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar20, "paypaytm");
                                    aasyncTask.delegate = (AsyncResponse) fr_qrcheckout.this;
                                    aasyncTask.execute(myTaskParams);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    myDB.showToast(getContext(), e.getMessage());
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
    public boolean onOptionsItemSelected(MenuItem items){
        int id=items.getItemId();
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(items);
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
        } else{
            Bundle bundle = new Bundle();
            bundle.putString("paymentref", txtreference.getText().toString());
            bundle.putString("orderref", "Order Ref :" + getArguments().getString("orderid"));
            bundle.putString("message", "Payment successfully completed. The payment reference is  " + txtreference.getText().toString());
            Fragment myFragment = new fr_checkout_confirm_payment();
            myFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
    }
}

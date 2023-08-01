package layout;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.aic.aicdelivery.AsyncResponse;
import com.aic.aicdelivery.HMConstants;
import com.aic.aicdelivery.HMCoreData;
import com.aic.aicdelivery.HMDataAccess;
import com.aic.aicdelivery.HMOwnException;
import com.aic.aicdelivery.IntroManager;
import com.aic.aicdelivery.Main2Activity;
import com.aic.aicdelivery.R;
import com.aic.aicdelivery.spinClass2;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static android.view.View.GONE;


public class fr_book_checkout extends Fragment implements AsyncResponse {
    private LinearLayout.LayoutParams params,params1;
    private HMCoreData myDB;
    private Toolbar mtoolbar;
    private TextView mTitle,mSubTitle,itemname,itemsubname;
    private ProgressBar progressBar4a;
    private View v;
    private ImageView img;
    private TextView detailQ1,detailQ2,detailQ3,detailQ4,detailQ5,detailR1,detailR2,detailR3,detailR4,detailR5;
    private TextView pricedesc,priceamount,taxdesc,taxamount,totaldesc,totalamount,disclaimer;
    private IntroManager intromanager;
    private Button btnaccept,btndecline;
    private ScrollView accscroll;
    private String estimate,qualcode;
    private Spinner spnqualify;

    public fr_book_checkout() {
        // Required empty public constructor
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.addinfo, menu);
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
        // Inflate the layout for this fragment
        Main2Activity.ln.setVisibility(View.GONE);
        intromanager = new IntroManager(getActivity());
    myDB=new HMCoreData(getActivity());
        v = inflater.inflate(R.layout.fragment_fr_book_checkout, container, false);

        Toolbar mtoolbar = (Toolbar) v.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(mtoolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order Details");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mtoolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

        progressBar4a = (ProgressBar) v.findViewById(R.id.progressBar4a);
        itemname=(TextView) v.findViewById(R.id.itemname);
        itemsubname=(TextView) v.findViewById(R.id.unitprice);
        img=(ImageView) v.findViewById(R.id.detailimage);
        btnaccept=(Button) v.findViewById(R.id.btnaccept);
        btndecline=(Button) v.findViewById(R.id.btndecline);
        accscroll=(ScrollView) v.findViewById(R.id.accscroll);
        detailQ1=(TextView) v.findViewById(R.id.detailQ1);
        detailQ2=(TextView) v.findViewById(R.id.detailQ2);
        detailQ3=(TextView) v.findViewById(R.id.detailQ3);
        detailQ4=(TextView) v.findViewById(R.id.detailQ4);
        detailQ5=(TextView) v.findViewById(R.id.detailQ5);
        detailR1=(TextView) v.findViewById(R.id.detailR1);
        detailR2=(TextView) v.findViewById(R.id.detailR2);
        detailR3=(TextView) v.findViewById(R.id.detailR3);
        detailR4=(TextView) v.findViewById(R.id.detailR4);
        detailR5=(TextView) v.findViewById(R.id.detailR5);
        pricedesc=(TextView) v.findViewById(R.id.pricedesc);
        priceamount=(TextView) v.findViewById(R.id.priceamount);
        taxdesc=(TextView) v.findViewById(R.id.taxdesc);
        taxamount=(TextView) v.findViewById(R.id.taxamount);
        totaldesc=(TextView) v.findViewById(R.id.totaldesc);
        totalamount=(TextView) v.findViewById(R.id.totalamount);
        disclaimer=(TextView) v.findViewById(R.id.disclaimer);
        spnqualify=(Spinner) v.findViewById(R.id.spnqualify);

        JSONObject jsonParam = new JSONObject();
        JSONObject jsonMain = new JSONObject();
        try {
            jsonMain.put("merchantcode", intromanager.getMerchantCode());
            jsonMain.put("mdevice", intromanager.getDevice());
            jsonMain.put("employeeid", intromanager.getEmployeeID());
            jsonMain.put("certificate", intromanager.getCertificate());
            jsonMain.put("outlet", intromanager.getOutletID());
            jsonMain.put("orderid", getArguments().getString("orderid"));
            jsonParam.put("indata", jsonMain);
        } catch (JSONException e) {
            e.printStackTrace();
            myDB.alertBox(getContext(), e.getMessage());
            return null ;
        }
        Log.i("Debugging", "Fetch Detail Input :" + jsonParam.toString());
        String[] myTaskParams = {"/SSMFetchOrderVendor", jsonParam.toString()};
        try {
            HMDataAccess aasyncTask = new HMDataAccess(this.getContext(), progressBar4a, "SSMFetchOrderVendor");
            aasyncTask.delegate = (AsyncResponse) fr_book_checkout.this;
            aasyncTask.execute(myTaskParams);
        } catch (Exception e) {
            e.printStackTrace();
            myDB.showToast(getContext(),e.getMessage());
            return null;
        }

        spnqualify.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                spinClass2 spin = (spinClass2) parent.getSelectedItem();
                qualcode=spin.getId();
                // myDB.showToast(getActivity(),qualcode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (qualcode == null){
                    myDB.showToast(getContext(),"Select Employee");
                    return;
                }
                if (qualcode.length()==0){
                    myDB.showToast(getContext(),"Select Employee");
                    return;
                }
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("employeeid", intromanager.getEmployeeID());
                    jsonMain.put("certificate", intromanager.getCertificate());
                    jsonMain.put("outlet", intromanager.getOutletID());
                    jsonMain.put("employeeaccount", qualcode);
                    jsonMain.put("fbtoken", intromanager.getFCMKey());
                    jsonMain.put("orderid", getArguments().getString("orderid"));
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(),e.getMessage());
                    return;
                }
                Log.i("Debugging"," Order Accept Input :" + jsonParam.toString());
                String[] myTaskParams = {"/SSMAcceptServiceOrder", jsonParam.toString()};
                try {
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4a, "SSMAcceptServiceOrder");
                    aasyncTask.delegate = (AsyncResponse) fr_book_checkout.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(),e.getMessage());
                    return;
                }
                //   FragmentTransaction ftr = getActivity().getSupportFragmentManager().beginTransaction();
                //   Fragment myFragment = new fr_qapage1();
                //   getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
            }
        });

        btndecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle(HMConstants.alertheader);
                alert.setMessage("You have selected to decline this booking, are you sure you want to continue?").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                JSONObject jsonParam = new JSONObject();
                JSONObject jsonMain = new JSONObject();
                try {
                    jsonMain.put("merchantcode", intromanager.getMerchantCode());
                    jsonMain.put("mdevice", intromanager.getDevice());
                    jsonMain.put("employeeid", intromanager.getEmployeeID());
                    jsonMain.put("certificate", intromanager.getCertificate());
                    jsonMain.put("outlet", intromanager.getOutletID());
                    jsonMain.put("orderid", getArguments().getString("orderid"));
                    jsonMain.put("fbtoken", intromanager.getFCMKey());
                    jsonParam.put("indata", jsonMain);
                } catch (JSONException e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(),e.getMessage());
                    return;
                }
                Log.i("Debugging"," Order Decline Input :" + jsonParam.toString());
                String[] myTaskParams = {"/SSMDeclineServiceOrder", jsonParam.toString()};
                try {
                    HMDataAccess aasyncTask = new HMDataAccess(getActivity(), progressBar4a, "SSMDeclineServiceOrder");
                    aasyncTask.delegate = (AsyncResponse) fr_book_checkout.this;
                    aasyncTask.execute(myTaskParams);
                } catch (Exception e) {
                    e.printStackTrace();
                    myDB.showToast(getContext(),e.getMessage());
                    return;
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
        accscroll.setVisibility(GONE);
        return v;
    }

    @Override
    public void processFinish(String output, String handle) throws HMOwnException, JSONException {
        output = output.substring(1, output.length() - 1);
        output = output.replace("\\", "");

        JSONObject object1 = new JSONObject(output);

         if (!object1.getString("statuscode").equals("000")) {
            myDB.showToast(getActivity(), object1.getString("statusdesc"));
            return;
        } else if(handle.equals("SSMFetchOrderVendor")) {
             JSONArray mjarray = object1.getJSONArray("documentlist");
             JSONObject mjson = mjarray.getJSONObject(0);

             intromanager.setAdditionalInfo(mjson.getString("details"));
             intromanager.setServiceImage1(mjson.getString("image1"));
             intromanager.setServiceImage2(mjson.getString("image2"));
             intromanager.setServiceImage3(mjson.getString("image3"));
             intromanager.setServiceImage4(mjson.getString("image4"));
            JSONArray myarray = object1.getJSONArray("catalog");
            JSONObject myjson = myarray.getJSONObject(0);
            itemname.setText(myjson.getString("itemname"));
            //  itemsubname.setText(myjson.getString("unitprice"));
            Picasso.with(getContext())
                    .load(myjson.getString("itemimage"))
                    .placeholder(R.drawable.haal_meer_large)
                    .fit()
                    .centerCrop()
                    .noFade()
                    .into(img);
            if (myjson.getString("question1").length()==0){
                detailQ1.setVisibility(View.GONE);
                detailR1.setVisibility(View.GONE);
            }
            if (myjson.getString("question2").length()==0){
                detailQ2.setVisibility(View.GONE);
                detailR2.setVisibility(View.GONE);
            }
            if (myjson.getString("question3").length()==0){
                detailQ3.setVisibility(View.GONE);
                detailR3.setVisibility(View.GONE);
            }
            if (myjson.getString("question4").length()==0){
                detailQ4.setVisibility(View.GONE);
                detailR4.setVisibility(View.GONE);
            }
            if (myjson.getString("question5").length()==0){
                detailQ5.setVisibility(View.GONE);
                detailR5.setVisibility(View.GONE);
            }

            detailQ1.setText(myjson.getString("question1"));
            detailR1.setText(myjson.getString("response1"));
            detailQ2.setText(myjson.getString("question2"));
            detailR2.setText(myjson.getString("response2"));
            detailQ3.setText(myjson.getString("question3"));
            detailR3.setText(myjson.getString("response3"));
            detailQ4.setText(myjson.getString("question4"));
            detailR4.setText(myjson.getString("response4"));
            detailQ5.setText(myjson.getString("question5"));
            detailR5.setText(myjson.getString("response5"));
            estimate=myjson.getString("directnego");
             intromanager.setCustomerCall(myjson.getString("customermobile"));
            if (myjson.getString("directnego").equals("1")){
                params = (LinearLayout.LayoutParams) pricedesc.getLayoutParams();
                params.weight = 1f;
                params1 = (LinearLayout.LayoutParams) priceamount.getLayoutParams();
                params1.weight = 0f;
                pricedesc.setLayoutParams(params);
                priceamount.setLayoutParams(params1);
                //    pricedesc.setTextColor(getResources().R.color.textcolor1);
                pricedesc.setText("Actual Quote will be provided after inspection");
                params = (LinearLayout.LayoutParams) taxdesc.getLayoutParams();
                params.weight = 1f;
                params1 = (LinearLayout.LayoutParams) taxamount.getLayoutParams();
                params1.weight = 0f;
                taxdesc.setLayoutParams(params);
                taxamount.setLayoutParams(params1);
                //   pricedesc.setTextColor(getResources().R.color.textcolor2);
                taxdesc.setText("A VAT of 5% will be applied to estimate amount provided by the vendor");
                totaldesc.setText("");
                totalamount.setText("");
            }else{
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                pricedesc.setText("Fixed Price");
                taxdesc.setText("VAT 5%");
                priceamount.setText(getString(R.string.Rs)  + " " +  formatter.format(Double.parseDouble(myjson.getString("catalogprice"))));
                taxamount.setText(getString(R.string.Rs)  + " " + formatter.format(Double.parseDouble(myjson.getString("sgst"))+Double.parseDouble(myjson.getString("cgst"))));
                totaldesc.setText("Total Amount");
                totalamount.setText(getString(R.string.Rs)  + " " +  formatter.format(Double.parseDouble(myjson.getString("catalogprice"))+Double.parseDouble(myjson.getString("sgst"))+Double.parseDouble(myjson.getString("cgst"))));
            }
            disclaimer.setText("*A visiting charges of " +getString(R.string.Rs)  + " " + intromanager.getVisitingCharges() + " plus VAT should be paid to the vendor incase job is not confirmed" );

             ArrayAdapter<spinClass2> adapter = new ArrayAdapter<spinClass2>(getActivity(), android.R.layout.simple_spinner_dropdown_item, intromanager.getAllEmployeesActive());
             spnqualify.setAdapter(adapter);

         } else if(handle == "SSMAcceptServiceOrder") {
             Bundle bundle = new Bundle();
             bundle.putString("orderref", "");
             bundle.putString("message", "Order Accepted, arrange visit to the customer. You can call the customer from the orders page.");
             Fragment myFragment = new fr_checkout_confirm();
             myFragment.setArguments(bundle);
             getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }   else if(handle == "SSMDeclineServiceOrder") {
        Bundle bundle = new Bundle();
        bundle.putString("orderref", "");
        bundle.putString("message", "Order Declined, declining orders will impact your ratings and you will be given last priority while assigning new orders.");
        Fragment myFragment = new fr_checkout_confirm();
        myFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
    }
        accscroll.setVisibility(View.VISIBLE);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem items){
        int id=items.getItemId();
        if (id == android.R.id.home){
            if( getActivity().getSupportFragmentManager().getBackStackEntryCount()>0)
                getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }else if (id == R.id.menu_contactcustomer) {
            startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:"+ intromanager.getCustomerCall())));
        } else if (id == R.id.action_image) {
            Fragment myFragment= new fr_addInfoView();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_0, myFragment).addToBackStack(null).commitAllowingStateLoss();
        }
        return super.onOptionsItemSelected(items);
    }
}
